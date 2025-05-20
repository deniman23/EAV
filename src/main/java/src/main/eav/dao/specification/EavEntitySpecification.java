package src.main.eav.dao.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import src.main.eav.model.EavEntity;
import src.main.eav.controller.filter.EavEntityFilter;

@Component
public class EavEntitySpecification {

    private Predicate buildPredicate(EavEntityFilter filter, Root<EavEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        // Начинаем со стандартного "всегда истина"
        Predicate predicate = cb.conjunction();

        // Если заполнена строка поиска, ищем по полю type и по attributeName в связанном объекте attributes
        if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
            String searchTerm = "%" + filter.getSearch().toLowerCase() + "%";

            // Для поиска по attributeName создаем join с коллекцией attributes
            Join<Object, Object> attributesJoin = root.join("attributes", JoinType.LEFT);

            // Пишем два предиката: один для поля type, другой для attributeName
            Predicate typePredicate = cb.like(cb.lower(root.get("type")), searchTerm);
            Predicate attributeNamePredicate = cb.like(cb.lower(attributesJoin.get("attributeName")), searchTerm);

            // Объединяем оба условия с логическим OR
            predicate = cb.and(predicate, cb.or(typePredicate, attributeNamePredicate));
        }

        // Если тип запроса не COUNT, подгружаем связанные коллекции attributes и relations.
        if (query != null && !query.getResultType().equals(Long.class)) {
            // Используем fetch для жадной загрузки (при условии, что они нужны в результате)
            root.fetch("attributes", JoinType.LEFT);
            root.fetch("relations", JoinType.LEFT);
        }

        return predicate;
    }

    /**
     * Универсальная спецификация для фильтрации с динамической сортировкой по полю type.
     * Сортировка применяется в зависимости от значения sortOrder из фильтра:
     * - ASC – сортировка по возрастанию
     * - DESC – сортировка по убыванию
     */
    public Specification<EavEntity> getFilter(EavEntityFilter filter) {
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
                // Применяем динамическую сортировку по полю type в зависимости от sortOrder
                if (filter.getSortOrder() != null && filter.getSortOrder() == EavEntityFilter.SortOrder.DESC) {
                    query.orderBy(cb.desc(root.get("type")));
                } else {
                    query.orderBy(cb.asc(root.get("type")));
                }
            }
            return buildPredicate(filter, root, query, cb);
        };
    }
}
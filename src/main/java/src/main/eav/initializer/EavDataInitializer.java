package src.main.eav.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import src.main.eav.model.EavEntity;
import src.main.eav.model.EavAttributeValue;
import src.main.eav.model.EavRelation;
import src.main.eav.repository.EavEntityRepository;

import java.util.HashSet;

@Component
public class EavDataInitializer implements CommandLineRunner {

    private final EavEntityRepository eavEntityRepository;

    @Autowired
    public EavDataInitializer(EavEntityRepository eavEntityRepository) {
        this.eavEntityRepository = eavEntityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (eavEntityRepository.count() == 0) {
            // Создаём основную сущность (явно через код)
            EavEntity mainEntity = new EavEntity();
            mainEntity.setType("MainEntity");

            // Инициализируем пустые наборы атрибутов и связей
            mainEntity.setAttributes(new HashSet<>());
            mainEntity.setRelations(new HashSet<>());

            // Добавляем базовый атрибут через EAV (например, "Название")
            EavAttributeValue attributeValue = new EavAttributeValue();
            attributeValue.setAttributeName("Название");
            attributeValue.setValue("Значение основного атрибута");
            attributeValue.setEntity(mainEntity);
            mainEntity.getAttributes().add(attributeValue);

            // Создаём дополнительную сущность, чтобы установить связь
            EavEntity relatedEntity = new EavEntity();
            relatedEntity.setType("RelatedEntity");
            relatedEntity.setAttributes(new HashSet<>());
            relatedEntity.setRelations(new HashSet<>());

            // Сохраняем связанную сущность в БД
            eavEntityRepository.save(relatedEntity);

            // Сохраняем основную сущность в БД (пока без связи)
            eavEntityRepository.save(mainEntity);

            // Создаём связь между основной сущностью и связанной сущностью
            EavRelation relation = new EavRelation();
            relation.setRelationType("linked_to");
            relation.setSource(mainEntity);
            relation.setTarget(relatedEntity);
            mainEntity.getRelations().add(relation);

            // Обновляем основную сущность, чтобы сохранить созданную связь
            eavEntityRepository.save(mainEntity);
        }
    }
}
package src.main.eav.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.main.eav.dao.EavEntityDaoService;
import src.main.eav.dto.EavEntityDto;
import src.main.eav.dto.mapper.EavEntityMapper;
import src.main.eav.model.EavAttributeValue;
import src.main.eav.model.EavEntity;
import src.main.eav.model.EavRelation;
import src.main.eav.error.ResourceNotFoundException;
import src.main.eav.service.EavEntityService;
import src.main.eav.controller.filter.EavEntityFilter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EavEntityServiceImpl implements EavEntityService {

    private final EavEntityDaoService daoService;

    @Autowired
    public EavEntityServiceImpl(EavEntityDaoService daoService) {
        this.daoService = daoService;
    }

    @Override
    @Transactional(readOnly = true)
    public EavEntityDto findById(Long id) {
        EavEntity entity = daoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сущность не найдена, id: " + id));
        return EavEntityMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EavEntityDto> findByType(String type) {
        // Можно использовать метод репозитория напрямую (findByType) или спецификацию,
        // здесь мы вызываем findByType как определено в DAO.
        List<EavEntity> entities = daoService.findByType(type);
        return entities.stream()
                .map(EavEntityMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EavEntityDto> findAll() {
        List<EavEntity> entities = daoService.findAll();
        return entities.stream()
                .map(EavEntityMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EavEntityDto> findAll(EavEntityFilter filter) {
        List<EavEntity> entities = daoService.findAll(filter);
        return entities.stream()
                .map(EavEntityMapper::entityToDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public EavEntityDto save(EavEntityDto dto) {
        // Преобразование DTO в сущность
        EavEntity entity = EavEntityMapper.dtoToEntity(dto);
        // Добавление атрибутов и связей в сущность
        addAttributesAndRelations(entity, dto);
        EavEntity savedEntity = daoService.save(entity);
        return EavEntityMapper.entityToDto(savedEntity);
    }

    @Override
    @Transactional
    public EavEntityDto update(Long id, EavEntityDto dto) {
        EavEntity entity = daoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сущность не найдена, id: " + id));

        // Обновление базовых полей
        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        // Добавление новых атрибутов и связей через единый метод
        addAttributesAndRelations(entity, dto);
        EavEntity updatedEntity = daoService.edit(entity);
        return EavEntityMapper.entityToDto(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteEntity(Long id) {
        daoService.deleteEntity(id);
    }

    /**
     * Метод для добавления атрибутов и связей в сущность из DTO.
     * Избавляет от дублирования кода в методах save() и update().
     *
     * @param entity сущность, в которую добавляются атрибуты и связи
     * @param dto    исходный DTO, содержащий данные
     */
    private void addAttributesAndRelations(EavEntity entity, EavEntityDto dto) {
        // Добавление атрибутов
        if (dto.getAttributes() != null) {
            dto.getAttributes().forEach(attrDto -> {
                EavAttributeValue attribute = new EavAttributeValue();
                attribute.setAttributeName(attrDto.getAttributeName());
                attribute.setValue(attrDto.getValue());
                entity.getAttributes().add(attribute);
                attribute.setEntity(entity);
            });
        }
        // Добавление связей
        if (dto.getRelations() != null) {
            dto.getRelations().forEach(relDto -> {
                EavEntity target = daoService.findById(relDto.getTargetId())
                        .orElseThrow(() -> new ResourceNotFoundException("Сущность не найдена, id: " + relDto.getTargetId()));
                EavRelation relation = new EavRelation();
                relation.setRelationType(relDto.getRelationType());
                relation.setTarget(target);
                relation.setSource(entity);
                entity.getRelations().add(relation);
            });
        }
    }
}
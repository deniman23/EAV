package src.main.eav.dto.mapper;

import src.main.eav.dto.EavEntityDto;
import src.main.eav.dto.EavAttributeValueDto;
import src.main.eav.dto.EavRelationDto;
import src.main.eav.model.EavEntity;
import src.main.eav.model.EavAttributeValue;
import src.main.eav.model.EavRelation;

import java.util.Set;
import java.util.stream.Collectors;

public class EavEntityMapper {

    /**
     * Преобразует сущность EavEntity в DTO.
     */
    public static EavEntityDto entityToDto(EavEntity source) {
        if (source == null) {
            return null;
        }
        EavEntityDto dto = new EavEntityDto();
        dto.setId(source.getId());
        dto.setType(source.getType());

        if (source.getAttributes() != null) {
            // Преобразование Set<EavAttributeValue> в Set<EavAttributeValueDto>
            Set<EavAttributeValueDto> attributesDto = source.getAttributes().stream()
                    .map(EavEntityMapper::attributeEntityToDto)
                    .collect(Collectors.toSet());
            dto.setAttributes(attributesDto);
        }

        if (source.getRelations() != null) {
            // Преобразование Set<EavRelation> в Set<EavRelationDto>
            Set<EavRelationDto> relationsDto = source.getRelations().stream()
                    .map(EavEntityMapper::relationEntityToDto)
                    .collect(Collectors.toSet());
            dto.setRelations(relationsDto);
        }

        return dto;
    }

    /**
     * Преобразует DTO в сущность EavEntity.
     * Атрибуты и связи, как правило, обрабатываются в сервисном слое.
     */
    public static EavEntity dtoToEntity(EavEntityDto dto) {
        if (dto == null) {
            return null;
        }
        EavEntity entity = new EavEntity();
        entity.setId(dto.getId());
        entity.setType(dto.getType());
        // Обработка коллекций (атрибутов и связей) оставлена для бизнес-логики в сервисе
        return entity;
    }

    public static EavAttributeValueDto attributeEntityToDto(EavAttributeValue source) {
        if (source == null) {
            return null;
        }
        EavAttributeValueDto dto = new EavAttributeValueDto();
        dto.setId(source.getId());
        dto.setAttributeName(source.getAttributeName());
        dto.setValue(source.getValue());
        return dto;
    }

    public static EavRelationDto relationEntityToDto(EavRelation source) {
        if (source == null) {
            return null;
        }
        EavRelationDto dto = new EavRelationDto();
        dto.setId(source.getId());
        dto.setRelationType(source.getRelationType());
        if (source.getTarget() != null) {
            dto.setTargetId(source.getTarget().getId());
        }
        return dto;
    }
}
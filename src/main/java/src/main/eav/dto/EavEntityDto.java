package src.main.eav.dto;

import java.util.HashSet;
import java.util.Set;

public class EavEntityDto {
    private Long id;

    private String type;

    private Set<EavAttributeValueDto> attributes = new HashSet<>();

    private Set<EavRelationDto> relations = new HashSet<>();

    public String getType() {
        return type;
    }

    public Set<EavAttributeValueDto> getAttributes() {
        return attributes;
    }

    public Set<EavRelationDto> getRelations() {
        return relations;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAttributes(Set<EavAttributeValueDto> attributes) {
        this.attributes = attributes;
    }

    public void setRelations(Set<EavRelationDto> relations) {
        this.relations = relations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

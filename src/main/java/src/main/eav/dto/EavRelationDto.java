package src.main.eav.dto;

public class EavRelationDto {

    private Long id;

    private String relationType;

    private Long targetId;


    public String getRelationType() {
        return relationType;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

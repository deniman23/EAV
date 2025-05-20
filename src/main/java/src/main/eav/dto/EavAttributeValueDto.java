package src.main.eav.dto;

public class EavAttributeValueDto {

    private Long id;

    private String attributeName;

    private String value;

    public String getAttributeName() {
        return attributeName;
    }

    public String getValue() {
        return value;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

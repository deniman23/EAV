package src.main.eav.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class EavAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Имя атрибута (например, "фио", "телефон", "почта", "описание")
    private String attributeName;

    // Значение атрибута (для упрощения хранится как строка)
    private String value;

    // Ссылка на сущность, к которой относится атрибут
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private EavEntity entity;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getValue() {
        return value;
    }

    public EavEntity getEntity() {
        return entity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setEntity(EavEntity entity) {
        this.entity = entity;
    }

    // Переопределённый equals() без учета поля entity
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EavAttributeValue that = (EavAttributeValue) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(attributeName, that.attributeName) &&
                Objects.equals(value, that.value);
    }

    // Переопределённый hashCode() без учета поля entity
    @Override
    public int hashCode() {
        return Objects.hash(id, attributeName, value);
    }

    // Переопределённый toString() без вывода поля entity (или только с его id, если необходимо)
    @Override
    public String toString() {
        return "EavAttributeValue{" +
                "id=" + id +
                ", attributeName='" + attributeName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
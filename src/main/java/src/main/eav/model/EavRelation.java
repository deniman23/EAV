package src.main.eav.model;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class EavRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Тип связи (например, "customer_of", "order_of")
    private String relationType;

    // Сущность-источник, у которой настраивается связь
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private EavEntity source;

    // Целевая сущность, с которой устанавливается связь
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private EavEntity target;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public String getRelationType() {
        return relationType;
    }

    public EavEntity getSource() {
        return source;
    }

    public EavEntity getTarget() {
        return target;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public void setSource(EavEntity source) {
        this.source = source;
    }

    public void setTarget(EavEntity target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EavRelation that = (EavRelation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(relationType, that.relationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, relationType);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id='" + id + '\'' +
                ", relationType=" + relationType +
                '}';
    }
}
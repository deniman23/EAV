package src.main.eav.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Objects;
import java.util.Set;

@Entity
public class EavEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", unique = true, nullable = false)
    private String type;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<EavAttributeValue> attributes;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<EavRelation> relations;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Set<EavAttributeValue> getAttributes() {
        return attributes;
    }

    public Set<EavRelation> getRelations() {
        return relations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAttributes(Set<EavAttributeValue> attributes) {
        this.attributes = attributes;
    }

    public void setRelations(Set<EavRelation> relations) {
        this.relations = relations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EavEntity that = (EavEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "EavEntity{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
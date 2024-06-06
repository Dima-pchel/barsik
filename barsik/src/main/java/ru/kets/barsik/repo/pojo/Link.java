package ru.kets.barsik.repo.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Type type;
    private String description;
    private String link;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public enum Type {
        YES,
        NO,
        MAYBE
    }

    @Override
    public String toString() {
        return String.join("\n", description, link);
    }
}

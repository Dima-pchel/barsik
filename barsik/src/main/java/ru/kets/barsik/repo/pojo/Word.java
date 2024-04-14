package ru.kets.barsik.repo.pojo;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString(of = {"word"})
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @Column(unique = true)
    private String word;

    private Type type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        ABUSE
    }
}

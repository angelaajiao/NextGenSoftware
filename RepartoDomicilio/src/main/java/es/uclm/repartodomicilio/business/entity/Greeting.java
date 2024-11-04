package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;



@Entity
public class Greeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String person;
    @Column
    private String content;

    public Greeting() {

    }

    public Greeting(String person, String content) {
        super();
        this.person = person;
        this.content = content;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("Greeting [ id=%s person=%s, content=%s]",id, person, content);
    }
}

package com.fri.code.subjects.models.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject_entity")
@NamedQueries(
        value = {
            @NamedQuery(name = "SubjectMetadataEntity.getAll", query = "SELECT subject FROM SubjectMetadataEntity subject"),
            @NamedQuery(name = "SubjectMetadataEntity.getSubjectById", query = "SELECT subject FROM SubjectMetadataEntity subject WHERE subject.ID =?1")
        }
)
public class SubjectMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "name")
    private String name;

    @Column(name = "programmingLanguage")
    private String programmingLanguage;

    @ElementCollection
    private List<Integer> users;

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }
}

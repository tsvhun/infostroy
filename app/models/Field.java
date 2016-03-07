package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Alexander on 01.02.2016.
 */

@Entity
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "FIELD_ID")
    private int fieldId;

    @Column(name = "LABEL")
    private String label;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private Type type;

    @Column(name = "REQUIRED")
    private boolean required;

    @Column(name = "ACTIVE")
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "field", fetch = FetchType.EAGER)
    private Set<Option> options = new HashSet<>();

    @JsonIgnore
    @OneToMany( orphanRemoval = true, mappedBy = "field", fetch = FetchType.EAGER)
    private Set<Answer> answers = new HashSet<>();

    public Field() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldId=" + fieldId +
                ", type=" + type +
                ", required=" + required +
                ", isActive=" + isActive +
                ", options=" + options +
                ", answers=" + answers +
                '}';
    }
}

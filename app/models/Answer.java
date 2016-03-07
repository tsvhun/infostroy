package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 05.02.2016.
 */
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ANSWER_ID")
    private int id;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fieldId")
    private Field field;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FORM_ANSWER_ID")
    private Response response;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ANSWER_OPTION", joinColumns = {
            @JoinColumn(name = "ANSWER_ID", nullable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "OPTION_ID", nullable = false) })
    private Set<Option> options = new HashSet<>();


    public Answer() {
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }



    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", field=" + field +
                ", response=" + response +
                '}';
    }
}

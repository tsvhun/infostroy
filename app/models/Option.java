package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Alexander on 05.02.2016.
 */
@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "OPTION_ID")
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FIELD_ID")
    private Field field;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "options")
    private Set<Answer> answer = new HashSet<>();

    @Column(name = "NAME")
    private String name;

    public Set<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(Set<Answer> answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Option() {
    }


    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", field=" + field +
                ", answer=" + answer +
                ", name='" + name + '\'' +
                '}';
    }
}

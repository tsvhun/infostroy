package models;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Alexander on 05.02.2016.
 */
@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "FROM_ANSWER_ID")
    private int id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "response", fetch = FetchType.EAGER)
    private Set<Answer> answers = new HashSet<>();

    public Response() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }


}

package dao.jpa;

import dao.CRUDinterface;
import models.Field;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Alexander on 16.02.2016.
 */
public class FieldDao implements CRUDinterface<Field> {


    @Override
    public Field create(Field obj) {
        JPA.em().persist(obj);
        return obj;
    }

    @Override
    public Field get(int id) {
        return JPA.em().find(Field.class, id);
    }

    @Override
    public void update(Field obj) {
        if (obj.getAnswers().size() == 0) {
            Field field = get(obj.getFieldId());
            field = obj;
        }
    }

    @Override
    public void delete(int id) {
        JPA.em().remove(JPA.em().find(Field.class, id));
        JPA.em().createQuery("delete from Response r where r.answers.size=0").executeUpdate();
    }

    @Override
    public List<Field> getAll() {

        return JPA.em().createQuery("select e from Field e").getResultList();
    }

    public List<Field> getActiveFields() {
        return JPA.em().createQuery("select e from Field e where e.isActive = true").getResultList();
    }

    public List<Field> getActiveRequiredFields() {
        return JPA.em().createQuery("select e from Field e where e.isActive = true and e.required=true ").getResultList();
    }

    public boolean getFieldByLabel(String label) {
        int i = JPA.em().createQuery("select  e from Field e where e.label=:label").setParameter("label", label).getResultList().size();
        return i == 0;
    }
}

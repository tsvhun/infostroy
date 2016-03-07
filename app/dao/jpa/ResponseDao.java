package dao.jpa;

import dao.CRUDinterface;
import models.Response;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Alexander on 16.02.2016.
 */
public class ResponseDao implements CRUDinterface<Response> {

    @Override
    public Response create(Response obj) {
        JPA.em().persist(obj);
        return obj;
    }

    @Override
    public Response get(int id) {
        return JPA.em().find(Response.class, id);
    }

    @Override
    public void update(Response obj) {
        Response response = JPA.em().find(Response.class, obj.getId());
        response = obj;
    }

    @Override
    public void delete(int id) {
        JPA.em().remove(get(id));
    }

    @Override
    public List<Response> getAll() {
        return JPA.em().createQuery("select e from Response e").getResultList();
    }


}

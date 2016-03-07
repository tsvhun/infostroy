package controllers;

import models.Users;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import service.AuthenticateService;
import service.FieldService;
import views.html.*;

import javax.inject.Inject;


public class Application extends Controller {

    @Inject
    private FieldService fieldService;

    /**
     * Login page
     *
     * @return 200-OK
     */
    @Transactional
    public Result loginGET() {
        return ok(login.render(""));
    }


    /**
     * Authenticates admin from request.
     *
     * @return 200-ok and redirect to responses page, if user is authenticated
     *         400-badRequest if user entered incorrectly login or password
     */
    @Transactional
    public Result loginPOST() {
        AuthenticateService as = new AuthenticateService();
        if (as.authenticate(Form.form().bindFromRequest().data())) {
            session().clear();
            session("username", Form.form().bindFromRequest().get("username"));
            return redirect("/responses");
        } else {
            session().clear();
            return badRequest(login.render("invalid username or password"));
        }
    }

    /**
     * Remove admin data from SESSION and redirect to login page
     * @return redirect to login page
     */
    public Result logOut(){
        session().clear();
        return redirect("/login");
    }

    /**
     * Render response form.
     *
     * @return 200-ok and render response form.
     */
    @Transactional
    public Result index() {
        return ok(index.render(fieldService.getActiveFields()));
    }


}

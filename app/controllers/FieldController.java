package controllers;

import models.Field;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import service.FieldService;
import validation.FieldValidation;
import views.html.createfield;
import views.html.fieldsTemplate;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alexander on 16.02.2016.
 */
public class FieldController extends Controller {
    @Inject
    private FieldService service;
    @Inject
    private FieldValidation validation;


    /**
     * Save new field
     * <p>
     * This action builds Field object from request.
     * If field is valid - return redirect to fields page
     * If field isn't valid - return badRequest with errors as JSON.
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public Result createFieldPOST() {
        Field field = service.makeFieldFromData(Form.form().bindFromRequest().data());
        Map<String, String> errors = validation.validate(field);
        if (errors.isEmpty()) {
            service.saveNewField(field);
            return redirect("/fields");
        } else {
            return badRequest(Json.toJson(errors));
        }
    }


    /**
     * This action renders create-field page
     *
     * @return 200-ok and render create-field page
     */
    @Security.Authenticated(Secured.class)
    public Result createFieldGET() {
        return ok(createfield.render(null));
    }

    /**
     * This action renders update-field page
     *
     * @return 200-ok and render create-field page
     * badRequest -  if search field was not found
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public Result updateFieldGET(int id) {
        Field field = service.getFieldById(id);
        if (field == null) {
            return badRequest();
        } else {
            return ok(createfield.render(field));
        }
    }

    /**
     * This action update field
     *
     * @return redirect to fields page if updated field is valid
     * badRequest with errors as JSON if field isn't valid
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public Result updateFieldPOST() {
        Map<String, String> errors = new HashMap<>();
        Field field = service.makeFieldFromData(Form.form().bindFromRequest().data());
        errors.putAll(service.fieldValidation(field));
        if (errors.isEmpty() && StringUtils.isNumeric(Form.form().bindFromRequest().data().get("fieldid"))) {
            field.setFieldId(Integer.parseInt(Form.form().bindFromRequest().data().get("fieldid")));
            service.updateField(field);
            return redirect("/fields");
        } else {
            return badRequest(Json.toJson(errors));
        }
    }

    /**
     * This action render fields page
     *
     * @return 200-ok and render fields page.
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public Result fieldsPage() {
        return ok(fieldsTemplate.render(service.getAllFields()));
    }


    /**
     * This action deletes field
     *
     * @return 200-ok if id from request is number
     *         badRequest and error message if id from request isn't a number
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public Result deleteField() {
        int id = 0;
        try {
            id = Integer.parseInt(Form.form().bindFromRequest().data().get("id"));
            service.deleteFieldById(id);
        } catch (NumberFormatException e) {
            return badRequest("NumberFormatException");
        }
        return ok();
    }

}

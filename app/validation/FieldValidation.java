package validation;

import dao.jpa.FieldDao;
import models.Field;
import models.Type;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander on 21.02.2016.
 */
public class FieldValidation {
    private Map<String, String> errors = new HashMap<>();
    @Inject
    FieldDao fieldDao;


    private void checkLabelError(Field field) {
        if (field.getLabel().isEmpty()) {
            errors.put("type", "Label is empty");
        }
        if(!fieldDao.getFieldByLabel(field.getLabel())){
            errors.put("label", "this label name is already exists");
        }
    }

    private void checkTypeError(Field field) {
        if (field.getType() == null) {
            errors.put("type", "Type is empty");
        } else if (field.getType().equals(Type.CHECK_BOX) && field.getOptions().isEmpty()) {
            errors.put("option", "No options");
        } else if ((field.getType().equals(Type.COMBO_BOX) || field.getType().equals(Type.RADIO_BUTTON)) && field.getOptions().size()<2) {
            errors.put("option", "There are less than 2 options");
        }
    }

    /**
     * Field validation
     * @param field - Field object
     * @return Map with String key and String value.
     */
    public Map<String, String> validate(Field field){
        errors.clear();
        checkLabelError(field);
        checkTypeError(field);
        return errors;
    }



}

package service;

import dao.jpa.FieldDao;
import models.Field;
import models.Option;
import models.Type;
import play.db.jpa.JPA;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by Alexander on 16.02.2016.
 */
public class FieldService {

    @Inject
    FieldDao fieldDao;

    /**
     * get field by id
     *
     * @param id - field identifier
     * @return Field object
     */
    public Field getFieldById(int id) {
        return fieldDao.get(id);
    }

    /**
     * Make field from data
     *
     * @param map - Map.
     * @return Field object.
     */
    public Field makeFieldFromData(Map<String, String> map) {
        Field field = new Field();
        Set<Option> options = new HashSet<>();
        field.setType(Type.valueOf(map.get("type")));
        if (field.getType() != null) {
            if (field.getType() != null && map.get("type").equals(Type.CHECK_BOX.name()) || map.get("type").equals(Type.COMBO_BOX.name()) || map.get("type").equals(Type.RADIO_BUTTON.name())) {
                for (String opt : map.keySet()) {
                    if (opt.startsWith("option")) {
                        Option option = new Option();
                        option.setName(map.get(opt));
                        option.setField(field);
                        options.add(option);
                    }
                }
            }

        }
        field.setLabel(map.get("label"));
        field.setRequired(map.get("required") != null);
        field.setIsActive(map.get("isactive") != null);
        field.setOptions(options);
        return field;
    }

    /**
     * Field validation
     *
     * @param field - Field object
     * @return map object
     */
    public Map<String, String> fieldValidation(Field field) {
        Map<String, String> errors = new HashMap<>();
        if (field.getType() == null) {
            errors.put("type", "this field is required");
        } else {
            if (field.getType().equals(Type.CHECK_BOX) && field.getOptions().isEmpty()) {
                errors.put("option", "at least one should be filled");
            } else if (field.getType().equals(Type.CHECK_BOX) && !field.getOptions().isEmpty()) {
                if (optionsValidation(field.getOptions()) == false) {
                    errors.put("option", "this field is required");
                }
            } else if (field.getType().equals(Type.RADIO_BUTTON) || field.getType().equals(Type.COMBO_BOX)) {
                if (field.getOptions().size() < 2) {
                    errors.put("option", "at least two should be filled");
                }
            }
        }
        if (field.getLabel().isEmpty()) {
            errors.put("label", "this field is required");
        }
        if (!fieldDao.getFieldByLabel(field.getLabel()) && field.getFieldId() != 0) {
            errors.put("label", "this label is already exists");
        }

        return errors;
    }

    private boolean optionsValidation(Set<Option> options) {
        boolean result = true;
        for (Option option : options) {
            if (option.getName().isEmpty()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Update field
     *
     * @param field Object
     */
    public void updateField(Field field) {
        deleteFieldById(field.getFieldId());
        JPA.em().merge(field);
    }

    /**
     * Save new field
     *
     * @param field - Field object
     * @return field Object
     */
    public Field saveNewField(Field field) {
        return fieldDao.create(field);
    }

    /**
     * Delete field by id
     *
     * @param id - identifier
     */
    public void deleteFieldById(int id) {
        fieldDao.delete(id);
    }


    /**
     * Get all fields
     *
     * @return List of Field object
     */
    public List<Field> getAllFields() {
        return sortListById(fieldDao.getAll());
    }

    private List<Field> sortListById(List<Field> list){
        Collections.sort(list, (c1, c2) -> c2.getFieldId() - c1.getFieldId());
        return list;
    }




    /**
     * Get active fields
     *
     * @return List of Field object
     */
    public List<Field> getActiveFields() {
        return sortListById(fieldDao.getActiveFields());
    }


}

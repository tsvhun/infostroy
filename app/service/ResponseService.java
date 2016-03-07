package service;


import dao.jpa.FieldDao;
import dao.jpa.ResponseDao;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alexander on 17.02.2016.
 */
public class ResponseService {
    @Inject
    private ResponseDao responseDao;
    @Inject
    private FieldDao fieldDao;

    private Map<String, String> errors = new HashMap<>();


    /**
     * Patse response object from map
     *
     * @param map - Map
     * @return Response object;
     */
    public Response parseResponseFromMap(Map<String, String> map) {
        List<Field> fields = fieldDao.getActiveFields();
        Set<Answer> answers = new HashSet<>();
        Response response = new Response();
        try {
            for (Field field : fields) {
                Set<Option> options = new HashSet<>();
                Answer answer = new Answer();
                answer.setResponse(response);
                answer.setField(field);
                if (field.getType().equals(Type.CHECK_BOX)) {
                    for (String s : map.keySet()) {
                        if (s.startsWith(Integer.toString(field.getFieldId()))) {
                            Option option = getOptionById(Integer.parseInt(map.get(s)));
                            if (option != null) {
                                option.setField(field);
                                options.add(option);
                            }
                        }
                    }
                } else if (field.getType().equals(Type.COMBO_BOX) || field.getType().equals(Type.RADIO_BUTTON)) {
                    Option option = getOptionById(Integer.parseInt(map.get(Integer.toString(field.getFieldId()))));
                    options.add(option);
                    answer.getOptions().add(option);
                } else {
                    answer.setValue(map.get(Integer.toString(field.getFieldId())));
                }
                answer.setOptions(options);
                answers.add(answer);
            }
        } catch (NumberFormatException e) {

        }
        response.setAnswers(answers);
        return response;
    }

    /**
     * Get all responses.
     *
     * @return List Response object.
     */
    public List<Response> getAllResponses() {
        return responseDao.getAll();
    }


    /**
     * Response validation
     *
     * @param response - Response Object
     * @return Map with String of name of field which has errors
     */
    public Map<String, String> validate(Response response) {
        errors.clear();
        List<Field> activeFields = fieldDao.getActiveFields();
        Set<Answer> answers = response.getAnswers();
        c:
        for (Field field : activeFields) {
            boolean notFound = true;
            for (Answer answer : answers) {
                if (answer.getField() == field) {
                    notFound = false;
                    if (!isRequiredAnswerValidation(answer)) {
                        continue c;
                    }
                    if (field.getType() == Type.CHECK_BOX || field.getType() == Type.COMBO_BOX || field.getType() == Type.RADIO_BUTTON) {
                        optionAnswerValidation(answer);
                    } else if (field.getType() == Type.SINGLE_LINE_TEXT || field.getType() == Type.TEXTAREA) {
                        textareaSingleValidation(answer);
                    } else if (field.getType() == Type.DATE) {
                        dateValidation(answer);
                    } else if (field.getType() == Type.SLIDER) {
                        sliderValidation(answer);
                    }
                }
            }
            if (notFound) {
                errors.put(field.getLabel(), "field is required");
            }

        }
        return errors;
    }

    /**
     * Save responses in database.
     *
     * @param response - Response object.
     */
    public void saveResponse(Response response) {
        responseDao.create(response);
    }

    private boolean isRequiredAnswerValidation(Answer answer) {
        if (answer.getValue() == null && answer.getOptions().isEmpty()) {
            errors.put(answer.getField().getLabel(), "is required");
            return false;
        }
        return true;
    }

    /**
     * Count of responses.
     *
     * @return int.
     */
    public int countResponses() {
        int result = 0;
        try {
            result = JPA.withTransaction(() -> {
                return responseDao.getAll().size();
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private void dateValidation(Answer answer) {
        try {
            format.parse(answer.getValue());
        } catch (ParseException e) {
            errors.put(answer.getField().getLabel(), "Invalid date format");
        }
    }

    private void sliderValidation(Answer answer) {
        Pattern pattern = Pattern.compile("[0-9]{1}");
        boolean sliderValid = pattern.matcher(answer.getValue()).matches();
        if (!sliderValid) {
            errors.put(answer.getField().getLabel(), "must be from 0 to 9");
        } else if (answer.getValue().isEmpty() && answer.getField().isRequired()) {
            errors.put(answer.getField().getLabel(), "field is required");
        }
    }

    private void textareaSingleValidation(Answer answer) {
        if (answer.getValue().length() > 255) {
            errors.put(answer.getField().getLabel(), "max length is 255");
        } else if (answer.getValue().isEmpty() && answer.getField().isRequired()) {
            errors.put(answer.getField().getLabel(), "field is required");
        }
    }

    private void optionAnswerValidation(Answer answer) {
        if (answer.getOptions().size() < 1 && answer.getField().isRequired()) {
            errors.put(answer.getField().getLabel(), "field is required");
        }
    }


    private Option getOptionById(int id) {
        return JPA.em().find(Option.class, id);
    }
}

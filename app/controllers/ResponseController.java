package controllers;

import models.Response;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import security.Secured;
import service.FieldService;
import service.ResponseService;
import views.html.responsesTemplate;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alexander on 21.02.2016.
 */
@Transactional
public class ResponseController extends Controller {
    @Inject
    ResponseService service;
    @Inject
    FieldService fieldService;

    private static final Set<WebSocket.Out<String>> SOCKETS = new HashSet<>();


    /**
     * This action render responses page.
     * @return 200-ok and renders responses page.
     */
    @Security.Authenticated(Secured.class)
    public Result responsesGET(){
        return ok(responsesTemplate.render(service.getAllResponses(), fieldService.getAllFields()));
    }

    /**
     * This web socket send count of responses to all its connections
     * @return WebSocket object
     */
    @Security.Authenticated(Secured.class)
    public WebSocket<String> socket() {
        return WebSocket.whenReady((in, out) -> {
            SOCKETS.add(out);
            out.write("{\"count\":" + service.countResponses() + "}");
            in.onMessage((String message) -> {
                SOCKETS.forEach(s -> s.write(message));
            });
            in.onClose(() -> {
                SOCKETS.remove(out);
            });
        });
    }

    /**
     * This action save response.
     * @return 200-ok if response is valid
     *         badRequest with errors as JSON,  if response isn't valid
     */
    public Result responsePOST(){
        Response response = service.parseResponseFromMap(Form.form().bindFromRequest().data());
        Map<String, String> errors = service.validate(response);
        if(errors.isEmpty()){
            service.saveResponse(response);
            for (WebSocket.Out<String> socket : SOCKETS) {
                String json = Json.toJson(response.getAnswers()).toString();
                socket.write(json);
            }
            return ok();
        } else {
            return badRequest(Json.toJson(errors));
        }
    }


}

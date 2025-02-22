package Controller;

import Service.SocialMediaService;
import Model.Account;
import Model.Message;

import java.util.ArrayList;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    SocialMediaService service;
    ObjectMapper mapper;

    public SocialMediaController() {
        this.service = new SocialMediaService();
        this.mapper = new ObjectMapper();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerUser);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUser(Context context) throws JsonProcessingException {
        Account newAccount = mapper.readValue(context.body(), Account.class);
        Account addedAccount = service.registerAccount(newAccount);

        if (addedAccount == null) { // registration failed
            context.status(400);
        } else {
            context.status(200);
            context.json(addedAccount);
        }

    }

    private void login(Context context) throws JsonProcessingException {
        Account accLogin = mapper.readValue(context.body(), Account.class);
        Account loggedInAccount = service.login(accLogin);

        if (loggedInAccount == null) { // login failed
            context.status(401);
        } else {
            context.status(200);
            context.json(loggedInAccount);
        }
    }

    private void createMessage(Context context) throws JsonProcessingException {
        Message message = mapper.readValue(context.body(), Message.class);
        Message postedMessage = service.createMessage(message);

        if (postedMessage == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(postedMessage);
        }

    }

    private void getAllMessages(Context context) {
        ArrayList<Message> messages = service.getAllMessages();
        
        context.status(200);
        context.json(messages);
    }

    private void getMessageById(Context context) {
        int message_id =  Integer.valueOf(context.pathParam("message_id"));
        Message message = service.getMessageById(message_id);

        context.status(200);
        if (message != null) { // if message null response body remains empty
            context.json(message);
        }
    }

    private void deleteMessageById(Context context) {
        int message_id =  Integer.valueOf(context.pathParam("message_id"));
        Message deletedMessage = service.deleteMessageById(message_id);

        context.status(200);
        if (deletedMessage != null) {
            context.json(deletedMessage);
        }
    }

    private void updateMessageById(Context context) throws JsonProcessingException {
        int message_id =  Integer.valueOf(context.pathParam("message_id"));
        String message_text = mapper.readValue(context.body(), Message.class).message_text;

        Message newMessage = service.updateMessageById(message_id, message_text);

        if (newMessage == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(newMessage);
        }
    }

    private void getAllMessagesFromUser(Context context) {
        int account_id =  Integer.valueOf(context.pathParam("account_id"));
        ArrayList<Message> messages = service.getAllMessagesFromUser(account_id);

        context.json(messages);
        context.status(200);
    }


}
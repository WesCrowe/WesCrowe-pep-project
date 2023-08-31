package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //////////////////////
        /* ACCOUNT HANDLERS */
        //////////////////////

        //get all accounts
        app.get("/accounts", this::getAllAccountsHandler);
        
        //create new account
        app.post("/register", this::addAccountHandler);
        
        //login to an account
        app.post("/login", this::loginAccountHandler);

        //////////////////////
        /* MESSAGE HANDLERS */
        //////////////////////

        //get all messages
        app.get("/messages", this::getAllMessagesHandler);
        
        //get a message by message ID
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        
        //get all the messages posted by an account
        app.get("/accounts/{account_id}/messages", this::getAllMessagesOfAccountHandler);
        
        //post messages
        app.post("/messages", this::postMessageHandler);

        //update message text
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        
        //delete a message
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        return app;
    }

    /**
     * Handler to retrieve all accounts.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllAccountsHandler(Context ctx) {
        List<Account> accounts = accountService.getAllAccounts();
        ctx.json(accounts);
    }

    /**
     * Handler to post accounts
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void addAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        
        if(addedAccount == null){ ctx.status(400); }
        else{
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    /**
     * Handler to login to accounts
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException
     */
    private void loginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedAccount = accountService.getAccountByUsernameAndPassword(account);

        if(loggedAccount == null){ ctx.status(401); }
        else{
            ctx.json(mapper.writeValueAsString(loggedAccount));
            ctx.status(200);
        }
    }

    /**
     * Handler to retrieve all messages.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * TODO: Handler to retrieve a message based on message_id.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(ctx.body(), Message.class);
        Message targetMessage = messageService.getMessageById(message);

        ctx.json(mapper.writeValueAsString(targetMessage));
        ctx.status(200);
    }

    /**
     * Handler to post messages.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        
        if(addedMessage == null){ ctx.status(400); }
        else{
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }
    }

    /**
     * TODO: Handler to update a message.
     *
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        //System.out.println(updatedMessage);
        
        if(updatedMessage == null){ ctx.status(400); }
        else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }
    }

    /**
     * TODO: Handler to delete a message based on message_id.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message targetMessage = mapper.readValue(ctx.body(), Message.class);

        if ((targetMessage = messageService.deleteMessage(targetMessage)) != null){
            ctx.json(mapper.writeValueAsString(targetMessage));
        }

        ctx.status(200);
    }

    /**
     * TODO: Handler to retrieve all messages of an account
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllMessagesOfAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        //Account targetAccount = mapper.readValue(ctx.body(), Account.class);
        //List<Message> messages = messageService.getMessagesByAccount(targetAccount);

        Message posted_by = mapper.readValue(ctx.body(), Message.class);
        List<Message> messages = messageService.getMessagesByAccount(posted_by);
        
        ctx.json(messages);
        ctx.status(200);
    }
}
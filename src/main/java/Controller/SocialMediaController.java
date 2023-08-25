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

        //Account handlers
        app.get("/accounts", this::getAllAccountsHandler);             //get all accounts
        //app.get("/accounts/{account_id}", this::getAccountByIdHandler);
        app.post("/register", this::postAccountHandler);               //create new account
        app.post("/login", this::loginAccountHandler);                 //login to an account
        app.put("/accounts/{account_id}", this::updateAccountHandler); //update an account

        //Message handlers
        app.get("/messages", this::getAllMessagesHandler);              //get all messages
        app.get("/messages/{message_id}", this::getMessageByIdHandler); //get a message by ID
        app.post("/messages", this::postMessagesHandler);               //post messages
        app.put("/messages/{message_id}", this::updateMessageHandler);  //edit messages

        return app;
    }

    /**
     * Handler to retrieve all accounts.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllAccountsHandler(Context ctx) {
        List<Account> accounts = accountService.getAllAccounts();
        ctx.json(accounts);
    }

    private void getAccountByIdHandler(Context ctx) {
        List<Account> accounts = accountService.getAllAccounts();
        ctx.json(accounts);
    }

    /**
     * Handler to post accounts
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to login to accounts
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException
     */
    private void loginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedAccount = accountService.getAccountByUsernameAndPassword(account);

        if(loggedAccount != null){
            ctx.json(mapper.writeValueAsString(loggedAccount));
        }
        else{
            ctx.status(401);
        }
    }

    /**
     * Handler to update an account.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * to conform to RESTful standards, the account that is being updated is identified from the path parameter,
     * but the information required to update an account is retrieved from the request body.
     * If accountService returns a null account (meaning updating an account was unsuccessful), the API will return a 400
     * status (client error).
     *
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        Account updatedAccount = accountService.updateAccount(account_id, account);
        System.out.println(updatedAccount);
        if(updatedAccount == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedAccount));
        }
    }

    /**
     * Handler to retrieve all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /**
     * Handler to retrieve a message based on message_id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(ctx.body(), Message.class);
        Message targetMessage = messageService.getMessageById(message);

        ctx.json(mapper.writeValueAsString(targetMessage));
    }

    /**
     * Handler to post messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void postMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to update a message.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * to conform to RESTful standards, the account that is being updated is identified from the path parameter,
     * but the information required to update an account is retrieved from the request body.
     * If accountService returns a null account (meaning updating an account was unsuccessful), the API will return a 400
     * status (client error).
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
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

}
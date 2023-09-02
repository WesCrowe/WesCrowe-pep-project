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
        
        //username is not blank and
        //the password is at least 4 characters long and
        //and an Account with that username does not already exist.
        System.out.println(accountService.getAccountByUsername(account));
        if (account.getUsername() != "" &&
            account.getPassword().length() > 3 &&
            accountService.getAccountByUsername(account) == null){
                
                //add account and return it through context
                Account addedAccount = accountService.addAccount(account);
                ctx.json(mapper.writeValueAsString(addedAccount));
        }
        else{
            //ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(400);
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
    }

    /**
     * Handler to retrieve a message based on message_id.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        /* Get message id from path /messages/{message_id} */

        //get the index where the last slash appears
        int slashIndex = 0;
        for (int i = ctx.path().length()-1; i > 0; i--){
            if (ctx.path().charAt(i) == '/'){
                slashIndex = i;
            }
        }

        //get the substring starting from the index after the slash
        //up to excluding the length of the path
        String id = ctx.path().substring(slashIndex+1, ctx.path().length());

        //turn the substring into an integer
        int message_id = Integer.parseInt(id);

        //get the target message
        Message targetMessage = messageService.getMessageById(message_id);

        //if the message exists, return it through the context
        if (targetMessage != null){
            ctx.json(mapper.writeValueAsString(targetMessage));
        }
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
        
        //the posted message is not null and
        //message_text is not blank and
        //is under 255 characters and
        //posted_by refers to a real, existing user.
        if (addedMessage != null &&
            message.getMessage_text() != "" &&
            message.getMessage_text().length() < 255 &&
            accountService.getAccountById(message.getPosted_by()) != null){
            
            //add the message
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
        else{ ctx.status(400); }
    }

    /**
     * Handler to update a message.
     *
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        /* Get message id from path /messages/{message_id} */

        //get the index where the last slash appears
        int slashIndex = 0;
        for (int i = ctx.path().length()-1; i > 0; i--){
            if (ctx.path().charAt(i) == '/'){
                slashIndex = i;
            }
        }

        //get the substring starting from the index after the last slash
        //and ending exclusive of the path's total length
        String id = ctx.path().substring(slashIndex+1, ctx.path().length());

        //turn the substring into an integer
        int message_id = Integer.parseInt(id);

        //get the message with a matching id
        Message targetMessage = messageService.getMessageById(message_id);

        //get the updated message information
        Message message = mapper.readValue(ctx.body(), Message.class);

        /* Update the message */

        //make sure the message exists and
        //the new message_text is not blank and
        //the new message_text is not over 255 characters
        if (targetMessage != null &&
            message.getMessage_text() != "" &&
            message.getMessage_text().length() < 255){

                //write the updated message into the database
                Message updatedMessage = messageService.updateMessage(message_id, message);

                //write the updated message into the return body
                ctx.json(mapper.writeValueAsString(updatedMessage));
            }
        else{ ctx.status(400); }
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

        //Message posted_by = mapper.readValue(ctx.body(), Message.class);
        //List<Message> messages = messageService.getMessagesByAccount(posted_by);

        Account account = mapper.readValue(ctx.endpointHandlerPath(), Account.class);
        List<Message> messages = messageService.getMessagesByAccount(account);
        
        ctx.json(messages);
    }
}
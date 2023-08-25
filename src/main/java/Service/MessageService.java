package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 */
public class MessageService {
    private MessageDAO messageDAO;
    /**
     * no-args constructor for creating a new MessageService with a new MessageDAO.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * Constructor for an MessageService when an MessageDAO is provided.
     * This is used for when a mock MessageDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of MessageService independently of MessageDAO.
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    /**
     * Use the MessageDAO to retrieve all authors.
     *
     * @return all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    /**
     * Use the MessageDAO to persist an message. The given Message will not have an id provided.
     *
     * @param message an Message object.
     * @return The persisted message if the persistence is successful.
     */
    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    /**
     * Use the MessageDAO to update an existing message from the database.
     *
     * @param message_id the ID of the message to be modified.
     * @param message an object containing all data that should replace the values contained by the existing message_id.
     *         the message object does not contain an message ID.
     * @return the newly updated message if the update operation was successful. Return null if the update operation was
     *         unsuccessful.
     */
    public Message updateMessage(int message_id, Message message){
        if (messageDAO.getMessageById(message_id) == null){
            return null;
        }
        messageDAO.updateMessage(message_id, message);
        return messageDAO.getMessageById(message_id);
    }
    
}

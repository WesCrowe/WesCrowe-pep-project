package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 */
public class AccountService {
    private AccountDAO accountDAO;
    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    /**
     * Constructor for an AccountService when an AccountDAO is provided.
     * This is used for when a mock AccountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AccountService independently of AccountDAO.
     * 
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    
    /**
     * Use the AccountDAO to retrieve all accounts.
     *
     * @return all accounts
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /**
     * Use the AccountDAO to persist an account. The given Account will not have an id provided.
     *
     * @param account an Account object.
     * @return The persisted account if the persistence is successful.
     */
    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }

    /**
     * Use the AccountDAO to retrieve an account by username.
     *
     * @return account with matching username
     */
    public Account getAccountByUsername(Account account) {
        return accountDAO.getAccountByUsername(account.getUsername());
    }

    /**
     * Use the AccountDAO to retrieve an account by id.
     *
     * @return account with matching id
     */
    public Account getAccountById(Account account) {
        return accountDAO.getAccountById(account.getAccount_id());
    }
    public Account getAccountById(int account_id) {
        return accountDAO.getAccountById(account_id);
    }

    /**
     * Use the AccountDAO to retrieve an account by username and password
     *
     * @return account with matching username and password. If unsuccessful, return null
     */
    public Account getAccountByUsernameAndPassword(Account account) {
        Account acc = getAccountByUsername(account);
        
        /* I could have used && logic here, but this looks nicer */
        //check to make sure the username exists
        if (acc != null){ 
            //check if the password matches
            if (acc.getPassword().compareTo(account.getPassword()) == 0){
                return acc;
            }
        }
        
        return null;
    }
}

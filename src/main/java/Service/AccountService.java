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
     * Use the AccountDAO to retrieve an account by username.
     *
     * @return account with matching username
     */
    public Account getAccountByUsername(Account account) {
        return accountDAO.getAccountByUsername(account.getUsername());
    }

    /**
     * Use the AccountDAO to retrieve an account by username and password
     *
     * @return account with matching username and password. If unsuccessful, return null
     */
    public Account getAccountByUsernameAndPassword(Account account) {
        Account acc = getAccountByUsername(account);
        if (acc.getPassword().compareTo(account.getPassword()) == 0){
            return acc;
        }
        return null;
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
     * Use the AccountDAO to update an existing account from the database.
     *
     * @param account_id the ID of the account to be modified.
     * @param account an object containing all data that should replace the values contained by the existing account_id.
     *         the account object does not contain an account ID.
     * @return the newly updated account if the update operation was successful. Return null if the update operation was
     *         unsuccessful.
     */
    public Account updateAccount(int account_id, Account account){
        if (accountDAO.getAccountById(account_id) == null){
            return null;
        }
        accountDAO.updateAccount(account_id, account);
        return accountDAO.getAccountById(account_id);
    }
    
}

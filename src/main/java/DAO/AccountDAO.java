package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Account;
import Util.ConnectionUtil;

/**
 * A DAO is a class that mediates the transformation of data between the format of 
 * objects in Java to rows in a database.
 * 
 * Table "Account"
 * account_id integer primary key auto_increment,
 * username varchar(255),
 * password varchar(255)
 */
public class AccountDAO {

    /**
     * Retrieve all accounts from the account table.
     *
     * @return all accounts.
     */
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //SQL query
            String sql = "SELECT * FROM account;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                                              rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * Add an account record into the database which matches the values contained in the account object.
     * The account_id will be automatically generated by the SQL database, and JDBC will be able
     * to retrieve the generated ID automatically.
     *
     * @param Account an object modelling an Account. the Account object does not contain an account ID.
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            //SQL Query
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //set the username and password
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            //execute query and generate primary key
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            //return account with generated primary key
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        return null;
    }

    /**
     * Retrieve a specific account using its username.
     *
     * @param username an account username.
     */
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL Query
            String sql = "SELECT * FROM account WHERE username=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //set the username
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                                              rs.getString("password"));
                return account;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve a specific account using its account ID.
     *
     * @param id an account ID.
     */
    public Account getAccountById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL query
            String sql = "SELECT * FROM account WHERE account_id=?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //set the id
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                                              rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}

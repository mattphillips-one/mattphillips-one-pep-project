package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    /**
     * Adds user to account database
     * @param acc Account object without account id
     * @return new Account object with newly created account id
     */
    public Account addUser(Account acc) {
        Connection conn = ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int id = rs.getInt("account_id");
                //return new Account(id, acc.getUsername(), acc.getPassword());
                acc.setAccount_id(id);
                return acc;
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * @param acc Account object with username and password fields to be tested
     * @return Account object of user if username and password correct, null otherwise
     */
    public Account login(Account acc) {
        if (acc == null)
            return null;
        
        Connection conn = ConnectionUtil.getConnection();
        
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("account_id");
                return new Account(id, acc.getUsername(), acc.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @param username
     * @return true if account with username exists in database
     */
    public boolean exists(String username) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * @param username
     * @return true if account with account_id exists in database
     */
    public boolean exists(int account_id) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
}

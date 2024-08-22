package BankingManagement;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Account {

	private Scanner scn;
	private Connection connection;

	public Account(Connection connection, Scanner scn2) {
		
				this.scn = scn;
				this.connection = connection;
	}

	public long openAccount(String email) {
		 if(!account_exist(email)) {
	            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
	            scn.nextLine();
	            System.out.print("Enter Full Name: ");
	            String full_name = scn.nextLine();
	            System.out.print("Enter Initial Amount: ");
	            double balance = scn.nextDouble();
	            scn.nextLine();
	            System.out.print("Enter Security Pin: ");
	            String security_pin = scn.nextLine();
	            try {
	                long account_number = generateAccountNumber();
	                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
	                preparedStatement.setLong(1, account_number);
	                preparedStatement.setString(2, full_name);
	                preparedStatement.setString(3, email);
	                preparedStatement.setDouble(4, balance);
	                preparedStatement.setString(5, security_pin);
	                int rowsAffected = preparedStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    return account_number;
	                } else {
	                    throw new RuntimeException("Account Creation failed!!");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        throw new RuntimeException("Account Already Exist");

	}
	
	public long getAccountNumber(String email) {
		  String query = "SELECT account_number from Accounts WHERE email = ?";
	        try{
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, email);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if(resultSet.next()){
	                return resultSet.getLong("account_number");
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        throw new RuntimeException("Account Number Doesn't Exist!");
	        
      
		
	}
	
	public long generateAccountNumber() {
		/*  try {
	            Statement statement = ConnectException.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
	            if (resultSet.next()) {
	                long last_account_number = resultSet.getLong("account_number");
	                return last_account_number+1;
	            } else {
	                return 10000100;
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        return 10000100;

		return 0;
		*/
		  String query = "SELECT account_number FROM Accounts ORDER BY account_number DESC LIMIT 1";
	        long nextAccountNumber = 10000100; // Default value if no records are found

	        try (Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(query)) {

	            if (resultSet.next()) {
	                long lastAccountNumber = resultSet.getLong("account_number");
	                nextAccountNumber = lastAccountNumber + 1;
	            }

	        } catch (SQLException e) {
	            e.printStackTrace(); // Consider logging this exception instead
	        }

	        return nextAccountNumber;

	}
	
	public boolean account_exist(String email) {
		
		  String query = "SELECT account_number from Accounts WHERE email = ?";
	        try{
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, email);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if(resultSet.next()){
	                return true;
	            }else{
	                return false;
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        
	        return false;

	}
}

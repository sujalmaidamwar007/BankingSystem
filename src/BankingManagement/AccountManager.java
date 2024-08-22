package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	private Scanner scn;
	private Connection connection;


	public AccountManager(Connection connection, Scanner scn) {
		this.scn = scn;
		this.connection = connection;
	}

	public void credit_money(long account_number) {
		System.out.println("Enter Ammount:");
		double ammount = scn.nextDouble();
		
		System.out.println("Enter Security pin:");
		String securityPin = scn.nextLine();
		
        try {
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, ammount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs."+ammount+" credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		
	}
	
	public void debit_money(long account_number) {
		System.out.println("Enter Ammount:");
		double ammount = scn.nextDouble();
		
		System.out.println("Enter Security pin:");
		String securityPin = scn.nextLine();
		
		try {
            connection.setAutoCommit(false);
            if(account_number!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (ammount<=current_balance){
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, ammount);
                        preparedStatement1.setLong(2, account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs."+ammount+" debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }
            }
        }catch (SQLException e){
                e.printStackTrace();
            }
        try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public void transfer_money(long sender_account_number) throws SQLException {
		System.out.println("Enter Receiver Account Number:");
		long recevierAccount_number = scn.nextLong();
		System.out.println("Enter Ammount:");
		double ammount = scn.nextDouble();
		System.out.println("Enter Security Pin:");
		String securityPin = scn.nextLine();
		
		 try{
	            connection.setAutoCommit(false);
	            if(sender_account_number!=0 && recevierAccount_number!=0){
	                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
	                preparedStatement.setLong(1, sender_account_number);
	                preparedStatement.setString(2, securityPin);
	                ResultSet resultSet = preparedStatement.executeQuery();

	                if (resultSet.next()) {
	                    double current_balance = resultSet.getDouble("balance");
	                    if (ammount<=current_balance){

	                       
	                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
	                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

	                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
	                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

	                       
	                        creditPreparedStatement.setDouble(1, ammount);
	                        creditPreparedStatement.setLong(2, recevierAccount_number);
	                        debitPreparedStatement.setDouble(1, ammount);
	                        debitPreparedStatement.setLong(2, sender_account_number);
	                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
	                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
	                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
	                            System.out.println("Transaction Successful!");
	                            System.out.println("Rs."+ammount+" Transferred Successfully");
	                            connection.commit();
	                            connection.setAutoCommit(true);
	                            return;
	                        } else {
	                            System.out.println("Transaction Failed");
	                            connection.rollback();
	                            connection.setAutoCommit(true);
	                        }
	                    }else{
	                        System.out.println("Insufficient Balance!");
	                    }
	                }else{
	                    System.out.println("Invalid Security Pin!");
	                }
	            }else{
	                System.out.println("Invalid account number");
	            }
		   }catch (SQLException e){
	            e.printStackTrace();
	        }
	        connection.setAutoCommit(true);
}
	
	
	public void getBalance(long account_number) {
		
		System.out.println("Enter Security Pin:");
		String securityPin = scn.nextLine();
		
		try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
 
        }
	}
	
}

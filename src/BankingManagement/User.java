package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	
	private Scanner scn;
	private Connection connection;
	public User(Connection connection, Scanner scn) {
		// TODO Auto-generated constructor stub
		this.scn = scn;
		this.connection = connection;
	}
	


	public void register() {
		System.out.println("Full Name:");
		String fullName = scn.nextLine();

		System.out.println("Email:");
		String email = scn.nextLine();
		
		System.out.println("Password:");
		String password = scn.nextLine();
		
		if(user_Exist(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        String register_query = "INSERT INTO User(fullName, email, password) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Registration Successfull!");
            } else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


	
	public String login() {
		System.out.println("Email:");
		
		String email = scn.nextLine();
		
		System.out.println("Password:");
		String password = scn.nextLine();
		 String login_query = "SELECT * FROM User WHERE email = ? AND password = ?";

		 try{
	            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
	            preparedStatement.setString(1, email);
	            preparedStatement.setString(2, password);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if(resultSet.next()){
	                return email;
	            }else{
	                return null;
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }

		return null;
	}
	
	public boolean user_Exist(String email) {
		String query = "SELECT * FROM user WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

		return false;
	}
}

package bams;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

	public void createAccount(String name, Double balance) {
		String sql = "INSERT INTO accounts (name, balance) values (?,?)";

		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, name);
			ps.setDouble(2, balance);

			int rows = ps.executeUpdate();
			if (rows > 0) {
				System.out.println("Account Created Successfully");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getAccountDetials(int accountId) {
		String sql = "SELECT * FROM accounts WHERE account_id = ?";

		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accountId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("Account Id: " + rs.getInt("account_id"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Available Balance: ₹" + rs.getDouble("balance"));
			}

			else {
				System.out.println("No account found with the given id: " + accountId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deposit(int accountId, double amount) {
		String sql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";

		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDouble(1, amount);
			ps.setInt(2, accountId);

			int rows = ps.executeUpdate();
			if (rows > 0) {
				try (PreparedStatement ps1 = con
						.prepareStatement("SELECT balance FROM accounts WHERE account_id = ?")) {
					
					ps1.setInt(1, accountId);
					ResultSet rs = ps1.executeQuery();
					if(rs.next()) {
						
						System.out.println("Deposit Successfull available balance: "+rs.getDouble("balance"));
					}
				}
			} else {
				System.out.println("Account not found");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void withdraw(int accountId, double amount) {
		String checkbalance = "SELECT balance FROM accounts WHERE account_id = ?";
		String sql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";

		try {
			Connection con = DBConnection.getConnection();

			PreparedStatement ps1 = con.prepareStatement(checkbalance);
			PreparedStatement ps = con.prepareStatement(sql);
			ps1.setInt(1, accountId);
			ResultSet rs = ps1.executeQuery();
			if (rs.next()) {
				double currentBalance = rs.getDouble("balance");
				if (currentBalance >= amount) {
					ps.setDouble(1, amount);
					ps.setInt(2, accountId);
					ps.executeUpdate();
					System.out.println("Withdrawal successfull, withfrawal amount: ₹" + amount);
				} else {
					System.out.println("Insufficient balance.");
				}
			} else {
				System.out.println("Account not found");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void checkAccountBalance(int accountId) {
		String sql = "SELECT balance FROM accounts WHERE account_id = ?";
		try(Connection con = DBConnection.getConnection()){
			try(PreparedStatement ps = con.prepareStatement(sql)){
				ps.setInt(1, accountId);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					System.out.println("Available balance "+rs.getDouble("balance"));
				}
				System.out.println("Account not found");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

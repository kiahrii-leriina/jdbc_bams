package bams;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {

	public void transferMoney(int fromAccountId, int toAccountId, double amount) {
		if (amount < 0) {
			System.out.println("Transfer amount must not be zero.");
			return;
		}
		if (fromAccountId == toAccountId) {
			System.out.println("Sender and reciever account must be different.");
			return;
		}

		String checkbalance = "SELECT balance FROM accounts WHERE account_id = ?";
		String debitsql = "UPDATE accounts SET balance  =  balance - ? WHERE account_id = ?";
		String creditsql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
		String insertTransaction = "INSERT INTO transactions(from_account, to_account, amount) VALUES (?,?,?)";

		try {

			try (Connection con = DBConnection.getConnection()) {
				con.setAutoCommit(false);

				try (PreparedStatement ps1 = con.prepareStatement(checkbalance)) {

					ps1.setInt(1, toAccountId);
					ResultSet rs = ps1.executeQuery();
					if (!rs.next() || rs.getDouble("balance") < amount) {
						System.out.println("Insufficient balance or sender account not found");
						con.rollback();
						return;
					}
				}
				try (PreparedStatement debitstmt = con.prepareStatement(debitsql)) {
					debitstmt.setDouble(1, amount);
					debitstmt.setInt(2, fromAccountId);
					int rows = debitstmt.executeUpdate();
					if (rows > 0) {
						try (PreparedStatement debitmsg = con
								.prepareStatement("SELECT * FROM accounts WHERE account_id = ?")) {
							debitmsg.setInt(1, fromAccountId);
							ResultSet rs = debitmsg.executeQuery();
							System.out.println("Dear " + rs.getString("name") + " your account is debited with ₹"
									+ amount + " available balance " + rs.getDouble("balance"));
						}
					}
				}

				try (PreparedStatement creditstmt = con.prepareStatement(creditsql)) {
					creditstmt.setDouble(1, amount);
					creditstmt.setInt(1, toAccountId);
					int rows = creditstmt.executeUpdate();
					if (rows > 0) {
						try (PreparedStatement creditmsg = con
								.prepareStatement("SELECT * FROM accounts WHERE account_id = ?")) {
							creditmsg.setInt(1, toAccountId);
							ResultSet rs = creditmsg.executeQuery();
							System.out.println("Dear " + rs.getString("name") + " you account is credited with ₹"
									+ amount + " available balance " + rs.getDouble("balance"));
						}
					}
					else {
						System.out.println("Recievers bank not found");
						con.rollback();
						return;
					}
				}
				
				
				try(PreparedStatement insertsmt = con.prepareStatement(insertTransaction)){
					insertsmt.setInt(1, fromAccountId);
					insertsmt.setInt(2, toAccountId);
					insertsmt.setDouble(3, amount);
					
					int rows = insertsmt.executeUpdate();
					if(rows>0) {
						System.out.println("Transaction saved sucessfully");
					}
				}
				con.commit();
				
				System.out.println("Transaction successfull");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void getTransactionHistory(int accountId) {
		String sql = "SELECT * FROM transactions WHERE from_account = ? OR to_account = ? ORDER BY timestamp DESC";
		
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, accountId);
			ps.setInt(2, accountId);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				System.out.printf("Txn ID: %d | From: %d To: %d | Amount: ₹%.2f | Date: %s/n",
						rs.getInt("transaction_id"),
						rs.getInt("from_account"),
						rs.getInt("to_account"),
						rs.getDouble("amount"),
						rs.getTimestamp("timestamp"));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

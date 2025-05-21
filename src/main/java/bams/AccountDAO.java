package bams;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountDAO {

	public void createAccount(String name, Double balance) {
		String sql = "INSERT INTO accounts (name, balance) values (?,?)";

		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, name);
			ps.setDouble(2, balance);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

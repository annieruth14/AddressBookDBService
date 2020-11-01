package AddressBookDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/AddressBookService?useSSL=false";
		String userName = "root";
		String password = "Admin@123";
		Connection connection;
		System.out.println("Connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password); // used DriverManager to get the connection
		return connection;
	}

	public ArrayList<AddressBook> getCount() throws AddressBookException {
		String sql = "select * from AddressBook;";
		ArrayList<AddressBook> addressList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			addressList = this.getPersonData(result);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
		}
		return addressList;	
	}

	private ArrayList<AddressBook> getPersonData(ResultSet resultSet) throws AddressBookException {
		ArrayList<AddressBook> list = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString(1);
				String lastName = resultSet.getString(2);
				String address = resultSet.getString(3);
				String city = resultSet.getString(4);
				String state = resultSet.getString(5);
				int zip = resultSet.getInt(6);
				long phone = resultSet.getLong(7);
				String email = resultSet.getString(8);
				list.add(new AddressBook(firstName, lastName, address, city, state, zip, phone, email));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
		}
		return list;
	}
}

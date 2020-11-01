package AddressBookDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	
	private PreparedStatement addressBookDataStatement;
	
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

	public int updateData(String name, String address) throws AddressBookException {
		String sql = "update AddressBook set address = ? where first_name = ? ;";
		try {
			Connection connection = getConnection();
			addressBookDataStatement = connection.prepareStatement(sql);
			addressBookDataStatement.setString(1, address);
			addressBookDataStatement.setString(2, name);
			return addressBookDataStatement.executeUpdate();
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
		}
	}

	public List<AddressBook> getPerson(String name) throws AddressBookException {
		List<AddressBook> list = new ArrayList<>();
		String sql = "Select * from AddressBook where first_name = ?;";
		try {
			Connection connection = getConnection();
			addressBookDataStatement = connection.prepareStatement(sql);
			addressBookDataStatement.setString(1, name);
			ResultSet resultSet = addressBookDataStatement.executeQuery();
			list = getPersonData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
		}
		return list;
	}

	public AddressBook addPersonWithDetails(int bookId, String bookName, String bookType, String firstName, String lastName, long phone, String email, String state, String city, String zip) throws AddressBookException {
		int personId = -1;
		Connection connection = null;
		AddressBook contact = null;
		
		// establishing connection
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
		}
		
		// inserting to first table
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("insert into person (book_id, name, phone, email) values (%s, '%s', %s, '%s');", bookId, firstName, phone, email) ;
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					personId = resultSet.getInt(1);
			} 
		} catch (SQLException e) {
			try {
				e.printStackTrace();
				connection.rollback();
				return contact;
			} catch (SQLException e1) {
				e.printStackTrace();
				throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
			}
		}
		
		// inserting into 2nd table
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("insert into book (book_id, book_name, type) values (%s, '%s', '%s')", bookId, bookName, bookType);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 0) {
				return contact;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e.printStackTrace();
				throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
			}
		}
		
		// inserting into third table
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("insert into adress (person_id, state, city, zip) values (%s, '%s', '%s', '%s')", personId, city, state, zip);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
				contact = new AddressBook(firstName, lastName , city, city, state, Integer.parseInt(zip), phone, email);
			}
		} catch (SQLException e) { 
			e.printStackTrace();
			try {
				connection.rollback();
				return contact;
			} catch (SQLException e1) {
				e.printStackTrace();
				throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
			}
		}
		
		// for final committing
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
		}
		
		// closing the connection
		finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
				}
			}
		}
		return contact;
	}
}

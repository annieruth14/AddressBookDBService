package AddressBookDB;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookTest {
	
	// check number of entries
	@Test 
	public void givenDatabase_shouldReturnCount() throws AddressBookException {
		ContactPerson contact = new ContactPerson();
		List<AddressBook> listOfAddress = contact.getCountFromDB();
		Assert.assertEquals(8, listOfAddress.size());
	}
}

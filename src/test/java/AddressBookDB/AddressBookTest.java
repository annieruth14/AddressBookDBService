package AddressBookDB;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookTest {
	
	// check number of entries
	@Test 
	public void givenDatabase_shouldReturnCount() throws AddressBookException {
		ContactPerson contact = new ContactPerson();
		List<AddressBook> listOfAddress = contact.getListFromDB();
		Assert.assertEquals(8, listOfAddress.size());
	}
	
	@Test
	public void givenNewAddress_whenUpdated_shouldSyncWithDatabase() throws AddressBookException {
		ContactPerson contact = new ContactPerson();
		List<AddressBook> listOfAddress = contact.getListFromDB();
		contact.updateContact("Brian", "Siliguri");
		boolean result = contact.checkInSync("Brian");
		Assert.assertTrue(result);
	}
}

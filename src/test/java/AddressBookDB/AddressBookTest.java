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
	
	// update data
	@Test
	public void givenNewAddress_whenUpdated_shouldSyncWithDatabase() throws AddressBookException {
		ContactPerson contact = new ContactPerson();
		List<AddressBook> listOfAddress = contact.getListFromDB();
		contact.updateContact("Brian", "Siliguri");
		boolean result = contact.checkInSync("Brian");
		Assert.assertTrue(result);
	}
	
	// add data to table and list
	@Test
	public void givenNewAddress_whenMultipleTables_shouldInsertInTheDatabase() throws AddressBookException {
		ContactPerson contact = new ContactPerson();
		boolean result = contact.addPersonInDB(6,"book6","batchmates","Neeraj","Ghosh",9874569362L,"james@gmail.com","Tripura","Agartala","258796");
		Assert.assertTrue(result);
	}
	
	// retrieve data by city
	@Test
	public void givenCity_shouldReturnPersons() throws AddressBookException {
		ContactPerson contact = new ContactPerson();
		List<String> listOfPersons = contact.getPersonsFromCity("Bangalore");
		Assert.assertEquals(3, listOfPersons.size());
	}
}

package AddressBookDB;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
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
	
	// add multiple contacts using threads
	@Test
	public void givenMultipleContacts_whenAdded_shouldBeAddedToDatabase() throws AddressBookException {
		AddressBook[] contacts = {
				new AddressBook("Sara", "Singh", "Bidhanpally", "Siliguri", "WB", 7894561, 9874563258L, "sara@gmail.com"),
				new AddressBook("Ashish", "Gopal", "Kadamtala", "Kolkata", "WB", 569856, 874596665L, "ashish@gmail.com"),
				new AddressBook("Alok", "Singh", "Kormangala", "Bangalore", "Karnataka", 6985554, 9874588774L, "alok@gmail.com")
		};
		ContactPerson service = new ContactPerson();
		Instant start = Instant.now();
		service.addPersonInDB(Arrays.asList(contacts), 9, "book9", "roommates");
		Instant end =Instant.now();
		System.out.println("Duration with thread: "+ Duration.between(start, end));
	}
}

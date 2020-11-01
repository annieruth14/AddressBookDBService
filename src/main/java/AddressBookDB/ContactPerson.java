package AddressBookDB;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContactPerson {
	public static ContactPerson obj = new ContactPerson();
	AddressBookDBService dbService = new AddressBookDBService();
	public static ArrayList<AddressBook> addressList = new ArrayList<AddressBook>();
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		obj.addPerson();
		System.out.println("Do you want to edit any details of any person? (yes/no)");
		String ans_edit = sc.nextLine();
		if (ans_edit.equalsIgnoreCase("yes"))
			obj.editContact();
		System.out.println("Do you want to delete a person? (yes/no)");
		String ans_del = sc.nextLine();
		if (ans_del.equalsIgnoreCase("yes"))
			obj.deleteContact();
		System.out.println("Do you want to search a person by city? (yes/no)");
		String ans_city = sc.nextLine();
		if (ans_city.equals("yes"))
			obj.searchPersonByCity();
		System.out.println("Do you want to search a person by State? (yes/no)");
		String ans_state = sc.nextLine();
		if (ans_state.equals("yes"))
			obj.searchPersonByState();
	}

	public void searchPersonByState() {
		System.out.println("Enter the name of the State");
		String state = sc.nextLine();
		int count = 0;
		HashMap<String, String> stateMap = new HashMap<>();
		for (AddressBook b : addressList) {
			if (b.getState().equals(state)) {
				stateMap.put(b.getFirst_name(), state);
			}
		}
		System.out.println("The persons present in " + state + " are :");
		for (Map.Entry entry : stateMap.entrySet()) {
			count++;
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		System.out.println("Count : " + count);
	}

	public void searchPersonByCity() {
		System.out.println("Enter the name of the city");
		String city = sc.nextLine();
		int count = 0;
		HashMap<String, String> cityMap = new HashMap<>();
		for (AddressBook b : addressList) {
			if (b.getCity().equals(city)) {
				cityMap.put(b.getFirst_name(), city);
			}
		}
		System.out.println("The persons present in " + city + " are :");
		for (Map.Entry m : cityMap.entrySet()) {
			count++;
			System.out.println(m.getKey() + " : " + m.getValue());
		}
		System.out.println("Count : " + count);
	}

	public void addPerson() {

		String answer = "null";
		while (!(answer.equals("no"))) {
			System.out.println("Enter the first name of a person");
			String f_name = sc.nextLine();
			System.out.println("Enter the last name of a person");
			String l_name = sc.nextLine();
			System.out.println("Enter the address");
			String address = sc.nextLine();
			System.out.println("Enter the city");
			String city = sc.nextLine();
			System.out.println("Enter the state");
			String state = sc.nextLine();
			System.out.println("Enter the zip code");
			int zip = sc.nextInt();
			System.out.println("Enter the phone number");
			long ph = sc.nextLong();
			sc.nextLine();
			System.out.println("Enter the email");
			String email = sc.nextLine();
			AddressBook book = new AddressBook(f_name, l_name, address, city, state, zip, ph, email);
			if (addressList.contains(book)) {
				System.out.println("Name already exists. Hence cannot be added   ");
			} else
				addressList.add(book);
			System.out.println("Do you want to enter more person? (yes/no)  ");
			answer = sc.nextLine();
		}
		System.out.println("The details after adding are :");
		for (AddressBook p : addressList) {
			System.out.println(p.getFirst_name() + " " + p.getLast_name() + " " + p.getAddress() + " " + p.getCity()
					+ " " + p.getState() + " " + p.getZip() + " " + p.getPhone() + " " + p.getEmail());
		}
	}

	public void editContact() {
		System.out.println("Enter the name to edit");
		String name = sc.nextLine();
		System.out.println("Enter the field you want to edit : (address/city/state/zip/phone/email)");
		String field = sc.nextLine();
		System.out.println("Enter the new value");
		String new_value = sc.nextLine();
		for (AddressBook b : addressList) {
			if (name.equalsIgnoreCase(b.getFirst_name())) {
				if (field.equalsIgnoreCase("address")) {
					b.setAddress(new_value);
				} else if (field.equalsIgnoreCase("city")) {
					b.setCity(new_value);
				} else if (field.equalsIgnoreCase("state")) {
					b.setState(new_value);
				} else if (field.equalsIgnoreCase("zip")) {
					int val = Integer.parseInt(new_value);
					b.setZip(val);
				} else if (field.equalsIgnoreCase("phone")) {
					long ph = Long.parseLong(new_value);
					b.setPhone(ph);
				} else if (field.equalsIgnoreCase("email")) {
					b.setEmail(new_value);
				} else
					System.out.println("The name of the field is not correct");
			}
		}
		System.out.println("The details after editing are :");
		for (AddressBook p : addressList) {
			System.out.println(p.getFirst_name() + " " + p.getLast_name() + " " + p.getAddress() + " " + p.getCity()
					+ " " + p.getState() + " " + p.getZip() + " " + p.getPhone() + " " + p.getEmail());
		}
	}

	public void deleteContact() {
		Iterator<AddressBook> itr = addressList.iterator();
		System.out.println("Enter name of the person to be deleted");
		String del_person = sc.nextLine();
		while (itr.hasNext()) {
			AddressBook book = itr.next();
			if (book.getFirst_name().equalsIgnoreCase(del_person)) {
				itr.remove();
			}
		}
		System.out.println("The details after deleting are :");
		for (AddressBook p : addressList) {
			System.out.println(p.getFirst_name() + " " + p.getLast_name() + " " + p.getAddress() + " " + p.getCity()
					+ " " + p.getState() + " " + p.getZip() + " " + p.getPhone() + " " + p.getEmail());

		}
	}
	
	// Retrieving from database
	public List<AddressBook> getListFromDB() throws AddressBookException {
		AddressBookDBService dbService = new AddressBookDBService();
		addressList = dbService.getCount();
		return addressList;
	}

	public void updateContact(String name, String address) throws AddressBookException {
		int result = dbService.updateData(name, address);
		if(result == 1)
			addressList.stream().forEach(person -> {
				if(person.getFirst_name().equals(name)) 
					person.setAddress(address);
			});
	}

	public boolean checkInSync(String name) throws AddressBookException {
		List<AddressBook> list = dbService.getPerson(name);
		AddressBook person = this.getPersonFromList(name);
		if(person.equals(list.get(0)))
			return true;
		return false;
	}

	private AddressBook getPersonFromList(String name) {
		return addressList.stream().filter(n -> n.getFirst_name().equals(name)).findFirst().orElse(null);
	}
}

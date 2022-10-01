package AddrBookFileIO;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AddressBook {

	Scanner sc = new Scanner(System.in);

	AddressBookCSVService addressBookCsvService = new AddressBookCSVService();

	private List<Contact> addressList = new LinkedList<Contact>();
	/**
	 * Map to store multiple address books
	 */
	HashMap<String, List<Contact>> addressBookMap = new HashMap<String, List<Contact>>();
	/**
	 * Dictionary of city and  state
	 */
	HashMap<Contact,String> personCityMap = new HashMap<Contact, String>();
	HashMap<Contact,String> personStateMap = new HashMap<Contact, String>();


	private String addressListName;

	private void init() {
		addressBookMap = addressBookCsvService.getAddressBookMap();
	}
	private void setAddressListName(String listName) {
		addressListName = listName;

	}


	/**
	 * @param contactIsAdded
	 * @param contactObj
	 */
	private void addToDictionary(boolean contactIsAdded,Contact contactObj) {
		if(contactIsAdded==true) {
			personCityMap.put(contactObj, contactObj.getCity());
			personStateMap.put(contactObj, contactObj.getState());
		}
	}


	/**
	 * Map to store multiple address books to satisfy condition of unique name
	 * @param contactObj
	 * @return
	 */
	public boolean addContact(Contact contactObj) {
		Contact contact;
		boolean isPresent = addressList.stream().anyMatch(obj -> obj.equals(contactObj));
		if (isPresent == false) {
			addressList.add(contactObj);
			new  AddressBookCSVService().writeContactToAddressBook(contactObj, addressListName);
			System.out.println("Contact added");
			return true;
		}
		else {
			System.out.println("Contact already present. Duplication not allowed");
			return false;
		}
	}

	/**
	 * Edit Contact Details
	 * Enter First Name and LastName of person to Edit Details
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public boolean editDetails(String firstName, String lastName) {
		Contact editObj;
		boolean contactFound = false;
		for (int i = 0; i < addressList.size(); i++) {
			editObj = addressList.get(i);
			if ((editObj.getFirstName().equals(firstName)) && (editObj.getLastName().equals(lastName))) {
				System.out.println("Enter new Address:");
				editObj.setAddress(sc.nextLine());
				System.out.println("Enter new City");
				editObj.setCity(sc.nextLine());
				System.out.println("Enter new State");
				editObj.setState(sc.nextLine());
				System.out.println("Enter new Zip");
				editObj.setZip(sc.nextLine());
				System.out.println("Enter new Phone no");
				editObj.setPhoneNo(sc.nextLine());
				System.out.println("Enter new Email");
				editObj.setEmail(sc.nextLine());
				contactFound = true;
				break;
			}
		}
		return contactFound;
	}
	/**
	 * Remove Contact from given Address Book
	 * Enter FirstName and LastName of person to delete data
	 * @param firstName
	 * @param lastName
	 * @return
	 */

	public boolean removeDetails(String firstName, String lastName) {
		Contact removeObj;
		boolean contactFound = false;
		for (int i = 0; i < addressList.size(); i++) {
			removeObj = addressList.get(i);
			if ((removeObj.getFirstName().equals(firstName)) && (removeObj.getLastName().equals(lastName))) {
				addressList.remove(i);
				contactFound = true;
				break;
			}
		}
		return contactFound;
	}

	/**
	 * Add an AddressBook to map
	 * @param listName
	 */
	public void addAddressList(String listName) {
		List<Contact> newAddressList = new LinkedList<Contact>();
		addressBookMap.put(listName, newAddressList);
		boolean isAddressBookAdded = new  AddressBookCSVService().addAddressBook(listName);
		if (isAddressBookAdded)
			System.out.println("Address book added");
		else
			System.out.println("Address book not added. Might already be present");
		addressListName = listName;
	}

	/**
	 * Search person in a city or state across multiple address book
	 * @param searchPerson
	 * @param searchChoice
	 * @param cityOrState
	 */

	private void searchPersonAcrossCityState(String searchPerson,int searchChoice, String cityOrState) {
		for (Map.Entry<String, List<Contact>> entry : addressBookMap.entrySet()) {
			List<Contact> list = entry.getValue();
			if (searchChoice == 1)
				list.stream().filter(obj -> ((obj.getCity().equals(cityOrState))&&(obj.getFirstName().equals(searchPerson)))).forEach(System.out::println);
			else if(searchChoice == 2)
				list.stream().filter(obj -> ((obj.getState().equals(cityOrState))&&(obj.getFirstName().equals(searchPerson)))).forEach(System.out::println);
		}
	}
	/**
	 * Ability to View Person by City or State
	 * @param cityOrState
	 * @param searchChoice
	 */

	private void viewPersonsByCityState(String cityOrState, int searchChoice) {
		for (Map.Entry<String, List<Contact>> entry : addressBookMap.entrySet()) {
			List<Contact> list = entry.getValue();
			if (searchChoice == 1)
				list.stream().filter(obj -> obj.getCity().equals(cityOrState)).forEach(System.out::println);
			else if(searchChoice == 2)
				list.stream().filter(obj -> obj.getState().equals(cityOrState)).forEach(System.out::println);
		}
	}
	/**
	 * Ability to get number of contact persons (count by City or State)
	 * @param cityOrState
	 * @param searchChoice
	 * @return
	 */

	private long getCountByCityState(String cityOrState, int searchChoice) {
		long count=0;
		for (Map.Entry<String, List<Contact>> entry : addressBookMap.entrySet()) {
			List<Contact> list = entry.getValue();
			if (searchChoice == 1)
				count+= list.stream().filter(obj -> obj.getCity().equals(cityOrState)).count();
			else if(searchChoice == 2)
				count+= list.stream().filter(obj -> obj.getState().equals(cityOrState)).count();
		}
		return count;
	}
	/**
	 * To Sort AddressBook Details by name
	 * Use Collection Library for Sorting
	 * @param sortList
	 * @return
	 */
	private List<Contact> sortAddressBookByName(List<Contact> sortList) {
		FlexibleSort flexibleSort = new FlexibleSort(FlexibleSort.Order.NAME);
		Collections.sort(sortList, flexibleSort);
		return sortList;
	}
	/**
	 * Ability to sort the entries in the address book by City,State, or Zip
	 * @param sortChoice
	 * @param sortList
	 * @return Sorted Address Book List By Choice (UC12)
	 */
	private List<Contact> sortAddressBookByChoice(int sortChoice, List<Contact> sortList) {
		FlexibleSort flexibleSort = null;
		switch (sortChoice) {
		case 1:
			flexibleSort = new FlexibleSort(FlexibleSort.Order.CITY);
			break;
		case 2:
			flexibleSort = new FlexibleSort(FlexibleSort.Order.STATE);
			break;
		case 3:
			flexibleSort = new FlexibleSort(FlexibleSort.Order.ZIP);
			break;
		default:
			System.out.println("Invalid Choice");
		}
		Collections.sort(sortList, flexibleSort);
		return sortList;
	}


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		AddressBook addressObj = new AddressBook();
		int choice = 0;
		/**
		 * If no address book is present, it asks to add at least one address book
		 * Then Enter the Name of Address Book to add
		 */
		System.out.println("Welcome to address book program");
		addressObj.init();
		while (choice != 12) {


			System.out.println("Enter a choice: \n 1)Add a new AddressBook\n 2)Add a New Contact \n 3)Edit a contact \n 4)Delete Contact \n 5)View current Address Book Contacts"+ " \n 6)Search person in a city or state across the multiple Address Books \n 7)View persons by city or state \n "+ "8)Get count of contact persons by city or state \n 9)Sort entries by name in current address book\n 10)Sort entries in current address book by city, state or zip \n 11)View all contacts from all address books \n 12)Exit");
			choice = Integer.parseInt(sc.nextLine());
			switch (choice) {
			case 1: {
				if (addressObj.addressBookMap.isEmpty()) {
					System.out.println("Please add an address book :");
					System.out.println("Enter the name of address book to add:");
					String listName = sc.nextLine();
					addressObj.addAddressList(listName);
				}

				System.out.println("Enter the name of the address book you want to access");
				String listName = sc.nextLine();
				if (addressObj.addressBookMap.containsKey(listName)) {
					addressObj.addressList = addressObj.addressBookMap.get(listName);
					addressObj.setAddressListName(listName);
				}

				else {
					System.out.println("Address list with name" + listName + " not present. Please add it first.");
				}
			}
			case 2: {
				System.out.println("Add Person Details:");
				System.out.println("First Name:");
				String firstName = sc.nextLine();
				System.out.println("Last Name:");
				String lastName = sc.nextLine();
				System.out.println("Address:");
				String address = sc.nextLine();
				System.out.println("City:");
				String city = sc.nextLine();
				System.out.println("State:");
				String state = sc.nextLine();
				System.out.println("Zip:");
				String zip = sc.nextLine();
				System.out.println("Phone no:");
				String phoneNo = sc.nextLine();
				System.out.println("Email");
				String email = sc.nextLine();
				// Input
				Contact contactObj = new Contact(firstName, lastName, address, city, state, zip, phoneNo,email);
				boolean contactIsAdded = addressObj.addContact(contactObj);
				addressObj.addToDictionary(contactIsAdded,contactObj);

				break;
			}
			case 3: {
				System.out.println(
						"Enter first name and  Enter last name of person to edit details:");
				String firstName = sc.nextLine();
				String lastName = sc.nextLine();
				boolean contactFound = addressObj.editDetails(firstName, lastName);
				if (contactFound == true)
					System.out.println("Details successfully edit");
				else
					System.out.println("Contact not found");
				break;
			}
			case 4: {
				System.out.println(
						"Enter first name and  enter last name of person to delete data");
				String firstName = sc.nextLine();
				String lastName = sc.nextLine();
				boolean contactFound = addressObj.removeDetails(firstName, lastName);
				if (contactFound == true)
					System.out.println("Details successfully deleted");
				else
					System.out.println("Contact not found");
				break;
			}

			case 5: {
				System.out.println(" " + addressObj.addressList);
				break;
			}
			case 6: {
				System.out.println("Enter first name of person to search");
				String searchPerson = sc.nextLine();
				System.out.println("Enter the name of city or state");
				String cityOrState = sc.nextLine();
				System.out.println("Enter 1 if you entered name of a city \nEnter 2 if you entered name of a state");
				int searchChoice = Integer.parseInt(sc.nextLine());
				addressObj.searchPersonAcrossCityState(searchPerson,searchChoice, cityOrState);
			}
			case 7: {
				System.out.println("Enter the name of city or state");
				String cityOrState = sc.nextLine();
				System.out.println("Enter 1 if you entered name of a city \nEnter 2 if you entered name of a state");
				int searchChoice = Integer.parseInt(sc.nextLine());
				addressObj.viewPersonsByCityState(cityOrState,searchChoice);
				break;
			}
			case 8: {
				System.out.println("Enter the name of city or state");
				String cityOrState = sc.nextLine();
				System.out.println("Enter 1 if you entered name of a city \nEnter 2 if you entered name of a state");
				int searchChoice = Integer.parseInt(sc.nextLine());
				System.out.println("Total persons in "+cityOrState+" = "+addressObj.getCountByCityState(cityOrState,searchChoice));
				break;
			}
			case 9: {
				List<Contact> sortedEntriesList = addressObj.sortAddressBookByName(addressObj.addressList);
				System.out.println("Entries sorted in current address book. Sorted Address Book Entries:");
				System.out.println(sortedEntriesList);
				break;
			}
			case 10: {
				System.out.println("Enter 1 to sort by city \nEnter 2 to sort by state \nEnter 3 to sort by zipcode");
				int sortChoice = Integer.parseInt(sc.nextLine());
				List<Contact> sortedEntriesList = addressObj.sortAddressBookByChoice(sortChoice,
						addressObj.addressList);
				System.out.println(sortedEntriesList);
				break;
			}
			case 11: {
				addressObj.addressBookCsvService.print();
				break;
			}
			case 12: {
				System.out.println("Thank you for using the application");
			}
			}
		}
	}

}

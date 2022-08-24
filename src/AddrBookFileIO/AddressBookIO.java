package AddrBookFileIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressBookIO {
	private static String HOME;
	private HashMap<String, List<Contact>> addressBookMap;
	public AddressBookIO() {
		HOME = "C:\\Users\\mraj\\mohan\\java\\day27\\AddressBookFileIO\\src\\AddrBookFileIO";
		addressBookMap = new HashMap<String, List<Contact>>();
		readDataFromAddressBook();
	}

	/**
	 * Get data from Address Book.txt file from directory and store it to address
	 * book map
	 */
	public void readDataFromAddressBook() {
		try {

			/**
			 * The Files.lines returns a stream (Stream<String>) of lines read from a file,
			 * The Files.list method returns a stream of paths (Stream<Path>) of all the entries in a directory.
			 * The Files.walk method to search files under a directory
			 * The Files.find, returns a stream of paths
			 */

			Files.walk(Paths.get(HOME)).filter(Files::isRegularFile).forEach(file -> {
				List<Contact> contactList = new ArrayList<Contact>();
				try {
					Files.lines(file.toAbsolutePath()).forEach(lines -> {
						String data = lines.toString();
						String[] arr = data.split(",");
						for (int i = 0; i < arr.length; i++) {
							String firstName = arr[i].replaceAll("First name=", "");
							i++;
							String lastName = arr[i].replaceAll(" Last name=", "");
							i++;
							String address = arr[i].replaceAll(" Address=", "");
							i++;
							String city = arr[i].replaceAll(" City=", "");
							i++;
							String state = arr[i].replaceAll(" State=", "");
							i++;
							String zip = arr[i].replaceAll(" Zip=", "");
							i++;
							String phone = arr[i].replaceAll(" Phone No=", "");
							i++;
							String email = arr[i].replaceAll(" Email=", "");
							Contact contact = new Contact(firstName, lastName, address, city, state, zip, phone,
									email);
							contactList.add(contact);
						}
					});
					String fileName = file.toAbsolutePath().toString().replace(HOME + "\\", "");
					addressBookMap.put(fileName.substring(0, fileName.length() - 4), contactList);
				} catch (IOException e) {
					e.printStackTrace();
				}

			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Creates an Address Book.txt file
	 *
	 * @param bookName
	 * @return true if address book is created
	 */
	public boolean addAddressBook(String bookName) {
		Path addressBooks = Paths.get(HOME + "/" + bookName + ".txt");
		if (Files.notExists(Paths.get(HOME + "/" + bookName + ".txt"))) {
			try {
				Files.createFile(addressBooks);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * Adds a contact to the particular address book
	 *
	 * @param contactObj
	 * @param addressBookName

	 * The Files.lines returns a stream (Stream<String>) of lines read from a file,
	 * The Files.list method returns a stream of paths (Stream<Path>) of all the entries in a directory.
	 * The Files.walk method to search files under a directory
	 * The Files.find, returns a stream of paths
	 */

	public void writeContactToAddressBook(Contact contactObj, String addressBookName) {
		StringBuffer contactsBuffer = new StringBuffer();
		String contactData = contactObj.toString();
		try {
			Files.lines(Paths.get(HOME + "/" + addressBookName + ".txt")).forEach(lines -> {
				String data = lines.toString().concat("\n");
				contactsBuffer.append(data);
			});
			contactsBuffer.append(contactData);
			Files.write(Paths.get(HOME + "/" + addressBookName + ".txt"), contactsBuffer.toString().getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Print contact details from address books
	 */
	public void print() {
		addressBookMap.entrySet().stream().map(entry -> entry.getValue()).forEach(System.out::println);
	}

	public static String getHOME() {
		return HOME;
	}

	public static void setHOME(String hOME) {
		HOME = hOME;
	}

	public HashMap<String, List<Contact>> getAddressBookMap() {
		return addressBookMap;
	}

	public void setAddressBookMap(HashMap<String, List<Contact>> addressBookMap) {
		this.addressBookMap = addressBookMap;
	}
}
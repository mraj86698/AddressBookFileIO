package AddrBookFileIO;

import java.util.Comparator;

public class FlexibleSort implements Comparator<Contact> {
	/**
	 * enum is special data type that enables for a variable to be a set of predefined constants
	 * @author mraj
	 *
	 */
	public enum Order {
		NAME, CITY, STATE, ZIP
	}

	public Order sortingBy;

	public FlexibleSort(Order sortingBy) {
		this.sortingBy = sortingBy;
	}
	/**
	 * Comparing two Objects and Return two integer value
	 * This method returns -1,0,1 to say that it is either less than, equal to, or greater than the other object.
	 */

	@Override
	public int compare(Contact o1, Contact o2) {
		switch (sortingBy) {
		case NAME:
			return (o1.getFirstName() + o1.getLastName()).compareTo(o2.getFirstName() + o2.getLastName());
		case CITY:
			return (o1.getCity().compareTo(o2.getCity()));
		case STATE:
			return (o1.getState().compareTo(o2.getState()));
		case ZIP:
			return (o1.getZip().compareTo(o2.getZip()));
		default:
			System.out.println("Invalid choice");
		}
		return 0;
	}

}

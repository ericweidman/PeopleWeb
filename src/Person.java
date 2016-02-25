/**
 * Created by ericweidman on 2/24/16.
 */
public class Person {

    int idNumber;
    String firstName;
    String lastName;
    String email;
    String country;
    String ipAddress;

    @Override
    public String toString() {
        return "Person{" +
                "idNumber=" + idNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public Person(int idNumber, String firstName, String lastName, String email, String country, String ipAddress) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.ipAddress = ipAddress;


    }
}

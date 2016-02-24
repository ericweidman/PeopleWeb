

public class PeopleWeb {
    public static void main(String[] args) {



    }
}



//        Parse the CSV file into an ArrayList<Person>.
//        Create a GET route for / that simply lists the names of each person in ArrayList<Person>.
//        It should only display 20 names, and should have a "Previous" and "Next" button at the bottom
//        only if necessary (don't show the "Previous" button on the first page, and don't show the "Next"
//        button on the last page). It should take a GET parameter which is the offset it is supposed to
//        start at, like this: /?offset=20.
//        Create another GET route called /person which displays all the data about a single person.
//        It should take a GET parameter which is the id for that person, like this: /person?id=1
//        Make all the people shown on the main page link to their /person page, so I can click on their names for additional information.
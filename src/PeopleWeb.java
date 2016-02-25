import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PeopleWeb {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Person> allPeople = new ArrayList<>();

        openFile(allPeople);

        Spark.init();


        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("person", allPeople);
                    return new ModelAndView(m, "home.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                ((request, response) -> {
                    int idNumber = Integer.valueOf(request.queryParams("id"));
                    HashMap m = new HashMap();
                    Person person = allPeople.get(idNumber - 1);
                    m.put("person", person);
                    return new ModelAndView(m, "person.html");
                }),
                new MustacheTemplateEngine()
        );



    }
    static void openFile(ArrayList<Person> allPeople) throws FileNotFoundException {
        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while(fileScanner.hasNext()){
            String line = fileScanner.nextLine();
            String[] column = line.split(",");
            Person person = new Person(Integer.valueOf(column[0]),
                    column[1], column[2], column[3], column[4], column[5]);
            allPeople.add(person);
        }

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
//        Make all the people shown on the main page link to their /person page, so I can
//        click on their names for additional information.
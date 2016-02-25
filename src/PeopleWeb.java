import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PeopleWeb {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Person> allPeople = new ArrayList<>();

        openFile(allPeople);

        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    String userClick = request.queryParams("userClick");
                    int userClickValue = 0;
                    boolean last = false;
                    boolean next = false;

                    if (userClick != null) {
                        userClickValue = Integer.valueOf(userClick);
                    }
                    ArrayList<Person> twentyPeople = new ArrayList<>(allPeople.subList(userClickValue, 20 + userClickValue));
                    if (userClickValue >= 20) {
                        last = true;
                    }
                    if (userClickValue < allPeople.size() - 20) {
                        next = true;
                    }

                    m.put("person", twentyPeople);
                    m.put("forward", userClickValue + 20);
                    m.put("previous", userClickValue - 20);
                    m.put("next", next);
                    m.put("last", last);
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
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] column = line.split(",");
            Person person = new Person(Integer.valueOf(column[0]),
                    column[1], column[2], column[3], column[4], column[5]);
            allPeople.add(person);
        }
    }
}
package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import static fr.umlv.javainside.IncompleteJSONParser.parse;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

public class JSONPrinterTest {
    record Alien(int age, String planet) {
        public Alien {
            if (age < 0) {
                throw new IllegalArgumentException("negative age");
            }
            requireNonNull(planet);
        }
    }
    record Person( String firstName, String lastName) {
        public Person {
            requireNonNull(firstName);
            requireNonNull(lastName);
        }
    }


    @Test
    public void json() {
        var person = new Person("John","Doe");
        var jsonPrint = new JSONPrinter();
        assertEquals(parse("{ \"firstName\": \"John\", \"lastName\": \"Doe\" }"), parse(jsonPrint.toJSON(person)));

    }

}
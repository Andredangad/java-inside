package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import static fr.umlv.javainside.IncompleteJSONParser.parse;
import static org.junit.jupiter.api.Assertions.*;

class JSONPrinterTest {
    @Test
    public void json() {
        var person = new Person("John","Doe");
        var JSONprint = new JSONPrinter();
        assertEquals(parse("{ \"firstName\": \"John\", \"lastName\": \"Doe\" }"), parse(JSONprint.toJSON(person)));
    }

}
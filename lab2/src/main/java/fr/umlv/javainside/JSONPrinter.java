package fr.umlv.javainside;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JSONPrinter {

   /* public static String toJSON(Person person) {
        return """
      {
        "firstName": "%s",
        "lastName": "%s"
      }
      """.formatted(person.firstName(), person.lastName());
    }

    public static String toJSON(Alien alien) {
        return """
      {
        "age": %s,
        "planet": "%s"
      }
      """.formatted(alien.age(), alien.planet());
    } */
    private static Object invokeAccessor(Method accessor, Record record){
        try {
            return accessor.invoke(record);
        }
        catch (IllegalAccessException e) {
            throw (IllegalAccessError) new IllegalAccessError().initCause(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if(cause instanceof RuntimeException re){
                throw re;
            }
            if(cause instanceof  Error error){
                throw error;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }
    public static String toJSON(Record record) {
        // return Arrays.stream(record.getClass().getRecordComponents()).map(e -> e.toString()).collect(Collectors.joining( " "));
        return "{ " + Arrays.stream(record.getClass().getRecordComponents())
                .map(RecordComponent::getAccessor).map(e ->"\""+ e.getName() + "\": " + "\""+ invokeAccessor(e, record)+ "\"")
                .map(e -> e.toString())
                .collect(Collectors.joining(", ")) + " }"
                ;
    }

    public static void main(String[] args) {
        var person = new Person("John", "Doe");
        System.out.println(toJSON(person));
        var alien = new Alien(100, "Saturn");
        System.out.println(toJSON(alien));
    }
}

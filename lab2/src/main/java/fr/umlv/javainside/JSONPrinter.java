package fr.umlv.javainside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
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
    private static class Cache extends ClassValue<List<Function<Record,String>>>{

        protected List<Function<Record,String>> computeValue(Class<?> type){
            return Arrays.stream(type.getRecordComponents())
                    .<Function<Record,String>>map(component -> {
                        var prefix = "\"" + name(component) + "\" : ";
                return record -> prefix + escape(invokeAccessor(component,record));
            })
            .collect(Collectors.toList());
    }

}
    public static final Cache CACHE = new Cache();

    private static Object invokeAccessor(RecordComponent accessor, Record record){
        try {
            return accessor.getAccessor().invoke(record);
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
        /*return "{ " + Arrays.stream(record.getClass().getRecordComponents())
                .map(RecordComponent::getAccessor).map(e ->"\""+ annotations(e) + "\": " + "\""+ invokeAccessor(e, record)+ "\"")
                .map(e -> e.toString())
                .collect(Collectors.joining(", ")) + " }"
                ;*/
        return CACHE.get(record.getClass()).stream().map(fun->fun.apply(record)).collect(Collectors.joining(", ", "{", "}"));
    }
    private static String annotations(Method method){
        var annotation = method.getAnnotation(JSONProperty.class);
        return annotation.value();
    }
    private static String name(RecordComponent component){
        var property = component.getAnnotation(JSONProperty.class);
        if(property == null){
            return component.getName();

        }
        return property.value();
    }
    private static String escape (Object o){
        return o instanceof String ? "\"" + o + "\"" : " " + o;
    }



    public static void main(String[] args) {
        var person = new Person("John", "Doe");
        System.out.println(toJSON(person));
        var alien = new Alien(100, "Saturn");
        System.out.println(toJSON(alien));
    }
}

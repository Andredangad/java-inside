package fr.umlv.javainside;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.WrongMethodTypeException;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTests {
    @Test
    public void of(){
        class Foo{}
        var logger = Logger.of(Foo.class, __->{});
        assertNotNull(logger);
    }

    @Test
    public void ofError(){
        class Foo{}
        assertAll(

                () -> assertThrows(NullPointerException.class, () -> {
                    Logger.of(null, __->{});


                }),
                () -> assertThrows(NullPointerException.class, () -> {
                    Logger.of(Foo.class, null);
                }));

    }
    @Test
    public void log(){
        class Foo{}
        var logger = Logger.of(Foo.class, message->{
            assertEquals("hello", message);
        });
        logger.log("hello");
    }

    @Test
    public void logWithNullAsValue(){
        class Foo{}
        var logger = Logger.of(Foo.class, Assertions::assertNotNull);
        logger.log("hello");
    }

}
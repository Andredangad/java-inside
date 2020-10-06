package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodHandleTests {
    @Test
    public void findStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        assertEquals(methodType(int.class, String.class), mh.type());

    }
}
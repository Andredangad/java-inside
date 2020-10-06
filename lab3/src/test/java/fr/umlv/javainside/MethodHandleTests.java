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

    @Test
    public void findVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        assertEquals(methodType(String.class, String.class), mh.type());

    }

    @Test
    public void invokeExactStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        var res = (int)mh.invokeExact("5");
        assertEquals(5,res);

    }

    @Test
    public void  invokeExactStaticWrongArgumentTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class));
        var res = (int)mh.invokeExact("5");
        assertEquals(5,res);

    }
}
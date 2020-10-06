package fr.umlv.javainside;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;


import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.*;

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
                "parseInt", methodType(int.class,String.class));
        assertThrows(WrongMethodTypeException.class, () -> mh.invokeExact(5));

    }

    @Test
    public void invokeExactVirtualTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        var res = (String)mh.invokeExact("a");
        assertEquals("A",res);

    }
    @Test
    public void invokeExactVirtualWrongArgumentTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        var res = (String)mh.invokeExact("a");
        assertThrows(WrongMethodTypeException.class, () -> mh.invokeExact(5));

    }

    @Test
    public void invokeStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        var res = (Integer)mh.invoke("5");
        Assertions.assertAll(
                () ->  assertEquals(5,res),
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s =(String) mh.invoke("5");
                }));


    }

    @Test
    public void invokeVirtualTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        var res = (Object)mh.invoke("a");
        assertThrows(WrongMethodTypeException.class, () -> mh.invoke(5));
        Assertions.assertAll(
                () ->  assertEquals("A",res),
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var d = (Double) mh.invoke(5);
                }));

    }
}
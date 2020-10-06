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
                "" +
                        "", methodType(int.class, String.class));
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

    @Test
    public void insertAndInvokeStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        Assertions.assertAll(
                () -> {
                    assertEquals(123, MethodHandles.insertArguments(mh, 0, "123").invoke());
                },
                () -> assertThrows(ClassCastException.class, () -> {
                    var s =MethodHandles.insertArguments(mh, 0, 123).invoke();
                }));


    }

    @Test
    public void bindToAndInvokeVirtualTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        Assertions.assertAll(
                () -> {
                    assertEquals("AAA", mh.bindTo("aaa").invoke());
                },
                () -> assertThrows(ClassCastException.class, () -> {
                    var s =mh.bindTo(123).invoke();
                }));


    }

    @Test
    public void dropAndInvokeStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        Assertions.assertAll(
                () -> {
                    assertEquals(123, MethodHandles.dropArguments(mh, 0, Integer.class).invoke(123, "123"));
                },
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s = MethodHandles.dropArguments(mh, 0, Integer.class).invoke(123, 123);
                }));


    }

    @Test
    public void dropAndInvokeVirtualTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        Assertions.assertAll(
                () -> {
                    assertEquals("AAA", MethodHandles.dropArguments(mh, 0, String.class).invoke("0", "aaa"));
                },
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s = MethodHandles.dropArguments(mh, 0, String.class).invoke(0, "aaa");
                }));


    }

    @Test
    public void asTypeAndInvokeExactStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        Assertions.assertAll(
                () -> {
                    assertEquals(123, (Integer)mh.asType(methodType(Integer.class, String.class)).invokeExact("123"));
                },
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s = (Integer)mh.asType(methodType(Integer.class, String.class)).invokeExact(123);
                }));


    }
    @Test
    public void invokeExactConstantTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        Assertions.assertAll(
                () -> {
                    assertEquals(42, MethodHandles.constant(Integer.class, 42).invoke());
                },
                () -> assertThrows(ClassCastException.class, () -> {
                    var s = MethodHandles.constant(Integer.class, "42").invoke();
                }));


    }



}
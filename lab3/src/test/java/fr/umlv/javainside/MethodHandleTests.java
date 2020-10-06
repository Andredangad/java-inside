package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.WrongMethodTypeException;
import java.util.List;


import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.*;

public class MethodHandleTests {
    @Test
    public void findStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = lookup();
        var mh = lookup.findStatic(Integer.class,
                "" +
                        "", methodType(int.class, String.class));
        assertEquals(methodType(int.class, String.class), mh.type());

    }

    @Test
    public void findVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        assertEquals(methodType(String.class, String.class), mh.type());

    }

    @Test
    public void invokeExactStaticTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        var res = (int)mh.invokeExact("5");
        assertEquals(5,res);

    }

    @Test
    public void  invokeExactStaticWrongArgumentTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class,String.class));
        assertThrows(WrongMethodTypeException.class, () -> mh.invokeExact(5));

    }

    @Test
    public void invokeExactVirtualTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        var res = (String)mh.invokeExact("a");
        assertEquals("A",res);

    }
    @Test
    public void invokeExactVirtualWrongArgumentTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        var res = (String)mh.invokeExact("a");
        assertThrows(WrongMethodTypeException.class, () -> mh.invokeExact(5));

    }

    @Test
    public void invokeStaticTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        var res = (Integer)mh.invoke("5");
        assertAll(
                () ->  assertEquals(5,res),
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s =(String) mh.invoke("5");
                }));


    }

    @Test
    public void invokeVirtualTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        var res = (Object)mh.invoke("a");
        assertThrows(WrongMethodTypeException.class, () -> mh.invoke(5));
        assertAll(
                () ->  assertEquals("A",res),
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var d = (Double) mh.invoke(5);
                }));

    }

    @Test
    public void insertAndInvokeStaticTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        assertAll(
                () -> {
                    assertEquals(123, insertArguments(mh, 0, "123").invoke());
                },
                () -> assertThrows(ClassCastException.class, () -> {
                    var s = insertArguments(mh, 0, 123).invoke();
                }));


    }

    @Test
    public void bindToAndInvokeVirtualTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        assertAll(
                () -> {
                    assertEquals("AAA", mh.bindTo("aaa").invoke());
                },
                () -> assertThrows(ClassCastException.class, () -> {
                    var s =mh.bindTo(123).invoke();
                }));


    }

    @Test
    public void dropAndInvokeStaticTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        assertAll(
                () -> {
                    assertEquals(123, dropArguments(mh, 0, Integer.class).invoke(123, "123"));
                },
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s = dropArguments(mh, 0, Integer.class).invoke(123, 123);
                }));


    }

    @Test
    public void dropAndInvokeVirtualTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findVirtual(String.class,
                "toUpperCase", methodType(String.class));
        assertAll(
                () -> {
                    assertEquals("AAA", dropArguments(mh, 0, String.class).invoke("0", "aaa"));
                },
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s = dropArguments(mh, 0, String.class).invoke(0, "aaa");
                }));


    }

    @Test
    public void asTypeAndInvokeExactStaticTest() throws Throwable {
        var lookup = lookup();
        var mh = lookup.findStatic(Integer.class,
                "parseInt", methodType(int.class, String.class));
        assertAll(
                () -> {
                    assertEquals(123, (Integer)mh.asType(methodType(Integer.class, String.class)).invokeExact("123"));
                },
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    var s = (Integer)mh.asType(methodType(Integer.class, String.class)).invokeExact(123);
                }));


    }
    @Test
    public void invokeExactConstantTest() throws Throwable {
        assertAll(
                () -> {
                    assertEquals(42, constant(Integer.class, 42).invoke());
                },
                () -> assertThrows(ClassCastException.class, () -> {
                    var s = constant(Integer.class, "42").invoke();
                }));


    }

    private static MethodHandle match(String s) throws NoSuchMethodException, IllegalAccessException {
        var lookup = lookup();

        var mh = lookup.findVirtual(String.class, "equals", methodType(boolean.class, Object.class));

        var test = insertArguments(mh, 1, s);
        var target = dropArguments(constant(int.class, 1), 0 , String.class);
        var fallback = dropArguments(constant(int.class, -1), 0 , String.class);
        return guardWithTest(test,target,fallback);

    }

    @Test
    public void matchTest() throws Throwable {
        var m = match("hello");
        assertAll(
                () -> {
                    assertEquals(1, (int) m.invokeExact("hello"));
                },
                () -> {
                    assertEquals(-1, (int) m.invokeExact("bad"));
                });


    }

    private static MethodHandle matchAll(List<String> list) throws NoSuchMethodException, IllegalAccessException {
        var lookup = lookup();

        var mh = lookup.findVirtual(String.class, "equals", methodType(boolean.class, Object.class));


        var target = dropArguments(constant(int.class, -1), 0 , String.class);
        var index = 0;
        for(var text:list){
            var test = insertArguments(mh, 1, text);
            var ok = dropArguments(constant(int.class, index), 0 , String.class);
            target = guardWithTest(test, ok, target);
            index++;
        }
        return target;

    }

    @Test
    public void matchAllTest() throws Throwable {
        var m = matchAll(List.of("hello", "java", "inside"));
        assertAll(
                () -> {
                    assertEquals(0, (int) m.invokeExact("hello"));
                },
                () -> {
                    assertEquals(1, (int) m.invokeExact("java"));
                },
                () -> {
                    assertEquals(2, (int) m.invokeExact("inside"));
                },
                () -> {
                    assertEquals(-1, (int) m.invokeExact("bad"));
                });


    }



}
package br.com.desafio.util;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class Assertions {

    public static void assertThrowsWithMessage(Class<? extends Throwable> expectedExceptionClass
            , Executable executable
            , String expectedExceptionMessage) {
        Throwable thrown = assertThrows(expectedExceptionClass, executable);
        assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

}
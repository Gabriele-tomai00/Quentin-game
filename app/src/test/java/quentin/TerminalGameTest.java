package quentin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TerminalGameTest {

    @Test
    public void regexTest() {
        assertTrue(isCellCoordiantesValid("A1"));
        assertTrue(isCellCoordiantesValid("A2"));
        assertTrue(isCellCoordiantesValid("b5"));
        assertFalse(isCellCoordiantesValid("z1"));
    }

    public static Boolean isCellCoordiantesValid(String cell) {
        final String coordinatePattern = "(?i)^[A-M](?:[0-9]|1[0-2])$";
        if (cell.matches(coordinatePattern)) {
            System.out.println("Coordinate valida: " + cell);
            return true;
        } else {
            System.out.println("Coordinate non valida: " + cell);
            return false;
        }
    }
}

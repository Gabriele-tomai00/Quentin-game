package quentin.network;

// JUnit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test; // Test of JUnit 5

class ServerTest {

    @Test
    void testCodeLength() {
        String code = Server.generateRandomCode(5);
        assertEquals(5, code.length(), "The code length should be 5 characters.");
    }

    @Test
    void testCodeContainsOnlyDigits() {
        String code = Server.generateRandomCode(5);
        assertTrue(code.matches("\\d{5}"), "The code should contain only digits (0-9).");
    }

    @Test
    void testCodeIsDifferentOnEachCall() {
        String code1 = Server.generateRandomCode(5);

        String code2 = Server.generateRandomCode(6);
        assertNotEquals(
                code1,
                code2,
                "Each call to generateRandomCode() should produce a different result.");
    }

    @Test
    void testCodeIsSameWithSameSeed() {
        String code = Server.generateRandomCode(0);
        String code2 = Server.generateRandomCode(0);
        assertEquals(code, code2);
    }
}

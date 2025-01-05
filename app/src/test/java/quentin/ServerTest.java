package quentin;

// JUnit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; // Test of JUnit 5
import quentin.network.Server;

public class ServerTest {

    private Server server;

    @BeforeEach
    public void setUp() {
        server = new Server();
    }

    @Test
    public void testCodeLength() {
        server.generateRandomCode();
        String code = server.getCodeForClientAuth();
        assertEquals(5, code.length(), "The code length should be 5 characters.");
    }

    @Test
    public void testCodeContainsOnlyDigits() {
        server.generateRandomCode();
        String code = server.getCodeForClientAuth();
        assertTrue(code.matches("\\d{5}"), "The code should contain only digits (0-9).");
    }

    @Test
    public void testCodeIsDifferentOnEachCall() throws InterruptedException {
        server.generateRandomCode();
        String code1 = server.getCodeForClientAuth();

        Thread.sleep(10);

        server.generateRandomCode();
        String code2 = server.getCodeForClientAuth();

        assertNotEquals(
                code1,
                code2,
                "Each call to generateRandomCode() should produce a different result.");
    }
}

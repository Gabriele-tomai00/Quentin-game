package quentin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void testParsedInput() {
        assertAll(
                "Parse some input",
                () -> assertEquals(new Cell(0, 0), new Parser("a1").parse()),
                () ->
                        assertThrows(
                                InvalidCellValuesException.class, () -> new Parser("n3").parse()),
                () -> assertEquals(new Cell(12, 12), new Parser("m13").parse()),
                () ->
                        assertThrows(
                                InvalidCellValuesException.class, () -> new Parser("a14").parse()));
        Exception exception =
                assertThrows(InvalidCellValuesException.class, () -> new Parser("n1").parse());
        String expectedMessage =
                String.format("Row values span from 'a' to 'm', received %c", "n3".charAt(0));
        assertTrue(expectedMessage.contains(exception.getMessage()));
    }
}

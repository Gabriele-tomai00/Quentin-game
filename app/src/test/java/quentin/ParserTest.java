package quentin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import quentin.exceptions.InvalidCellValuesException;
import quentin.game.Cell;

class ParserTest {

    @Test
    void testParsedInput() {
        assertAll(
                "Parse some input",
                () -> assertEquals(new Cell(0, 0), new MoveParser("a1").parse()),
                () ->
                        assertThrows(
                                InvalidCellValuesException.class,
                                () -> new MoveParser("n3").parse()),
                () -> assertEquals(new Cell(12, 12), new MoveParser("m13").parse()),
                () ->
                        assertThrows(
                                InvalidCellValuesException.class,
                                () -> new MoveParser("a14").parse()));
        Exception exception =
                assertThrows(InvalidCellValuesException.class, () -> new MoveParser("n1").parse());
        String expectedMessage =
                String.format("Row values span from 'a' to 'm', received %c", "n3".charAt(0));
        assertTrue(expectedMessage.contains(exception.getMessage()));
    }
}

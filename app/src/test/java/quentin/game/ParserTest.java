package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import quentin.exceptions.InvalidCellValuesException;

class ParserTest {

    @Test
    void testParsedInput() {
        assertAll(
                () -> assertEquals(new Cell(0, 0), new MoveParser("a1").parse()),
                () -> assertEquals(new Cell(12, 12), new MoveParser("m13").parse()));
        MoveParser parser = new MoveParser("n1");
        Exception exception = assertThrows(InvalidCellValuesException.class, parser::parse);
        String expectedMessage = "Row values span from 'a' to 'm', received";
        assertTrue(exception.getMessage().contains(expectedMessage));
        parser = new MoveParser("a14");
        exception = assertThrows(InvalidCellValuesException.class, parser::parse);
        expectedMessage = "Column values span from '1' to '13', received";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}

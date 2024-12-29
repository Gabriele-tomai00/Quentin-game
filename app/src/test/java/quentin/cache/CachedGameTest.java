package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CachedGameTest {

    @Test
    public void letterToIndexTest() {
        CachedGame game = new CachedGame();
        assertEquals(0, game.letterToIndex('A'));
        assertEquals(1, game.letterToIndex('B'));
        assertEquals(2, game.letterToIndex('C'));
        assertEquals(3, game.letterToIndex('D'));
        assertEquals(4, game.letterToIndex('E'));
        assertEquals(5, game.letterToIndex('F'));
        assertEquals(6, game.letterToIndex('G'));
        assertEquals(7, game.letterToIndex('H'));
        assertEquals(8, game.letterToIndex('I'));
        assertEquals(9, game.letterToIndex('J'));
        assertEquals(10, game.letterToIndex('K'));
        assertEquals(11, game.letterToIndex('L'));
        assertEquals(12, game.letterToIndex('M'));

        assertEquals(-1, game.letterToIndex('O'));
        assertEquals(-1, game.letterToIndex('Z'));
    }
}

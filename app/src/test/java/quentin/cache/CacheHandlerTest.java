package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import quentin.game.Cell;
import quentin.game.LocalGame;

public class CacheHandlerTest {
    CacheHandler cache = new CacheHandler();

    @Test
    public void readAndWriteCacheToFileTest() {
        Cache<GameLog> cache = new Cache<>();
        LocalGame game = new LocalGame();
        game.place(new Cell(0, 0));
        game.changeCurrentPlayer();
        cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
        game.place(new Cell(1, 0));
        game.changeCurrentPlayer();
        cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
        CacheHandler.saveCache(cache);
        Cache<GameLog> cache2 = CacheHandler.initialize();
        assertEquals(cache2, cache);
    }
}

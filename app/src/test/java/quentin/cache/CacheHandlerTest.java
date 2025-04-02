package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import quentin.game.Cell;
import quentin.game.LocalGame;

class CacheHandlerTest {
    @Test
    void readAndWriteCacheToFileTest(@TempDir Path tempDir) {
        Path path = tempDir.resolve("cache");
        Cache<GameLog> cache = new Cache<>();
        LocalGame game = new LocalGame();
        game.place(new Cell(0, 0));
        game.changeCurrentPlayer();
        cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
        game.place(new Cell(1, 0));
        game.changeCurrentPlayer();
        cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
        CacheHandler.saveCache(path.toFile(), cache);
        Cache<GameLog> cache2 = CacheHandler.initialize(path.toFile());
        assertAll(
                () -> assertTrue(Files.exists(path)),
                () -> assertTrue(cache.getMemorySize() > 1),
                () -> assertEquals(cache2, cache));
    }
}

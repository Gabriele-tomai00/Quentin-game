package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import quentin.game.Cell;
import quentin.game.LocalGame;

class CacheHandlerTest {
  @Test
  void readAndWriteCacheToFileTest(@TempDir File temp) {
    Cache<GameLog> cache = new Cache<>();
    LocalGame game = new LocalGame();
    game.place(new Cell(0, 0));
    game.changeCurrentPlayer();
    cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
    game.place(new Cell(1, 0));
    game.changeCurrentPlayer();
    cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
    CacheHandler.saveCache(temp, cache);
    Cache<GameLog> cache2 = CacheHandler.initialize();
    assertEquals(cache2, cache);
  }
}

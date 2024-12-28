package quentin.cache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.LocalGame;
import quentin.game.Player;

public class CachedGame extends LocalGame {

    private final CacheHandler cacheHandler;
    private Boolean matchInProgress;
    private String timestampOfLastMove;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private int moveCounter = 0;
    private boolean isFirstMove = true;

    public CachedGame() {
        super();
        this.cacheHandler = new CacheHandler();
        this.matchInProgress = true;
    }

    @Override
    public void place(Cell cell) {
        if (isFirstMove) {
            cacheHandler.saveLog(this);
            isFirstMove = false;
        }
        timestampOfLastMove = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        moveCounter++;
        cacheHandler.saveLog(this);

        // System.out.println(
        // "FINE PLEACE: indice dal cache: "
        // + cacheHandler.getMoveIndex()
        // + " moveCounter: "
        // + moveCounter);
        super.place(cell);
    }

    public Boolean isInProgress() {
        return matchInProgress;
    }

    public boolean isOldMatch() {
        try {
            cacheHandler.loadLogsInMemoryFromDisk();
            cacheHandler.readLastLog();
        } catch (Exception e) {
            System.out.println("NO Previous match found");
            return false;
        }
        return true;
    }

    public void getOldMatch() {
        BoardLog log = cacheHandler.readLastLog();
        getBoard().fromCompactString(log.board());
        timestampOfLastMove = log.timestamp();
        setCurrentPlayer(new Player(BoardPoint.fromString(log.nextMove())));
        moveCounter = log.moveCounter();
        matchInProgress = true;
        cacheHandler.clearCache();
    }

    public void removeOldMatches() {
        cacheHandler.clearCache();
    }

    public String getTimestampOfLastMove() {
        return timestampOfLastMove;
    }

    public void goBackOneMove() {
        if (cacheHandler.getMoveIndex() <= 0) {
            System.out.println("No previous move found");
            return;
        }
        try {
            cacheHandler.decrementIndex();
            BoardLog log = cacheHandler.readLog(cacheHandler.getMoveIndex());
            getBoard().fromCompactString(log.board());
            setCurrentPlayer(new Player(BoardPoint.fromString(log.nextMove())));
            moveCounter--;
        } catch (Exception e) {
            System.out.println("No previous move found");
        }
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public int letterToIndex(char letter) {
        // Convert the character to uppercase to handle both cases
        char upperLetter = Character.toUpperCase(letter);
        if (upperLetter < 'A' || upperLetter > 'M') {
            return -1;
        }
        return upperLetter - 'A';
    }
}

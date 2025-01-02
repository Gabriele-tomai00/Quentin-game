package quentin.cache;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import quentin.game.LocalGame;

public record GameLog(String timeStamp, LocalGame game) implements Serializable {

    public String getReadableTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        return LocalDateTime.now().format(formatter);
    }
}

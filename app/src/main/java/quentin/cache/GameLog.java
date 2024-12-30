package quentin.cache;

import java.io.Serializable;
import quentin.game.LocalGame;

public record GameLog(String timeStamp, LocalGame game) implements Serializable {}

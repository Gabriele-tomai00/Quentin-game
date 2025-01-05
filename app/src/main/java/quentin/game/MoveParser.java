package quentin.game;

import quentin.exceptions.InvalidCellValuesException;

public class MoveParser {

    private final String string;

    public MoveParser(String string) {
        this.string = string.replace(" ", "");
    }

    public Cell parse() {
        int col;
        if (string.substring(0, 1).matches("[a-m]")) {
            col = string.charAt(0) - 'a';
        } else {
            throw new InvalidCellValuesException(
                    String.format(
                            "Row values span from 'a' to 'm', received %c", string.charAt(0)));
        }
        int row;
        if (string.substring(1).matches("\\b(1[0-3]|[1-9])\\b")) {
            row = Integer.parseInt(string.substring(1)) - 1;
        } else {
            throw new InvalidCellValuesException(
                    String.format(
                            "Column values span from '1' to '13', received: %s",
                            string.substring(1)));
        }
        return new Cell(row, col);
    }
}

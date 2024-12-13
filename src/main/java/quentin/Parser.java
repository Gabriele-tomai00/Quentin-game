package quentin;

public class Parser {

  private final String string;

  public Parser(String string) {
    this.string = string.replace(" ", "");
  }

  /*
   * public enum TokenType { ROW("[a-m]"), COLUMN("[0-9]+");
   *
   * private final String regex;
   *
   * TokenType(String regex) { this.regex = regex; }
   *
   * public Token next(String s, int i) { Matcher matcher = Pattern.compile(regex)
   * .matcher(s); if (!matcher.find(i)) { return null; } return new
   * Token(matcher.start(), matcher.end()); }
   *
   * public String getRegex() { return regex; } }
   *
   * private static class Token { private final int start; private final int end;
   *
   * public Token(int start, int end) { this.start = start; this.end = end; }
   *
   * }
   */
  public Cell parse() {

    int row;
    if (string.substring(0, 1).matches("[a-m]")) {
      row = string.charAt(0) - 'a';
    } else {
      throw new IllegalArgumentException();
    }
    int col;
    if (string.substring(1).matches("\\b(1[0-3]|[1-9])\\b")) {
      col = Integer.parseInt(string.substring(1)) - 1;
    } else {
      throw new IllegalArgumentException();
    }
    return new Cell(row, col);
  }
}

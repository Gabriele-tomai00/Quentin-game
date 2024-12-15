package quentin;

import java.util.Scanner;

public class TerminalGame extends GameStarter {

  private final String CLEAR;
  private final Scanner scanner;

  public TerminalGame() {
    super();
    CLEAR = "\033[H\033[2J";
    this.scanner = new Scanner(System.in);
  }

  @Override
  protected void displayMessage(String format) {
    System.out.printf("%s it's your turn:", game().getCurrentPlayer());
  }

  @Override
  protected void displayWinner() {
    System.out.print(CLEAR + String.format("%s has won", super.game().getCurrentPlayer()));
  }

  @Override
  protected Cell getInput() throws InvalidCellValuesException {
    return new Parser(scanner.next()).parse();
  }

  @Override
  protected void display() {
    System.out.println(CLEAR + super.game().getBoard());
  }

  @Override
  protected void startDisplay() {
    System.out.println("   ____  _    _            _   _          _____                      ");
    System.out.println("  / __ \\| |  | |          | | (_)        / ____|                     ");
    System.out.println(" | |  | | |  | | ___ _ __ | |_ _ _ __   | |  __  __ _ _ __ ___   ___ ");
    System.out.println(
        " | |  | | |  | |/ _ \\ '_ \\| __| | '_ \\  | | |_ |/ _` | '_ ` _ \\ / _ \\");
    System.out.println(" | |__| | |__| |  __/ | | | |_| | | | | | |__| | (_| | | | | | |  __/");
    System.out.println(
        "  \\___\\_\\\\____/ \\___|_| |_|\\__|_|_| |_|  \\_____|\\__,_|_| |_| |_|\\___|");
    System.out.println(
        " by Luis Bolaños Mures                                                          \n\n\n");

    System.out.println(super.game().getBoard());
  }
}

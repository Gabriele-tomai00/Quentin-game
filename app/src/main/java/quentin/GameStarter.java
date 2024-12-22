package quentin;

public interface GameStarter {

  public void start();

  public void startDisplay();

  public void displayMessage(String format);

  public void displayWinner();

  public Cell getInput();

  public void display();
}

import java.util.Scanner;
import java.util.InputMismatchException;
public class ConnectFourPlayer {
    private char symbol;
    private Scanner scanner;
    private String name;

    public ConnectFourPlayer(char symbol, Scanner scanner, String name) {
        this.symbol = symbol;
        this.scanner = scanner;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void makeMove(ConnectFourBoard board) {
        System.out.println(name + "'s turn. Enter column number (1-7):");
        int col = -1;
        while (col == -1) {
            try {
                col = scanner.nextInt() - 1; // Subtract 1 to convert to array index
                if (!board.isValidMove(col)) {
                    System.out.println("Invalid move. Please try again.");
                    col = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry. Please try again.");
                scanner.next(); // Clear the invalid input from the scanner
            }
        }
        board.setCurrentPlayer(symbol);
        board.dropDisc(col, symbol);
    }
}

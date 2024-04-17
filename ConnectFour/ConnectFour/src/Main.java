import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Connect Four!");
        System.out.println("Select game mode:");
        System.out.println("1. 2 player (Human vs Human)");
        System.out.println("2. 1 player (Human vs AI)");
        int choice = scanner.nextInt();

        if (choice == 1) {
            playHumanVsHuman(scanner);
        } else if (choice == 2) {
            playHumanVsAI(scanner);
        } else {
            System.out.println("Invalid choice. Exiting...");
        }

        scanner.close();
    }

    private static void playHumanVsHuman(Scanner scanner) {
        ConnectFourBoard board = new ConnectFourBoard();
        board.displayBoard();

        System.out.print("Enter Player 1's name: ");
        String player1Name = scanner.next();
        ConnectFourPlayer player1 = new ConnectFourPlayer('R', scanner, player1Name);

        System.out.print("Enter Player 2's name: ");
        String player2Name = scanner.next();
        ConnectFourPlayer player2 = new ConnectFourPlayer('Y', scanner, player2Name);

        while (!board.checkWin() && !board.isBoardFull()) {
            player1.makeMove(board);
            board.displayBoard();
            if (board.checkWin() || board.isBoardFull()) break;
            player2.makeMove(board);
            board.displayBoard();
        }

        printResult(board, player1, player2);
    }


    private static void playHumanVsAI(Scanner scanner) {
        ConnectFourBoard board = new ConnectFourBoard();
        board.displayBoard();

        System.out.print("Enter your name: ");
        String playerName = scanner.next();
        System.out.print("Choose your symbol (R/Y): ");
        char playerSymbol = scanner.next().charAt(0);

        ConnectFourPlayer humanPlayer = new ConnectFourPlayer(playerSymbol, scanner, playerName);
        ConnectFourAI aiPlayer = new ConnectFourAI(playerSymbol == 'R' ? 'Y' : 'R');

        System.out.println("Who will go first?");
        System.out.println("1. " + playerName);
        System.out.println("2. AI");
        int firstMoveChoice = scanner.nextInt();

        boolean playerTurn = (firstMoveChoice == 1);

        while (!board.checkWin() && !board.isBoardFull()) {
            if (playerTurn) {
                humanPlayer.makeMove(board);
                board.displayBoard();
                if (board.checkWin()) {
                    System.out.println(playerName + " wins!");
                    return;
                }
            } else {
                aiPlayer.makeMove(board);
                board.displayBoard();
                if (board.checkWin()){
                    System.out.println("AI wins!");
                    return;
                }
            }
            playerTurn = !playerTurn; //Switch turns
        }

        if (board.isBoardFull()){
            System.out.println("It's a draw!");
        }

    }

    // PRINTING RESULTS FOR HUMAN VS HUMAN
    private static void printResult(ConnectFourBoard board, ConnectFourPlayer player1, ConnectFourPlayer player2) {
        if (board.checkWin()) {
            char winningSymbol = board.getCurrentPlayer();
            String winnerName = (winningSymbol == 'R') ? player1.getName() : player2.getName();
            System.out.println(winnerName + " wins!");
        } else if (board.isBoardFull()) {
            System.out.println("It's a draw!");
            System.out.println("Do you want to restart the game? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("yes")) {
                main(new String[]{});
            } else {
                System.out.println("Exiting...");
            }
            scanner.close();
        } else {
            System.out.println("It's a draw!");
        }
    }

    // PRINTING RESULTS BETWEEN HUMAN VS AI
    private static void printResultAI(ConnectFourBoard board, char aiSymbol, Scanner scanner) {
        char winningSymbol = board.getCurrentPlayer();
        String winner;

        if (board.checkWin()) {
            if (winningSymbol == aiSymbol) {
                winner = "AI";
            } else {
                winner = "You";
            }
            System.out.println(winner + " wins!");
        } else if (board.isBoardFull()) {
            System.out.println("It's a draw!");
            System.out.println("Do you want to restart the game? (yes/no)");

            String choice = scanner.next();
            if (choice.equalsIgnoreCase("yes")) {
                main(new String[]{});
            } else {
                System.out.println("Exiting...");
            }
            scanner.close();
        } else {
            System.out.println("It's a draw!");
        }
    }

}
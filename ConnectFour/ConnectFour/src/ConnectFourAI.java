public class ConnectFourAI {
    private char symbol;
    private char opponentSymbol;

    public ConnectFourAI(char symbol) {
        this.symbol = symbol;
        this.opponentSymbol = (symbol == 'R') ? 'Y' : 'R';
    }

    public void makeMove(ConnectFourBoard board) {
        int[] bestMove = minimax(board, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        board.dropDisc(bestMove[1], symbol);
    }

    private int[] minimax(ConnectFourBoard board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        int[] bestMove = new int[2];
        int bestScore;
        char currentPlayer = maximizingPlayer ? symbol : opponentSymbol;

        if (depth == 0 || board.checkWin() || board.isBoardFull()) {
            int score = evaluate(board);
            return new int[]{score, -1};
        }

        if (maximizingPlayer) {
            bestScore = Integer.MIN_VALUE;
            for (int col = 0; col < board.getNumCols(); col++) {
                if (board.isValidMove(col)) {
                    board.dropDisc(col, symbol);
                    int score = minimax(board, depth - 1, alpha, beta, false)[0];
                    board.undoDrop(col);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = score;
                        bestMove[1] = col;
                    }
                    alpha = Math.max(alpha, score);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int col = 0; col < board.getNumCols(); col++) {
                if (board.isValidMove(col)) {
                    board.dropDisc(col, opponentSymbol);
                    int score = minimax(board, depth - 1, alpha, beta, true)[0];
                    board.undoDrop(col);
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove[0] = score;
                        bestMove[1] = col;
                    }
                    beta = Math.min(beta, score);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return bestMove;
    }

    private int evaluate(ConnectFourBoard board) {
        int score = 0;
        char currentPlayer = board.getCurrentPlayer();
        char aiSymbol = this.symbol; // Assuming AI's symbol is stored in the ConnectFourAI class

        if (board.checkWin() && currentPlayer == aiSymbol) {
            // AI WINS
            score = 1000;
        } else if (board.checkWin() && currentPlayer != aiSymbol) {
            // HUMAN WINS
            score = -1000;
        } else {
            // AI PRIORITIZE MOVE THAT COULD LED TO WIN
            char opponentSymbol = (aiSymbol == 'R') ? 'Y' : 'R';
            // ADD POINTS FOR POTENTIAL WINNING MOVE
            score += countPotentialWins(board, aiSymbol) * 100;
            // DEDUCT POINTS IF OPPONENT HAS POTENTIAL WINNING MOVE
            score -= countPotentialWins(board, opponentSymbol) * 100;
            score += evaluateStrategicPositioning(board, aiSymbol);
        }
        return score;
    }


    private int countPotentialWins(ConnectFourBoard board, char playerSymbol) {
        int potentialWins = 0;

        // Iterate through each column on the board
        for (int col = 0; col < board.getNumCols(); col++) {
            // Check if the current column has available space for a new disc
            if (board.isValidMove(col)) {
                // Get the row where the new disc would land
                int row = getNextAvailableRow(board, col);
                // Simulate dropping the disc and check if it leads to a potential win
                if (isPotentialWin(board, row, col, playerSymbol)) {
                    potentialWins++;
                }
            }
        }

        return potentialWins;
    }

    // Helper method to get the next available row in a column
    private int getNextAvailableRow(ConnectFourBoard board, int col) {
        for (int row = board.getNumRows() - 1; row >= 0; row--) {
            if (board.getBoard()[row][col] == ' ') {
                return row;
            }
        }
        return -1; // Column is full
    }

    // Helper method to check if placing a disc at a specific position leads to a potential win
    private boolean isPotentialWin(ConnectFourBoard board, int row, int col, char playerSymbol) {
        // Check horizontally
        int horizontalCount = countConsecutiveDiscs(board, row, col, 0, 1, playerSymbol);
        // Check vertically
        int verticalCount = countConsecutiveDiscs(board, row, col, 1, 0, playerSymbol);
        // Check diagonally (both directions)
        int diagonalCount1 = countConsecutiveDiscs(board, row, col, 1, 1, playerSymbol);
        int diagonalCount2 = countConsecutiveDiscs(board, row, col, 1, -1, playerSymbol);
        // A potential win occurs if there are at least three consecutive discs in any direction
        return horizontalCount >= 3 || verticalCount >= 3 || diagonalCount1 >= 3 || diagonalCount2 >= 3;
    }

    // Helper method to count consecutive discs in a specific direction from a given position
    private int countConsecutiveDiscs(ConnectFourBoard board, int row, int col, int rowDir, int colDir, char playerSymbol) {
        int count = 0;
        // Check consecutive discs in the specified direction
        while (row >= 0 && row < board.getNumRows() && col >= 0 && col < board.getNumCols() &&
                board.getBoard()[row][col] == playerSymbol) {
            count++;
            // Move to the next position in the specified direction
            row += rowDir;
            col += colDir;
        }
        return count;
    }


    private int evaluateStrategicPositioning(ConnectFourBoard board, char playerSymbol) {
        int strategicScore = 0;

        // Iterate through each position on the board
        for (int row = 0; row < board.getNumRows(); row++) {
            for (int col = 0; col < board.getNumCols(); col++) {
                if (board.getBoard()[row][col] == ' ') {
                    // Calculate the strategic score for the current position
                    strategicScore += calculatePositionScore(board, row, col, playerSymbol);
                }
            }
        }

        return strategicScore;
    }

    // Helper method to calculate the strategic score for a specific position
    private int calculatePositionScore(ConnectFourBoard board, int row, int col, char playerSymbol) {
        int positionScore = 0;

        // CHECK HORIZONTALLY
        int horizontalScore = calculateDirectionalScore(board, row, col, 0, 1, playerSymbol);
        // CHECK VERTICALLY
        int verticalScore = calculateDirectionalScore(board, row, col, 1, 0, playerSymbol);
        // CHECK DIAGONALLY (BOTH DIRECTIONS)
        int diagonalScore1 = calculateDirectionalScore(board, row, col, 1, 1, playerSymbol);
        int diagonalScore2 = calculateDirectionalScore(board, row, col, 1, -1, playerSymbol);

        // Sum up the scores for all directions
        positionScore = horizontalScore + verticalScore + diagonalScore1 + diagonalScore2;

        return positionScore;
    }

    // HELPER METHOD TO CALCULATE THE STRATEGIC SCORE FROM A GIVEN POSITION
    private int calculateDirectionalScore(ConnectFourBoard board, int row, int col, int rowDir, int colDir, char playerSymbol) {
        int score = 0;

        // CHECK IF PLACING DISK IN THIS DIRECTION COULD LEAD TO WIN
        if (hasPotentialWin(board, row, col, rowDir, colDir, playerSymbol)) {
            // ASSIGN HIGHER SCORE FOR POTENTIAL WINNING POSITIONS
            score += 10;
        }
        return score;
    }

    // HELPER METHOD
    private boolean hasPotentialWin(ConnectFourBoard board, int row, int col, int rowDir, int colDir, char playerSymbol) {
        int consecutiveDiscs = 0;
        int emptySpaces = 0;

        // Check consecutive discs and empty spaces in the specified direction
        while (row >= 0 && row < board.getNumRows() && col >= 0 && col < board.getNumCols()) {
            if (board.getBoard()[row][col] == playerSymbol) {
                consecutiveDiscs++;
            } else if (board.getBoard()[row][col] == ' ') {
                emptySpaces++;
            } else {
                // STOP CHECKING IF OPPONENT'S DISC IS ENCOUNTERED
                break;
            }
            row += rowDir;
            col += colDir;
        }
        // POTENTIAL WIN OCCURS IF THERE ARE
        // ATLEAST THREE DISCS IN ONE EMPTY SPACE
        return consecutiveDiscs >= 3 && emptySpaces >= 1;
    }



}

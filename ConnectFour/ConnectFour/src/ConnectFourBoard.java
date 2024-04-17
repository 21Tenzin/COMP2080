public class ConnectFourBoard {
    private char[][] board;
    private int numRows = 6;
    private int numCols = 7;
    private char currentPlayer;

    public ConnectFourBoard() {
        board = new char[numRows][numCols];
        initializeBoard();
        currentPlayer = 'R'; // Red player starts
    }

    private void initializeBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void displayBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("---------------");

    }


    public boolean checkWin() {
        // HORIZONTAL WINS
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col <= numCols - 4; col++) {
                if (checkLine(row, col, 0, 1))
                    return true;

            }
        }
        // VERTICAL WINS
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row <= numRows - 4; row++) {
                if (checkLine(row, col, 1, 0))
                    return true;

            }
        }

        // DIAGONAL WINS : BOTTOM LEFT TO TOP RIGHT
        for (int row = 3; row < numRows; row++) {
            for (int col = 0; col <= numCols - 4; col++) {
                if (checkLine(row, col, -1, 1))
                    return true;

            }
        }

        // DIAGONAL WINS : TOP LEFT TO BOTTOM RIGHT
        for (int row = 0; row <= numRows - 4; row++) {
            for (int col = 0; col <= numCols - 4; col++) {
                if (checkLine(row, col, 1, 1))
                    return true;

            }
        }

        return false;
    }

    // Method to check if there is a winning line
    private boolean checkLine(int row, int col, int rowDir, int colDir) {
        // GET THE COLOR OF THE DISC AT THE SPECIFIED POSITION
        char color = board[row][col];

        // IF THE COLOR IS EMPTY (NO DISC AT THIS POSITION) RETURN FALSE
        if (color == ' ')
            return false;

        // ITERATE THROUGH THE NEXT THREE POSITIONS IN THE SPECIFIED DIRECTION
        for (int i = 1; i < 4; i++) {
            // CALCULATE THE ROW AND COLUMN INDICES OF THE NEXT POSITION
            int nextRow = row + i * rowDir;
            int nextCol = col + i * colDir;

            // IF THE COLOR IS NOT THE SAME AS THE ORIGINAL COLOR, RETURN FALSE
            if (board[nextRow][nextCol] != color)
                return false;

        }
        // IF ALL POSITIONS IN THE LINE HAS THE SAME COLOR, RETURN TRUE
        // WE HAVE A WINNER
        return true;
    }



    public boolean isBoardFull() {
        for (int col = 0; col < numCols; col++) {
            // IF COLUMN HAS AN EMPTY SPACE, BOARD IS NOT YET FULL
            if (board[0][col] == ' ') {
                return false;
            }
        }
        // IF THERE'S NO MORE SPACE, THE BOARD IS FULL
        return true;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isValidMove(int col) {
        return col >= 0 && col < numCols && board[0][col] == ' ';
    }

    public void dropDisc(int col, char symbol) {
        for (int i = numRows - 1; i >= 0; i--) {
            if (board[i][col] == ' ') {
                board[i][col] = symbol;
                break;
            }
        }
    }

    public void undoDrop(int col) {
        for (int i = 0; i < numRows; i++) {
            if (board[i][col] != ' ') {
                board[i][col] = ' ';
                break;
            }
        }
    }

    public char getCell(int row, int col) {
        return board[row][col];
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}

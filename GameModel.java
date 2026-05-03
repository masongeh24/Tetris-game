public class GameModel {
    public static final int ROWS = 20;
    public static final int COLS = 10;
    
    private int[][] playfield;
    private int score;
    private int level;
    private int linesCleared;
    private boolean isGameOver;
    private boolean isPaused;

    // Piece tracking
    private int[][] currentPiece;
    private int currentX;
    private int currentY;
    
    // A simple default T-shape piece for testing logic
    private static final int[][] DEFAULT_SHAPE = {
        {1, 1, 1},
        {0, 1, 0}
    };

    public GameModel() {
        playfield = new int[ROWS][COLS];
        score = 0;
        level = 1;
        linesCleared = 0;
        isGameOver = false;
        isPaused = false;
        spawnPiece();
    }

    private void spawnPiece() {
        currentPiece = DEFAULT_SHAPE;
        currentY = 0;
        currentX = COLS / 2 - currentPiece[0].length / 2;
        
        if (!isValidPosition(currentPiece, currentX, currentY)) {
            isGameOver = true;
        }
    }

    private boolean isValidPosition(int[][] piece, int x, int y) {
        for (int r = 0; r < piece.length; r++) {
            for (int c = 0; c < piece[r].length; c++) {
                if (piece[r][c] != 0) {
                    int newX = x + c;
                    int newY = y + r;
                    
                    // Check bounds
                    if (newX < 0 || newX >= COLS || newY >= ROWS) {
                        return false;
                    }
                    
                    // Check collision with existing blocks on the playfield
                    if (newY >= 0 && playfield[newY][newX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void lockPiece() {
        for (int r = 0; r < currentPiece.length; r++) {
            for (int c = 0; c < currentPiece[r].length; c++) {
                if (currentPiece[r][c] != 0) {
                    // Only lock onto the board if it's within bounds
                    if (currentY + r >= 0) {
                        playfield[currentY + r][currentX + c] = currentPiece[r][c];
                    }
                }
            }
        }
        // TODO: clear lines after locking
    }

    /**
     * Called by the timer loop to handle gravity (moving piece down).
     */
    public void update() {
        if (isPaused || isGameOver) return;
        
        // Handle automatic gravity / dropping
        if (isValidPosition(currentPiece, currentX, currentY + 1)) {
            currentY++;
        } else {
            lockPiece();
            spawnPiece();
        }
    }

    public void moveLeft() {
        if (isPaused || isGameOver) return;
        if (isValidPosition(currentPiece, currentX - 1, currentY)) {
            currentX--;
        }
    }

    public void moveRight() {
        if (isPaused || isGameOver) return;
        if (isValidPosition(currentPiece, currentX + 1, currentY)) {
            currentX++;
        }
    }

    public void softDrop() {
        if (isPaused || isGameOver) return;
        if (isValidPosition(currentPiece, currentX, currentY + 1)) {
            currentY++;
        }
    }

    public void hardDrop() {
        if (isPaused || isGameOver) return;
        while (isValidPosition(currentPiece, currentX, currentY + 1)) {
            currentY++;
        }
        lockPiece();
        spawnPiece();
    }

    public void rotateClockwise() {
        if (isPaused || isGameOver) return;
        // Rotate current piece clockwise (to be implemented)
    }

    public void rotateCounterClockwise() {
        if (isPaused || isGameOver) return;
        // Rotate current piece counter-clockwise (to be implemented)
    }

    public void togglePause() {
        if (isGameOver) return;
        isPaused = !isPaused;
    }

    public void restart() {
        // Reset game state
        playfield = new int[ROWS][COLS];
        score = 0;
        level = 1;
        linesCleared = 0;
        isGameOver = false;
        isPaused = false;
        spawnPiece();
    }

    // Getters for the View
    public int[][] getPlayfield() { return playfield; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLinesCleared() { return linesCleared; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isPaused() { return isPaused; }
    
    // New getters for tracking the current piece
    public int[][] getCurrentPiece() { return currentPiece; }
    public int getCurrentX() { return currentX; }
    public int getCurrentY() { return currentY; }
}

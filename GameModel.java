public class GameModel {
    public static final int ROWS = 20;
    public static final int COLS = 10;
    
    private int[][] playfield;
    private int score;
    private int level;
    private int linesCleared;
    private boolean isGameOver;
    private boolean isPaused;

    // TODO: Add fields for current piece, next piece

    public GameModel() {
        playfield = new int[ROWS][COLS];
        score = 0;
        level = 1;
        linesCleared = 0;
        isGameOver = false;
        isPaused = false;
    }

    /**
     * Called by the timer loop to handle gravity (moving piece down).
     */
    public void update() {
        if (isPaused || isGameOver) return;
        // Handle automatic gravity / dropping
    }

    public void moveLeft() {
        if (isPaused || isGameOver) return;
        // Move current piece left
    }

    public void moveRight() {
        if (isPaused || isGameOver) return;
        // Move current piece right
    }

    public void softDrop() {
        if (isPaused || isGameOver) return;
        // Accelerate piece descent
    }

    public void hardDrop() {
        if (isPaused || isGameOver) return;
        // Instantly drop piece to bottom
    }

    public void rotateClockwise() {
        if (isPaused || isGameOver) return;
        // Rotate current piece clockwise
    }

    public void rotateCounterClockwise() {
        if (isPaused || isGameOver) return;
        // Rotate current piece counter-clockwise
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
    }

    // Getters for the View
    public int[][] getPlayfield() { return playfield; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLinesCleared() { return linesCleared; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isPaused() { return isPaused; }
}

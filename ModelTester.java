public class ModelTester {

    public static void main(String[] args) {
        System.out.println("--- Running Model Tests ---");
        testLeftWallCollision();
        testRightWallCollision();
        testHardDropAndLock();
        testGameOverCondition();
        System.out.println("--- Tests Complete ---");
    }

    private static void testLeftWallCollision() {
        GameModel model = new GameModel();

        // Move the piece to the far left
        for (int i = 0; i < 20; i++) {
            model.moveLeft();
        }

        int pieceX = model.getCurrentX();
        int[][] piece = model.getCurrentPiece();

        // Find the leftmost block of the piece's matrix
        int minCol = piece[0].length;
        for (int r = 0; r < piece.length; r++) {
            for (int c = 0; c < piece[r].length; c++) {
                if (piece[r][c] != 0 && c < minCol) {
                    minCol = c;
                }
            }
        }

        int absoluteLeft = pieceX + minCol;
        boolean pass = (absoluteLeft == 0);
        System.out
                .println("Test Left Wall Collision (Should stop at 0): " + pass + " | Absolute Left = " + absoluteLeft);
    }

    private static void testRightWallCollision() {
        GameModel model = new GameModel();

        // Move the piece to the far right
        for (int i = 0; i < 20; i++) {
            model.moveRight();
        }

        int pieceX = model.getCurrentX();
        int[][] piece = model.getCurrentPiece();

        // Find the rightmost block of the piece's matrix
        int maxCol = 0;
        for (int r = 0; r < piece.length; r++) {
            for (int c = 0; c < piece[r].length; c++) {
                if (piece[r][c] != 0 && c > maxCol) {
                    maxCol = c;
                }
            }
        }

        int absoluteRight = pieceX + maxCol;
        int expectedRight = GameModel.COLS - 1; // Standard grid size 10 means rightmost column is 9
        boolean pass = (absoluteRight == expectedRight);
        System.out.println("Test Right Wall Collision (Should stop at " + expectedRight + "): " + pass
                + " | Absolute Right = " + absoluteRight);
    }

    private static void testHardDropAndLock() {
        GameModel model = new GameModel();

        // Perform a hard drop
        model.hardDrop();

        // Once hard dropped, the piece should instantly lock to the playfield.
        // Verify by ensuring the permanent playfield grid is no longer all empty zeros.
        int[][] grid = model.getPlayfield();
        boolean foundLockedPiece = false;

        for (int r = 0; r < GameModel.ROWS; r++) {
            for (int c = 0; c < GameModel.COLS; c++) {
                if (grid[r][c] != 0) {
                    foundLockedPiece = true;
                    break;
                }
            }
        }

        System.out.println("Test Hard Drop & Piece Locking (Playfield shouldn't be empty): " + foundLockedPiece);
    }

    private static void testGameOverCondition() {
        GameModel model = new GameModel();

        // Constantly hard drop until the stack reaches the top and physically triggers
        // a Game Over
        for (int i = 0; i < 30; i++) {
            model.hardDrop();
        }

        boolean pass = model.isGameOver();
        System.out.println("Test Game Over State (Triggered when stack gets too high): " + pass);
    }
}

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;

public class GameView extends JFrame {
    private GameModel model;
    private GamePanel panel;

    private static final int BLOCK_SIZE = 25;
    private static final int BOARD_X = 275;
    private static final int BOARD_Y = 50;

    public GameView(GameModel model) {
        this.model = model;
        this.setTitle("Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // 4:3 Aspect ratio window (e.g., 800x600)
        panel = new GamePanel();
        panel.setPreferredSize(new Dimension(800, 600));
        this.add(panel);

        this.pack();
        this.setLocationRelativeTo(null); // Center on screen
    }

    /**
     * Refreshes the display based on the current model state.
     */
    public void refresh() {
        panel.repaint();
    }

    /**
     * Inner panel class responsible for custom drawing.
     */
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw static background
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw board background
            g.setColor(Color.BLACK);
            g.fillRect(BOARD_X, BOARD_Y, GameModel.COLS * BLOCK_SIZE, GameModel.ROWS * BLOCK_SIZE);

            // Draw board border
            g.setColor(Color.WHITE);
            g.drawRect(BOARD_X - 1, BOARD_Y - 1, GameModel.COLS * BLOCK_SIZE + 1, GameModel.ROWS * BLOCK_SIZE + 1);

            // Draw settled blocks from the playfield
            int[][] playfield = model.getPlayfield();
            for (int r = 0; r < GameModel.ROWS; r++) {
                for (int c = 0; c < GameModel.COLS; c++) {
                    if (playfield[r][c] != 0) {
                        drawBlock(g, BOARD_X + c * BLOCK_SIZE, BOARD_Y + r * BLOCK_SIZE, playfield[r][c]);
                    }
                }
            }

            // Draw the active falling piece
            int[][] currentPiece = model.getCurrentPiece();
            if (currentPiece != null) {
                int px = model.getCurrentX();
                int py = model.getCurrentY();
                for (int r = 0; r < currentPiece.length; r++) {
                    for (int c = 0; c < currentPiece[r].length; c++) {
                        if (currentPiece[r][c] != 0) {
                            // Only draw if the block is within the visible board
                            if (py + r >= 0 && py + r < GameModel.ROWS) {
                                drawBlock(g, BOARD_X + (px + c) * BLOCK_SIZE, BOARD_Y + (py + r) * BLOCK_SIZE,
                                        currentPiece[r][c]);
                            }
                        }
                    }
                }
            }

            // Draw side panels (Score, Level, Lines)
            int panelX = BOARD_X + (GameModel.COLS * BLOCK_SIZE) + 30;
            int panelY = BOARD_Y;
            int panelWidth = 150;
            int panelHeight = 130;
            
            // Draw panel background
            g.setColor(Color.BLACK);
            g.fillRect(panelX, panelY, panelWidth, panelHeight);
            
            // Draw panel border
            g.setColor(Color.WHITE);
            g.drawRect(panelX - 1, panelY - 1, panelWidth + 1, panelHeight + 1);
            
            // Draw text
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            int textX = panelX + 15;
            int textY = panelY + 35;
            g.drawString("Score: " + model.getScore(), textX, textY);
            g.drawString("Lines: " + model.getLinesCleared(), textX, textY + 40);
            g.drawString("Level: " + model.getLevel(), textX, textY + 80);
            
            // TODO: Draw Next Piece
            
            // Draw overlays (Game Over)
            if (model.isGameOver()) {
                g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black overlay
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.WHITE);
                g.setFont(new Font("SansSerif", Font.BOLD, 40));
                FontMetrics fm = g.getFontMetrics();
                String gameOverText = "GAME OVER";
                String restartText = "Press R to restart";
                
                int goWidth = fm.stringWidth(gameOverText);
                int goX = (getWidth() - goWidth) / 2;
                int goY = getHeight() / 2 - 20;
                g.drawString(gameOverText, goX, goY);
                
                g.setFont(new Font("SansSerif", Font.PLAIN, 20));
                fm = g.getFontMetrics();
                int rWidth = fm.stringWidth(restartText);
                int rX = (getWidth() - rWidth) / 2;
                int rY = getHeight() / 2 + 20;
                g.drawString(restartText, rX, rY);
            }
            
            // TODO: Draw Paused overlay
        }

        /**
         * Helper method to draw a single block with a gradient-like textured border.
         */
        private void drawBlock(Graphics g, int x, int y, int type) {
            Color color = getColorForType(type);

            // Draw the main block center
            g.setColor(color);
            g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);

            // Draw darker outer border
            g.setColor(color.darker().darker());
            g.drawRect(x, y, BLOCK_SIZE - 1, BLOCK_SIZE - 1);

            // Draw lighter inner highlights for a 3D bevel look
            g.setColor(color.brighter());
            g.drawLine(x, y, x + BLOCK_SIZE - 1, y);
            g.drawLine(x, y, x, y + BLOCK_SIZE - 1);
        }

        /**
         * Maps a shape ID to its traditional Tetris color.
         */
        private Color getColorForType(int type) {
            switch (type) {
                case 1:
                    return Color.CYAN;
                case 2:
                    return Color.BLUE;
                case 3:
                    return Color.ORANGE;
                case 4:
                    return Color.YELLOW;
                case 5:
                    return Color.GREEN;
                case 6:
                    return new Color(128, 0, 128); // Purple
                case 7:
                    return Color.RED;
                default:
                    return Color.GRAY;
            }
        }
    }
}

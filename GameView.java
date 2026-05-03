import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class GameView extends JFrame {
    private GameModel model;
    private GamePanel panel;

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
            
            // TODO: Draw the 10x20 playfield grid
            // TODO: Draw the active piece and settled blocks
            // TODO: Draw side panels (Score, Level, Lines, Next Piece)
            // TODO: Draw overlays (Game Over, Paused)
        }
    }
}

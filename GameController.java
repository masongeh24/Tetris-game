import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

public class GameController {
    private GameModel model;
    private GameView view;
    private Timer gameLoopTimer;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        // Add KeyListener to View to map inputs
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e);
            }
        });

        // Ensure the view can receive key events
        view.setFocusable(true);
        view.requestFocusInWindow();

        // Initialize game loop timer
        gameLoopTimer = new Timer(500, e -> {
            model.update();
            updateTimerSpeed();
            view.refresh();
            if (model.isGameOver()) {
                gameLoopTimer.stop();
            }
        });
    }

    private void updateTimerSpeed() {
        int baseSpeed = 500;
        int level = model.getLevel();
        int newSpeed = Math.max(50, baseSpeed - (level - 1) * 50); // Decrease by 50ms per level
        gameLoopTimer.setDelay(newSpeed);
    }

    /**
     * Maps keyboard inputs to the appropriate Model methods.
     */
    private void handleInput(KeyEvent e) {
        if (model.isTitleScreen()) {
            model.startGame();
            view.refresh();
            return;
        }

        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                model.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                model.moveRight();
                break;
            case KeyEvent.VK_DOWN:
                model.softDrop();
                break;
            case KeyEvent.VK_SPACE:
                model.hardDrop();
                break;
            case KeyEvent.VK_SLASH: // Forward slash
                model.rotateClockwise();
                break;
            case KeyEvent.VK_PERIOD: // Period
                model.rotateCounterClockwise();
                break;
            case KeyEvent.VK_P:
            case KeyEvent.VK_ESCAPE:
                model.togglePause();
                if (model.isPaused()) {
                    gameLoopTimer.stop();
                } else {
                    gameLoopTimer.start();
                }
                break;
            case KeyEvent.VK_R:
                if (model.isGameOver()) {
                    model.restart();
                    gameLoopTimer.start();
                }
                break;
        }
        view.refresh();
    }

    public void start() {
        view.setVisible(true);
        gameLoopTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel();
            GameView view = new GameView(model);
            GameController controller = new GameController(model, view);
            
            controller.start();
        });
    }
}

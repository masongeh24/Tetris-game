import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
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
            checkSoundEvents();
            if (model.isGameOver()) {
                playGameOver();
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
        checkSoundEvents();
        view.refresh();
    }

    private void playTone(float frequency, int durationMs) {
        playTone(frequency, durationMs, getAmplitude());
    }

    private void playTone(float frequency, int durationMs, int amplitude) {
        try {
            AudioFormat af = new AudioFormat(44100, 8, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(af);
            line.open(af);
            line.start();

            byte[] buf = new byte[44100 / 10]; // 100ms buffer
            for (int i = 0; i < buf.length; i++) {
                double angle = i / (44100.0 / frequency) * 2.0 * Math.PI;
                buf[i] = (byte) (Math.signum(Math.sin(angle)) * amplitude);
            }

            int samples = (int) ((durationMs / 1000.0) * 44100);
            int remaining = samples;
            while (remaining > 0) {
                int chunk = Math.min(buf.length, remaining);
                line.write(buf, 0, chunk);
                remaining -= chunk;
            }

            line.drain();
            line.close();
        } catch (LineUnavailableException e) {
            // Sound not available, ignore
        }
    }

    private void playGameOver() {
        // Two-tone game over sound
        new Thread(() -> {
            playTone(150, 300);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            playTone(100, 500);
        }).start();
    }

    private void playPieceLanded() {
        new Thread(() -> {
            playTone(200, 100);
        }).start();
    }

    private void playLineCleared() {
        new Thread(() -> {
            playTone(400, 150);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}
            playTone(600, 200);
        }).start();
    }

    private void checkSoundEvents() {
        if (model.consumeLineCleared()) {
            playLineCleared();
        } else if (model.consumePieceLanded()) {
            playPieceLanded();
        }
    }

    private int getAmplitude() {
        return Math.max(1, 3);
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

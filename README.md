# Tetris Game Specification

This document outlines the design and requirements for a Java Swing-based Tetris game, structured using the Model-View-Controller (MVC) architectural pattern.

## Architecture: Model-View-Controller (MVC)

### 1. Model (Game State & Logic)
The Model manages all the data, rules, and logic of the game, completely independent of the visual representation or user input.
*   **Playfield:** A 2D grid representing the board, sized at 10 columns by 20 rows.
*   **Tetrominoes:** Includes all 7 standard shapes (I, J, L, O, S, T, Z) with their traditional colors. Each piece can rotate in both directions (clockwise and counter-clockwise).
*   **State Tracking:** Maintains the currently falling piece (its type, rotation state, and X/Y coordinates), the "next piece" waiting in the queue, and the frozen/settled blocks on the grid.
*   **Scoring & Progression:** 
    *   Awards points based on the number of lines cleared simultaneously (1, 2, 3, or 4).
    *   Tracks the total lines cleared and current level. 
    *   The game speed (gravity) increases as the player clears more lines and levels up.
*   **Game Over Condition:** The game is lost when a newly spawned piece immediately collides with existing blocks on the playfield.

### 2. View (Rendering & User Interface)
The View is responsible for rendering the game state provided by the Model onto the screen and capturing keyboard events to send to the Controller.
*   **Window Layout:** A 4:3 aspect ratio game window featuring a static background.
*   **Main Playfield:** Renders the 10x20 grid where the active falling piece and settled blocks reside.
*   **Side Panel Information:** Renders UI elements alongside the main grid:
    *   "Next Piece" preview box.
    *   Current Score.
    *   Current Level.
    *   Total lines cleared.
*   **Block Visuals:** Pieces are rendered using their traditional colors, but drawn with a darker outer border than the center to create a gradient-like texture and depth.
*   **Overlays:**
    *   Displays a "Paused" overlay when the game is paused.
    *   Displays a "Game Over" overlay with a prompt to press 'R' to restart when the loss condition is met.

### 3. Controller (Input Handling & Game Loop)
The Controller acts as the bridge, listening to user input from the View, interpreting it, updating the Model accordingly, and telling the View when to refresh.
*   **Game Loop:** A timer-based loop that handles the automatic dropping (gravity) of the current piece. The tick rate of this timer decreases (speeds up the game) as the level increases.
*   **Input Mapping:**
    *   `Left Arrow`: Move piece left.
    *   `Right Arrow`: Move piece right.
    *   `Down Arrow`: Soft drop (accelerate piece descent).
    *   `Spacebar`: Hard drop (instantly drop the piece to the bottom).
    *   `/` (Forward Slash): Rotate piece Clockwise.
    *   `.` (Period): Rotate piece Counter-Clockwise.
    *   `Escape`: Pause / Unpause the game.
    *   `R`: Restart the game (active primarily during the Game Over state).

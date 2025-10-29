import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * A simplified FloodIt game implementation using standard Java libraries
 * The goal is to flood the entire board with the same color in as few moves as possible
 */
public class FloodItSimple extends JFrame implements MouseListener {
    
    private static final int CELL_SIZE = 30;
    private static final int BOARD_SIZE = 14;
    private static final int NUM_COLORS = 6;
    private static final int MAX_MOVES = BOARD_SIZE * 2;
    
    private Cell[][] board;
    private Color currentFloodColor;
    private int moves;
    private boolean gameOver;
    private boolean won;
    private Color[] availableColors;
    private Random random;
    
    public FloodItSimple() {
        this.random = new Random();
        this.moves = 0;
        this.gameOver = false;
        this.won = false;
        
        initializeColors();
        initializeBoard();
        
        setTitle("FloodIt Game - Moves: " + moves + "/" + MAX_MOVES);
        setSize(BOARD_SIZE * CELL_SIZE + 50, BOARD_SIZE * CELL_SIZE + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addMouseListener(this);
        
        // Set the top-left cell as flooded
        board[0][0].flooded = true;
        currentFloodColor = board[0][0].color;
    }
    
    private void initializeColors() {
        availableColors = new Color[NUM_COLORS];
        availableColors[0] = Color.RED;
        availableColors[1] = Color.BLUE;
        availableColors[2] = Color.GREEN;
        availableColors[3] = Color.YELLOW;
        availableColors[4] = Color.MAGENTA;
        availableColors[5] = Color.CYAN;
    }
    
    private void initializeBoard() {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Color randomColor = availableColors[random.nextInt(NUM_COLORS)];
                board[row][col] = new Cell(randomColor, false);
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Enable double buffering to reduce flickering
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the board
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = board[row][col];
                g.setColor(cell.color);
                g.fillRect(col * CELL_SIZE + 25, row * CELL_SIZE + 50, CELL_SIZE, CELL_SIZE);
                // Removed black border
            }
        }
        
        // Draw status
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String statusText = "Moves: " + moves + "/" + MAX_MOVES;
        if (gameOver) {
            statusText = won ? "You Won! Click to restart." : "Game Over! Click to restart.";
        }
        g.drawString(statusText, 25, 30);
        
        // Draw instructions
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Click on a color to flood the board. Goal: Make all cells the same color!", 
                     25, BOARD_SIZE * CELL_SIZE + 70);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameOver) {
            // Restart game
            initializeBoard();
            board[0][0].flooded = true;
            currentFloodColor = board[0][0].color;
            moves = 0;
            gameOver = false;
            won = false;
            setTitle("FloodIt Game - Moves: " + moves + "/" + MAX_MOVES);
            // Use repaint with specific bounds to reduce flickering
            repaint(25, 50, BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE);
            return;
        }
        
        int x = e.getX();
        int y = e.getY();
        
        // Convert mouse coordinates to board coordinates
        int col = (x - 25) / CELL_SIZE;
        int row = (y - 50) / CELL_SIZE;
        
        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            Cell clickedCell = board[row][col];
            
            // Only allow clicking on cells that are not already flooded and different color
            if (!clickedCell.flooded && clickedCell.color != currentFloodColor) {
                currentFloodColor = clickedCell.color;
                floodBoard();
                moves++;
                
                // Check win condition
                checkWinCondition();
                
                // Check if game is over
                if (moves >= MAX_MOVES && !won) {
                    gameOver = true;
                }
                
                setTitle("FloodIt Game - Moves: " + moves + "/" + MAX_MOVES);
                // Use repaint with specific bounds to reduce flickering
                repaint(25, 50, BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE);
            }
        }
    }
    
    private void floodBoard() {
        // Change the color of all currently flooded cells to the new flood color
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col].flooded) {
                    board[row][col].color = currentFloodColor;
                }
            }
        }
        
        // Now flood fill to add new cells that are adjacent and have the same color
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    Cell cell = board[row][col];
                    if (!cell.flooded && cell.color == currentFloodColor && isAdjacentToFlooded(row, col)) {
                        cell.flooded = true;
                        changed = true;
                    }
                }
            }
        }
    }
    
    private boolean isAdjacentToFlooded(int row, int col) {
        // Check all four directions
        if (row > 0 && board[row - 1][col].flooded) return true;
        if (row < BOARD_SIZE - 1 && board[row + 1][col].flooded) return true;
        if (col > 0 && board[row][col - 1].flooded) return true;
        if (col < BOARD_SIZE - 1 && board[row][col + 1].flooded) return true;
        
        return false;
    }
    
    private void checkWinCondition() {
        Color firstColor = board[0][0].color;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col].color != firstColor) {
                    return;
                }
            }
        }
        won = true;
        gameOver = true;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    // Inner class to represent a cell
    private static class Cell {
        Color color;
        boolean flooded;
        
        public Cell(Color color, boolean flooded) {
            this.color = color;
            this.flooded = flooded;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FloodItSimple().setVisible(true);
        });
    }
}

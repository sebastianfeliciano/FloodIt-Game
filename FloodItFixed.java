import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

//Interface for all Cells
interface ICell {
    //Creates a new Cell on a scene
    void makeCell(Graphics g, int x, int y);
    
    //Fixes the alignment and makes sure that a cell or Empty Cell
    //Exists on the border
    void fixOther(ICell left, ICell top, ICell right, ICell bottom);
}

//Class for MtCell (Empty)
class MtCell implements ICell {
    MtCell() {
    }

    //Places a cell made from this cell, onto a scene. 
    public void makeCell(Graphics g, int x, int y) {
        //Returns nothing because it cannot return a cell on a void method
        //Also we cannot return an MTCell because it should not exist on the board
    }

    //Method/Constructor that updates and aligns 
    //the cells surrounding and checks to see if a cell exists
    public void fixOther(ICell left, ICell top, ICell right, ICell bottom) {
        //Cannot enlist a ICell to Mt without checking if a left, top, etc; exists
        //So it cannot return anything
    }
}

//Cell Class
class Cell implements ICell {
    int x;
    int y;
    Color color;
    boolean flooded;
    ICell left;
    ICell top;
    ICell right;
    ICell bottom;

    //Standard size of x and y for board
    static int size = 20;

    //start Cell constructor
    Cell(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.flooded = false;
        this.left = null;
        this.top = null;
        this.right = null;
        this.bottom = null;
    }

    //constructor with full (Main Cell) with adjacent cells
    Cell(int x, int y, Color color, boolean flooded,
         ICell left, ICell top, ICell right, ICell bottom) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.flooded = flooded;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    //Places a cell made from this cell, onto a scene. 
    public void makeCell(Graphics g, int offsetX, int offsetY) {
        g.setColor(this.color);
        g.fillRect(offsetX + this.x * size, offsetY + this.y * size, size, size);
        // Removed black border
    }

    //Method/Constructor that updates and aligns the cells surrounding
    public void fixOther(ICell left, ICell top, ICell right, ICell bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}

//flood it game (Class Extends JFrame)
class FloodItWorld extends JFrame {
    
    private GamePanel gamePanel;

    //List of cells on the board
    ArrayList<Cell> board;

    //List of Colors Available
    ArrayList<Color> colors;
    int sizeOfBoard;
    int numColors;
    Random rand;
    int moves;
    int maxMoves;
    Color currentFloodColor;
    boolean gameOver;
    boolean won;

    ArrayList<Color> blankList = new ArrayList<Color>();

    //Empty and Default Flood It World
    //containing all 13 colors and a board size of 20
    FloodItWorld() {
        this(13, 20);
    }

    //Creates the Board by row and column from the
    //board size and placing the random colored cells
    FloodItWorld(int numColors, int sizeOfBoard) {
        //initialize sizeOfBoard
        this.sizeOfBoard = sizeOfBoard;
        this.numColors = numColors;
        this.board = new ArrayList<Cell>();
        this.colors = new ArrayList<Color>();
        this.moves = 0;
        this.maxMoves = sizeOfBoard * 2; // Reasonable move limit
        this.gameOver = false;
        this.won = false;

        colors.add(Color.red);
        colors.add(Color.green);
        colors.add(Color.blue);
        colors.add(Color.yellow);
        colors.add(Color.cyan);
        colors.add(Color.magenta);
        colors.add(Color.white);
        colors.add(Color.black);
        colors.add(Color.gray);
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.DARK_GRAY);
        colors.add(Color.orange);
        colors.add(Color.pink);
        //Allows for the list of colors to be randomized in order
        //ENHANCER - Shuffles the order so that a new set of colors are played each time
        Collections.shuffle(colors);

        this.rand = new Random();

        for (int r = 0; r < sizeOfBoard; r++) {
            for (int c = 0; c < sizeOfBoard; c++) {
                Color randomColor = colors.get(rand.nextInt(numColors));
                Cell newCell = new Cell(c, r, randomColor);
                board.add(newCell);
            }
        }

        // Set the top-left cell as flooded and set current flood color
        if (!board.isEmpty()) {
            board.get(0).flooded = true;
            this.currentFloodColor = board.get(0).color;
        }

        this.fixOther();
        
        // Set up the JFrame
        setTitle("FloodIt Game - Moves: " + moves + "/" + maxMoves);
        setSize(sizeOfBoard * Cell.size + 100, sizeOfBoard * Cell.size + 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create and add the game panel
        gamePanel = new GamePanel();
        add(gamePanel);
    }

    //FloodIt World that uses a seed to generate
    //a same colored Game. (This is used for testing makeScene())
    FloodItWorld(Random rand, int numColors, int sizeOfBoard) {
        this.numColors = numColors;
        this.sizeOfBoard = sizeOfBoard;
        this.rand = rand;
        this.board = new ArrayList<Cell>();
        this.colors = new ArrayList<Color>();
        this.moves = 0;
        this.maxMoves = sizeOfBoard * 2;
        this.gameOver = false;
        this.won = false;

        colors.add(Color.red);
        colors.add(Color.green);
        colors.add(Color.blue);
        colors.add(Color.yellow);
        colors.add(Color.cyan);
        colors.add(Color.magenta);
        colors.add(Color.white);
        colors.add(Color.black);
        colors.add(Color.gray);
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.DARK_GRAY);
        colors.add(Color.orange);
        colors.add(Color.pink);

        for (int i = 0; i < numColors; i++) {
            blankList.add(colors.get(rand.nextInt(colors.size())));
        }

        for (int r = 0; r < sizeOfBoard; r++) {
            for (int c = 0; c < sizeOfBoard; c++) {
                //Restricts amount of colors that will exist
                Color randomColor = blankList.get(rand.nextInt(numColors));
                Cell newCell = new Cell(c, r, randomColor);
                board.add(newCell);
            }
        }

        // Set the top-left cell as flooded and set current flood color
        if (!board.isEmpty()) {
            board.get(0).flooded = true;
            this.currentFloodColor = board.get(0).color;
        }

        this.fixOther();
        
        // Set up the JFrame
        setTitle("FloodIt Game - Moves: " + moves + "/" + maxMoves);
        setSize(sizeOfBoard * Cell.size + 100, sizeOfBoard * Cell.size + 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create and add the game panel
        gamePanel = new GamePanel();
        add(gamePanel);
    }

    //modifies the left, right, top, and bottom of this cell's links
    public void fixOther() {
        ICell leftNeighbor;
        ICell rightNeighbor;
        ICell topNeighbor;
        ICell bottomNeighbor;

        for (int i = 0; i < board.size(); i++) {
            int row = i / sizeOfBoard;
            int col = i % sizeOfBoard;

            // check bottom cell
            if (row + 1 < sizeOfBoard) {
                bottomNeighbor = board.get(i + sizeOfBoard);
            }
            else {
                bottomNeighbor = new MtCell();
            }

            // check top cell
            if (row - 1 >= 0) {
                topNeighbor = board.get(i - sizeOfBoard);
            }
            else {
                topNeighbor = new MtCell();
            }

            // check left cell
            if (col - 1 >= 0) {
                leftNeighbor = board.get(i - 1);
            }
            else {
                leftNeighbor = new MtCell();
            }

            // check right cell
            if (col + 1 < sizeOfBoard) {
                rightNeighbor = board.get(i + 1);
            }
            else {
                rightNeighbor = new MtCell();
            }

            board.get(i).fixOther(leftNeighbor, topNeighbor, rightNeighbor, bottomNeighbor);
        }
    }

    // Custom GamePanel with double buffering
    private class GamePanel extends JPanel implements MouseListener {
        public GamePanel() {
            addMouseListener(this);
            setPreferredSize(new Dimension(FloodItWorld.this.sizeOfBoard * Cell.size + 100, FloodItWorld.this.sizeOfBoard * Cell.size + 150));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Enable double buffering to reduce flickering
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw the board
            for (int i = 0; i < FloodItWorld.this.board.size(); i++) {
                FloodItWorld.this.board.get(i).makeCell(g, 25, 50);
            }
            
            // Add game status text
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            String statusText = "Moves: " + FloodItWorld.this.moves + "/" + FloodItWorld.this.maxMoves;
            if (FloodItWorld.this.gameOver) {
                statusText = FloodItWorld.this.won ? "You Won! Click to restart." : "Game Over! Click to restart.";
            }
            g.drawString(statusText, 25, 30);
            
            // Add instructions
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Click on a color to flood the board. Goal: Make all cells the same color!", 
                         25, FloodItWorld.this.sizeOfBoard * Cell.size + 70);
        }
        
        // Handle mouse clicks
        public void mouseClicked(MouseEvent e) {
            if (FloodItWorld.this.gameOver) {
                // Restart game
                FloodItWorld.this.initializeBoard();
                FloodItWorld.this.board.get(0).flooded = true;
                FloodItWorld.this.currentFloodColor = FloodItWorld.this.board.get(0).color;
                FloodItWorld.this.moves = 0;
                FloodItWorld.this.gameOver = false;
                FloodItWorld.this.won = false;
                FloodItWorld.this.setTitle("FloodIt Game - Moves: " + FloodItWorld.this.moves + "/" + FloodItWorld.this.maxMoves);
                repaint();
                return;
            }
            
            // Calculate which cell was clicked
            int cellX = (e.getX() - 25) / Cell.size;
            int cellY = (e.getY() - 50) / Cell.size;
            
            if (cellX >= 0 && cellX < FloodItWorld.this.sizeOfBoard && cellY >= 0 && cellY < FloodItWorld.this.sizeOfBoard) {
                int cellIndex = cellY * FloodItWorld.this.sizeOfBoard + cellX;
                Cell clickedCell = FloodItWorld.this.board.get(cellIndex);
                
                // Only allow clicking on cells that are not already flooded
                if (!clickedCell.flooded && clickedCell.color != FloodItWorld.this.currentFloodColor) {
                    // Change the flood color and flood the board
                    FloodItWorld.this.currentFloodColor = clickedCell.color;
                    FloodItWorld.this.floodBoard();
                    FloodItWorld.this.moves++;
                    
                    // Check win condition
                    FloodItWorld.this.checkWinCondition();
                    
                    // Check if game is over
                    if (FloodItWorld.this.moves >= FloodItWorld.this.maxMoves && !FloodItWorld.this.won) {
                        FloodItWorld.this.gameOver = true;
                    }
                    
                    FloodItWorld.this.setTitle("FloodIt Game - Moves: " + FloodItWorld.this.moves + "/" + FloodItWorld.this.maxMoves);
                    repaint();
                }
            }
        }
        
        // Required mouse event methods
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    // Initialize board for restart
    private void initializeBoard() {
        board.clear();
        for (int r = 0; r < sizeOfBoard; r++) {
            for (int c = 0; c < sizeOfBoard; c++) {
                Color randomColor = colors.get(rand.nextInt(numColors));
                Cell newCell = new Cell(c, r, randomColor);
                board.add(newCell);
            }
        }
        fixOther();
    }

    // Flood fill algorithm
    private void floodBoard() {
        // Change the color of all currently flooded cells to the new flood color
        for (Cell cell : board) {
            if (cell.flooded) {
                cell.color = currentFloodColor;
            }
        }
        
        // Now flood fill to add new cells that are adjacent and have the same color
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < board.size(); i++) {
                Cell cell = board.get(i);
                if (!cell.flooded && cell.color == currentFloodColor && isAdjacentToFlooded(i)) {
                    cell.flooded = true;
                    changed = true;
                }
            }
        }
    }

    // Check if a cell is adjacent to a flooded cell
    private boolean isAdjacentToFlooded(int cellIndex) {
        int row = cellIndex / sizeOfBoard;
        int col = cellIndex % sizeOfBoard;
        
        // Check all four directions
        if (row > 0 && board.get(cellIndex - sizeOfBoard).flooded) return true;
        if (row < sizeOfBoard - 1 && board.get(cellIndex + sizeOfBoard).flooded) return true;
        if (col > 0 && board.get(cellIndex - 1).flooded) return true;
        if (col < sizeOfBoard - 1 && board.get(cellIndex + 1).flooded) return true;
        
        return false;
    }

    // Check if all cells are the same color (win condition)
    private void checkWinCondition() {
        Color firstColor = board.get(0).color;
        for (Cell cell : board) {
            if (cell.color != firstColor) {
                return;
            }
        }
        won = true;
        gameOver = true;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FloodItWorld().setVisible(true);
        });
    }
}

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import tester.Tester;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//We created an EmptyCell to distinguish the border cells

//Interface for all Cells
interface ICell {

  //Creates a new Cell on a scene
  void makeCell(WorldScene scene);

  //Fixes the alginment and make sures that a cell or Empty Cell
  //Exists on the border
  void fixOther(ICell left, ICell top, ICell right, ICell bottom);

}

//Class for MtCell (Empty)
class MtCell implements ICell {
  MtCell(){

  }

  //Places a cell made from this cell, onto a scene. 
  public void makeCell(WorldScene scene) {
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
  //Note: TA told me I should be returning
  public void makeCell(WorldScene scene) {
    RectangleImage image = new RectangleImage(size, size, OutlineMode.SOLID, this.color);
    scene.placeImageXY(image, this.x * size + size/2, this.y * size + size/2);
  }

  //Method/Constructor that updates and aligns the cells surrounding
  public void fixOther(ICell left, ICell top, ICell right, ICell bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }
}

//flood it game (Class Extends World)
class FloodItWorld extends World {

  //List of cells on the board
  ArrayList<Cell> board;

  //List of Colors Avalible
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

  // draws scene
  public WorldScene makeScene() {

    WorldScene scene = new WorldScene((sizeOfBoard * Cell.size) + 100, 
        (sizeOfBoard * Cell.size) + 100);

    for (int i = 0; i < board.size(); i++) {
      board.get(i).makeCell(scene);
    }
    
    // Add game status text
    String statusText = "Moves: " + moves + "/" + maxMoves;
    if (gameOver) {
      statusText = won ? "You Won!" : "Game Over!";
    }
    
    TextImage status = new TextImage(statusText, 20, Color.BLACK);
    scene.placeImageXY(status, sizeOfBoard * Cell.size / 2, sizeOfBoard * Cell.size + 30);
    
    return scene;
  }

  // Handle mouse clicks
  public void onMouseClicked(Posn pos) {
    if (gameOver) return;
    
    // Calculate which cell was clicked
    int cellX = (pos.x - Cell.size/2) / Cell.size;
    int cellY = (pos.y - Cell.size/2) / Cell.size;
    
    if (cellX >= 0 && cellX < sizeOfBoard && cellY >= 0 && cellY < sizeOfBoard) {
      int cellIndex = cellY * sizeOfBoard + cellX;
      Cell clickedCell = board.get(cellIndex);
      
      // Only allow clicking on cells that are not already flooded
      if (!clickedCell.flooded && clickedCell.color != currentFloodColor) {
        // Change the flood color and flood the board
        currentFloodColor = clickedCell.color;
        floodBoard();
        moves++;
        
        // Check win condition
        checkWinCondition();
        
        // Check if game is over
        if (moves >= maxMoves && !won) {
          gameOver = true;
        }
      }
    }
  }

  // Flood fill algorithm
  private void floodBoard() {
    // Reset all flooded flags except the top-left cell
    for (int i = 1; i < board.size(); i++) {
      board.get(i).flooded = false;
    }
    
    // Start flood fill from top-left cell
    floodFill(0);
  }

  // Recursive flood fill
  private void floodFill(int cellIndex) {
    if (cellIndex < 0 || cellIndex >= board.size()) return;
    
    Cell cell = board.get(cellIndex);
    if (cell.flooded) return;
    
    // Check if this cell should be flooded
    if (cell.color == currentFloodColor || isAdjacentToFlooded(cellIndex)) {
      cell.flooded = true;
      cell.color = currentFloodColor;
      
      // Flood adjacent cells
      int row = cellIndex / sizeOfBoard;
      int col = cellIndex % sizeOfBoard;
      
      // Check and flood adjacent cells
      if (row > 0) floodFill(cellIndex - sizeOfBoard); // top
      if (row < sizeOfBoard - 1) floodFill(cellIndex + sizeOfBoard); // bottom
      if (col > 0) floodFill(cellIndex - 1); // left
      if (col < sizeOfBoard - 1) floodFill(cellIndex + 1); // right
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
}

//to test examples and tests
class Example {
  Cell cell1;
  Cell cell2; 
  Cell cell3;
  Cell cell4; 
  Cell cell5;

  ArrayList<Cell> listOfCells;
  ArrayList<Cell> listOfCells2;

  ArrayList<Color> colors;

  WorldScene scene;
  WorldScene scene1cell1;
  WorldScene scene1cell2;
  Random rand;

  FloodItWorld game;
  FloodItWorld game2;
  FloodItWorld gameRandomSeed;
  FloodItWorld gameNone;


  // initializes test cases
  void initData() {
    cell1 = new Cell(0, 0, Color.RED);
    cell2 = new Cell(1, 0, Color.BLUE);
    cell3 = new Cell(0, 1, Color.GREEN);
    cell4 = new Cell(1, 1, Color.PINK);
    // cellExample1 = new Cell(1, 2, Color.CYAN);

    listOfCells = new ArrayList<Cell>();
    listOfCells.add(cell1);
    listOfCells.add(cell2);
    listOfCells.add(cell3);

    listOfCells2 = new ArrayList<Cell>();
    listOfCells2.add(cell1);
    listOfCells2.add(cell4);

    colors = new ArrayList<Color>();
    colors.add(Color.BLUE);
    colors.add(Color.YELLOW);
    colors.add(Color.RED);
    colors.add(Color.BLACK);

    scene = new WorldScene(500, 500);
    scene1cell1 = new WorldScene(500, 500);
    scene1cell2 = new WorldScene(500, 500);
    scene1cell1.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, cell1.color), 
        cell1.x * 20 + 10, cell1.y * 20 + 10);
    scene1cell2.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, cell2.color), 
        cell2.x * 20 + 10, cell2.y * 20 + 10);
    rand = new Random(13);

  }

  //big bang
  void testBigBang(Tester t) {
    this.initData();

    game = new FloodItWorld(new Random(5), 2, 2);
    game2 = new FloodItWorld(2, 10);
    gameNone = new FloodItWorld();

    int sizeW = game2.sizeOfBoard * Cell.size;
    int sizeH = game2.sizeOfBoard * Cell.size;

    game2.bigBang(sizeW + 50, sizeH + 50, 1.0);
    game.bigBang(sizeW + 50, sizeH + 50, 1.0);
    gameNone.bigBang(sizeW + 350, sizeH + 350, 1.0); //default game

  }

  //tests makeScene()
  void testmakeScene(Tester t) {
    initData();

    //Seed Random
    gameRandomSeed = new FloodItWorld(new Random(5), 2, 2);

    WorldScene newScene = new WorldScene((2 * Cell.size) + 100, (2 * Cell.size) + 100);

    RectangleImage redCell = new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED);
    RectangleImage blueCell = new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE);
    newScene.placeImageXY(blueCell, 20, 20);
    newScene.placeImageXY(redCell, 20, 40);
    newScene.placeImageXY(blueCell, 40, 20);
    newScene.placeImageXY(redCell, 40, 40);

    // Add status text to expected scene
    TextImage status = new TextImage("Moves: 0/4", 20, Color.BLACK);
    newScene.placeImageXY(status, 2 * Cell.size / 2, 2 * Cell.size + 30);

    t.checkExpect(this.gameRandomSeed.makeScene(), newScene);
  }

  // tests fixOther()
  void testFixOther(Tester t) {
    initData();

    gameRandomSeed = new FloodItWorld(new Random(5), 2, 2);
    gameRandomSeed.fixOther();
    Cell topLeft = gameRandomSeed.board.get(0);      // (0,0)
    Cell topRight = gameRandomSeed.board.get(1);     // (1,0)
    Cell bottomLeft = gameRandomSeed.board.get(2);   // (0,1)
    Cell bottomRight = gameRandomSeed.board.get(3);  // (1,1)

    // Test top neighbors
    t.checkExpect(topLeft.top, new MtCell());
    t.checkExpect(topRight.top, new MtCell());
    t.checkExpect(bottomLeft.top, topLeft);
    t.checkExpect(bottomRight.top, topRight);

    // Test bottom neighbors
    t.checkExpect(topLeft.bottom, bottomLeft);
    t.checkExpect(topRight.bottom, bottomRight);
    t.checkExpect(bottomLeft.bottom, new MtCell());
    t.checkExpect(bottomRight.bottom, new MtCell());

    // Test left neighbors
    t.checkExpect(topLeft.left, new MtCell());
    t.checkExpect(topRight.left, topLeft);
    t.checkExpect(bottomLeft.left, new MtCell());
    t.checkExpect(bottomRight.left, bottomLeft);

    // Test right neighbors
    t.checkExpect(topLeft.right, topRight);
    t.checkExpect(topRight.right, new MtCell());
    t.checkExpect(bottomLeft.right, bottomRight);
    t.checkExpect(bottomRight.right, new MtCell());
  }

  // test makeCell(WorldScene scene)
  void testMakeCell(Tester t) {
    initData();
    cell1.makeCell(scene);
    t.checkExpect(scene, scene1cell1);

    initData();
    cell2.makeCell(scene);
    t.checkExpect(scene, scene1cell2);
  }

}
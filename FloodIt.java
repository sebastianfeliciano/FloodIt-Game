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
    scene.placeImageXY(image, this.x * 2 + 20, this.y * 2 + 20);
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

  ArrayList<Color> blankList = new ArrayList<Color>(numColors);

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
    this.board = new ArrayList<Cell>();
    this.colors = new ArrayList<Color>();

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

        Cell newCell = new Cell(((r * Cell.size) / 2),
            ((c * Cell.size) / 2), randomColor);
        board.add(newCell);
      }
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

        Cell newCell = new Cell(((r * Cell.size) / 2),
            ((c * Cell.size) / 2), randomColor);
        board.add(newCell);
      }
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

      // check bottom cell is an EmptyCell
      if (i + sizeOfBoard < board.size()) {
        bottomNeighbor = board.get(i + sizeOfBoard);

      }
      else {
        bottomNeighbor = new MtCell();
      }

      // check top cell is an EmptyCell
      if (i - sizeOfBoard > 0) {
        topNeighbor = board.get(i - sizeOfBoard);
      }
      else {
        topNeighbor = new MtCell();
      }

      // check left cell is an EmptyCell
      if (i / sizeOfBoard != 0) {
        leftNeighbor = board.get(i - 1);
      }
      else {
        leftNeighbor = new MtCell();
      }

      // check right cell is an EmptyCell
      if (i / sizeOfBoard != sizeOfBoard - 1) {
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

    WorldScene scene = new WorldScene((sizeOfBoard * Cell.size) + 500, 
        (sizeOfBoard * Cell.size) + 500);

    for (int i = 0; i < board.size(); i++) {

      board.get(i).makeCell(scene);
    }
    return scene;
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
        cell1.x * 2 + 20, cell1.y * 2 + 20);
    scene1cell2.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, cell2.color), 
        cell2.x * 2 + 20, cell2.y * 2 + 20);
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

    WorldScene newScene = new WorldScene((2 * Cell.size) + 500, (2 * Cell.size) + 500);

    RectangleImage redCell = new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED);
    RectangleImage blueCell = new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE);
    newScene.placeImageXY(blueCell, 20, 20);
    newScene.placeImageXY(redCell, 20, 40);
    newScene.placeImageXY(blueCell, 40, 20);
    newScene.placeImageXY(redCell, 40, 40);

    t.checkExpect(this.gameRandomSeed.makeScene(), newScene);
  }

  // tests fixOther()
  void testFixOther(Tester t) {
    initData();

    gameRandomSeed = new FloodItWorld(new Random(5), 2, 2);
    gameRandomSeed.fixOther();
    Cell leftNeighbor = gameRandomSeed.board.get(0);
    Cell rightNeighbor = gameRandomSeed.board.get(1);
    Cell topNeighbor = gameRandomSeed.board.get(2);
    Cell bottomNeighbor = gameRandomSeed.board.get(3);

    t.checkExpect(leftNeighbor.top, new MtCell());
    t.checkExpect(rightNeighbor.top, new MtCell());
    t.checkExpect(topNeighbor.top, new MtCell());
    t.checkExpect(bottomNeighbor.top, gameRandomSeed.board.get(2 - 1));

    t.checkExpect(leftNeighbor.bottom, gameRandomSeed.board.get(0 + gameRandomSeed.sizeOfBoard));
    t.checkExpect(rightNeighbor.bottom, gameRandomSeed.board.get(1 + gameRandomSeed.sizeOfBoard));
    t.checkExpect(topNeighbor.bottom, new MtCell());
    t.checkExpect(bottomNeighbor.bottom, new MtCell());

    t.checkExpect(leftNeighbor.left, new MtCell());
    t.checkExpect(rightNeighbor.left, new MtCell());
    t.checkExpect(topNeighbor.left, gameRandomSeed.board.get(0 + 1));
    t.checkExpect(bottomNeighbor.left, gameRandomSeed.board.get(0 + gameRandomSeed.sizeOfBoard));

    t.checkExpect(leftNeighbor.right, gameRandomSeed.board.get(2 - 1));
    t.checkExpect(rightNeighbor.right, gameRandomSeed.board.get(0 + gameRandomSeed.sizeOfBoard));
    t.checkExpect(topNeighbor.right, new MtCell());
    t.checkExpect(bottomNeighbor.right, new MtCell());


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
package logic;
 
import java.util.Random;
import java.util.Stack;

public class Board {
  public static final int NUM_ROWS = 15;
  public static final int NUM_COLUMNS = 20;
  public static final int NUM_MINES = NUM_ROWS * NUM_COLUMNS / 8;
 
  public Square[][] square;
  public Stack<boolean[][]> undoList ;

 
  public Board() {
    undoList = new Stack<>();
    square = new Square[NUM_ROWS][NUM_COLUMNS];
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        square[i][j] = new Square();
        //squaresStats[i][j]=square[i][j].isOpen();
      }
    }
    //undoList.push(squaresStats);// save opened and unopened status
 
    // đặt mìn vào các ô ngẫu nhiên
    for (int i = 0; i < NUM_MINES; i++) {
      int x = genRan(NUM_ROWS);
      int y = genRan(NUM_COLUMNS);
      // nếu có mìn rồi thì đặt ngẫu nhiên vào ô khác
      while (square[x][y].isHasMine()) {
        x = genRan(NUM_ROWS);
        y = genRan(NUM_COLUMNS);
      }
      square[x][y].setHasMine(true);
    }
 
    // ghi số lượng mìn xung quanh vào ô
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        int count = 0;
        for (int m = -1; m <= 1; m++) {
          if (i + m < 0) { m++; }
          if (i + m > NUM_ROWS - 1) { break; }
          for (int n = -1; n <= 1; n++) {
            if (j + n < 0) { n++; }
            if (j + n > NUM_COLUMNS - 1) { break; }
            if (!(m == 0 && n == 0) && square[i + m][j + n].isHasMine()) {
              count++;
            }
          }
        }
        square[i][j].setNumMineAround(count);
      }
    }
    //undoList.add(getListSquare());
  }
 
  private int genRan(int range) {
    Random rd = new Random();
    return rd.nextInt(range);
  }
 
  public Square[][] getListSquare() {
    return square;
  }
 
  public boolean play(int x, int y) {
    if (!square[x][y].isOpen()) {
      square[x][y].setOpen(true);
      if (square[x][y].isHasMine()) {
        return false;
      }
      if (square[x][y].getNumMineAround() == 0 ||(square[x][y].getNumMineAround() == 0 && square[x][y].isTarget())) { //if clicked on a square with surrounding mines =0
        for (int m = -1; m <= 1; m++) {
          if (x + m < 0) { m++; }
          if (x + m > NUM_ROWS - 1) { break; }
          for (int n = -1; n <= 1; n++) { //expand area until surrounded with squares with surroundind mines!=0
            if (y + n < 0) { n++; }
            if (y + n > NUM_COLUMNS - 1) { break; }
            play(x + m, y + n);
          }
        }
      }
    }
    return true;
  }
 
  public void target(int x, int y) {
    if (!square[x][y].isOpen()) { //not open
      if (!square[x][y].isTarget()) { // and hasn't put flag
        square[x][y].setTarget(true); //put flag
          
      } else {
        square[x][y].setTarget(false); //remove flag
          //remainingMines++;
      }
    }
  }

  //reveal all
  public void showAllSquares() {
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
          square[i][j].setOpen(true);
      }
    }
  }
  // undo for backing the move you just do
  public void undo(){
    if(!undoList.isEmpty()){
      for (int i = 0; i < square.length; i++) {
        for (int j = 0; j < square[0].length; j++) {
          square[i][j].setOpen(undoList.peek()[i][j]);
        }
      }
      undoList.pop();
    }

  }
  // save a move to stack and use undo class to back your step
  public void saveToStatusStack(){
    boolean[][] squaresStats =new boolean[NUM_ROWS][NUM_COLUMNS];
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        squaresStats[i][j]=square[i][j].isOpen();
      }
    }
    undoList.push(squaresStats);
  }

}
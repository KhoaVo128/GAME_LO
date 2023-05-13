package logic;
 
import java.util.Random;
 
public class Board {
  public static final int NUM_ROWS = 15;
  public static final int NUM_COLUMNS = 20;
  public static final int NUM_MINES = NUM_ROWS * NUM_COLUMNS / 5;
  public int remainingMines;
 
  private Square[][] square;
 
  public Board() {
    remainingMines=NUM_MINES;
    square = new Square[NUM_ROWS][NUM_COLUMNS];
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        square[i][j] = new Square();
      }
    }
 
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
      if (square[x][y].getNumMineAround() == 0) {
        for (int m = -1; m <= 1; m++) {
          if (x + m < 0) { m++; }
          if (x + m > NUM_ROWS - 1) { break; }
          for (int n = -1; n <= 1; n++) {
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
    if (!square[x][y].isOpen()) {
      if (!square[x][y].isTarget()) {
        square[x][y].setTarget(true);
      } else {
        square[x][y].setTarget(false);
      }
    }
  }
 
  public void showAllSquares() {
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        square[i][j].setOpen(true);
      }
    }
  }
}
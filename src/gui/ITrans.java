package gui;
 
import logic.Square;

import java.util.List;

public interface ITrans {
  Square[][] getListSquare();
  void play(int x, int y);
  void target(int x, int y);
  void restart();
  void undo();

  void saveToStatusStack();

}
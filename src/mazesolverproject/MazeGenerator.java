package mazesolverproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MazeGenerator {
  private int dimensionX, dimensionY; 
  private int gridDimensionX, gridDimensionY; 
  private char[][] grid; 
  private Cell[][] cells; 
  private Random random = new Random(); 

  
  public MazeGenerator(int aDimension) {
      this(aDimension, aDimension);
  }
  
  public MazeGenerator(int xDimension, int yDimension) {
      dimensionX = xDimension;
      dimensionY = yDimension;
      gridDimensionX = xDimension * 4 + 1;
      gridDimensionY = yDimension * 2 + 1;
      grid = new char[gridDimensionX][gridDimensionY];
      init();
      generateMaze();
  }

  private void init() {

      cells = new Cell[dimensionX][dimensionY];
      for (int x = 0; x < dimensionX; x++) {
          for (int y = 0; y < dimensionY; y++) {
              cells[x][y] = new Cell(x, y, false); 
          }
      }
  }

   private class Cell {
    int x, y; 
    ArrayList<Cell> neighbors = new ArrayList<>();
    boolean visited = false;    
    Cell parent = null;    
    boolean inPath = false;    
    double travelled;    
    double projectedDist;    
    boolean wall = true;    
    boolean open = true;
    
    Cell(int x, int y) {
        this(x, y, true);
    }
    
    Cell(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.wall = isWall;
    }
    
    void addNeighbor(Cell other) {
        if (!this.neighbors.contains(other)) { 
            this.neighbors.add(other);
        }
        if (!other.neighbors.contains(this)) { 
            other.neighbors.add(this);
        }
    }
    
    boolean isCellBelowNeighbor() {
        return this.neighbors.contains(new Cell(this.x, this.y + 1));
    }
    
    boolean isCellRightNeighbor() {
        return this.neighbors.contains(new Cell(this.x + 1, this.y));
    }

    @Override
    public String toString() {
        return String.format("Cell(%s, %s)", x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Cell)) return false;
        Cell otherCell = (Cell) other;
        return (this.x == otherCell.x && this.y == otherCell.y);
    }
    
    @Override
    public int hashCode() {
        return this.x + this.y * 256;
    }
  }
  
  private void generateMaze() {
      generateMaze(0, 0);
  }
 
  private void generateMaze(int x, int y) {
      generateMaze(getCell(x, y)); 
  }
  
  private void generateMaze(Cell startAt) {
      if (startAt == null) return;
      startAt.open = false; 
      ArrayList<Cell> cells = new ArrayList<>();
      cells.add(startAt);

      while (!cells.isEmpty()) {
          Cell cell;
          if (random.nextInt(10)==0)
              cell = cells.remove(random.nextInt(cells.size()));
          else cell = cells.remove(cells.size() - 1);
          ArrayList<Cell> neighbors = new ArrayList<>();
          Cell[] potentialNeighbors = new Cell[]{
              getCell(cell.x + 1, cell.y),
              getCell(cell.x, cell.y + 1),
              getCell(cell.x - 1, cell.y),
              getCell(cell.x, cell.y - 1)
          };
          for (Cell other : potentialNeighbors) {              
              if (other==null || other.wall || !other.open) continue;
              neighbors.add(other);
          }
          if (neighbors.isEmpty()) continue;
          Cell selected = neighbors.get(random.nextInt(neighbors.size()));
          selected.open = false; 
          cell.addNeighbor(selected);
          cells.add(cell);
          cells.add(selected);
      }
  }
  
  public Cell getCell(int x, int y) {
      try {
          return cells[x][y];
      } catch (ArrayIndexOutOfBoundsException e) { 
          return null;
      }
  }

  public void solve() {
      this.solve(0, 0, dimensionX - 1, dimensionY -1);
  }
  
  public void solve(int startX, int startY, int endX, int endY) {
      
      for (Cell[] cellrow : this.cells) {
          for (Cell cell : cellrow) {
              cell.parent = null;
              cell.visited = false;
              cell.inPath = false;
              cell.travelled = 0;
              cell.projectedDist = -1;
          }
      }
      
      ArrayList<Cell> openCells = new ArrayList<>();
      
      Cell endCell = getCell(endX, endY);
      if (endCell == null) return; 
      { 
          Cell start = getCell(startX, startY);
          if (start == null) return; 
          start.projectedDist = getProjectedDistance(start, 0, endCell);
          start.visited = true;
          openCells.add(start);
      }
      boolean solving = true;
      while (solving) {
          if (openCells.isEmpty()) return; 
          Collections.sort(openCells, new Comparator<Cell>(){
              @Override
              public int compare(Cell cell1, Cell cell2) {
                  double diff = cell1.projectedDist - cell2.projectedDist;
                  if (diff > 0) return 1;
                  else if (diff < 0) return -1;
                  else return 0;
              }
          });
          Cell current = openCells.remove(0);
          if (current == endCell) break; 
          for (Cell neighbor : current.neighbors) {
              double projDist = getProjectedDistance(neighbor,
                      current.travelled + 1, endCell);
              if (!neighbor.visited || 
                      projDist < neighbor.projectedDist) { 
                  neighbor.parent = current;
                  neighbor.visited = true;
                  neighbor.projectedDist = projDist;
                  neighbor.travelled = current.travelled + 1;
                  if (!openCells.contains(neighbor))
                      openCells.add(neighbor);
              }
          }
      }

      Cell backtracking = endCell;
      backtracking.inPath = true;
      while (backtracking.parent != null) {
          backtracking = backtracking.parent;
          backtracking.inPath = true;
      }
  }
  
  public double getProjectedDistance(Cell current, double travelled, Cell end) {
      return travelled + Math.abs(current.x - end.x) + 
              Math.abs(current.y - current.x);
  }

  public void updateGrid() {
      char backChar = ' ', wallChar = 'X', cellChar = ' ', pathChar = '*';
      for (int x = 0; x < gridDimensionX; x ++) {
          for (int y = 0; y < gridDimensionY; y ++) {
              grid[x][y] = backChar;
          }
      }
      for (int x = 0; x < gridDimensionX; x ++) {
          for (int y = 0; y < gridDimensionY; y ++) {
              if (x % 4 == 0 || y % 2 == 0)
                  grid[x][y] = wallChar;
          }
      }
      for (int x = 0; x < dimensionX; x++) {
          for (int y = 0; y < dimensionY; y++) {
              Cell current = getCell(x, y);
              int gridX = x * 4 + 2, gridY = y * 2 + 1;
              if (current.inPath) {
                  grid[gridX][gridY] = pathChar;
                  if (current.isCellBelowNeighbor())
                      if (getCell(x, y + 1).inPath) {
                          grid[gridX][gridY + 1] = pathChar;
                          grid[gridX + 1][gridY + 1] = backChar;
                          grid[gridX - 1][gridY + 1] = backChar;
                      } else {
                          grid[gridX][gridY + 1] = cellChar;
                          grid[gridX + 1][gridY + 1] = backChar;
                          grid[gridX - 1][gridY + 1] = backChar;
                      }
                  if (current.isCellRightNeighbor())
                      if (getCell(x + 1, y).inPath) {
                          grid[gridX + 2][gridY] = pathChar;
                          grid[gridX + 1][gridY] = pathChar;
                          grid[gridX + 3][gridY] = pathChar;
                      } else {
                          grid[gridX + 2][gridY] = cellChar;
                          grid[gridX + 1][gridY] = cellChar;
                          grid[gridX + 3][gridY] = cellChar;
                      }
              } else {
                  grid[gridX][gridY] = cellChar;
                  if (current.isCellBelowNeighbor()) {
                      grid[gridX][gridY + 1] = cellChar;
                      grid[gridX + 1][gridY + 1] = backChar;
                      grid[gridX - 1][gridY + 1] = backChar;
                  }
                  if (current.isCellRightNeighbor()) {
                      grid[gridX + 2][gridY] = cellChar;
                      grid[gridX + 1][gridY] = cellChar;
                      grid[gridX + 3][gridY] = cellChar;
                  }
              }
          }
      }
  }

  public void draw() {
      System.out.print(this);
  }
  
  @Override
  public String toString() {
      updateGrid();
      String output = "";
      for (int y = 0; y < gridDimensionY; y++) {
          for (int x = 0; x < gridDimensionX; x+=2) {
              output += grid[x][y];
          }
          output += "\n";
      }
      return output;
  }

 
}
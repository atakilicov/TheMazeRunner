package mazesolverproject;

public class mysolution {

    public static void main(String[] args) {
        Solution();
        
        

    }

    public static void Solution() {
        MazeGenerator maze = new MazeGenerator(5, 5);
        int[][] maze2D = getMaze(maze);
        Stack stack = new Stack<>();
        int x = 1;
        int y = 1;


        //int[][] == 3 is visited cell

        while (!(maze2D[x][y] == 1 && (x == 9 && y == 9))) {
            if (maze2D[x + 1][y] == 0 )
             { // go right
                x++;
                maze2D[x][y] = 2;
                print2dmaze(maze2D);
                stack.push(maze.getCell(x, y));
            } else if (maze2D[x][y + 1] == 0) { // if not right , go down
                y++;
                maze2D[x][y] = 2;
                print2dmaze(maze2D);
                stack.push(maze.getCell(x, y));
            } else if (maze2D[x][y - 1] == 0) { // if not down , go up
                y--;
                maze2D[x][y] = 2;
                print2dmaze(maze2D);
                stack.push(maze.getCell(x, y));
            } else if (maze2D[x - 1][y] == 0) { // if not up, go left
                x--;
                maze2D[x][y] = 2;
                print2dmaze(maze2D);
                stack.push(maze.getCell(x, y));
            } else { 
                maze2D[x][y]=3;
                

               
            }
        }
        
    
        
    
       
        }


      

   

    public static int[][] getMaze(MazeGenerator m) {

        String str = m.toString();

        int[][] maze2D = MazeUtility.Convert2D(str);
        return maze2D;

    }

    public static void print2dmaze(int[][] maze) {
        MazeUtility.plotMaze(maze);

    }

    

} 

package mazesolverproject;



public class MazeSolverProject {

    public static void main(String[] args) {
        int grid = 5;
        int[][] maze = getMaze(grid);
        
        Stack path = new Stack();

        maze[1][1]=2;
        MazeUtility.plotMaze(maze);
        
      


        // To do: starting from the coordinates [1,1], use the path stack to navigate in the maze and 
        // find a way to [2*grid-1, 2*grid-1] coordinates
        // use the following code to print the maze at each step
        // MazeUtility.plotMaze(maze);  
        // DO NOT change any of the given code
    }
    
    public static int[][] getMaze(int grid) {
        MazeGenerator maze = new MazeGenerator(grid);
        String str = maze.toString();
        
        int[][] maze2D = MazeUtility.Convert2D(str);    
        return maze2D;
    }   
}

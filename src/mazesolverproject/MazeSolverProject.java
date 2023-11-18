public class MazeSolverProject {

    public static void main(String[] args) throws InterruptedException {
        int grid = 5;
        int[][] maze = getMaze(grid);
        
        Stack path = new Stack();
        int x = 1;
        int y = 1;
        maze[x][y]=2;
        MazeUtility.plotMaze(maze);

        int oldX;
        int oldY;

        while (maze[2*grid-1][2*grid-1] != 2){
            if(maze[x][y+1]==0){ //First go right
                y++;
                maze[x][y]=2;
                path.push(new int[]{x,y});
            } else if (maze[x+1][y]==0) { //If not right, go down
                x++;
                maze[x][y] = 2;
                path.push(new int[]{x,y});
            } else if (maze[x][y-1]==0) { //If not down, go left
                y--;
                maze[x][y] = 2;
                path.push(new int[]{x,y});
            }
            else if (maze[x-1][y]==0) { //If not left, go up
                x--;
                maze[x][y]=2;
                path.push(new int[]{x,y});
            }
            else {
                if (!path.isEmpty()) { //If no more moves, return to a coordinate with possible moves
                    oldX= x;
                    oldY= y;
                    int[] lastPose = (int[]) path.pop();
                    x=lastPose[0];
                    y=lastPose[1];
                    if ((x-1==1 && y ==1)||(x==1 && y-1==1))
                        maze[1][1]=0;
                    maze[oldX][oldY]=3; // since 1=wall and 2=path, 3 used for old path
                }
            }
            //System.out.println("x= " + x);
            //System.out.println("y= " + y);
            MazeUtility.plotMaze(maze);
            System.out.println();
            Thread.sleep(500); //for ease of follow 0.5 sec delay.
        }


        // To do: starting from the coordinates [1,1], use the path stack to navigate in the maze and 
        // find a way to [2*grid-1, 2*grid-1] coordinates
        // use the following code to print the maze at each step
        // MazeUtility.plotMaze(maze);  
        // DO NOT change any of the given code
    }
    
    public static int[][] getMaze(int grid) {
        MazeGenerator maze = new MazeGenerator(grid);
        String str = maze.toString();

        //int[][] maze2D = MazeUtility.Convert2D(str); //
        //return maze2D;
        return MazeUtility.Convert2D(str);
    }   
}

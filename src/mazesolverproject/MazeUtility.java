public class MazeUtility {
    
    public static int[][] Convert2D(String str) {
                int i=0, j=0;
                for (i=0;str.charAt(i)!='\n';i++) {}                    
                int[][] maze2D = new int[i+1][i+1];
                i=0;
        for (int c = 0; c<str.length(); c++) {
            if (str.charAt(c)=='X') {
                maze2D[i][j]=1;
                j++;
            }
            else if (str.charAt(c)=='O') {
                maze2D[i][j]=2;
                j++;
            }
            else if (str.charAt(c)==' ') {
                j++;
            }
            else if (str.charAt(c)=='\n') {
                i++; j=0;
            }                    
        }
        return maze2D;

    }
    
    public static void plotMaze(int[][] maze) {
        for (int i=0;i<maze.length;i++) {
            for (int j=0;j<maze[0].length;j++)
                if (maze[i][j]==1)
                    System.out.print('X');
                else if (maze[i][j]==2)
                    System.out.print('O');
                else
                    System.out.print(' ');
            System.out.println();
        }        
    }
}

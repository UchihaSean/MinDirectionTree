import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Jly_wave on 5/13/16.
 */
public class Test {
    public static void main(String[]args) throws FileNotFoundException {
        //read file
        Scanner input=new Scanner(new File("read.txt"));
        int n=input.nextInt();
        int m=input.nextInt();
//        int root=input.nextInt();
        int root=1;
        double [][] edges=new double[n+1][n+1];
        double [][] newEdges=new double[n+1][n+1];
        for (int i=1;i<n+1;i++)
            for (int j=1;j<n+1;j++)
                edges[i][j]=-1;
        for (int i=1;i<m+1;i++){
            int x=input.nextInt();
            int y=input.nextInt();
            double z=input.nextDouble();
            if ((z<edges[x][y])||(edges[x][y]==-1))
                edges[x][y]=z;
        }
        //test
        Graph graph=new Graph(root,n,edges);
        newEdges=graph.getMinGraph();
    }
}

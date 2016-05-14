import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Jly_wave on 5/12/16.
 */
public class MakeFile {
    public static void main(String[]args) throws FileNotFoundException {
        PrintWriter output=new PrintWriter("read.txt");
        PrintWriter outputx=new PrintWriter("readx.txt");
        int n=10;
        int m=20;
        output.println(n+" "+m);
        outputx.println(n+" "+m);

        for (int i=1;i<n;i++)
            output.println(i+" "+(i+1)+" "+(n+1));
        for (int i=1;i<m+1;i++){
            int x=(int)(Math.random()*n)+1;
            int y=(int)(Math.random()*n)+1;
            int z=(int)(Math.random()*n)+1;
            output.println(x+" "+y+" "+z);
//            output.println(y+" "+x);
            outputx.println(x+" "+y);
        }
        output.close();
        outputx.close();
    }


}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Jly_wave on 5/12/16.
 */
public class Kluskal {
    static int MAXINT=999999999;
    public static void main(String[]args) throws FileNotFoundException {
        Scanner input=new Scanner(new File("readx.txt"));
        while (input.hasNext()) {
            int n = 0, m = 0;
            int x[], y[], preMinNode[];
            double edges[][];

            //Mark
//            Test++;
//            System.out.println("TEST "+Test+" :");
            //prepare
            n = input.nextInt();
            m = input.nextInt();
            x = new int[n + 1];
            y = new int[n + 1];
            int[] u=new int[m+1];
            int[] v=new int[m+1];
            double[] w=new double[m+1];
            int num=0;
            edges = new double[n + 1][n + 1];
            preMinNode = new int[n + 1];
            for (int i = 1; i < n + 1; i++)
                for (int j = 1; j < n + 1; j++) {
                    edges[i][j] = MAXINT;
                }
            for (int i = 1; i < n + 1; i++) {
                x[i] = input.nextInt();
                y[i] = input.nextInt();
                preMinNode[i] = i;
            }
            for (int i = 1; i < m + 1; i++) {
                int a = input.nextInt();
                int b = input.nextInt();
                //prevent self-loop && edges into root
                if (a != b) {
                    edges[a][b] = distance(x, y, a, b);
                }
                num++;
                u[num]=a;
                v[num]=b;
                w[num]=edges[a][b];
            }
            quickSort(u,v,w,1,m);
//            for (int i=1;i<n+1;i++){
//                System.out.println(w[i]);
//            }
            int[] fa=new int[m+1];
            for (int i=1;i<m+1;i++){
                fa[i]=i;
            }
            double ans=0;
            for (int i=1;i<m+1;i++){
                int uu=getFather(fa,u[i]);
                int vv=getFather(fa,v[i]);
                if (uu!=vv){
                    fa[fa[uu]]=fa[vv];
                    System.out.println(u[i]+" "+v[i]);
                    ans+=w[i];
                }
            }
            ans=(double)Math.round(ans*100)/100;
            System.out.println(ans);
        }
    }
    public static double distance(int[] x,int[] y,int u,int v){
        return Math.sqrt((x[u]-x[v])*(x[u]-x[v])+(y[u]-y[v])*(y[u]-y[v]));
    }
    public static void quickSort(int[] u,int[] v,double[] w,int l,int r){
        if (l<r){
            int q=partition(u,v,w,l,r);
            quickSort(u,v,w,l,q-1);
            quickSort(u,v,w,q+1,r);
        }
    }
    public static int partition(int[] u,int[] v,double[] w,int l,int r){
        int q=r;
        int t=l-1;
        double s;
        int ss,sss;
        for (int i=l;i<r;i++){
            if (w[i]<=w[q]){
                t++;
                s=w[t];
                w[t]=w[i];
                w[i]=s;
                ss=u[t];
                u[t]=u[i];
                u[i]=ss;
                sss=v[t];
                v[t]=v[i];
                v[i]=sss;
            }
        }
        t++;
        s=w[t];
        w[t]=w[q];
        w[q]=s;
        ss=u[t];
        u[t]=u[q];
        u[q]=ss;
        sss=v[t];
        v[t]=v[q];
        v[q]=sss;
        return t;

    }
    public static int getFather(int[] fa,int i){
        if (fa[i]==i) return i;
        fa[i]=getFather(fa,fa[i]);
        return fa[i];
    }
}

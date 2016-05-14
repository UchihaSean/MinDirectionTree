
import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.Scanner;

public class Main {
    static int root=1;
    static int MAXINT=99999999;
    public static void main(String[] args) throws FileNotFoundException {
        //read file
        Scanner input=new Scanner(new File("read.txt"));
        int n=0,m=0;
        int x[],y[],preMinNode[][];
        double edges[][],origEdges[][];
        int origLeftEdges[][],origRightEdges[][];
        boolean reserEdges[][];
        int Test=0;
        //read until End of File
        while (input.hasNext()){
            //Mark
            Test++;
//            System.out.println("TEST "+Test+" :");
            //prepare
            n=input.nextInt();
            m=input.nextInt();
            x=new int[n+1];
            y=new int[n+1];
            edges=new double[n+1][n+1];
            origEdges=new double[n+1][n+1];
            origLeftEdges=new int[n+1][n+1];
            origRightEdges=new int[n+1][n+1];
            reserEdges=new boolean[n+1][n+1];
            preMinNode=new int[n+1][n+1];
            for (int i=1;i<n+1;i++)
                for (int j=1;j<n+1;j++){
                    edges[i][j]=MAXINT;
                    origLeftEdges[i][j]=i;
                    origRightEdges[i][j]=j;
                    reserEdges[i][j]=false;
                }
            for (int i=1;i<n+1;i++){
                x[i]=input.nextInt();
                y[i]=input.nextInt();
            }
            for (int i=1;i<m+1;i++){
                int u=input.nextInt();
                int v=input.nextInt();
                //prevent self-loop && edges into root
                if ((u!=v)&&(v!=root)){
                    edges[u][v]=distance(x,y,u,v);
                    origEdges[u][v]=edges[u][v];
                }
            }
            //check graph connection
            if (!graphConnected(n,edges)) {
                System.out.println("poor snoopy");
                continue;
            }
//            System.out.println("Graph Connected");

            //loop group
//            int fa[]=new int[n+1];
            boolean inLoop[]=new boolean[n+1];
            boolean inMerge[]=new boolean[n+1];
            for (int i=1;i<n+1;i++){
                inMerge[i]=false;
            }

            //MinTree result
            double ans=0;
            int loopCount=0;
            //find loops
            while (true){
                loopCount++;
                //find Min preNode
                for (int i=1;i<n+1;i++){
                    if ((i==root)|| inMerge[i]) continue;
                    edges[i][i]=MAXINT;
                    preMinNode[loopCount][i]=i;
                    for (int j=1;j<n+1;j++){
                        if (inMerge[j]) continue;
                        if (edges[j][i]<edges[preMinNode[loopCount][i]][i]){
                            preMinNode[loopCount][i]=j;
                        }
                    }
                }
                //
//                System.out.println("preMinnode:");
//                for (int i=1;i<n+1;i++){
//                    if ((i==root)|| inMerge[i]) continue;
//                    System.out.println(i+" "+preMinNode[i]);
//                }
                // check loops prepare
                for (int i=1;i<n+1;i++){
//                    fa[i]=i;
                    inLoop[i]=false;
                }

                //test
//                for (int i=1;i<n+1;i++){
//                    System.out.println(i+" "+preMinNode[i]);
//                }

                //check loops exist
                boolean hasLoop=false;
                int loopNode=root;
                for (int i=1;i<n+1;i++){
                    if ((inMerge[i])||(i==root)) continue;
                    int j=i;
                    int count=0;
                    while (true){
                        count++;
                        j=preMinNode[loopCount][j];
                        if (j==i) {
                            hasLoop=true;
                            break;
                        }
                        if (count>n){
                            hasLoop=false;
                            break;
                        }
                    }
                    if (hasLoop) {
                        loopNode = i;
                        break;
                    }
                }


                preMinNode[loopCount][0]=loopNode;
                //no loop
                if (!hasLoop){
//                    System.out.println("no loop:");
                    for (int i=1;i<n+1;i++){
                        if ((i==root)|| (inMerge[i])) continue;
                        ans+=edges[preMinNode[loopCount][i]][i];
                        int left = origLeftEdges[preMinNode[loopCount][i]][i];
                        int right = origRightEdges[preMinNode[loopCount][i]][i];
                        reserEdges[left][right] = true;
//                        System.out.println(left + " " + right);
                        int count=0;
                        for (int j=1;j<loopCount;j++){
                            if (preMinNode[j][0]==i){
                                count++;
                                if (count==1){
                                    left=preMinNode[j][right];
                                    int origLeft=origLeftEdges[left][right];
                                    int origRight=origRightEdges[left][right];
                                    reserEdges[origLeft][origRight]=false;
                                } else{
                                    left=preMinNode[j][i];
                                    right=i;
                                    int origLeft=origLeftEdges[left][right];
                                    int origRight=origRightEdges[left][right];
                                    reserEdges[origLeft][origRight]=false;
                                }
                            }
                        }
                    }
                    break;
                }
                //has loops && merge in
                int t=loopNode;
//                System.out.println("loop: ");
                while (true){
//                    System.out.print(t + " ");
                    inMerge[t]=true;
                    inLoop[t]=true;
                    ans+=edges[preMinNode[loopCount][t]][t];
                    int left = origLeftEdges[preMinNode[loopCount][t]][t];
                    int right = origRightEdges[preMinNode[loopCount][t]][t];
                    reserEdges[left][right] = true;
                    t=preMinNode[loopCount][t];
                    if (t==loopNode) break;
                }
                inMerge[loopNode]=false;
//                System.out.println();
//                System.out.println("after merge");
//                for (int i=1;i<n+1;i++)
//                    for (int j=1;j<n+1;j++){
//                        if (reserEdges[i][j])
//                            System.out.println(i+" "+j);
//                    }

                //renew edges
                int lastLeftNode=-1,lastRightNode=-1;
//                System.out.println("renew edges : loopNode "+loopNode);
                for (int i=1;i<n+1;i++)if (inLoop[i]){
                    for (int j=1;j<n+1;j++)if (!inLoop[j]){
                        if (edges[i][j]<edges[loopNode][j]){
                            edges[loopNode][j]=edges[i][j];
                            origLeftEdges[loopNode][j] = origLeftEdges[i][j];
                            origRightEdges[loopNode][j] = origRightEdges[i][j];
                        }
//                        if (i==loopNode) continue;
                        if ((edges[j][i]<MAXINT)&& (edges[j][i]-edges[preMinNode[loopCount][i]][i]<edges[j][loopNode])){
                            edges[j][loopNode]=edges[j][i]-edges[preMinNode[loopCount][i]][i];
//                            int left,right;
//                            if (lastLeftNode!=-1) {
//                                reserEdges[lastLeftNode][lastRightNode] = true;
//                                System.out.print("edges " + j + " " + i + " resever " + lastLeftNode + " " + lastRightNode);
//                            }

                            origLeftEdges[j][loopNode] = origLeftEdges[j][i];
                            origRightEdges[j][loopNode] = origRightEdges[j][i];
//                            left = origLeftEdges[preMinNode[loopCount][i]][i];
//                            right = origRightEdges[preMinNode[loopCount][i]][i];
//                            System.out.print(" delete "+left+" "+right);
//                            reserEdges[left][right] = false;
//                            lastLeftNode=left;
//                            lastRightNode=right;
//                            System.out.println(" newedges="+edges[j][loopNode]);

                        }
                    }
                }
//                System.out.println("after renew edges");
//                for (int i=1;i<n+1;i++)
//                    for (int j=1;j<n+1;j++){
//                        if (reserEdges[i][j])
//                            System.out.println(i+" "+j);
//                    }

            }

            ans=(double)Math.round(ans*100)/100;
//            System.out.println(ans) ;
            double ansx=0;
            for (int i=1;i<n+1;i++)
                for (int j=1;j<n+1;j++){
                    if (j==root) continue;
                    if (reserEdges[i][j]){
                        ansx+=origEdges[i][j];
                        System.out.println(i + " " + j);
                    }
                }
            System.out.println(ans) ;
            System.out.println(ansx);
        }
        input.close();
    }

    //check graph connection
    public static boolean graphConnected(int n,double[][] edges){
        //check vertex arrived
        boolean flag[]=new boolean[n+1];
        for (int i=1;i<n+1;i++){
            flag[i]=false;
        }
        flag[root]=true;

        //bfs
        int l=0,r=1;
        int q[]=new int[n+1];
        q[r]=root;
        while (l<r){
            l++;
            int node=q[l];
            for (int i=1;i<n+1;i++){
                if ((!flag[i])&&(edges[node][i]<MAXINT)){
                    flag[i]=true;
                    r++;
                    q[r]=i;
                }
            }
        }
        for (int i=1;i<n+1;i++){
            if (!flag[i]) return false;
        }
        return true;
    }
    public static double distance(int[] x,int[] y,int u,int v){
        return Math.sqrt((x[u]-x[v])*(x[u]-x[v])+(y[u]-y[v])*(y[u]-y[v]));
    }
    public static int getFather(int[] fa,int i){
        if (fa[i]==i) return i;
        fa[i]=getFather(fa,fa[i]);
        return fa[i];
    }
}

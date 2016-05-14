import org.omg.Messaging.SYNC_WITH_TRANSPORT;

/**
 * Created by Jly_wave on 5/12/16.
 */
public class Graph {
    private  int root;
    private  int n;
    private  double[][] edges,origEdges;
    private int[][] origLeftEdges,origRightEdges,origPoint,loops;
    private boolean[][] reserEdges;
    private int MAXINT=99999999;
    public Graph(int root,int n,double[][] edges){
        this.setRoot(root);
        this.setN(n);
        this.setEdges(edges);
    }
    private void setRoot(int root){
        this.root=root;
    }
    private void setN(int n){
        this.n=n;
    }
    private void setEdges(double[][] edges){
        this.origEdges=new double[n+1][n+1];
        this.edges=new double[n+1][n+1];
        this.reserEdges=new boolean[n+1][n+1];
        this.origLeftEdges=new int[n+1][n+1];
        this.origRightEdges=new int[n+1][n+1];
        this.origPoint=new int[n+1][n+1];
        this.loops=new int[n+1][n+1];
        for (int i=1;i<n+1;i++)
            for (int j=1;j<n+1;j++){
                if (edges[i][j]==-1){
                    edges[i][j]=MAXINT;
                }
                if (i==j) edges[i][j]=MAXINT;
                this.edges[i][j]=edges[i][j];
                this.origEdges[i][j]=edges[i][j];
                this.reserEdges[i][j]=false;
                this.origLeftEdges[i][j]=i;
                this.origRightEdges[i][j]=j;
            }
    }

    public double[][] getMinGraph(){
        int[][] preMinNode=new int[n+1][n+1];

        //check graph connection
        if (!graphConnected(n,edges)) {
            System.out.println("Connection Error");
            return edges;
        }
        boolean inLoop[]=new boolean[n+1];
        boolean inMerge[]=new boolean[n+1];
        for (int i=1;i<n+1;i++){
            inMerge[i]=false;
        }

        //MinTree result
        double ans=0;
        int loopCount=0;
        //find loops
        while (true) {
            loopCount++;
            //find Min preNode
            for (int i = 1; i < n + 1; i++) {
                if ((i == root) || inMerge[i]) continue;
                edges[i][i] = MAXINT;
                preMinNode[loopCount][i] = i;
                for (int j = 1; j < n + 1; j++) {
                    if (inMerge[j]) continue;
                    if (edges[j][i] < edges[preMinNode[loopCount][i]][i]) {
                        preMinNode[loopCount][i] = j;
                    }
                }
            }
            // check loops prepare
            for (int i = 1; i < n + 1; i++) {
                inLoop[i] = false;
            }

            //check loops exist
            boolean hasLoop = false;
            int loopNode = root;
            for (int i = 1; i < n + 1; i++) {
                if ((inMerge[i]) || (i == root)) continue;
                int j = i;
                int count = 0;
                while (true) {
                    count++;
                    j = preMinNode[loopCount][j];
                    if (j == i) {
                        hasLoop = true;
                        break;
                    }
                    if (count > n) {
                        hasLoop = false;
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
            if (!hasLoop) {

                //test end reserve edges
//                System.out.println("no loop reserve Edges");
//                for (int i=1;i<n+1;i++)
//                    for (int j=1;j<n+1;j++){
//                        if (reserEdges[i][j]){
//                            //test
//                            System.out.println(i + " " + j);
//                            //
//                        }
//                    }
//                System.out.print("loopNode");
//                for (int i=1;i<loopCount;i++)
//                    System.out.print(" "+preMinNode[i][0]);
//                System.out.println();
                //

                //

                for (int i = 1; i < n + 1; i++) {
                    if ((i == root) || (inMerge[i])) continue;
                    ans += edges[preMinNode[loopCount][i]][i];
                    int left = origLeftEdges[preMinNode[loopCount][i]][i];
                    int right = origRightEdges[preMinNode[loopCount][i]][i];
//                    if (left==right){
//                        System.out.println("left==right "+left);
//                    }
                    reserEdges[left][right] = true;
//                    //test change
//                    System.out.println("true "+left+" "+right);
//                    //

                }

                for (int i=loopCount-1;i>0;i--){
//                    System.out.println("loopCount "+i);
                    for (int k=1;k<loops[i][0]+1;k++){
                        inMerge[loops[i][k]]=false;
                        }
                    int node=preMinNode[i][0];
                    int key=0;
                    for (int j=1;j<n+1;j++){
                        if (inMerge[j]) continue;
                        int left=origLeftEdges[j][node];
                        int right=origRightEdges[j][node];
//                        if (i==1){
//                            left=j;
//                            right=node;
//                        }
//                        System.out.println(left + " " + right + " " +"xx");
                        boolean flag=false;
                        for (int k=1;k<loops[i][0]+1;k++) {
                            if (right == loops[i][k]) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            for (int t = i - 1; t > 0; t--) {
                                boolean flagx=false;
                                for (int k=1;k<loops[i][0]+1;k++) {
                                    if (loops[t][1] == loops[i][k]) {
                                        flagx = true;
                                        break;
                                    }
                                }
                                if (flagx){
                                    for (int k=1;k<loops[t][0]+1;k++) {
                                        if (right == loops[t][k]) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
                                if (flag) break;
                            }
                        }

                        if (!flag) continue;
//                            System.out.println(left + " " + right + " " + reserEdges[left][right]);
                            if ((reserEdges[left][right]) && (preMinNode[i][node] != j)) {
                                key = j;
                                break;
                            }


                    }

//                    System.out.println(node+" "+key+" ");
                    int right=origPoint[i][key];
                    int left=preMinNode[i][right];
                    int origLeft=origLeftEdges[left][right];
                    int origRight=origRightEdges[left][right];
                    reserEdges[origLeft][origRight]=false;
                    //test change
//                    System.out.println("false "+origLeft+" "+origRight);
                    //
                }

                break;
            }
            //has loops && merge in
            int t = loopNode;

            //test loop
//            System.out.println("test loop:");
            //
            while (true) {
                inMerge[t] = true;
                inLoop[t] = true;
                loops[loopCount][0]++;
                loops[loopCount][loops[loopCount][0]]=t;
                ans += edges[preMinNode[loopCount][t]][t];
                int left = origLeftEdges[preMinNode[loopCount][t]][t];
                int right = origRightEdges[preMinNode[loopCount][t]][t];
                reserEdges[left][right] = true;
                //test
//                System.out.print(t+" ");
                //
                t = preMinNode[loopCount][t];
                if (t == loopNode) break;
            }
            inMerge[loopNode] = false;

            //test lopp end
//            System.out.println();
            //

            //renew edges
            for (int i = 1; i < n + 1; i++)
                if (inLoop[i]) {
                    for (int j = 1; j < n + 1; j++)
                        if (!inLoop[j]) {
                            if (inMerge[j]) continue;
                            if (edges[i][j] < edges[loopNode][j]) {
                                edges[loopNode][j] = edges[i][j];
                                origLeftEdges[loopNode][j] = origLeftEdges[i][j];
                                origRightEdges[loopNode][j] = origRightEdges[i][j];
                            }
                            if ((edges[j][i] < MAXINT) && (edges[j][i] - edges[preMinNode[loopCount][i]][i] <= edges[j][loopNode])) {
                                edges[j][loopNode] = edges[j][i] - edges[preMinNode[loopCount][i]][i];
                                origPoint[loopCount][j]=i;
                                origLeftEdges[j][loopNode] = origLeftEdges[j][i];
                                origRightEdges[j][loopNode] = origRightEdges[j][i];
                            }
                        }
                }
        }
//        ans=(double)Math.round(ans*100)/100;
        //test reser ans
//        System.out.println("test reser ans");
        //
        bfs();
        double ansx=0;
        for (int i=1;i<n+1;i++)
            for (int j=1;j<n+1;j++){
                if (reserEdges[i][j]){
                    ansx+=origEdges[i][j];
                    //test
//                    System.out.println(i + " " + j);
                    //
                }
            }

        for (int i=1;i<n+1;i++)
            for (int j=1;j<n+1;j++)
                if (!reserEdges[i][j]){
                    origEdges[i][j]=-1;
                }
//        System.out.println(ans) ;
        System.out.println(ansx);
        return origEdges;
    }

    private void bfs(){
        int l=0,r=1;
        int[] q=new int[n*n];
        boolean[] flag=new boolean[n+1];
        for (int i=1;i<n+1;i++)
            flag[i]=false;
        q[1]=root;
        flag[root]=true;
        while (l<r){
            l++;
            int t=q[l];
            for (int i=1;i<n+1;i++){
                if (reserEdges[t][i]){
                    if (flag[i]) reserEdges[t][i]=false; else{
                        r++;
                        q[r]=i;
                        flag[i]=true;
                    }
                }
            }
        }

    }

    private boolean graphConnected(int n,double[][] edges){
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



}

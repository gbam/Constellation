package stars;

public class UnionFind {

  public static Node find(Node n){
    if(n.parent == null){
      return n;
    }
    else{
      Node parent = find(n.parent);
      n.parent = parent;
      return parent;
    }
  }
  public static boolean union(Node a, Node b){
    Node aRoot = find(a);
    Node bRoot = find(b);

    if(aRoot == bRoot){
      return false;
    }
    if(aRoot.rank > bRoot.rank){
    	bRoot.parent = aRoot;
    }
    else if(aRoot.rank < bRoot.rank){
    	aRoot.parent = bRoot;

    }
    else{
    	bRoot.parent = aRoot;
      aRoot.rank = aRoot.rank +1;
    }
    return true;

  }



}

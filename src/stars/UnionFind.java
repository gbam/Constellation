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

  public static Star find(Star s){
    if(s.parentStar == null){
      return s;
    }
    else{
      Star parent = find(s.parentStar);
      s.parentStar = parent;
      return parent;
    }
  }
  public static boolean union(Star a, Star b){
    Star aRoot = find(a);
    Star bRoot = find(b);

    if(aRoot == bRoot){
      return false;
    }
    if(aRoot.rank > bRoot.rank){
     bRoot.parentStar = aRoot;
    }
    else if(aRoot.rank < bRoot.rank){
     aRoot.parentStar = bRoot;

    }
    else{
     bRoot.parentStar = aRoot;
      aRoot.rank = aRoot.rank +1;
    }
    return true;

  }


}

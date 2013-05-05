package stars;

public class UnionFindConstellation {

  public static Constellation find(Constellation n){
    if(n.parent == null){
      return n;
    }
    else{
      Constellation parent = find(n.parent);
      n.parent = parent;
      return parent;
    }
  }
  public static boolean union(Constellation a, Constellation b){
    Constellation aRoot = find(a);
    Constellation bRoot = find(b);

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

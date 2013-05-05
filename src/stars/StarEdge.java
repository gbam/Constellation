package stars;

public class StarEdge {
  public Star starA, starB;
  public int weight;

  public StarEdge(Star starA, Star starB, int weight){
    this.weight = weight;
    this.starA = starA;
    this.starB = starB;
  }
}

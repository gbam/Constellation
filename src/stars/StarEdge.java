package stars;

public class StarEdge {
  public Star cA, cB;
  Double weight;

  public StarEdge(Star cA, Star cB, Double closestValue){
    this.cA = cA;
    this.cB = cB;
    this.weight = closestValue;
  }
}

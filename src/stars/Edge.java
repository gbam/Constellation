package stars;

public class Edge {
  public Node nodeA, nodeB;
  public int weight;

  public Edge(Node nodeA, Node nodeB, int weight){
    this.weight = weight;
    this.nodeA = nodeA;
    this.nodeB = nodeB;
  }


}

package stars;

public class Node {
  int x, y, intensity, rank;
  Node parent; 

  public Node(int x, int y, int rgb){
    int red = (rgb >> 16) & 0xff;
    int green = (rgb >> 8) & 0xff;
    int blue = (rgb) & 0xff;
    this.intensity = Math.max(red, Math.max(blue,green));
    this.x = x;
    this.y = y;
    this.parent = null;
    rank = 0;
  }




}

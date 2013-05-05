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

  public long getCoordinate() {
    return makeCoordinate(x, y);
  }

  /**
   * Combines the x, y ints into 1 long with x being first 16 bits, y the second
   * @return
   */
  public static long makeCoordinate(int x, int y) {
    return (((long) x) << 16) + ((long) y);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Node)) {
      return false;
    }
    Node other = (Node) obj;
    if (x != other.x) {
      return false;
    }
    if (y != other.y) {
      return false;
    }
    return true;
  }
}

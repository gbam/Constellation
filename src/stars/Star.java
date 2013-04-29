package stars;

public class Star {
  public double centerX;
  public double centerY;
  public double radiusX; // ellipse so need to radii
  public double radiusY;
  
  private int maxX = Integer.MIN_VALUE;
  private int minX = Integer.MAX_VALUE;
  private int maxY = Integer.MIN_VALUE;
  private int minY = Integer.MAX_VALUE;
  
  public Node parent = null; // parent of tree of all nodes in star
  public int numNodes = 0; // number of nodes in star
  
  public Star(Node n) {
    centerX = -1;
    centerY = -1;
    radiusX = 0;
    radiusY = 0;
    parent = n;
  }
  
  public void addPixel(Node n) {
    calculateCenter(n);
    
    maxX = Math.max(maxX, n.x);
    minX = Math.min(minX, n.x);
    maxY = Math.max(maxY, n.y);
    minY = Math.min(minY, n.y);
    
    calculateRadii();
    numNodes++;
  }
  
  private void calculateCenter(Node n) {
    centerX = ((centerX * numNodes) + n.x) / (numNodes + 1);
    centerY = ((centerY * numNodes) + n.y) / (numNodes + 1);
  }
  
  private void calculateRadii() {
    if (maxX == Integer.MIN_VALUE || minX == Integer.MAX_VALUE || maxY == Integer.MIN_VALUE || minY == Integer.MAX_VALUE) {
      // no points added to calculate radii
      return;
    }
    
    radiusX = maxX - minX;
    radiusY = maxY - minY;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((parent == null) ? 0 : parent.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   * 
   * If parents are equal then stars are equal, only 1 star per parent ndoe
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Star)) {
      return false;
    }
    Star other = (Star) obj;
    if (parent == null) {
      if (other.parent != null) {
        return false;
      }
    }
    else if (!parent.equals(other.parent)) {
      return false;
    }
    return true;
  }
}

package tests;

import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import javax.imageio.ImageIO;
import org.junit.Test;
import stars.Edge;
import stars.Node;
import stars.Star;
import stars.StarSpace;
import stars.Stars;

public class StarSpaceTest {

  @Test
  public void testGetNeighbors() {
    int NEIGHBOR_DIST = 150;
    BufferedImage starImg = Stars.readImage("stars.bmp");
    
    Map<Long, Node> nodes = new HashMap<Long, Node>();
    PriorityQueue<Edge> edges = new PriorityQueue<Edge>(starImg.getWidth() * starImg.getHeight(), new Comparator<Edge>() {
      public int compare(Edge e1, Edge e2) {
        return e1.weight - e2.weight;
      };
    });
    
    Stars.buildPixelEdges(starImg, nodes, edges);
    
    Set<Node> starNodes = new HashSet<Node>();
    List<Star> stars = new ArrayList<Star>();
    Stars.findStars(nodes, edges, starNodes, stars);
    
    StarSpace sp = new StarSpace(starImg.getWidth(), starImg.getHeight(), NEIGHBOR_DIST);
    
    for (Star s : stars) {
      // only add pixels with diameter 4+
      if (Math.max(s.getRadiusX(), s.getRadiusY()) > 3)
        sp.add(s);
    }
    
    int random;
    Star root;
    while (true) {
      random = (int)(Math.random() * stars.size());
      root = stars.get(random);
      if (Math.max(root.getRadiusX(), root.getRadiusY()) > 3)
        break;
    }
    List<Star> neighbors = sp.getNeighbors(root, NEIGHBOR_DIST);
    
    Graphics2D g2d = starImg.createGraphics();
    // draw yellow circle showing where neighbors should be located
    g2d.setColor(Color.YELLOW);
    g2d.draw(new Ellipse2D.Double(root.getCenterX() - NEIGHBOR_DIST, root.getCenterY() - NEIGHBOR_DIST, NEIGHBOR_DIST * 2, NEIGHBOR_DIST * 2));
    
    g2d.setColor(Color.RED);
    for (Star s : neighbors) {
      g2d.drawLine(root.getCenterX(), root.getCenterY(), s.getCenterX(), s.getCenterY());
    }
    
    Stars.writeImage(starImg, "starSpaceTest.bmp");
    
    
  }

}

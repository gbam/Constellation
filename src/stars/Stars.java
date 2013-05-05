package stars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Stars {

  private static int minIntensity = 64;
  private static String inputFileName = "stars.bmp";
  // right now these are very uneducated guesses...
  public static final int INTENSITY_THRESHOLD = 64;
  public static final int GRADIENT_THRESHOLD = 128;
  private static final int MAX_DISTANCE = 150;

  public static void main(String[] args) {
    //Variables
    int width, height, pixelTotal;
    BufferedImage constellationImg;
    BufferedImage starPixelImg;

    ////////Reading Input Files//////// 
    BufferedImage img = readImage(inputFileName);
    
    //Creating Constellations Map
    constellationImg = copyImage(img);
    starPixelImg = copyImage(img);

    //Gather basic file information
    width = img.getWidth();
    height = img.getHeight();
    pixelTotal = width * height;

    Map<Long, Node> nodes = new HashMap<Long, Node>();
    PriorityQueue<Edge> edges = new PriorityQueue<Edge>(width * height, new Comparator<Edge>() {
      public int compare(Edge e1, Edge e2) {
        return e1.weight - e2.weight;
      };
    });
    //Iterate through buffered image
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {				
        //The first node is created on its current iteration otherwise all nodes are created in the node above them.
        Node n = null;

        //Create the first node
        if(x == 0 && y == 0){
          n = new Node(x, y, img.getRGB(x, y));
          //Hashmap (16 bits: x, 16 bits: y)
          nodes.put(n.getCoordinate(), n);

          Node right = new Node(x+1, y, img.getRGB(x+1, y));
          nodes.put(right.getCoordinate(), right);
          Edge e = new Edge(n, right, n.intensity + right.intensity);
          if (edgeNeeded(e))
            edges.add(e);

          Node down = new Node(x, y+1, img.getRGB(x, y+1));
          nodes.put(down.getCoordinate(), down);
          e = new Edge(n, right, n.intensity + down.intensity);
          if (edgeNeeded(e))
            edges.add(e);

        }
        //Checks the first row
        else if(y==0){
          //This will be used to make the edge nodes
          n = nodes.get(Node.makeCoordinate(x, y));

          //Check if we have anything on the right
          if(x+1 != width){
            //This could be moved in the previous if block
            //The first row will not have been created vs all other rows would have been.
            Node down = new Node(x, y+1, img.getRGB(x, y+1));
            nodes.put(down.getCoordinate(), down);
            Edge e = new Edge(n, down, n.intensity + down.intensity);
            if (edgeNeeded(e))
              edges.add(e);

            Node right = new Node(x+1, y, img.getRGB(x+1, y));
            nodes.put(right.getCoordinate(), right);
            e = new Edge(n, right, n.intensity + right.intensity);
            if (edgeNeeded(e))
              edges.add(e);
          }
          //If we are at the edge, don't add the right edge, duh.
          else{
            Node down = new Node(x, y+1, img.getRGB(x, y+1));
            nodes.put(down.getCoordinate(), down);
            Edge e = new Edge(n, down, n.intensity + down.intensity);
            if (edgeNeeded(e))
              edges.add(e);
          }
        }
        //Covers the bottom row
        else if(y + 1 == height){
          if(x+1 != width){
            n  = nodes.get(Node.makeCoordinate(x, y));
            Node right = new Node(x+1, y, img.getRGB(x+1, y));
            nodes.put(right.getCoordinate(), right);
            Edge e = new Edge(n, right, n.intensity + right.intensity);
            if (edgeNeeded(e))
              edges.add(e);
          }
        }
        //Covers the right most column
        else if(x + 1 == width){
          n  = nodes.get(Node.makeCoordinate(x, y));
          Node down = new Node(x, y+1, img.getRGB(x, y+1));
          nodes.put(down.getCoordinate(), down);
          Edge e = new Edge(n, down, n.intensity + down.intensity);
          if (edgeNeeded(e))
            edges.add(e);

        }
        //Creates all the non first, bottom, or right nodes and relationships
        else{
          n  = nodes.get(Node.makeCoordinate(x, y));

          Node right = new Node(x+1, y, img.getRGB(x+1, y));
          nodes.put(right.getCoordinate(), right);
          Edge e = new Edge(n, right, n.intensity + right.intensity);
          if (edgeNeeded(e))
            edges.add(e);


          Node down = new Node(x, y+1, img.getRGB(x, y+1));
          nodes.put(down.getCoordinate(), down);
          e = new Edge(n, down, n.intensity + down.intensity);
          if (edgeNeeded(e))
            edges.add(e);
        }
        System.out.println("Finished (" + x + ", " + y + ")");
      }
    }

    HashSet<Node> starNodes = new HashSet<Node>();
    // Kruskal's algorithm
    while (!edges.isEmpty()) {
    	Edge e = edges.poll();
      Node nodeA = nodes.get(Node.makeCoordinate(e.nodeA.x, e.nodeA.y));
      Node nodeB = nodes.get(Node.makeCoordinate(e.nodeB.x, e.nodeB.y));
      starNodes.add(nodeA);
      starNodes.add(nodeB);
      System.out.println("Edge of Node A: " + nodeA.x + ", " + nodeA.y + "--->" + nodeB.x + ", " + nodeB.y);
      if (UnionFind.find(nodeA) != UnionFind.find(nodeB)) {
      	UnionFind.union(nodeA, nodeB);
      }
    }
    
    HashSet<Node> parents = new HashSet<Node>();
    for (Node n : starNodes) {
    	if(!(parents.contains(UnionFind.find(n)))){
      parents.add(UnionFind.find(n));
    	}
    }
    System.out.println("Number of parents after union find: " + parents.size());

    List<Star> stars = new ArrayList<Star>();
    // Go through all star nodes and create list of Stars (ie cluster of nodes)
    for (Node n : starNodes) {
      Star s = new Star(n);
      if (stars.contains(s)){
        int index = stars.indexOf(s);
        s = stars.get(index);
        s.addPixel(n);
      }
      else{
        s = new Star(n);
        s.addPixel(n);
        stars.add(s);
      }
    }
    
    StarSpace starSp = new StarSpace(width, height, MAX_DISTANCE);
    for (Star s : stars) {
      
      // only add pixels with diameter 4+
      if (Math.max(s.getRadiusX(), s.getRadiusY()) > 3)
        starSp.add(s);
    }
    
    // Gather all edges that are close enough together
    PriorityQueue<StarEdge> starEdgeQueue = new PriorityQueue<StarEdge>(10, new Comparator<StarEdge>() {
      public int compare(StarEdge e1, StarEdge e2) {
        if (e1.weight > e2.weight)
          return 1;
        else if (e1.weight < e2.weight)
          return -1;
        else
          return 0;
      }
    });
    
    for (int i = 0; i < stars.size(); i++) {
      Star s1 = stars.get(i);
      List<Star> neighbors = starSp.getNeighbors(s1, MAX_DISTANCE);
      for (Star s2 : neighbors) {
        if (s1 == s2)
          continue;
        int dist = (int)Math.round(s1.distanceTo(s2));

        starEdgeQueue.add(new StarEdge(s1, s2, dist));
      }
    }
    
    System.out.println("Star edges made.");
    
    // run Kruskal's alg on stars to form constellations
    ArrayList<StarEdge> constellationEdges = new ArrayList<StarEdge>();
    while (!starEdgeQueue.isEmpty()) {
      StarEdge e = starEdgeQueue.poll();
       Star starA = e.starA;
       Star starB = e.starB;
       if (UnionFind.find(starA) != UnionFind.find(starB)) {
        UnionFind.union(starA, starB);
        constellationEdges.add(e);
       }
     }
    
    
    
    List<Constellation> constel = new ArrayList<Constellation>();
    for (Star s: stars){
    	Double smallestDistance = Double.MAX_VALUE;
    	Constellation cc = null;
    	if(constel.isEmpty()){
    		Constellation c = new Constellation(s, MAX_DISTANCE);
    		constel.add(c);
    	}
    	for(Constellation c: constel){
    		Double dist = c.distanceTo(s);
    		if(dist != null && dist < smallestDistance){
    			smallestDistance = dist;
    			cc = c;
    		}
    	}
    	if(cc == null){
    		Constellation c = new Constellation(s, MAX_DISTANCE);
    		constel.add(c);
    	}
    }
    System.out.println("Number of Constellations: " + constel.size());
    
    // Color star pixels red in an image to see if they're identified correctly
    int color = Color.RED.getRGB();
    for (Node n : starNodes) {
      starPixelImg.setRGB(n.x, n.y, color);
    }
    // color parent pixel of star yellow
    color = Color.YELLOW.getRGB();
  //  for (Star s : stars.values()) {
   //   starPixelImg.setRGB(s.parent.x, s.parent.y, color);
    //}

    
    
    // write out image
    try {
      // retrieve image
      File outputfile = new File("starPixels.bmp");
      ImageIO.write(starPixelImg, "bmp", outputfile);
    } catch (IOException e) {
      System.out.println("Could not write output file.");
    }

    System.out.println("Number of Stars: " + stars.size());
    System.out.println("Width: " + width + " Height: " + height);
    System.out.println("Amount of the Night Sky Representing Stars: " + Math.rint(( (double) (starNodes.size())) / ((double)(width*height))*100) + "%");
    System.out.println("Average Star Size: " +  starNodes.size() / stars.size());
    System.out.println("Total Pixels: " + width * height);

    //Draw constellations
    Graphics2D g2 = constellationImg.createGraphics();
    BasicStroke bs = new BasicStroke(2);
    g2.setStroke(bs);
    g2.setColor(Color.RED);
    for (StarEdge e : constellationEdges) {
      g2.drawLine(e.starA.getCenterX(), e.starA.getCenterY(), e.starB.getCenterX(), e.starB.getCenterY());
    }
    //for(Star s : stars.values()){
     // g2.setPaint(Color.red);
      //g2.fill (new Ellipse2D.Double(s.getCenterX(), s.getCenterY(), s.getRadiusX(), s.getRadiusY()));

    //}
    try {
      // retrieve image
      File outputfile = new File("constellation.bmp");
      ImageIO.write(constellationImg, "bmp", outputfile);
    } catch (IOException e) {
      System.out.println("Could not write output file.");
      System.exit(0);
    }
  }

  private static boolean edgeNeeded(Edge e) {
    // check that average intensity is high enough to be a star
    //if ((e.nodeA.intensity + e.nodeB.intensity) / 2 < INTENSITY_THRESHOLD)
    if (e.nodeA.intensity < INTENSITY_THRESHOLD || e.nodeB.intensity < INTENSITY_THRESHOLD)
      return false;

    // check that difference between pixels isn't high indicating a star edge
    if (Math.abs(e.nodeA.intensity - e.nodeB.intensity) > GRADIENT_THRESHOLD)
      return false;

    return true;
  }
  
  private static BufferedImage readImage(String filename) {
    File in = new File(filename);
    BufferedImage img = null;
    try {
      img = ImageIO.read(in);
    } catch (IOException ioException) {
      System.out.println("Input File Could Not Be Found.");
      System.exit(0);
    }
    return img;
  }
  
  private static BufferedImage copyImage(BufferedImage in) {
    ColorModel cm = in.getColorModel();
    boolean alpha = cm.isAlphaPremultiplied();
    WritableRaster rasterConstellation = in.copyData(null);
    return new BufferedImage(cm, rasterConstellation, alpha, null);
  }
}
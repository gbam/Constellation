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
	private static int maxDistance;


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

		buildPixelEdges(img, nodes, edges);

		HashSet<Node> starNodes = new HashSet<Node>();
		List<Star> stars = new ArrayList<Star>();
		findStars(nodes, edges, starNodes, stars);
		List<Constellation> constellations = new ArrayList<Constellation>();
		//Iterate through all the stars
		System.out.println("Number of Stars: " + stars.size());
		System.out.println("Height + Width / #Stars:  " + (height * width) / (2  * stars.size()));
		System.out.println("Sqrt Height * Width " + Math.sqrt(height * width));
		//maxDistance = (height + width) / stars.size();
		maxDistance = (height * width) / (5*stars.size());



		for(Star s: stars){
			Double closestDistance = Double.MAX_VALUE;
			Constellation c = null;
			//If there are no constellations we need to make one
			if(constellations.isEmpty()){
				c = new Constellation(s, maxDistance);
				constellations.add(c);
			}
			//Otherwise check all the constellations & find the closest one
			else{
				for(Constellation cc: constellations){
					Double dist2star = cc.distanceTo(s);
					if(dist2star != 0.0 && dist2star < closestDistance){

						c = cc;
					}

				}
				//If it's still empty, we need to create a new constellation for that star
				if(c == null){
					c = new Constellation(s, maxDistance);
					constellations.add(c);
				}
				//Else add that star to the closest constellation
				else if(constellations.contains(c)){

					c.addStar(s);

				}
			}


		}


		// Color star pixels red in an image to see if they're identified correctly
		int color = Color.RED.getRGB();
		for (Node n : starNodes) {
			starPixelImg.setRGB(n.x, n.y, color);
		}
		// color parent pixel of star yellow
		color = Color.YELLOW.getRGB();
		for (Star s : stars) {
			starPixelImg.setRGB(s.parent.x, s.parent.y, color);
		}



		// write out image
		writeImage(starPixelImg, "starPixels.bmp");

		System.out.println("Width: " + width + " Height: " + height);
		System.out.println("Amount of the Night Sky Representing Stars: " + Math.rint(( (double) (starNodes.size())) / ((double)(width*height))*100) + "%");
		System.out.println("Average Star Size: " +  starNodes.size() / stars.size());
		System.out.println("Number of Constellations: " + constellations.size());
		System.out.println("Total Pixels: " + width * height);

		//Draw constellations
		Graphics2D g2 = constellationImg.createGraphics();
		BasicStroke bs = new BasicStroke(2);
		g2.setStroke(bs);
		g2.setColor(Color.RED);
		for(Constellation cc: constellations){
			//cc.clean();
			ArrayList<StarEdge> seList = cc.getStars();
			for(StarEdge se: seList){
				g2.drawLine(se.cA.getCenterX(), se.cA.getCenterY(), se.cB.getCenterX(), se.cB.getCenterY());
			}
		}



		//for(Constellation cc: constellations){
		//	g2.setColor(Color.RED);
		//	constellationImg.setRGB(cc.centerX, cc.centerY, color);
		//	g2.setColor(Color.YELLOW);
		//if(cc.stars.size() > 1){
		//	for(int i = 0; i < cc.stars.size()-1; i++){
		//		Star s = cc.stars.get(i);
		//		Star s2 = cc.stars.get(i+1);
		//		 g2.drawLine(s.getCenterX(), s.getCenterY(), s2.getCenterX(), s2.getCenterY());

		//}
		//}

		//}

		//for (StarEdge e : constellationEdges) {
		//      g2.drawLine(e.starA.getCenterX(), e.starA.getCenterY(), e.starB.getCenterX(), e.starB.getCenterY());
		//  }
		//for(Star s : stars.values()){
		// g2.setPaint(Color.red);
		//g2.fill (new Ellipse2D.Double(s.getCenterX(), s.getCenterY(), s.getRadiusX(), s.getRadiusY()));

		//}
		writeImage(constellationImg, "constellation.bmp");

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

	public static BufferedImage readImage(String filename) {
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

	public static BufferedImage copyImage(BufferedImage in) {
		ColorModel cm = in.getColorModel();
		boolean alpha = cm.isAlphaPremultiplied();
		WritableRaster rasterConstellation = in.copyData(null);
		return new BufferedImage(cm, rasterConstellation, alpha, null);
	}

	public static void writeImage(BufferedImage im, String filename) {
		try {
			// retrieve image
			File outputfile = new File(filename);
			int period = filename.lastIndexOf('.');
			ImageIO.write(im, filename.substring(period+1), outputfile);
		} catch (IOException e) {
			System.out.println("Could not write output file.");
			System.exit(0);
		}
	}

	public static void buildPixelEdges(BufferedImage img, Map<Long, Node> nodes, PriorityQueue<Edge> edges) {
		int width = img.getWidth();
		int height = img.getHeight();
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

			}
		}
	}

	public static void findStars(Map<Long, Node> nodes, Queue<Edge> edges, Set<Node> starNodes, List<Star> stars) {
		// Kruskal's algorithm
		while (!edges.isEmpty()) {
			Edge e = edges.poll();
			Node nodeA = nodes.get(Node.makeCoordinate(e.nodeA.x, e.nodeA.y));
			Node nodeB = nodes.get(Node.makeCoordinate(e.nodeB.x, e.nodeB.y));
			starNodes.add(nodeA);
			starNodes.add(nodeB);
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
	}
}
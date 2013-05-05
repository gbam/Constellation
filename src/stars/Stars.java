package stars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Stars {

	private static int minIntensity = 64;
	private static String inputFileName = "small_stars.bmp";
	// right now these are very uneducated guesses...
	public static final int INTENSITY_THRESHOLD = 64;
	public static final int GRADIENT_THRESHOLD = 128;
	private static final int MAX_DISTANCE = 40;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//Variables
		int width, height, pixelTotal;
		BufferedImage constellationImg;
		BufferedImage starPixelImg;

		////////Reading Input Files//////// 
		File in = new File(inputFileName);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException ioException) {
			System.out.println("Input File Could Not Be Found.");
			System.exit(0);
		}
		//Creating Constellations Map
		ColorModel cm = img.getColorModel();
		boolean alpha = cm.isAlphaPremultiplied();
		WritableRaster rasterConstellation = img.copyData(null);
		constellationImg = new BufferedImage(cm, rasterConstellation, alpha, null);
		WritableRaster rasterStar = img.copyData(null);
		starPixelImg = new BufferedImage(cm, rasterStar, alpha, null);

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
				//	System.out.println("Finished (" + x + ", " + y + ")");
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
			//System.out.println("Edge of Node A: " + nodeA.x + ", " + nodeA.y + "--->" + nodeB.x + ", " + nodeB.y);
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

		//System.out.println("Number of parents after union find: " + parents.size());

		List<Star> stars = new ArrayList<Star>();
		// Go through all star nodes and create list of Stars (ie cluster of nodes)
		for (Node n : starNodes) {
			Star s = new Star(n);
			if (stars.contains(s)){
				int index = stars.indexOf(s);
				stars.get(index);
			}
			else{
				s = new Star(n);
			}
			s.addPixel(n);

			stars.add(s);
		}
		//Adding stars is messed up.  It gives you 588 stars when it is ran on stars small which should have like 11 stars.
		
		
		List<Node> actualStars = new ArrayList<Node>();

		for(Star s: stars){
			int sX = s.getCenterX();
			int sY = s.getCenterY();
			Node n = new Node(sX, sY, 0);
			if(!actualStars.contains(n)){
				actualStars.add(n);
			}
		}
		


		//Creating the constellations
		List<Constellation> constel = new ArrayList<Constellation>();
		for (Node s: actualStars){
			Constellation cc = null;
			if(constel.isEmpty()){
				Constellation c = new Constellation(s, MAX_DISTANCE);
				constel.add(c);
			}
			else{
				Double smallestDistance = Double.MAX_VALUE;
				for(Constellation c: constel){
					Double dist = c.distanceTo(s);
					if(dist != null && dist < smallestDistance){
						smallestDistance = dist;
						cc = c;
					}				
				}

			}
			if(cc == null){
				cc = new Constellation(s, MAX_DISTANCE);
			}
			else if (cc != null){
				cc.addStar(s); 
			}
			System.out.println("Center X" + cc.centerX + " Center Y: " + cc.centerY);
		}
		System.out.println("Number of Constellations: " + constel.size());

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
		try {
			// retrieve image
			File outputfile = new File("starPixels.bmp");
			ImageIO.write(starPixelImg, "bmp", outputfile);
		} catch (IOException e) {
			System.out.println("Could not write output file.");
		}

		System.out.println("Number of Star Pixels: " + stars.size());
		System.out.println("Number of Stars: " + parents.size());
		System.out.println("Width: " + width + " Height: " + height);
		System.out.println("Amount of the Night Sky Representing Stars: " + Math.rint(( (double) (starNodes.size())) / ((double)(width*height))*100) + "%");
		System.out.println("Average Star Size: " +  nodes.size() / stars.size());
		System.out.println("Total Pixels: " + width * height);

		//Color the nodes
		Graphics2D g2 = constellationImg.createGraphics();
		BasicStroke bs = new BasicStroke(1);
		g2.setStroke(bs);
		g2.setColor(Color.RED);
		for(Constellation c: constel){
			System.out.println("Constellation: " + c.centerX + ", " + c.centerY);

			ArrayList<Node> sList = c.getStars();
			System.out.println("Constellation Size: " + sList.size());
			for(Node s: sList){
				g2.drawLine(s.x, s.y, c.centerX, c.centerY);
			}
		}
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
}
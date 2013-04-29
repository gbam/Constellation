package stars;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Stars {

	private static int minIntensity = 64;
	private static String inputFileName = "stars.jpg";

	public static void main(String[] args) {
		//Variables
		int width, height, pixelTotal;
		BufferedImage constellationImg;

		////////Reading Input Files//////// 
		File in = new File(inputFileName);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException ioException) {
			System.out.println("Input File Could Not Be Found.");
			System.exit(0);
		}
		//Gather basic file information
		width = img.getWidth();
		height = img.getHeight();
		pixelTotal = width * height;

		Map<Long, Node> nodes = new HashMap<Long, Node>();
		Queue<Edge> edges = new PriorityQueue<Edge>();
		
		//Iterate through buffered image
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {				
				//All nodes will already be created before we iterate through them
				Node n = null;
				if(x == 0 && y == 0){
					n = new Node(x, y, img.getRGB(x, y));
					//Hashmap (16 bits: x, 16 bits: y)
					nodes.put((((long) x) << 16 + ((long) y)), n);
				}
				if(y==0 && x+1 != width){
				  //This could be moved in the previous if block
				  //The first row will not have been created vs all other rows would have been.
				  
				  //This will be used to make the edge nodes
				  Node m = nodes.find((x << 16) + y);
				  
				  n = new Node(x, y+1, img.getRGB(x, y+1));
				  nodes.put((((long) x) << 16 + ((long) y+1)), n)
				  //Create relationships between nodes here.
				  n = new Node(x+1, y, img.getRGB(x+1, y));
				  nodes.put((((long) x+1) << 16 + ((long) y)), n)
				}
				else if(y -1 != height){
				  Node current  = nodes.find((x << 16) + y);
				  Node right = nodes.find(((x+1) << 16) + y);
				  Node down = new Node(x, y+1, img.getRGB(x, y+1));
				  //Add edges here to connect nodes
				}
				else{
				  //We now know this has to be the bottom row
				  Node current  = nodes.find((x << 16) + y);
				  //Create relationships between current 
				  Node right = nodes.find(((x+1) << 16) + y);
				}



			}
		}
	}
}
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
				
				if(x == width - 1){
					
				}
				if(y == height - 1){
					
				}



			}
		}
	}
}
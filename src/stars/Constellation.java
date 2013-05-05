package stars;

import java.util.ArrayList;

public class Constellation {
	private int starsCount;
	public int centerX, centerY;
	public int rank;

	ArrayList<Node> stars;
	public Constellation(Node s, int maxDistance){
		stars = new ArrayList<Node>();
		stars.add(s);
		starsCount++;
		rank = 0;
		centerX = s.x;
		centerY = s.y;
	}

	public void addStar(Node s){

		stars.add(s);
		centerY = (s.x + starsCount * centerY)/(starsCount+1);
		centerX = (s.y + starsCount * centerX)/(starsCount+1);
		starsCount++;

	}
	public Double distanceTo(Node s){
		Double xCoord =  Math.exp((double)(s.x- centerX));
		Double yCoord = Math.exp((double)(s.y - centerY));
		Double d = xCoord + yCoord;
		d = Math.sqrt(d);
		return d;
	}

	public ArrayList<Node>getStars(){
		return stars;
	}


}

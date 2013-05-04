package stars;

import java.util.ArrayList;

public class Constellation {
	private int maxDistance, centerX, centerY, starsCount;

	ArrayList<Star> stars;
	public Constellation(Star s, int maxDistance){
		stars = new ArrayList<Star>();
		stars.add(s);
		this.maxDistance = maxDistance;
		starsCount++;
		centerX = s.getCenterX();
		centerY = s.getCenterY();
	}

	public void addStar(Star s){

		stars.add(s);
		centerY = s.getCenterY() + starsCount * centerY;
		centerX = s.getCenterX() + starsCount * centerX;
		starsCount++;

	}
	public Double distanceTo(Star s){
		Double d = Math.exp((double)(s.getCenterX()- centerX)) + Math.exp((double)(s.getCenterY() - centerY));
		if(d < maxDistance){
return d;
		}
		else return null;
	}

}

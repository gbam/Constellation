package stars;

import java.util.ArrayList;

public class Constellation {
	public int centerX, centerY, starsCount, maxDistance;

	ArrayList<Star> stars;
	public Constellation(Star s, int maxDistance){
		stars = new ArrayList<Star>();
		stars.add(s);
		starsCount++;
		this.maxDistance = maxDistance;
		centerX = s.getCenterX();
		centerY = s.getCenterY();
	}

	public void addStar(Star s){

		stars.add(s);
		centerY = s.getCenterY() + starsCount * centerY;
		centerX = s.getCenterX() + starsCount * centerX;
		starsCount++;
		centerY = centerY / starsCount;
		centerX = centerX / starsCount;

	}
	public Double distanceTo(Star s){
		Double xValue = (double) (s.getCenterX()- centerX);
		Double yValue = (double) (s.getCenterY() - centerY);
		Double d = xValue * xValue + yValue * yValue;
		d = Math.sqrt(d);
		if(d < maxDistance)return d;
		else
			return Double.MAX_VALUE;
	}

}

package stars;

import java.util.ArrayList;

public class Constellation {
	public int centerX, centerY, starsCount, maxDistance;

	ArrayList<Star> stars;
	ArrayList<StarEdge> ce;
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
	public void clean(){
		ce = new ArrayList<StarEdge>();
		if(stars.size() > 1){
			for(Star s: stars){
				Star closest = null;
				Double closestValue = Double.MAX_VALUE;
				for(Star s2: stars){
					if(s != s2){
						Double xValue = (double) (s.getCenterX()- s2.getCenterX());
						Double yValue = (double) (s.getCenterY()- s2.getCenterY());
						Double d = xValue * xValue + yValue * yValue;
						d = Math.sqrt(d);
						if(d < closestValue){
							closestValue = d;
							closest = s2;
						}
					}
				}
				StarEdge e = new StarEdge(s, closest);
				ce.add(e);

			}
		}
	}
	public ArrayList<StarEdge> getStars(){
		return this.ce;
	}

}

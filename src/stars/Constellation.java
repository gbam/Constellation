package stars;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

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
		ce = new ArrayList<StarEdge>();

	}

	public void addStar(Star s){

		if(!stars.contains(s)){
			centerY = s.getCenterY() + starsCount * centerY;
			centerX = s.getCenterX() + starsCount * centerX;
			starsCount++;
			centerY = centerY / starsCount;
			centerX = centerX / starsCount;
			if(stars.size() > 1){
				StarEdge closest = findClosest(s);
				ce.add(closest);
			}
			stars.add(s);
		}

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


	private StarEdge findClosest(Star s){
		Double closest = Double.MAX_VALUE;
		Star sClose = null;
		for(Star s2: stars){
			if(s != s2){
				Double xValue = (double) (s.getCenterX()- s2.getCenterX());
				Double yValue = (double) (s.getCenterY()- s2.getCenterY());
				Double d = xValue * xValue + yValue * yValue;
				d = Math.sqrt(d);

				//This may actually cause more problems than it'll help but I think they'll be rare....
				if(d < closest){
					sClose = s2;
					closest = d;
				}
			}
		}
		StarEdge e = new StarEdge(s, sClose, closest);
		return e;


	}


	public void clean(){
		ce = new ArrayList<StarEdge>();


		PriorityQueue<StarEdge> sePQ = new PriorityQueue<StarEdge>(stars.size(), new Comparator<StarEdge>() {
			public int compare(StarEdge e1, StarEdge e2) {
				return (int) (e1.weight - e2.weight);
			};
		});
		//Adds all the edges for a given star
		for(Star s: stars){
			for(Star s2: stars){
				if(s != s2){
					Double xValue = (double) (s.getCenterX()- s2.getCenterX());
					Double yValue = (double) (s.getCenterY()- s2.getCenterY());
					Double d = xValue * xValue + yValue * yValue;
					d = Math.sqrt(d);

					//This may actually cause more problems than it'll help but I think they'll be rare....
					if(d < maxDistance){
						StarEdge e = new StarEdge(s, s2, d);
						sePQ.add(e);
					}
				}
			}
		}

		//
		ArrayList<Star> sList = new ArrayList<Star>();
		while(!sePQ.isEmpty()){
			StarEdge se = sePQ.poll();
			boolean add = false;
			if(!sList.contains(se.cA)){
				sList.add(se.cA);
				add = true;
			}
			if(!sList.contains(se.cB)){
				sList.add(se.cB);
				add = true;
			}
			if(add)ce.add(se);

		}

	}


	public ArrayList<StarEdge> getStars(){
		return this.ce;
	}

}

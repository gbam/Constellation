package stars;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StarSpace {
  
  ArrayList<ArrayList<Set<Star>>> stars;
  int width;
  int height;
  int division;
  int horizontal_num;
  int vertical_num;

  public StarSpace (int width, int height, int division) {
    this.width = width;
    this.height = height;
    this.division = division;
    this.horizontal_num = (int)Math.ceil((double)width / (double)division);
    this.vertical_num = (int)Math.ceil((double)height / (double)division);
    
    stars = new ArrayList <ArrayList<Set<Star>>>(horizontal_num + 1);
    for (int i = 0; i <= horizontal_num; i++) {
      ArrayList<Set<Star>> col = new ArrayList<Set<Star>>(vertical_num + 1);
      for (int j = 0; j <= vertical_num; j++) {
        col.add(new HashSet<Star>());
      }
      stars.add(col);
    }
  }
  
  public void add(Star s) {
    int x = s.getCenterX() / division;
    int y = s.getCenterY() / division;
    
    stars.get(x).get(y).add(s);
  }
  
  public List<Star> getNeighbors(Star s, int dist) {
    List<Star> neighbors = new ArrayList<Star>();
    
    int x = s.getCenterX() / division;
    int y = s.getCenterY() / division;
    
    for (Star st : stars.get(x).get(y))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (x != horizontal_num)
    for (Star st : stars.get(x+1).get(y))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (x != horizontal_num && y != vertical_num)
    for (Star st : stars.get(x+1).get(y+1))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (y != vertical_num)
    for (Star st : stars.get(x).get(y+1))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (x != 0 && y != vertical_num)
    for (Star st : stars.get(x-1).get(y+1))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (x != 0)
    for (Star st : stars.get(x-1).get(y))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (x != 0 && y != 0)
    for (Star st : stars.get(x-1).get(y-1))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (y != 0)
    for (Star st : stars.get(x).get(y-1))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    if (x != horizontal_num && y != 0)
    for (Star st : stars.get(x+1).get(y-1))
      if (s.distanceTo(st) <= dist)
        neighbors.add(st);
    
    return neighbors;
        
  }
}

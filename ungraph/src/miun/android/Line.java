package miun.android;

import android.graphics.Rect;

public class Line {
	private long x1,y1;
	private long x2,y2;
	
	public Line(long _x1,long _y1,long _x2,long _y2) {
		x1 = _x1;
		x2 = _x2;
		y1 = _y1;
		y2 = _y2;
	}
	
	public Line(double rho,double theta,Rect dst) {
		//Create a line in the euclidian room from the hough parameters
		
	}
	
	public String toString() {
		return x1 + " - " + y1 + " - " + x2 + " - " + y2;
	}
	
	public boolean equal(Line line) {
		return (line.x1 == this.x1) && (line.x2 == this.x2) && (line.y1 == this.y1) && (line.y2 == this.y2);
	}
	
	public void draw() {
		//TODO
	}
}

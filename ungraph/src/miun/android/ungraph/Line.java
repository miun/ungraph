package miun.android.ungraph;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import android.graphics.Rect;


public class Line {
	private long x1,y1;
	private long x2,y2;
	private double rho,theta;
	
	public Line(long _x1,long _y1,long _x2,long _y2) {
		//Point temp = new Point(10,20);
		
		x1 = _x1;
		x2 = _x2;
		y1 = _y1;
		y2 = _y2;
		
		//not used in this case
		rho = theta = 0; 
	}
	
	//Create a line in the euclidian room from hough parameters
	public Line(double rho,double theta,Rect dst) {
		double cosa = Math.cos(theta);
		double sina = Math.sin(theta);
		int edges = 0;
		int xt,yt;
		
		//Remeber parameters
		this.rho = rho;
		this.theta = theta;
		
		//Test for vertical and horizontal lines
		if(cosa == 0) {
			y1 = Math.round(rho);
			y2 = Math.round(rho);
			x1 = 0;
			x2 = dst.right;
			return;
		}
		else if(sina == 0) {
			x1 = Math.round(rho);
			x2 = Math.round(rho);
			y1 = 0;
			y2 = dst.bottom;
			return;
		}

		//Top border
		x1 = Math.round(rho / cosa);
		if(x1 >= dst.left && x1 <= dst.right) {
			y1 = 0;
			++edges;
		}
		
		//Left border
		y2 = Math.round(rho / sina);
		if(y2 >= dst.top && y2 <= dst.bottom) {
			if(++edges == 2) {
				x2 = 0;
				return;
			}
			else {
				y1 = y2;
				x1 = 0;
			}
		}
		
		//Bottom border
		x2 = Math.round(Math.tan(theta) * ((rho / sina) - dst.bottom));
		if(x2 >= dst.left && x2 <= dst.right) {
			if(++edges == 2) {
				y2 = dst.bottom;
				return;
			}
			else {
				x1 = x2;
				y1 = dst.bottom;
			}
		}
		
		//Right border
		y2 = Math.round(((rho / cosa) - dst.right) / Math.tan(theta));
		if(y2 >= dst.top && y2 <= dst.bottom) {
			x2 = dst.right;
		}
	}
	
	//Rotation relative to y axis from hough parameters
	public double rotation() {
		return rho;
	}
	
	public String toString() {
		return x1 + " - " + y1 + " - " + x2 + " - " + y2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (x1 != other.x1)
			return false;
		if (x2 != other.x2)
			return false;
		if (y1 != other.y1)
			return false;
		if (y2 != other.y2)
			return false;
		return true;
	}

	public void draw() {
		//TODO
	}
	
	public Point begin() {
		return new Point(x1,y1);
	}

	public Point end() {
		return new Point(x2,y2);
	}
	
	//Find the beginning and end of the line in an black / white (for example canny filtered) image
	public Line analyseLineLength(Mat image) {
		LineIterator li = new LineIterator(this,false);
		Point p0 = begin();
		Point p1 = end();
		Point current;
		int threshold = 0;
		double[] pix;
		
		Size size = image.size();
		
		//Find beginning of line
		while(li.hasNext()) {
			current = (Point)li.next();
			
			if(current.x >= size.width || current.y >= size.height) {
				assert(true);
			}
			
			pix = image.get((int)Math.round(current.y),(int)Math.round(current.x));
			if(pix == null) {
				assert(true);
			}
			
			if(pix[0] != 0) {
				//Increase threshold
				threshold++;

				//Check threshold
				if(threshold == 1) {
					p0 = current;
				}
				if(threshold == 5) {
					break;
				}
			}
			else {
				//Reset threshold
				p0 = current;
				threshold = 0;
			}
		}

		//Find end of line
		threshold = 0;
		while(li.hasNext()) {
			current = (Point)li.next();
			if(image.get((int)Math.round(current.y),(int)Math.round(current.x))[0] == 0) {
				//Increase threshold and remember end
				threshold++;
				
				//Check threshold
				if(threshold == 5) {
					break;
				}
			}
			else {
				//Reset threshold
				p1 = current;
				threshold = 0;
			}
		}
		
		//Set new line
		x1 = Math.round(p0.x);
		y1 = Math.round(p0.y);
		x2 = Math.round(p1.x);
		y2 = Math.round(p1.y);

		return this;
	}
	
	public double length() {
		//Return greek length
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)); 
	}
}

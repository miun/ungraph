package miun.android.ungraph; 

import java.util.Iterator;

import org.opencv.core.Point;

/*
void line(int x0, int y0, int x1, int y1)
{
  int dx =  abs(x1-x0), sx = x0<x1 ? 1 : -1;
  int dy = -abs(y1-y0), sy = y0<y1 ? 1 : -1; 
  int err = dx+dy, e2; // error value e_xy
 
  for(;;){
    setPixel(x0,y0);
    if (x0==x1 && y0==y1) break;
    e2 = 2*err;
    if (e2 > dy) { err += dy; x0 += sx; } // e_xy+e_x > 0
    if (e2 < dx) { err += dx; y0 += sy; } // e_xy+e_y < 0
  }
*/

public class LineIterator implements Iterator {
	long x0,x1;
	long y0,y1;
	
	long dx,dy;
	long sx,sy;
	long err,e2;
	
	public LineIterator(Line line,boolean eight_step) {
		Point p1 = line.begin();
		Point p2 = line.end();
		
		x0 = Math.round(p1.x);
		y0 = Math.round(p1.y);
		x1 = Math.round(p2.x);
		y1 = Math.round(p2.y);
		
		dx = Math.abs(x1 - x0);
		sx = x0 < x1 ? 1 : -1;
		
		dy = -Math.abs(y1 - y0);
		sy = y0 < y1 ? 1: -1;
		
		err = dx + dy;
	}

	public boolean hasNext() {
		//More points?
		return ((y1 - y0) * sy) > 0 || ((x1 - x0) * sx) > 0;
	}

	public Object next() {
	    Point point = new Point(x0,y0);
	    
	    e2 = 2 * err;
	    if (e2 > dy) { err += dy; x0 += sx; } // e_xy+e_x > 0
	    if (e2 < dx) { err += dx; y0 += sy; } // e_xy+e_y < 0

	    return point;
	}

	public void remove() {
		//Unimplemented
	}
}
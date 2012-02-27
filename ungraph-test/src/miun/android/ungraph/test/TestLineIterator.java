package miun.android.ungraph.test;

import junit.framework.TestCase;
import miun.android.ungraph.Line;
import miun.android.ungraph.LineIterator;

import org.opencv.core.Point;

public class TestLineIterator extends TestCase {
	public TestLineIterator() {
		this("TestLineIterator default");
	}

	public TestLineIterator(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testLineIteration() {
		Line line = new Line(10,10,100,100);
		LineIterator li = new LineIterator(line,false);
		Point point;
		
		//Test line iterator
		while(li.hasNext()) {
			System.out.println(li.next());
			
			point = (Point)li.next();
			assertNotNull(point);
			assertEquals(point.x,point.y);
		}
	}
}

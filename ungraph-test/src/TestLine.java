import junit.framework.TestCase;
import miun.android.Line;
import android.graphics.Rect;

public class TestLine extends TestCase {
	public TestLine() {
		this("TestTransHelp default");
	}
	
	public TestLine(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testLineParamToRect() {
		Line result,expected;
		Rect dst;
		
		dst = new Rect(0,0,100,100);
		
		expected = new Line(0,0,100,100);
		result = new Line(0,0,dst);
		
		assert(result == expected);
	}
	
	public void testLineEqual() {
		assert(false);
	}
	
	public void testLineToString() {
		assert(false);
	}
	
	public void testLineDraw() {
		assert(false);
	}
}

package miun.android.ungraph.test;

import junit.framework.TestCase;
import miun.android.ungraph.Line;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

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
		Line result, expected;
		Rect dst;

		dst = new Rect(0, 0, 100, 100);

		expected = new Line(0, 0, 0, 100);
		result = new Line(0, 0, dst);
		assertEquals(result, expected);

		expected = new Line(14, 0, 0, 14);
		result = new Line(10, Math.PI / 4, dst);
		assertEquals(result, expected);

		expected = new Line(70, 100, 100, 70);
		result = new Line(120, Math.PI / 4, dst);
		assertEquals(result, expected);

		expected = new Line(28, 0, 100, 72);
		result = new Line(20, -Math.PI / 4, dst);
		assertEquals(result, expected);
	}

	public void testLineEqual() {
		Line l1, l2, l3;

		l1 = new Line(0, 0, 100, 100);
		l2 = new Line(1, 2, 3, 4);
		l3 = new Line(5, 6, 7, 8);

		assertEquals(l1, l1);
		assertEquals(l2, l2);
		assertEquals(l3, l3);
		assertTrue(l1 != l2);
		assertTrue(l1 != l2);
		assertTrue(l2 != l3);
	}

	public void testLineToString() {
		assertTrue(new Line(0, 0, 100, 100).toString().equals(
				"0 - 0 - 100 - 100"));
	}

	public void testLineDraw() {
		// Not implemented yet
		// assert(false);
		fail("Not implemented yet!");
	}

	// Test the line begin and end point functions
	public void testLineBeginEnd() {
		Line line = new Line(10, 20, 30, 40);
		
		assertEquals(line.begin(), new Point(10, 20));
		assertEquals(line.end(), new Point(30, 40));
	}

	// Test the line analysing function
	public void testAnalyseLineLength() {
		Mat image = new Mat(100, 100, CvType.CV_8UC1);
		Line line = new Line(0, 0, 100, 100);
		Line result;

		Core.line(image, new Point(10, 10), new Point(70, 70), new Scalar(255));
		result = line.analyseLineLength(image);

		assertEquals(line, result);
	}
}

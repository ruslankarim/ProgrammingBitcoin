package point;

import fieldElement.FieldElement;
import org.junit.Test;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static point.Point.isOnCurve;

public class PointTest {

	final BigInteger prime = BigInteger.valueOf(223);
	final BigInteger a = BigInteger.valueOf(5);
	final BigInteger b = BigInteger.valueOf(7);
	final FieldElement fieldElementA = new FieldElement(BigInteger.ZERO, prime);
	final FieldElement fieldElementB = new FieldElement(b, prime);

	@Test
	public void isOnCurveTest() {
		assertTrue(isOnCurve(BigInteger.valueOf(3), BigInteger.valueOf(7), a, b));
		assertFalse(isOnCurve(BigInteger.valueOf(-1), BigInteger.valueOf(2), a, b));

		List<List<FieldElement>> fieldElements = Arrays.asList(
				Arrays.asList(
						new FieldElement(BigInteger.valueOf(192), prime),
						new FieldElement(BigInteger.valueOf(105), prime)),
				Arrays.asList(
						new FieldElement(BigInteger.valueOf(17), prime),
						new FieldElement(BigInteger.valueOf(56), prime)),
				Arrays.asList(
						new FieldElement(BigInteger.ONE, prime),
						new FieldElement(BigInteger.valueOf(193), prime))
		);

		List<List<FieldElement>> fieldElementsIncorrect = Arrays.asList(
				Arrays.asList(
						new FieldElement(BigInteger.valueOf(200), prime),
						new FieldElement(BigInteger.valueOf(119), prime)),
				Arrays.asList(
						new FieldElement(BigInteger.valueOf(42), prime),
						new FieldElement(BigInteger.valueOf(99), prime))
		);

		fieldElements.forEach(list -> assertTrue(isOnCurve(list.get(0), list.get(1), fieldElementA, fieldElementB)));
		fieldElementsIncorrect.forEach(list -> assertFalse(isOnCurve(list.get(0), list.get(1), fieldElementA, fieldElementB)));

	}

	@Test
	public void addTestWhenThisXIsNull() {
		Point other = new Point(BigInteger.valueOf(3), BigInteger.valueOf(7), a, b);
		Point pointThis = new Point(null, BigInteger.valueOf(7), a, b);
		assertEquals(other, pointThis.add(other));
	}

	@Test
	public void addTestWhenOtherXIsNull() {
		Point pointThis = new Point(BigInteger.valueOf(3), BigInteger.valueOf(7), a, b);
		Point other = new Point(null, BigInteger.valueOf(7), a, b);
		assertEquals(pointThis, pointThis.add(other));
	}

	@Test
	public void addTestWhenThisEqualsOther() {
		Point point = new Point(BigInteger.valueOf(-1), BigInteger.valueOf(-1), a, b);
		Point expected = new Point(BigInteger.valueOf(18), BigInteger.valueOf(77), a, b);
		assertEquals(expected, point.add(point));
	}

	@Test
	public void addTestWhenThisNotEqualsOther() {
		Point pointThis = new Point(BigInteger.valueOf(3), BigInteger.valueOf(7), a, b);
		Point pointOther = new Point(BigInteger.valueOf(-1), BigInteger.valueOf(-1), a, b);
		Point expected = new Point(BigInteger.valueOf(2), BigInteger.valueOf(-5), a, b);
		assertEquals(expected, pointThis.add(pointOther));

		List<List<Point>> additions = Arrays.asList(
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(192), prime),
								new FieldElement(BigInteger.valueOf(105), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(17), prime),
								new FieldElement(BigInteger.valueOf(56), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(170), prime),
								new FieldElement(BigInteger.valueOf(142), prime),
								fieldElementA, fieldElementB
						)
				),
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(47), prime),
								new FieldElement(BigInteger.valueOf(71), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(117), prime),
								new FieldElement(BigInteger.valueOf(141), prime),
								fieldElementA, fieldElementB
						),

						new Point(
								new FieldElement(BigInteger.valueOf(60), prime),
								new FieldElement(BigInteger.valueOf(139), prime),
								fieldElementA, fieldElementB
						)
				),
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(143), prime),
								new FieldElement(BigInteger.valueOf(98), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(76), prime),
								new FieldElement(BigInteger.valueOf(66), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(47), prime),
								new FieldElement(BigInteger.valueOf(71), prime),
								fieldElementA, fieldElementB
						)
				)
		);

		additions.forEach(list -> assertEquals(list.get(2), list.get(0).add(list.get(1))));
	}

	@Test
	public void multiplyTest() {
		List<BigInteger> multipliers = Arrays.asList(
				BigInteger.valueOf(2),
				BigInteger.valueOf(2),
				BigInteger.valueOf(4),
				BigInteger.valueOf(8),
				BigInteger.valueOf(21)
		);
		List<List<Point>> points = Arrays.asList(
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(192), prime),
								new FieldElement(BigInteger.valueOf(105), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(49), prime),
								new FieldElement(BigInteger.valueOf(71), prime),
								fieldElementA, fieldElementB
						)
				),
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(143), prime),
								new FieldElement(BigInteger.valueOf(98), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(64), prime),
								new FieldElement(BigInteger.valueOf(168), prime),
								fieldElementA, fieldElementB
						)
				),
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(47), prime),
								new FieldElement(BigInteger.valueOf(71), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(194), prime),
								new FieldElement(BigInteger.valueOf(51), prime),
								fieldElementA, fieldElementB
						)
				),
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(47), prime),
								new FieldElement(BigInteger.valueOf(71), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								new FieldElement(BigInteger.valueOf(116), prime),
								new FieldElement(BigInteger.valueOf(55), prime),
								fieldElementA, fieldElementB
						)
				),
				Arrays.asList(
						new Point(
								new FieldElement(BigInteger.valueOf(47), prime),
								new FieldElement(BigInteger.valueOf(71), prime),
								fieldElementA, fieldElementB
						),
						new Point(
								null,
								null,
								fieldElementA, fieldElementB
						)
				)
		);

		IntStream.range(0, multipliers.size()).forEach(i -> assertEquals(
				points.get(i).get(1),
				points.get(i).get(0).multiply(multipliers.get(i), prime)));

	}

}

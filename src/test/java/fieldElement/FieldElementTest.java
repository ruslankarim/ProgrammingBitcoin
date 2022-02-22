package fieldElement;

import org.junit.Assert;
import org.junit.Test;
import java.math.BigInteger;

import static mock.Mock.getFieldElement;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldElementTest {

	@Test
	public void addTest() {
		final FieldElement fieldElementA = getFieldElement(BigInteger.valueOf(17), BigInteger.valueOf(31));
		final FieldElement fieldElementB = getFieldElement(BigInteger.valueOf(21), BigInteger.valueOf(31));
		final FieldElement fieldElementC = getFieldElement(BigInteger.valueOf(7), BigInteger.valueOf(31));
		final FieldElement fieldElementIncorrectPrime = getFieldElement(BigInteger.valueOf(7), BigInteger.valueOf(37));
		try {
			assertEquals(fieldElementC, fieldElementA.add(fieldElementB));
			assertEquals(fieldElementC, fieldElementB.add(fieldElementIncorrectPrime));
		} catch (RuntimeException e) {
			Assert.assertEquals("Exception message must be correct",
					"Cannot add two numbers in different Fields", e.getMessage());
		}

	}

	@Test
	public void subtractTest() {
		final FieldElement fieldElementA = getFieldElement(BigInteger.valueOf(15), BigInteger.valueOf(31));
		final FieldElement fieldElementB = getFieldElement(BigInteger.valueOf(30), BigInteger.valueOf(31));
		final FieldElement fieldElementC = getFieldElement(BigInteger.valueOf(16), BigInteger.valueOf(31));
		assertEquals(fieldElementC, fieldElementA.subtract(fieldElementB));
	}

	@Test
	public void multiplyTest() {
		final FieldElement fieldElementA = getFieldElement(BigInteger.valueOf(24), BigInteger.valueOf(31));
		final FieldElement fieldElementB = getFieldElement(BigInteger.valueOf(19), BigInteger.valueOf(31));
		final FieldElement fieldElementC = getFieldElement(BigInteger.valueOf(22), BigInteger.valueOf(31));
		assertEquals(fieldElementC, fieldElementA.multiply(fieldElementB));
	}

	@Test
	public void powTest() {
		final FieldElement fieldElementA = getFieldElement(BigInteger.valueOf(17), BigInteger.valueOf(31));
		final FieldElement fieldElementB = getFieldElement(BigInteger.valueOf(15), BigInteger.valueOf(31));
		assertEquals(fieldElementB, fieldElementA.pow(3));
	}

	@Test
	public void divide() {
		final FieldElement fieldElementA = getFieldElement(BigInteger.valueOf(3), BigInteger.valueOf(31));
		final FieldElement fieldElementB = getFieldElement(BigInteger.valueOf(24), BigInteger.valueOf(31));
		final FieldElement fieldElementC = getFieldElement(BigInteger.valueOf(4), BigInteger.valueOf(31));
		assertEquals(fieldElementC, fieldElementA.divide(fieldElementB));
	}

}

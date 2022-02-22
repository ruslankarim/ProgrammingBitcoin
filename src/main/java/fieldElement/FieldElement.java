package fieldElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@EqualsAndHashCode
@ToString
public class FieldElement {

	private final BigInteger num;
	private final BigInteger prime;

	public FieldElement(BigInteger num, BigInteger prime) {
		if (num.compareTo(prime) >= 0 || num.signum() < 0) {
			throw new RuntimeException(String.format("Num %d not in field range 0 to %d", num, prime.subtract(BigInteger.ONE)));
		}
		this.num = num;
		this.prime = prime;
	}

	public FieldElement add(FieldElement other) {
		if (!this.prime.equals(other.prime)) {
			throw new RuntimeException("Cannot add two numbers in different Fields");
		}
		return new FieldElement(this.num.add(other.num).mod(prime), prime);
	}

	public FieldElement subtract(FieldElement other ) {
		if (!this.prime.equals(other.prime)) {
			throw new RuntimeException("Cannot add two numbers in different Fields");
		}
		return new FieldElement(this.num.subtract(other.num).mod(prime), prime);
	}

	public FieldElement multiply(FieldElement other) {
		if (!this.prime.equals(other.prime)) {
			throw new RuntimeException("Cannot add two numbers in different Fields");
		}
		return new FieldElement(this.num.multiply(other.num).mod(prime), prime);
	}

	public FieldElement multiplyScalar(BigInteger scale) {
		return new FieldElement(this.num.multiply(scale).mod(prime), prime);
	}

	public FieldElement pow(int exponent) {
		exponent = BigInteger.valueOf(exponent).mod(prime.subtract(BigInteger.ONE)).intValue();
		return new FieldElement(this.num.pow(exponent).mod(prime), prime);
	}

	public FieldElement divide(FieldElement other) {
		if (!this.prime.equals(other.prime)) {
			throw new RuntimeException("Cannot add two numbers in different Fields");
		}
		BigInteger n = this.num.multiply(other.num.modPow(this.prime.subtract(BigInteger.valueOf(2)).mod(prime), prime));
		return new FieldElement(n.mod(prime), prime);
	}

}

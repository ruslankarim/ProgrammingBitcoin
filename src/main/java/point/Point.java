package point;

import fieldElement.FieldElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

import static fieldElement.S256FieldElement.PRIME;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
public class Point {

	private BigInteger x;
	private BigInteger y;
	private BigInteger a;
	private BigInteger b;
	private FieldElement fieldElementA;
	private FieldElement fieldElementB;
	private FieldElement fieldElementX;
	private FieldElement fieldElementY;

	public Point(BigInteger x, BigInteger y, BigInteger a, BigInteger b) {
		if (x != null && y != null) {
			if (!isOnCurve(x, y, a, b)) {
				throw new RuntimeException(String.format("(%s, %s) is not on the curve", x, y));
			}
		}
		this.a = a;
		this.b = b;
		this.x = x;
		this.y = y;
	}

	public Point(FieldElement fieldElementX, FieldElement fieldElementY, FieldElement fieldElementA, FieldElement fieldElementB) {
		if (fieldElementX != null && fieldElementY != null) {
			if (!isOnCurve(fieldElementX, fieldElementY, fieldElementA, fieldElementB)) {
				throw new RuntimeException(String.format("(%s, %s) is not on the curve", fieldElementX, fieldElementB));
			}
		}
		this.fieldElementA = fieldElementA;
		this.fieldElementB = fieldElementB;
		this.fieldElementX = fieldElementX;
		this.fieldElementY = fieldElementY;
	}

	public static boolean isOnCurve(BigInteger x, BigInteger y, BigInteger a, BigInteger b) {
		return y.pow(2).equals(x.pow(3).add(a.multiply(x)).add(b));
	}

	public static boolean isOnCurve(FieldElement x, FieldElement y, FieldElement a, FieldElement b) {
		return y.pow(2).equals(x.pow(3).add(a.multiply(x)).add(b));
	}

	public Point add(Point other) {
		if (this.fieldElementA != null && this.fieldElementB != null) {
			if (!this.fieldElementA.getNum().equals(other.fieldElementA.getNum()) || !this.fieldElementB.getNum().equals(other.fieldElementB.getNum())) {
				throw new RuntimeException(String.format("Points %s, %s are not on the same curve",
						this, other));

			} else if (this.fieldElementX == null) {
				return other;

			} else if (other.fieldElementX == null) {
				return this;

			} else if (this.fieldElementX.equals(other.fieldElementX) && !this.fieldElementY.equals(other.fieldElementY)) {
				return new Point(null, null, this.fieldElementA, this.fieldElementB);

			} else if (this.equals(other) && this.fieldElementY.getNum().equals(BigInteger.ZERO)) {
				return new Point(null, null, this.fieldElementA, this.fieldElementB);

			} else if (this.equals(other)) {
				FieldElement s = (this.fieldElementX.pow(2).multiplyScalar(BigInteger.valueOf(3)).add(this.fieldElementA)).divide(this.fieldElementY.multiplyScalar(BigInteger.valueOf(2)));
				FieldElement x = (s.pow(2)).subtract(this.fieldElementX.multiplyScalar(BigInteger.valueOf(2)));
				FieldElement y = (s.multiply(this.fieldElementX.subtract(x))).subtract(this.fieldElementY);
				return new Point(x, y, this.fieldElementA, this.fieldElementB);

			} else {
				FieldElement s = other.fieldElementY.subtract(this.fieldElementY).divide(other.fieldElementX.subtract(this.fieldElementX));
				FieldElement x = s.pow(2).subtract(this.fieldElementX).subtract(other.fieldElementX);
				FieldElement y = s.multiply(this.fieldElementX.subtract(x)).subtract(this.fieldElementY);
				return new Point(x, y, this.fieldElementA, this.fieldElementB);

			}

		} else {
			if (!this.a.equals(other.a) || !this.b.equals(other.b)) {
				throw new RuntimeException(String.format("Points %s, %s are not on the same curve", this, other));

			} else if (this.x == null) {
				return other;

			} else if (other.x == null) {
				return this;

			} else if (this.x.equals(other.x) && !this.y.equals(other.y)) {
				return new Point(null, null, this.a, this.b);

			} else if (this.equals(other) && this.y.equals(BigInteger.ZERO)) {
				return new Point(null, null, this.a, this.b);

			} else if (this.equals(other)) {
				BigInteger s = (this.x.pow(2).multiply(BigInteger.valueOf(3)).add(this.a)).divide(this.y.multiply(BigInteger.valueOf(2)));
				BigInteger x = (s.pow(2)).subtract(this.x.multiply(BigInteger.valueOf(2)));
				BigInteger y = (s.multiply(this.x.subtract(x))).subtract(this.y);

				return new Point(x, y, this.a, this.b);
			} else {
				BigInteger s = other.y.subtract(this.y).divide(other.x.subtract(this.x));
				BigInteger x = s.pow(2).subtract(this.x).subtract(other.x);
				BigInteger y = s.multiply(this.x.subtract(x)).subtract(this.y);

				return new Point(x, y, this.a, this.b);
			}
		}
	}

	public Point multiply(BigInteger scale, BigInteger prime) {
		scale = scale.mod(prime);
		Point result;
		if (this.fieldElementA != null && this.fieldElementB != null) {
			result = new Point(null, null, fieldElementA, fieldElementB);
		} else {
			result = new Point(null, null, this.a, this.b);
		}
		Point current = this;
		while (scale.compareTo(BigInteger.ZERO) > 0) {
			if (scale.and(BigInteger.ONE).compareTo(BigInteger.ZERO) > 0) {
				result = result.add(current);
			}
			current = current.add(current);
			scale = scale.shiftRight(1);
		}
		return result;
	}

	public Point multiply(BigInteger scale) {
		return multiply(scale, PRIME);
	}


}

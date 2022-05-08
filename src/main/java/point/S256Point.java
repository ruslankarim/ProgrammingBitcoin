package point;

import fieldElement.FieldElement;
import fieldElement.S256FieldElement;
import lombok.Getter;
import lombok.ToString;
import signature.Signature;

import java.math.BigInteger;
import java.util.Arrays;

import static fieldElement.S256FieldElement.PRIME;

@ToString(callSuper = true)
@Getter
public class S256Point extends Point{

	public static final S256FieldElement A = new S256FieldElement(BigInteger.ZERO);
	public static final S256FieldElement B = new S256FieldElement(BigInteger.valueOf(7));
	public static final BigInteger N = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
	public static final S256FieldElement X = new S256FieldElement(new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16));
	public static final S256FieldElement Y = new S256FieldElement(new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8", 16));
	public static final S256Point G = getPointG();

	private S256FieldElement s256FieldElementX;
	private S256FieldElement s256FieldElementY;

	private S256Point(S256FieldElement fieldElementX, S256FieldElement fieldElementY, FieldElement fieldElementA, FieldElement fieldElementB) {
		super(fieldElementX, fieldElementY, fieldElementA, fieldElementB);
	}

	public S256Point(S256FieldElement fieldElementX, S256FieldElement fieldElementY) {
		super(fieldElementX, fieldElementY, A, B);
		this.s256FieldElementX = fieldElementX;
		this.s256FieldElementY = fieldElementY;
	}

	private S256Point() {
		this(X, Y);
	}

	public static S256Point getPointG() {
		return new S256Point();
	}

	public S256Point multiply(BigInteger scale) {
		scale = scale.mod(N);
		Point point = super.multiply(scale, PRIME);
		S256FieldElement fieldElementX = point.getFieldElementX() != null ? new S256FieldElement(point.getFieldElementX().getNum()) : null;
		S256FieldElement fieldElementY = point.getFieldElementY() != null ? new S256FieldElement(point.getFieldElementY().getNum()) : null;
		return new S256Point(fieldElementX, fieldElementY);
	}

	public boolean verify(BigInteger z, Signature sig) {
		BigInteger sInv = sig.getS().modPow(N.subtract(BigInteger.valueOf(2)), N);
		BigInteger u = z.multiply(sInv).mod(N);
		BigInteger v = sig.getR().multiply(sInv).mod(N);
		Point total = G.multiply(u, PRIME).add(this.multiply(v, PRIME));
		return total.getFieldElementX().getNum().equals(sig.getR());
	}

	public byte[] sec(boolean isCompressed) {
		byte[] bytesX = this.getFieldElementX().getNum().toByteArray();
		byte[] bytesY = this.getFieldElementY().getNum().toByteArray();
		byte[] res;
		if (isCompressed) {
			res = new byte [1 + bytesX.length];
			if (this.getFieldElementY().getNum().mod(BigInteger.TWO).signum() == 0) {
				res[0] = 2;
			} else {
				res[0] = 3;
			}
			System.arraycopy(bytesX, 0, res, 1, bytesX.length);
		} else {
			res = new byte [1 + (bytesX.length == 32 ? (bytesX.length + 1) : bytesX.length) + (bytesY.length == 32 ? (bytesY.length + 1) : bytesY.length)];
			res[0] = 4;
			System.arraycopy(bytesX, 0, res, 1, bytesX.length);
			System.arraycopy(bytesY, 0, res, bytesX.length + 2, bytesY.length);
		}

		return res;
	}

	//todo
	public static S256Point parse(byte[] sec) {

		if (sec[0] == 4) {
			S256FieldElement x = new S256FieldElement(new BigInteger(Arrays.copyOfRange(sec, 1, 34)));
			S256FieldElement y = new S256FieldElement(new BigInteger(Arrays.copyOfRange(sec, 34, sec.length)));
			return new S256Point(x, y);
		}

		boolean isEven = sec[0] == 2;
		S256FieldElement s256FieldElementX = new S256FieldElement(new BigInteger(Arrays.copyOfRange(sec, 1, sec.length)));
		S256FieldElement alpha = new S256FieldElement(s256FieldElementX.pow(3).add(A).getNum());
		S256FieldElement beta = alpha.sqrt();
		S256FieldElement evenBeta;
		S256FieldElement oddBeta;

		if (beta.getNum().mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
			evenBeta = beta;
			oddBeta = new S256FieldElement(PRIME.subtract(beta.getNum()));
		} else {
			evenBeta = new S256FieldElement(PRIME.subtract(beta.getNum()));
			oddBeta = beta;
		}

		if (isEven) {
			return new S256Point(s256FieldElementX, evenBeta);
		} else {
			return new S256Point(s256FieldElementX, oddBeta);
		}

	}
}

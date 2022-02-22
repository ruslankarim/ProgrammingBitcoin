package point;

import fieldElement.FieldElement;
import fieldElement.S256FieldElement;
import lombok.ToString;
import signature.Signature;

import java.math.BigInteger;

import static fieldElement.S256FieldElement.PRIME;

@ToString(callSuper = true)
public class S256Point extends Point{

	public static final S256FieldElement A = new S256FieldElement(BigInteger.ZERO);
	public static final S256FieldElement B = new S256FieldElement(BigInteger.valueOf(7));
	public static final BigInteger N = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
	public static final S256FieldElement X = new S256FieldElement(new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16));
	public static final S256FieldElement Y = new S256FieldElement(new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8", 16));
	public static final S256Point G = getPointG();

	private S256Point(S256FieldElement fieldElementX, S256FieldElement fieldElementY, FieldElement fieldElementA, FieldElement fieldElementB) {
		super(fieldElementX, fieldElementY, A, B);
	}

	public S256Point(S256FieldElement fieldElementX, S256FieldElement fieldElementY) {
		super(fieldElementX, fieldElementY, A, B);
	}

	private S256Point() {
		this(X, Y);
	}

	private static S256Point getPointG() {
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
		Point G = new Point(X, Y, A, B);
		Point total = G.multiply(u, PRIME).add(this.multiply(v, PRIME));
		return total.getFieldElementX().getNum().equals(sig.getR());
	}


}

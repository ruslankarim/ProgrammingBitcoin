package constants;

import fieldElement.FieldElement;

import java.math.BigInteger;

public class Constants {

	public static final BigInteger PRIME = BigInteger.valueOf(2).pow(256).subtract(BigInteger.valueOf(2).pow(32)).subtract(BigInteger.valueOf(977));
	public static final FieldElement A = new FieldElement(BigInteger.ZERO, PRIME);
	public static final FieldElement B = new FieldElement(BigInteger.valueOf(7), PRIME);
	public static final FieldElement GX = new FieldElement(new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16), PRIME);
	public static final FieldElement GY = new FieldElement(new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8", 16), PRIME);
	public static final BigInteger N = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
	//public static final Point G = new Point(GX, GY, A, B);

}

package fieldElement;

import lombok.ToString;
import java.math.BigInteger;

@ToString(callSuper = true)
public class S256FieldElement extends FieldElement{

	public static final BigInteger PRIME = BigInteger.valueOf(2).pow(256)
			.subtract(BigInteger.valueOf(2).pow(32)).subtract(BigInteger.valueOf(977));

	private S256FieldElement(BigInteger num, BigInteger prime) {
		super(num, prime);
	}

	public S256FieldElement(BigInteger num) {
		super(num, PRIME);
	}

	public S256FieldElement sqrt() {
		return new S256FieldElement(this.getNum().modPow((PRIME.add(BigInteger.ONE)).divide(BigInteger.valueOf(4)), PRIME));
	}


}

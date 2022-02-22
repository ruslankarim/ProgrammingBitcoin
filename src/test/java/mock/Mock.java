package mock;

import fieldElement.FieldElement;
import java.math.BigInteger;

public class Mock {

	public static FieldElement getFieldElement(BigInteger num, BigInteger prime) {
		return new FieldElement(num, prime);
	}

}

package signature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@AllArgsConstructor
@Getter
@ToString
public class Signature {

	private BigInteger r;

	private BigInteger s;

	public byte[] der() {
		byte[] bytes = this.r.toByteArray();
		//not implemented
		return null;
	}

}

package privateKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import signature.Signature;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static point.S256Point.N;

public class PrivateKeyTest {

	@Test
	public void signTest() throws NoSuchAlgorithmException, InvalidKeyException {
		BigInteger secret = new BigInteger(N.bitLength(), new Random());
		PrivateKey privateKey = new PrivateKey(secret);
		BigInteger z = new BigInteger(BigInteger.valueOf(2).pow(256).bitLength(), new Random());
		Signature signature = privateKey.sign(z);
		Assertions.assertTrue(privateKey.getPoint().verify(z, signature));
	}
}

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

	String hello = "1c8aff950685c2ed4bc3174f3472287b56d9517b9c948127319a09a7a36deac8";

	@Test
	public void signTest() throws NoSuchAlgorithmException, InvalidKeyException {
		BigInteger secret = new BigInteger(N.bitLength(), new Random());
		PrivateKey privateKey = new PrivateKey(secret);
		BigInteger z = new BigInteger(BigInteger.valueOf(2).pow(256).bitLength(), new Random());
		Signature signature = privateKey.sign(z);
		Assertions.assertTrue(privateKey.getPublicPoint().verify(z, signature));
	}
}

package privateKey;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import point.S256Point;
import signature.Signature;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static point.S256Point.G;
import static point.S256Point.N;

@Getter
public class PrivateKey {

	private static final String ALGORITHM = "HmacSHA256";

	private final BigInteger secret;
	private final S256Point point;

	public PrivateKey(BigInteger secret) {
		this.secret = secret;
		this.point = G.multiply(this.secret);
	}

	public Signature sign(BigInteger z) throws NoSuchAlgorithmException, InvalidKeyException {
		BigInteger k = deterministicK(z);
		BigInteger r = G.multiply(k).getFieldElementX().getNum();
		BigInteger kInv = k.modPow(N.subtract(BigInteger.valueOf(2)), N);
		BigInteger s = (r.multiply(this.secret).add(z)).multiply(kInv.mod(N));
		if (s.compareTo(N.divide(BigInteger.valueOf(2))) > 0) {
			s = N.subtract(s);
		}
		return new Signature(r, s);
	}

	private BigInteger deterministicK(BigInteger z) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] k = fillArray((byte) 0, new byte[32]);
		byte[] v = fillArray((byte) 1, new byte[32]);
		if (z.compareTo(N) > 0) {
			z = z.subtract(N);
		}
		byte[] zBytes = z.toByteArray();
		byte[] secretBytes = this.secret.toByteArray();
		k = hmac(k, ArrayUtils.add(v, (byte) 0), zBytes, secretBytes);
		v = hmac(k, v);
		k = hmac(k, ArrayUtils.add(v, (byte) 1), zBytes, secretBytes);
		v = hmac(k, v);

		while (true) {
			v = hmac(k, v);
			BigInteger candidate = new BigInteger(v);
			if (candidate.compareTo(BigInteger.ONE) >= 0 && candidate.compareTo(N) < 0) {
				return candidate;
			}
			k = hmac(k, ArrayUtils.add(v, (byte) 0), zBytes, secretBytes);
			v = hmac(k, v);
		}

	}

	private byte[] hmac(byte[] ...bytes) throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(bytes[0], ALGORITHM);
		Mac mac = Mac.getInstance(ALGORITHM);
		mac.init(secretKeySpec);
		byte [] v = bytes[1];
		if (bytes.length == 4) {
			byte [] zBytes = bytes[2];
			byte [] secretBytes = bytes[3];
			v = ArrayUtils.addAll(
					ArrayUtils.addAll(v, zBytes), secretBytes
			);
		}

		return mac.doFinal(v);
	}

	private byte[] fillArray(byte b, byte[] bytes) {
		Arrays.fill(bytes, b);
		return bytes;
	}
}

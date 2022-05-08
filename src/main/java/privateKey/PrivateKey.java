package privateKey;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jcajce.provider.digest.Keccak;
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

	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final String KECCAK = "Keccak-256";

	private final BigInteger secret;
	private final S256Point publicPoint;

	public PrivateKey(BigInteger secret) {
		this.secret = secret;
		this.publicPoint = G.multiply(this.secret);
	}

	public Signature sign(BigInteger z) throws NoSuchAlgorithmException, InvalidKeyException {
		BigInteger k = deterministicK(z, KECCAK);
		BigInteger r = G.multiply(k).getFieldElementX().getNum();
		BigInteger kInv = k.modPow(N.subtract(BigInteger.valueOf(2)), N);
		BigInteger s = (r.multiply(this.secret).add(z)).multiply(kInv.mod(N));
		if (s.compareTo(N.divide(BigInteger.valueOf(2))) > 0) {
			s = N.subtract(s);
		}
		return new Signature(r, s);
	}

	private BigInteger deterministicK(BigInteger z, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] k = fillArray((byte) 0, new byte[32]);
		byte[] v = fillArray((byte) 1, new byte[32]);
		if (z.compareTo(N) > 0) {
			z = z.subtract(N);
		}
		byte[] zBytes = z.toByteArray();
		byte[] secretBytes = this.secret.toByteArray();
		k = hmac(algorithm, k, ArrayUtils.add(v, (byte) 0), zBytes, secretBytes);
		v = hmac(algorithm, k, v);
		k = hmac(algorithm, k, ArrayUtils.add(v, (byte) 1), zBytes, secretBytes);
		v = hmac(algorithm, k, v);

		while (true) {
			v = hmac(algorithm, k, v);
			BigInteger candidate = new BigInteger(v);
			if (candidate.compareTo(BigInteger.ONE) >= 0 && candidate.compareTo(N) < 0) {
				return candidate;
			}
			k = hmac(algorithm, k, ArrayUtils.add(v, (byte) 0), zBytes, secretBytes);
			v = hmac(algorithm, k, v);
		}
	}

	private byte[] hmac(String algorithm, byte[] ...bytes) throws NoSuchAlgorithmException, InvalidKeyException {
		byte [] v = bytes[1];
		if (bytes.length == 4) {
			byte [] zBytes = bytes[2];
			byte [] secretBytes = bytes[3];
			v = ArrayUtils.addAll(
					ArrayUtils.addAll(v, zBytes), secretBytes);
		}
		byte[] result;
		if (HMAC_SHA256.equals(algorithm)) {
			SecretKeySpec secretKeySpec = new SecretKeySpec(bytes[0], HMAC_SHA256);
			Mac mac = Mac.getInstance(HMAC_SHA256);
			mac.init(secretKeySpec);
			result = mac.doFinal(v);
		} else {
			Keccak.Digest256 digest256 = new Keccak.Digest256();
			result = digest256.digest(v);
		}
		return result;
	}

	private byte[] fillArray(byte b, byte[] bytes) {
		Arrays.fill(bytes, b);
		return bytes;
	}
}

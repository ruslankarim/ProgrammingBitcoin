package point;

import fieldElement.S256FieldElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import privateKey.PrivateKey;
import signature.Signature;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static point.S256Point.G;
import static point.S256Point.N;

public class S256PointTest {

	@Test
	public void testOrder() {
		S256Point point = G.multiply(N);
		Assertions.assertNull(point.getFieldElementX());
	}

	@Test
	public void testPubPoint() {
		List<BigInteger> secrets = Arrays.asList(
				BigInteger.valueOf(7),
				BigInteger.valueOf(1485),
				BigInteger.valueOf(2).pow(128),
				BigInteger.valueOf(2).pow(240).add(BigInteger.valueOf(2).pow(31))
		);

		List<S256Point> points = Arrays.asList(
				new S256Point(
						new S256FieldElement(new BigInteger("5cbdf0646e5db4eaa398f365f2ea7a0e3d419b7e0330e39ce92bddedcac4f9bc", 16)),
						new S256FieldElement(new BigInteger("6aebca40ba255960a3178d6d861a54dba813d0b813fde7b5a5082628087264da", 16))
				),
				new S256Point(
						new S256FieldElement(new BigInteger("c982196a7466fbbbb0e27a940b6af926c1a74d5ad07128c82824a11b5398afda", 16)),
						new S256FieldElement(new BigInteger("7a91f9eae64438afb9ce6448a1c133db2d8fb9254e4546b6f001637d50901f55", 16))
				),
				new S256Point(
						new S256FieldElement(new BigInteger("8f68b9d2f63b5f339239c1ad981f162ee88c5678723ea3351b7b444c9ec4c0da", 16)),
						new S256FieldElement(new BigInteger("662a9f2dba063986de1d90c2b6be215dbbea2cfe95510bfdf23cbf79501fff82", 16))
				),
				new S256Point(
						new S256FieldElement(new BigInteger("9577ff57c8234558f293df502ca4f09cbc65a6572c842b39b366f21717945116", 16)),
						new S256FieldElement(new BigInteger("10b49c67fa9365ad7b90dab070be339a1daf9052373ec30ffae4f72d5e66d053", 16))
				)
		);

		IntStream.range(0, secrets.size()).forEach(i -> Assertions.assertEquals(G.multiply(secrets.get(i)), points.get(i)));
	}

	@Test
	public void verifyTest() {
		S256Point point = new S256Point(
				new S256FieldElement(new BigInteger("887387e452b8eacc4acfde10d9aaf7f6d9a0f975aabb10d006e4da568744d06c", 16)),
				new S256FieldElement(new BigInteger("61de6d95231cd89026e286df3b6ae4a894a3378e393e93a0f45b666329a0ae34", 16))
		);
		BigInteger z = new BigInteger("ec208baa0fc1c19f708a9ca96fdeff3ac3f230bb4a7ba4aede4942ad003c0f60", 16);
		BigInteger r = new BigInteger("ac8d1c87e51d0d441be8b3dd5b05c8795b48875dffe00b7ffcfac23010d3a395", 16);
		BigInteger s = new BigInteger("68342ceff8935ededd102dd876ffd6ba72d6a427a3edb13d26eb0781cb423c4", 16);

		Assertions.assertTrue(point.verify(z, new Signature(r, s)));

		z = new BigInteger("7c076ff316692a3d7eb3c3bb0f8b1488cf72e1afcd929e29307032997a838a3d", 16);
		r = new BigInteger("eff69ef2b1bd93a66ed5219add4fb51e11a840f404876325a1e8ffe0529a2c", 16);
		s = new BigInteger("c7207fee197d27c618aea621406f6bf5ef6fca38681d82b2f06fddbdce6feab6", 16);

		Assertions.assertTrue(point.verify(z, new Signature(r, s)));

	}

	@Test
	public void secTest() {
		PrivateKey privateKey = new PrivateKey(BigInteger.valueOf(5000));
		byte[] bytes = privateKey.getPublicPoint().sec(true);
		System.out.println();
	}

	@Test
	public void parseTest() {
		PrivateKey privateKey = new PrivateKey(BigInteger.valueOf(5000));
		byte[] bytes = privateKey.getPublicPoint().sec(true);
		S256Point point = S256Point.parse(bytes);
		point.getS256FieldElementX().sqrt();
		System.out.println(point);

	}

}

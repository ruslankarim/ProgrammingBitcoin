package script;

import lombok.AllArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static utils.Utils.convertToArrIntUnsigned;
import static utils.Utils.littleEndianToInt;
import static utils.Utils.readVarInt;

@AllArgsConstructor
public class Script {

	private final List<BigInteger[]> commands;

	public static Script parse(ByteArrayInputStream stream) throws IOException {
		BigInteger len = readVarInt(stream);
		BigInteger count = BigInteger.ZERO;
		List<BigInteger[]> commands = new ArrayList<>();
		while (count.compareTo(len) < 0) {
			BigInteger[] cur = convertToArrIntUnsigned(stream.readNBytes(1));
			count = count.add(BigInteger.ONE);
			BigInteger curInt = cur[0];
			if (curInt.compareTo(BigInteger.ONE)>= 1 && curInt.compareTo(BigInteger.valueOf(75)) <= 0) {
				BigInteger n = curInt;
				commands.add(convertToArrIntUnsigned(stream.readNBytes(n.intValue())));
				count = count.add(n);
			} else if (curInt.compareTo(BigInteger.valueOf(76)) == 0) {
				BigInteger dataLength = littleEndianToInt(stream.readNBytes(1));
				commands.add(convertToArrIntUnsigned(stream.readNBytes(dataLength.intValue())));
				count = count.add(dataLength);
			} else if (curInt.compareTo(BigInteger.valueOf(77)) == 0) {
				BigInteger dataLength = littleEndianToInt(stream.readNBytes(2));
				commands.add(convertToArrIntUnsigned(stream.readNBytes(dataLength.intValue())));
				count = count.add(dataLength.add(BigInteger.TWO));
			} else {
				commands.add(new BigInteger[]{curInt});
			}
		}
		if (count.compareTo(len) != 0) {
			throw new RuntimeException("parsing script failed");
		}
		return new Script(commands);
	}

}

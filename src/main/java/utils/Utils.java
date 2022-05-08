package utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Utils {

	public static BigInteger readVarInt(ByteArrayInputStream stream) throws IOException {
		int i = stream.readNBytes(1)[0] & 0xff;
		if (i == 0xfd) {
			return littleEndianToInt(stream.readNBytes(2));
		} else if (i == 0xfe) {
			return littleEndianToInt(stream.readNBytes(4));
		} else if (i == 0xff) {
			return littleEndianToInt(stream.readNBytes(8));
		} else {
			return BigInteger.valueOf(i);
		}
	}

	public static byte[] encodeVarInt(int n) {
		if (n < 0xfd) {
			return new byte[]{(byte) n};
		}
		intToLittleEndian(n, 2);
		//not implemented
		return null;
	}

	private static byte[] intToLittleEndian(int n, int len) {
		return ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN).putInt(n).array();
	}

	public static BigInteger littleEndianToInt(byte[] bytes) {
		for (int i = 0; i < bytes.length / 2; i++) {
			byte temp = bytes[i];
			bytes[i] = bytes[bytes.length - i - 1];
			bytes[bytes.length - i - 1] = temp;
		}
		return new BigInteger(1, bytes);
	}

	public static BigInteger[] convertToArrIntUnsigned(byte[] bytes) {
		BigInteger [] ints = new BigInteger[bytes.length];
		for (int i = 0; i < bytes.length; i++) {

			ints[i] = BigInteger.valueOf(bytes[i] & 0xFF);
		}
		return ints;
	}

	public static int convertToIntUnsigned(byte b) {
		return b & 0xFF;
	}

}

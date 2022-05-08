package Tx;

import utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Tx {

	public static Tx parse(ByteArrayInputStream stream) throws IOException {

		BigInteger version = Utils.littleEndianToInt(stream.readNBytes(4));
		BigInteger numInputs = Utils.readVarInt(stream);
		TxIn [] inputs = new TxIn[numInputs.intValue()];
		for (int i = 0; i < numInputs.intValue(); i++) {
			inputs[i] = TxIn.parse(stream);
		}
		System.out.println("");
		return null;

	}

}

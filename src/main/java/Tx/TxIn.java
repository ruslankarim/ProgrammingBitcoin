package Tx;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import script.Script;
import utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;

@AllArgsConstructor
public class TxIn {

	private BigInteger [] prevTx;
	private BigInteger prevIndex;
	private Script script;
	private BigInteger sequence;


	public static TxIn parse(ByteArrayInputStream stream) throws IOException {
		BigInteger [] prevTx = Utils.convertToArrIntUnsigned(stream.readNBytes(32));
		ArrayUtils.reverse(prevTx);
		BigInteger prevIndex = Utils.littleEndianToInt(stream.readNBytes(4));
		Script script = Script.parse(stream);
		BigInteger sequence = Utils.littleEndianToInt(stream.readNBytes(4));
		return new TxIn(prevTx, prevIndex, script, sequence);
	}
}

package com.walker.core.encode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * base64编码解码工具
 *
 */
public class Base64 {
	private static final Base64Encoder encoder = new Base64Encoder();

	public static byte[] encode(byte[] data) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			encoder.encode(data, 0, data.length, bOut);
		} catch (IOException arg2) {
			throw new RuntimeException("exception encoding base64 string: " + arg2);
		}
		return bOut.toByteArray();
	}

	public static int encode(byte[] data, OutputStream out) throws IOException {
		return encoder.encode(data, 0, data.length, out);
	}

	public static int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
		return encoder.encode(data, off, length, out);
	}

	public static byte[] decode(byte[] data) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			encoder.decode(data, 0, data.length, bOut);
		} catch (IOException arg2) {
			throw new RuntimeException("exception decoding base64 string: " + arg2);
		}
		return bOut.toByteArray();
	}

	public static byte[] decode(String data) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			encoder.decode(data, bOut);
		} catch (IOException arg2) {
			throw new RuntimeException("exception decoding base64 string: " + arg2);
		}
		return bOut.toByteArray();
	}

	public static int decode(String data, OutputStream out) throws IOException {
		return encoder.decode(data, out);
	}
}

class Base64Encoder {
	protected final byte[] encodingTable = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
			81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110,
			111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43,
			47 };
	protected byte padding = 61;
	protected final byte[] decodingTable = new byte[128];

	protected void initialiseDecodingTable() {
		for (int i = 0; i < this.encodingTable.length; ++i) {
			this.decodingTable[this.encodingTable[i]] = (byte) i;
		}
	}

	public Base64Encoder() {
		this.initialiseDecodingTable();
	}

	public int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
		int modulus = length % 3;
		int dataLength = length - modulus;
		int b1;
		for (b1 = off; b1 < off + dataLength; b1 += 3) {
			int a1 = data[b1] & 255;
			int a2 = data[b1 + 1] & 255;
			int a3 = data[b1 + 2] & 255;
			out.write(this.encodingTable[a1 >>> 2 & 63]);
			out.write(this.encodingTable[(a1 << 4 | a2 >>> 4) & 63]);
			out.write(this.encodingTable[(a2 << 2 | a3 >>> 6) & 63]);
			out.write(this.encodingTable[a3 & 63]);
		}
		int b2;
		int d1;
		switch (modulus) {
		case 0:
		default:
			break;
		case 1:
			d1 = data[off + dataLength] & 255;
			b1 = d1 >>> 2 & 63;
			b2 = d1 << 4 & 63;
			out.write(this.encodingTable[b1]);
			out.write(this.encodingTable[b2]);
			out.write(this.padding);
			out.write(this.padding);
			break;
		case 2:
			d1 = data[off + dataLength] & 255;
			int d2 = data[off + dataLength + 1] & 255;
			b1 = d1 >>> 2 & 63;
			b2 = (d1 << 4 | d2 >>> 4) & 63;
			int b3 = d2 << 2 & 63;
			out.write(this.encodingTable[b1]);
			out.write(this.encodingTable[b2]);
			out.write(this.encodingTable[b3]);
			out.write(this.padding);
		}
		return dataLength / 3 * 4 + (modulus == 0 ? 0 : 4);
	}

	private boolean ignore(char c) {
		return c == 10 || c == 13 || c == 9 || c == 32;
	}

	public int decode(byte[] data, int off, int length, OutputStream out) throws IOException {
		int outLen = 0;
		int end;
		for (end = off + length; end > off && this.ignore((char) data[end - 1]); --end) {
			;
		}
		int finish = end - 4;
		for (int i = this.nextI(data, off, finish); i < finish; i = this.nextI(data, i, finish)) {
			byte b1 = this.decodingTable[data[i++]];
			i = this.nextI(data, i, finish);
			byte b2 = this.decodingTable[data[i++]];
			i = this.nextI(data, i, finish);
			byte b3 = this.decodingTable[data[i++]];
			i = this.nextI(data, i, finish);
			byte b4 = this.decodingTable[data[i++]];
			out.write(b1 << 2 | b2 >> 4);
			out.write(b2 << 4 | b3 >> 2);
			out.write(b3 << 6 | b4);
			outLen += 3;
		}
		outLen += this.decodeLastBlock(out, (char) data[end - 4], (char) data[end - 3], (char) data[end - 2],
				(char) data[end - 1]);
		return outLen;
	}

	private int nextI(byte[] data, int i, int finish) {
		while (i < finish && this.ignore((char) data[i])) {
			++i;
		}
		return i;
	}

	public int decode(String data, OutputStream out) throws IOException {
		int length = 0;
		int end;
		for (end = data.length(); end > 0 && this.ignore(data.charAt(end - 1)); --end) {
			;
		}
		byte i = 0;
		int finish = end - 4;
		for (int arg10 = this.nextI((String) data, i, finish); arg10 < finish; arg10 = this.nextI(data, arg10,
				finish)) {
			byte b1 = this.decodingTable[data.charAt(arg10++)];
			arg10 = this.nextI(data, arg10, finish);
			byte b2 = this.decodingTable[data.charAt(arg10++)];
			arg10 = this.nextI(data, arg10, finish);
			byte b3 = this.decodingTable[data.charAt(arg10++)];
			arg10 = this.nextI(data, arg10, finish);
			byte b4 = this.decodingTable[data.charAt(arg10++)];
			out.write(b1 << 2 | b2 >> 4);
			out.write(b2 << 4 | b3 >> 2);
			out.write(b3 << 6 | b4);
			length += 3;
		}
		length += this.decodeLastBlock(out, data.charAt(end - 4), data.charAt(end - 3), data.charAt(end - 2),
				data.charAt(end - 1));
		return length;
	}

	private int decodeLastBlock(OutputStream out, char c1, char c2, char c3, char c4) throws IOException {
		byte b1;
		byte b2;
		if (c3 == this.padding) {
			b1 = this.decodingTable[c1];
			b2 = this.decodingTable[c2];
			out.write(b1 << 2 | b2 >> 4);
			return 1;
		} else {
			byte b3;
			if (c4 == this.padding) {
				b1 = this.decodingTable[c1];
				b2 = this.decodingTable[c2];
				b3 = this.decodingTable[c3];
				out.write(b1 << 2 | b2 >> 4);
				out.write(b2 << 4 | b3 >> 2);
				return 2;
			} else {
				b1 = this.decodingTable[c1];
				b2 = this.decodingTable[c2];
				b3 = this.decodingTable[c3];
				byte b4 = this.decodingTable[c4];
				out.write(b1 << 2 | b2 >> 4);
				out.write(b2 << 4 | b3 >> 2);
				out.write(b3 << 6 | b4);
				return 3;
			}
		}
	}

	private int nextI(String data, int i, int finish) {
		while (i < finish && this.ignore(data.charAt(i))) {
			++i;
		}
		return i;
	}
}
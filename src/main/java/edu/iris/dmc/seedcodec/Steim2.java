package edu.iris.dmc.seedcodec;

//import edu.iris.Fissures.seed.util.*;

/**
 *  Class for decoding or encoding Steim2-compressed data blocks
 *  to or from an array of integer values.
 * <p>
 * Steim compression scheme Copyrighted by Dr. Joseph Steim.<p>
 * <dl>
 * <dt>Reference material found in:</dt>
 * <dd>
 * Appendix B of SEED Reference Manual, 2nd Ed., pp. 119-125
 * <i>Federation of Digital Seismic Networks, et al.</i>
 * February, 1993
 * </dd>
 * <dt>Coding concepts gleaned from code written by:</dt>
 * <dd>Guy Stewart, IRIS, 1991</dd>
 * <dd>Tom McSweeney, IRIS, 2000</dd>
 * </dl>
 *
 * @author Philip Crotwell (U South Carolina)
 * @author Robert Casey (IRIS DMC)
 * @version 10/23/2002
 */

public class Steim2 {

	/**
	 *  Decode the indicated number of samples from the provided byte array and
	 *  return an integer array of the decompressed values.  Being differencing
	 *  compression, there may be an offset carried over from a previous data
	 *  record.  This offset value can be placed in <b>bias</b>, otherwise leave
	 *  the value as 0.
	 *  @param b input byte array to be decoded
	 *  @param numSamples the number of samples that can be decoded from array
	 *  <b>b</b>
	 *  @param swapBytes if true, swap reverse the endian-ness of the elements of
	 *  byte array <b>b</b>.
	 *  @param bias the first difference value will be computed from this value.
	 *  If set to 0, the method will attempt to use the X(0) constant instead.
	 *  @return int array of length <b>numSamples</b>.
	 *  @throws SteimException - encoded data length is not multiple of 64
	 *  bytes.
	 */
	public static int[] decode(byte[] b, int numSamples, boolean swapBytes, int bias) throws SteimException {
		if (b.length % 64 != 0) {
			throw new SteimException("encoded data length is not multiple of 64 bytes (" + b.length + ")"); 
		}
		int[] samples = new int[numSamples];
		int[] tempSamples;
		int numFrames = b.length / 64;
		int current = 0;
		int start=0, end=0;
		int firstData=0;
		int lastValue = 0;
        
		//System.err.println("DEBUG: number of samples: " + numSamples + ", number of frames: " + numFrames + ", byte array size: " + b.length);
		for (int i=0; i< numFrames; i++ ) {
			//System.err.println("DEBUG: start of frame " + i);
			tempSamples = extractSamples(b, i*64, swapBytes);   // returns only differences except for frame 0
			firstData = 0; // d(0) is byte 0 by default
			if (i==0) {   // special case for first frame
				lastValue = bias; // assign our X(-1)
				// x0 and xn are in 1 and 2 spots
				start = tempSamples[1];  // X(0) is byte 1 for frame 0
				end = tempSamples[2];    // X(n) is byte 2 for frame 0
				firstData = 3; // d(0) is byte 3 for frame 0
				//System.err.println("DEBUG: frame " + i + ", bias = " + bias + ", x(0) = " + start + ", x(n) = " + end);
				// if bias was zero, then we want the first sample to be X(0) constant
				if (bias == 0) lastValue = start - tempSamples[3];  // X(-1) = X(0) - d(0)
			}
			//System.err.print("DEBUG: ");
			for (int j = firstData; j < tempSamples.length && current < numSamples; j++) {
				samples[current] = lastValue + tempSamples[j];  // X(n) = X(n-1) + d(n)
				lastValue = samples[current];
				//System.err.print("d(" + (j-firstData) + ")" + tempSamples[j] + ", x(" + current + ")" + samples[current] + ";");
				current++;
			}
			//System.err.println("DEBUG: end of frame " + i);
		}  // end for each frame...
        if (current != numSamples) {
            throw new SteimException("Number of samples decompressed doesn't match number in header: "+current+" != "+numSamples);
        }
		return samples;
	}

	/**
	 * Abbreviated, zero-bias version of decode().
	 *
	 * @see edu.iris.Fissures.codec.Steim2#decode(byte[],int,boolean,int)
	 */
	public static int[] decode(byte[] b, int numSamples, boolean swapBytes) throws SteimException {
		// zero-bias version of decode
		return decode(b,numSamples,swapBytes,0);
	}

	/**
	 * Extracts differences from the next 64 byte frame of the given compressed
	 * byte array (starting at offset) and returns those differences in an int
	 * array.
	 * An offset of 0 means that we are at the first frame, so include the header
	 * bytes in the returned int array...else, do not include the header bytes
	 * in the returned array.
	 * @param bytes byte array of compressed data differences
	 * @param offset index to begin reading compressed bytes for decoding
	 * @param swapBytes reverse the endian-ness of the compressed bytes being read
	 * @return integer array of difference (and constant) values
	 */
	protected static int[] extractSamples(byte[] bytes,
			int offset, 
			boolean swapBytes) 
	{
		/* get nibbles */
		int nibbles = Utility.bytesToInt(bytes[offset], 
				bytes[offset+1], 
				bytes[offset+2], 
				bytes[offset+3], 
				swapBytes);
		int currNibble = 0;
		int dnib = 0;
		int[] temp = new int[106]; // 7 samples * 15 long words + 1 nibble int
		int tempInt;
		int currNum = 0;
		for (int i=0; i<16; i++) {
			currNibble = (nibbles >> (30 - i*2 ) ) & 0x03;
			switch (currNibble) {
				case 0:
					//System.out.println("0 means header info");
					// only include header info if offset is 0
					if (offset == 0) {
						temp[currNum++] = Utility.bytesToInt(bytes[offset+(i*4)],
								bytes[offset+(i*4)+1],
								bytes[offset+(i*4)+2],
								bytes[offset+(i*4)+3],
								swapBytes);
					}
					break;
				case 1:
					//System.out.println("1 means 4 one byte differences");
					temp[currNum++] = Utility.bytesToInt(bytes[offset+(i*4)]);
					temp[currNum++] = Utility.bytesToInt(bytes[offset+(i*4)+1]);
					temp[currNum++] = Utility.bytesToInt(bytes[offset+(i*4)+2]);
					temp[currNum++] = Utility.bytesToInt(bytes[offset+(i*4)+3]);
					break;
				case 2:
					tempInt = Utility.bytesToInt(bytes[offset+(i*4)], 
							bytes[offset+(i*4)+1],
							bytes[offset+(i*4)+2], 
							bytes[offset+(i*4)+3], 
							swapBytes);
					dnib = (tempInt >> 30) & 0x03;
					switch (dnib) {
						case 1:
							//System.out.println("2,1 means 1 thirty bit difference");
							temp[currNum++] = (tempInt << 2) >> 2;
							break;
						case 2:
							//System.out.println("2,2 means 2 fifteen bit differences");
							temp[currNum++] = (tempInt << 2) >> 17;  // d0
							temp[currNum++] = (tempInt << 17) >> 17; // d1
							break;
						case 3:
							//System.out.println("2,3 means 3 ten bit differences");
							temp[currNum++] = (tempInt << 2) >> 22;  // d0
							temp[currNum++] = (tempInt << 12) >> 22; // d1
							temp[currNum++] = (tempInt << 22) >> 22; // d2
							break;
						default:
							//System.out.println("default");
					}
					break;
				case 3:
					tempInt = Utility.bytesToInt(bytes[offset+(i*4)], 
							bytes[offset+(i*4)+1],
							bytes[offset+(i*4)+2], 
							bytes[offset+(i*4)+3],
							swapBytes);
					dnib = (tempInt >> 30) & 0x03;
					// for case 3, we are going to use a for-loop formulation that
					// accomplishes the same thing as case 2, just less verbose.
					int diffCount = 0;  // number of differences
					int bitSize = 0;    // bit size
					int headerSize = 0; // number of header/unused bits at top
					switch (dnib) {
						case 0:
							//System.out.println("3,0 means 5 six bit differences");
							headerSize = 2;
							diffCount = 5;
							bitSize = 6;
							break;
						case 1:
							//System.out.println("3,1 means 6 five bit differences");
							headerSize = 2;
							diffCount = 6;
							bitSize = 5;
							break;
						case 2:
							//System.out.println("3,2 means 7 four bit differences, with 2 unused bits");
							headerSize = 4;
							diffCount = 7;
							bitSize = 4;
							break;
						default:
							//System.out.println("default");
					}
					if (diffCount > 0) {
						for (int d=0; d<diffCount; d++) {  // for-loop formulation
							temp[currNum++] = ( tempInt << (headerSize+(d*bitSize)) ) >> (((diffCount-1)*bitSize) + headerSize);
						}
					}
			}
		}
		int[] out = new int[currNum];
		System.arraycopy(temp, 0, out, 0, currNum);
		return out;
	}

	/**
	 * Static method for testing the decode() method.
	 * @param args not used
	 * @throws SteimException from called method(s)
	 */
	public static void main(String[] args) throws SteimException {
		byte[] b = new byte[64];
		int[] temp;

		for (int i=0; i< 64 ; i++) {
			b[i] = 0x00;
		}
		b[0] = 0x01;
		b[1] = (byte)0xb0;
		System.out.println(b[1]);
		b[2] = (byte)0xff;
		b[3] = (byte)0xff;

		b[4] = 0;
		b[5] = 0;
		b[6] = 0;
		b[7] = 0;

		b[8] = 0;
		b[9] = 0;
		b[10] = 0;
		b[11] = 0;

		b[12] = 1;
		b[13] = 2;
		b[14] = 3;
		b[15] = 0;

		b[16] = 1;
		b[17] = 1;
		b[18] = 0;
		b[19] = 0;

		b[20] = 0;
		b[21] = 1;
		b[22] = 0;
		b[23] = 0;
		temp = Steim2.decode(b, 17, false);
	}

}

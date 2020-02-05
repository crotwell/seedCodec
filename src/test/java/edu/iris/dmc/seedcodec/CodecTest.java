package edu.iris.dmc.seedcodec;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class CodecTest {

    @Test
    public void testShort() throws UnsupportedCompressionType, CodecException {
        Codec codec = new Codec();
        short[] data = new short[] {1,
                                    -1,
                                    2,
                                    -2,
                                    1230,
                                    -5324,
                                    Short.MAX_VALUE - 1,
                                    Short.MIN_VALUE + 1,
                                    Short.MAX_VALUE,
                                    Short.MIN_VALUE};
        short[] roundTrip = codec.decompress(1, codec.encodeAsBytes(data), data.length, false).getAsShort();
        assertArrayEquals(data, roundTrip);
    }

    @Test
    public void testInt() throws UnsupportedCompressionType, CodecException {
        Codec codec = new Codec();
        int[] data = new int[] {1,
                                -1,
                                2,
                                -2,
                                1230,
                                -5324,
                                Short.MAX_VALUE - 1,
                                Short.MIN_VALUE + 1,
                                Short.MAX_VALUE,
                                Short.MIN_VALUE,
                                Integer.MAX_VALUE - 1,
                                Integer.MIN_VALUE + 1,
                                Integer.MAX_VALUE,
                                Integer.MIN_VALUE};
        int[] roundTrip = codec.decompress(3, codec.encodeAsBytes(data), data.length, false).getAsInt();
        assertArrayEquals(data, roundTrip);
    }

    @Test
    public void testFloat() throws UnsupportedCompressionType, CodecException {
        Codec codec = new Codec();
        float[] data = new float[] {0,
                                    1.1f,
                                -1.2f,
                                2,
                                -2,
                                1230.543f,
                                -5324.8294f,
                                Short.MAX_VALUE - 1,
                                Short.MIN_VALUE + 1,
                                Short.MAX_VALUE,
                                Short.MIN_VALUE,
                                Integer.MAX_VALUE - 1,
                                Integer.MIN_VALUE + 1,
                                Integer.MAX_VALUE,
                                Integer.MIN_VALUE};
        float[] roundTrip = codec.decompress(4, codec.encodeAsBytes(data), data.length, false).getAsFloat();
        assertArrayEquals(data, roundTrip, 0.000001f);
    }

    @Test
    public void testDouble() throws UnsupportedCompressionType, CodecException {
        Codec codec = new Codec();
        double[] data = new double[] {0,
                                    1.1,
                                -1.2,
                                2,
                                -2,
                                1230.543,
                                -5324.8294,
                                Short.MAX_VALUE - 1,
                                Short.MIN_VALUE + 1,
                                Short.MAX_VALUE,
                                Short.MIN_VALUE,
                                Integer.MAX_VALUE - 1,
                                Integer.MIN_VALUE + 1,
                                Integer.MAX_VALUE,
                                Integer.MIN_VALUE};
        double[] roundTrip = codec.decompress(5, codec.encodeAsBytes(data), data.length, false).getAsDouble();
        assertArrayEquals(data, roundTrip, 0.00000001);
    }
}

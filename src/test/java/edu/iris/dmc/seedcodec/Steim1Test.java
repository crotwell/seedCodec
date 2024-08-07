package edu.iris.dmc.seedcodec;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;


public class Steim1Test {

    @Test
    public void testEncodeSingleFrame() throws SteimException {
        int[] data = new int[52]; // one word for nibbles, one for first sample, one for last sample, all differences stored so redundant info means max 52 samples in first block
        Steim1 steim1 = new Steim1();
        SteimFrameBlock sfb = Steim1.encode(data, 1);
        assertEquals(data.length, sfb.getNumSamples(), "num samples");
    }

    @Test
    public void testRoundTrip() throws SteimException, IOException {
        int i=0;
        int[] data = new int[200];
        for (int j = 0; j < data.length; j++) {
            data[j] = i;
            int diff = 0;
            if (j%50 == 0) {
                diff = 33000; // make sure bigger than 2 bytes
            } else if (j % 10 == 0) {
                diff = 150; // bigger than one byte
            } else {
                diff = j;
            }
            i += ( j % 2 ) * -1 * diff;
        }
        SteimFrameBlock sfb = Steim1.encode(data, 5);
        assertEquals(data.length, sfb.getNumSamples(), "num encoded");
        int[] out = Steim1.decode(sfb.getEncodedData(), data.length, false);
        assertArrayEquals(data, out);
    }

    @Test
    public void testRoundTripOffset() throws SteimException, IOException {
        int i=0;
        int[] data = new int[200];
        int offset = 123;
        int[] offsetData = new int[data.length+offset];

        for (int j = 0; j < offset; j++) {
            int diff = 0;
            if (j%50 == 0) {
                diff = 33000; // make sure bigger than 2 bytes
            } else if (j % 10 == 0) {
                diff = 150; // bigger than one byte
            } else {
                diff = j;
            }
            i += ( j % 2 ) * -1 * diff;
            offsetData[j] = i;
        }
        for (int j = 0; j < data.length; j++) {
            int diff = 0;
            if (j%50 == 0) {
                diff = 33000; // make sure bigger than 2 bytes
            } else if (j % 10 == 0) {
                diff = 150; // bigger than one byte
            } else {
                diff = j;
            }
            if (j%2 == 0) {diff *= -1;}
            i += diff;
            data[j] = i;
        }
        System.arraycopy(data, 0, offsetData, offset, data.length);
        // data fits in 6 frames
        SteimFrameBlock sfb = Steim1.encode(offsetData, 6, 0, offset);
        assertEquals(data.length, sfb.getNumSamples(), "num encoded");
        int[] out = Steim1.decode(sfb.getEncodedData(), data.length, false);
        assertArrayEquals(data, out);
    }


    @Test
    public void testBigRoundTrip() throws SteimException, IOException {
        int i=0;
        int[] data = new int[10000];
        for (int j = 0; j < data.length; j++) {
            data[j] = i;
            int diff = 0;
            if (j%49 == 0) {
                diff = 33000; // make sure bigger than 2 bytes
            } else if (j % 9 == 0) {
                diff = 150; // bigger than one byte
            } else {
                diff = j;
            }
            if (j%2 == 0) {diff *= -1;}
            i += diff;
        }
        SteimFrameBlock sfb = Steim1.encode(data, 0);
        assertEquals(data.length, sfb.getNumSamples(), "num encoded");
        assertTrue(sfb.getNumFrames()> 63, sfb.getNumFrames()+" <= 63");
        int[] out = Steim1.decode(sfb.getEncodedData(), data.length, false);
        assertArrayEquals(data, out);
    }
}

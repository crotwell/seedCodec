package edu.iris.dmc.seedcodec;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;


public class Steim2Test {

    
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
        SteimFrameBlock sfb = Steim2.encode(data, 5);
        assertEquals("num encoded",  data.length, sfb.getNumSamples());
        int[] out = Steim2.decode(sfb.getEncodedData(), data.length, false);
        assertArrayEquals(data, out);
    }
}

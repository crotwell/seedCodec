package edu.iris.dmc.seedcodec;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;



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
        assertEquals(data.length, sfb.getNumSamples(), "num encoded");
        int[] out = Steim2.decode(sfb.getEncodedData(), data.length, false);
        assertArrayEquals(data, out);
    }


    @Test
    public void testRoundTripTwo() throws SteimException, IOException {

        int[] data = new int[512];
        // make some fake data, use sqrt so more data will be "small"
        for (int i = 0; i < data.length; i++) {
            data[i] = (int)(Math.round(Math.sqrt(Math.random())*2000)) * (Math.random() > 0.5? 1 : -1);
        }
        SteimFrameBlock sfb = Steim2.encode(data, 63);
        assertEquals(data.length, sfb.getNumSamples(), "num encoded");
        int[] out = Steim2.decode(sfb.getEncodedData(), data.length, false);
        assertArrayEquals(data, out);
    }
}

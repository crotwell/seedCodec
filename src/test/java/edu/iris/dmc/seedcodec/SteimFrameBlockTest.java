package edu.iris.dmc.seedcodec;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SteimFrameBlockTest {

    @Test
    public void testFillFrame() throws SteimException {
        int zeroWord = 0;
        int onesWord = (1 & 0xff) << 24;    // clip to 8 bits, then shift
        onesWord |= (1 & 0xff) << 16;
        onesWord |= (1 & 0xff) << 8;
        onesWord |= (1 & 0xff);
        int nibble = 1;  // size 1 = 01, one byte per sample
        int maxNumFrames = 1;
        SteimFrameBlock sfb = new SteimFrameBlock(maxNumFrames, 1);
        // first,last values special
        sfb.addEncodedWord(zeroWord, 0, 0);
        sfb.addEncodedWord(zeroWord, 0, 0);
        // steim1 frame holds 60 values in 15 words, but first frame holds only 13 words
        int maxSamps = 13*4 +(maxNumFrames-1)*15*4;
        int maxWords = maxNumFrames*15-2; //
        for (int i = 0; i < maxWords-1; i++) {
            boolean isFull = sfb.addEncodedWord(onesWord, 4, nibble);
            assertFalse(isFull);
        }
        System.err.println("added "+maxWords+" to sfb frames: "+sfb.getNumFrames()+" samp: "+sfb.getNumSamples());
        boolean isFull = sfb.addEncodedWord(onesWord, 4, nibble);
        assertTrue(isFull);
        assertEquals(maxSamps, sfb.getNumSamples());
        assertEquals(maxNumFrames, sfb.getNumFrames());
        System.err.println("is full: "+(sfb.currentSteimFrame == null));
    }
}

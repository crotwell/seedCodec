package edu.iris.dmc.seedcodec;

import static org.junit.Assert.*;

import org.junit.Test;


public class UtilityTest {

    @Test
    public void testBytesToIntByte() {
        assertEquals(100, Utility.bytesToInt((byte)100));
    }

    @Test
    public void testBytesToIntByteByteBoolean() {
        byte a = (byte)2;
        byte b = (byte)114;
        assertEquals(626, Utility.bytesToInt(a, b, false));
        assertEquals(626, Utility.bytesToInt(b, a, true));
    }

    @Test
    public void testBytesToIntByteByteByteBoolean() {
        byte a = (byte)0;
        byte b = (byte)2;
        byte c = (byte)114;
        int ans = 626;
        assertEquals(ans, Utility.bytesToInt(a, b, c, false));
        assertEquals(ans, Utility.bytesToInt(c, b, a, true));
    }

    @Test
    public void testBytesToIntByteByteByteByteBoolean() {
        byte a = (byte)0;
        byte b = (byte)0;
        byte c = (byte)2;
        byte d = (byte)114;
        int ans = 626;
        assertEquals(ans, Utility.bytesToInt(a, b, c, d, false));
        assertEquals(ans, Utility.bytesToInt(d, c, b, a, true));
    }
}

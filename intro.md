# Introduction #

SeedCodec is meant to be a small, focused project on just seismic
compression and decompression routines in java.

[javadocs](http://wiki.seedcodec.googlecode.com/hg/javadoc/index.html)

# Example #

To Decompress use the Codec.decompress method. For example:

```
int compression = Codec.STEIM1;
byte[] values = ...
int num_points = ...
boolean byte_order = false;
DecompressedData decomp = codec.decompress(compression,
                                           values,
                                           num_points,
                                           byte_order);
```
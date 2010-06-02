seedCodec

Compression and Decompression

      This is a collection of compression and decompression routines
for standard seismic data formats in Java. The goal is to support all
the formats available within seed, but submissions from the broader
community are needed to accomplish this.

      The current supported formats are Steim1 (compression and decompression), steim2 (decompression only), DWSSN, CDSN, SRO and the simple float, int, 
double, short and 24 bit types.

      The distribution jar can be downloaded at
http://www.seis.sc.edu/downloads/seedCodec

To Decompress use the Codec.decompress method. For eample:
int compression = Codec.STEIM1;
byte[] values = ...
int num_points = ...
boolean byte_order = false;
DecompressedData decomp = codec.decompress(compression,
                                           values,
                                           num_points,
                                           byte_order);


seedCodec can be recompiled with the gradlew script, like
./gradlew jar
which will place a recompiled jar in the build/libs directory. See gradle.org
for more information.


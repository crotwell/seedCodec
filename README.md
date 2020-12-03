[![Maven Central](https://img.shields.io/maven-central/v/edu.sc.seis/seedCodec.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22edu.sc.seis%22%20AND%20a:%22seedCodec%22)

SeedCodec is a collection of compression and decompression routines for standard seismic data formats in Java. The goal is to support all the formats available within seed, but submissions from the broader community are needed to accomplish this.

The current supported formats are Steim1, Steim2 (compression and decompression), and decompression only for DWSSN, CDSN, SRO and the simple float, int, double, short and 24 bit types. These are only the decompression routines, and are independent of a particular file type. In particular, other than focusing on the data compression algorithms defined as defined in SEED, there is no direct connection to with SEED or miniSEED.
The [SeisFile](/crotwell/seisFile) project has code for dealing with actual miniseed files.

More information can be found at [http://www.seis.sc.edu/seedCodec.html].

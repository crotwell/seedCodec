package edu.iris.dmc.seedcodec;




/**
 * CodecException.java
 *
 *
 * Created: Fri Nov 22 15:31:06 2002
 *
 * @author <a href="mailto:crotwell@Philip-Crotwells-Computer.local.">Philip Crotwell</a>
 * @version
 */

public class CodecException extends Exception {
    public CodecException() {
	
    }
    public CodecException(String reason) {
	super(reason);
    }

    public CodecException(String reason, Exception e) {
	super(reason, e);
    }
    public CodecException( Exception e) {
	super(e);
    }
}// CodecException

package org.usfirst.frc.team1592.arch.tm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.function.DoubleSupplier;

/**
 * Simplot Telemetry Stream
 * <p>
 * Implements a telemetry stream for a Simplot HPL file.
 */
public final class SimplotStream extends FileStream {

	//===============================//
	//      Internal Components      //
	//===============================//

	private final ByteBuffer buffer;
	private final boolean isFloat;
	private final Charset charset;



	//=======================//
	//      Constructor      //
	//=======================//
	
	/**
	 * Standard Constructor
	 * <p>
	 * This version stores all data as floats (4 byte) for space and performance reasons.
	 *
	 * @param path  the file path for the Simplot output file
	 */
	public SimplotStream(Path path) {
		this(path, true);
	}

	/**
	 * Expanded Constructor
	 *
	 * @param path  the file path for the Simplot output file
	 * @param isFloat  logical specifying whether to save the data as floats (4 byte) or doubles (8 byte)
	 */
	public SimplotStream(Path path, boolean isFloat) {
		super(path);
		buffer = ByteBuffer.allocate(16384);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		this.isFloat = isFloat;
		charset = Charset.forName("UTF-8");
	}



	//=============================================//
	//      Package Private Methods (Stream)       //
	//=============================================//
	
	private double initTime = 0;
	
	@Override void initializeStream() throws IOException {
		super.initializeStream();
		SortedMap<String, DoubleSupplier> data = getUnmodifiableDataSupplierMap();
		if (data.get("Time")==null) {
			this.addDataSupplier("Time", ()->{return (System.currentTimeMillis()-initTime)/1000;});
		}
		buffer.putInt(data.size());
		buffer.putInt(0);
		buffer.putInt(isFloat ? 4 : 8);
		
		String info = "Generated Telemetry :: SimplotStream";
		byte[] infoBytes = info.getBytes(charset);
		List<byte[]> labelBytes = new ArrayList<>();
		int labelMax = 0;
		for (String s : data.keySet()) {
			byte[] bytes = s.getBytes(charset);
			labelBytes.add(bytes);
			labelMax = Math.max(labelMax, bytes.length);
		}
		
		buffer.putInt(labelMax);
		buffer.putInt(infoBytes.length);
		buffer.put(infoBytes);
		for (byte[] b : labelBytes) {
			buffer.put(b);
			if (b.length<labelMax) {
				buffer.position(buffer.position()+(labelMax-b.length));
			}
		}
		buffer.flip();
		if (getFileChannel()!=null) {
			getFileChannel().write(buffer);
		}
	}
	
	@Override void updateStream() throws IOException {
		buffer.clear();
		if (initTime==0) {
			initTime = System.currentTimeMillis();
		}
		SortedMap<String, DoubleSupplier> data = getUnmodifiableDataSupplierMap();
		for (DoubleSupplier v : data.values()) {
			if (isFloat) {
				buffer.putFloat((float) v.getAsDouble());
			} else {
				buffer.putDouble(v.getAsDouble());
			}
		}
		buffer.flip();
		if (getFileChannel()!=null) {
			getFileChannel().write(buffer);
		}
	}
	
	@Override void pauseStream() throws IOException {
		buffer.clear();
		SortedMap<String, DoubleSupplier> data = getUnmodifiableDataSupplierMap();
		for (int i=0; i<data.size(); i++) {
			if (isFloat) {
				buffer.putFloat((float) 1234567.0);
			} else {
				buffer.putDouble(1234567.0);
			}
		}
		buffer.flip();
		if (getFileChannel()!=null) {
			getFileChannel().write(buffer);
		}
		super.pauseStream();
	}
	
	@Override void continueStream() throws IOException {
		long offset = 0;
		if (isFloat) {
			offset = Float.BYTES*getUnmodifiableDataSupplierMap().size();
		} else {
			offset = Double.BYTES*getUnmodifiableDataSupplierMap().size();
		}
		getFileChannel().position(getFileChannel().position()-offset);
	}
	
	@Override void finalizeStream() throws IOException {
		buffer.clear();
		SortedMap<String, DoubleSupplier> data = getUnmodifiableDataSupplierMap();
		for (int i=0; i<data.size(); i++) {
			if (isFloat) {
				buffer.putFloat((float) 1234567.0);
			} else {
				buffer.putDouble(1234567.0);
			}
		}
		buffer.flip();
		if (getFileChannel()!=null) {
			getFileChannel().write(buffer);
		}
		super.finalizeStream();
	}

}

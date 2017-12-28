package org.usfirst.frc.team1592.arch.tm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * File Telemetry Stream
 * <p>
 * Implements telemetry stream using {@link FileChannel} under the hood.
 */
public abstract class FileStream extends TelemetryStream {

	//===============================//
	//      Internal Components      //
	//===============================//

	private Path filepath;
	private FileOutputStream stream;
	private FileChannel channel;



	//=======================//
	//      Constructor      //
	//=======================//

	/** Protected Constructor */
	protected FileStream(Path path) {
		filepath = path;
	}
	
	/**
	 * Returns the FileChannel on which to write in {@link #updateStream()}. The
	 * returned channel can be null either because the stream has not been initialized
	 * or because a failure occurred during initialization.
	 *
	 * @return the file channel to write to
	 */
	protected FileChannel getFileChannel() {
		return channel;
	}
	
	/**
	 * Sets the file path to output to
	 *
	 * @param filepath  the file path to output to
	 * @throws NullPointerException if {@code filepath} is null
	 * @throws IllegalStateException if {@link #isLocked()} returns {@code true}
	 */
	public final void setPath(Path filepath) {
		if (filepath==null) {throw new NullPointerException();}
		if (isLocked()) {throw new IllegalStateException();}
		this.filepath = filepath;
	}



	//=============================================//
	//      Package Private Methods (Stream)       //
	//=============================================//
	
	@Override void initializeStream() throws IOException {
		// Check For FMS
		DriverStation ds = DriverStation.getInstance();
		if (ds.isFMSAttached()) {
			filepath = filepath.resolveSibling(String.join("_", "Match", filepath.getFileName().toString()));
		}
		Set<OpenOption> opts = new HashSet<>();
		opts.add(StandardOpenOption.CREATE);
		opts.add(StandardOpenOption.TRUNCATE_EXISTING);
		try {
			stream = new FileOutputStream(filepath.toString());
			channel = stream.getChannel();
		} catch (IOException e) {
			channel = null;
			e.printStackTrace();
		}
	}
	
	@Override void pauseStream() throws IOException {
		if (channel!=null) {
			try {
				channel.force(true);
			} catch (IOException e) {}
		}
	}
	
	@Override void finalizeStream() throws IOException {
		if (channel!=null) {
			try {
				channel.force(true);
			} catch (IOException e) {}
			try {
				channel.close();
				stream.close();
			} catch (IOException e1) {}
		}
		super.finalizeStream();
	}
	

}

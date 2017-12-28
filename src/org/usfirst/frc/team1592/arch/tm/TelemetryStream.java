package org.usfirst.frc.team1592.arch.tm;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.DoubleSupplier;

import org.usfirst.frc.team1592.arch.RobotUtility;

/**
 * Telemetry Stream Class
 * <p>
 * An instantiation of this class controls a single telemetry stream. Users must register data streams 
 * with this class and perform any other setup prior to the stream being locked. After locking only a 
 * few high-level methods will remain accessible. A default telemetry stream updates at a 100 millisecond
 * period or roughly 10 Hz.
 */
public abstract class TelemetryStream {
	
	//===============================//
	//      Internal Components      //
	//===============================//
	
	private long period;
	private final SortedMap<String, DoubleSupplier> dataMap;
	private boolean locked;
	
	
	
	//=======================//
	//      Constructor      //
	//=======================//
	
	/** Protected Constructor */
	protected TelemetryStream() {
		period = 100;
		dataMap = new TreeMap<>();
		locked = false;
	}
	
	
	
	//===========================//
	//      Public Methods       //
	//===========================//
	
	/**
	 * Returns whether the telemetry stream has been locked from editing. This will occur
	 * once the {@link #lock()} method is called which is only publicly accessed using the
	 * {@link org.usfirst.frc.team1592.arch.tm.Telemetry} object in which this stream resides.
	 */
	public final boolean isLocked() {
		return locked;
	}
	
	/**
	 * Sets the time in milliseconds between subsequent telemetry stream data acquisitions. Note
	 * that this input is fixed delay not fixed rate and will vary the exact separation based on
	 * the length of time needed to run the telemetry update as well as other CPU considerations.
	 *
	 * @param period  the time in milliseconds between subsequent telemetry stream data acquisitions
	 * @throws IllegalArgumentException if {@code period <= 0}
	 * @throws IllegalStateException if {@link #isLocked()} returns {@code true}
	 */
	public final void setPeriod(long period) {
		if (period<=0) {throw new IllegalArgumentException();}
		if (isLocked()) {throw new IllegalStateException();}
		this.period = period;
	}
	
	/**
	 * Returns the time in milliseconds between subsequent telemetry stream data acquisitions.
	 */
	public final long getPeriod() {
		return period;
	}
	
	/**
	 * Returns an unmodifiable view of the data supplier map.
	 */
	public final SortedMap<String, DoubleSupplier> getUnmodifiableDataSupplierMap() {return Collections.unmodifiableSortedMap(dataMap);}
	
	/**
	 * Add a data supplier to the supplier map. If the supplier already exists in the map,
	 * the current name of that supplier in the map will be returned. Otherwise the supplier
	 * will be added to the map. A name will be generated for the supplier and will be returned.
	 *
	 * @param supplier  the data supplier
	 * @return the name actually used for the supplier in the map
	 */
	public final String addDataSupplier(DoubleSupplier supplier) {
		if (supplier==null) {throw new NullPointerException();}
		if (dataMap.containsValue(supplier)) {
			for (Map.Entry<String, DoubleSupplier> e : dataMap.entrySet()) {
				if (e.getValue().equals(supplier)) {
					return e.getKey();
				}
			}
		}
		String name = RobotUtility.ensureUnique(null, dataMap.keySet());
		dataMap.put(name, supplier);
		return name;
	}
	
	/**
	 * Add a data supplier to the supplier map. If the supplier already exists in the map,
	 * the current name of that supplier in the map will be returned. Otherwise the supplier
	 * will be added to the map. It will be added under the input name, if that name does not
	 * already exist in the map, otherwise a new name will be produced so that it can be added.
	 * The name actually used to insert it into the map is returned.
	 *
	 * @param name  the recommended name
	 * @param supplier  the data supplier
	 * @return the name actually used for the supplier in the map
	 */
	public final String addDataSupplier(String name, DoubleSupplier supplier) {
		if (supplier==null) {throw new NullPointerException();}
		if (dataMap.containsValue(supplier)) {
			for (Map.Entry<String, DoubleSupplier> e : dataMap.entrySet()) {
				if (e.getValue().equals(supplier)) {
					return e.getKey();
				}
			}
		}
		name = RobotUtility.ensureUnique(name, dataMap.keySet());
		dataMap.put(name, supplier);
		return name;
	}
	
	/**
	 * Removes the data supplier from the supplier map with the input name, if it exists.
	 *
	 * @param name  the supplier name
	 * @return the data supplier removed, or null if no supplier is removed
	 */
	public final DoubleSupplier removeDataSupplier(String name) {
		return dataMap.remove(name);
	}
	
	
	
	//=============================================//
	//      Package Private Methods (Stream)       //
	//=============================================//
	
	/**
	 * Locks the telemetry stream object. This method will be called prior to any of the
	 * xxxStream() methods.
	 */
	final void lock() {
		if (locked) {return;}
		locked = true;
	}
	
	/**
	 * Internal method for opening the stream and preparing for data output. This method is only called
	 * once at the point at which the telemetry stream data collection is to commence.
	 */
	abstract void initializeStream() throws IOException;
	
	/**
	 * Internal method for updating the stream with data. This method is called at a rate where each
	 * subsequent call is approximately separated from the previous call be the value returned by 
	 * {@link #getPeriod()} in milliseconds. The exact separation can vary slightly (longer, never shorter)
	 * than the specified value due to the runtime of this method, CPU usage, and other conflicts.
	 */
	abstract void updateStream() throws IOException;
	
	/**
	 * Internal method for pausing the stream. This method is called once at each time that pause() is
	 * initiated. The conclusion of this method should leave the stream in a valid state such that if no
	 * other action occurred, the data would be valid. This method can either be followed by {@link #continueStream()}
	 * or {@link #finalizeStream()}.
	 */
	abstract void pauseStream() throws IOException;
	
	/**
	 * Internal method for continue the stream. This method is called once at each time that continue() is
	 * initiated. This method will only follow after a call to {@link #pauseStream()}. Once this method completes,
	 * telemetry stream data collection recommences.
	 */
	abstract void continueStream() throws IOException;
	
	/**
	 * Internal method for closing the stream. This method is only called once at the point at which the 
	 * telemetry stream data collection is completed. 
	 */
	void finalizeStream() throws IOException {
		locked = false;
	}
	
}
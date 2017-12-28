package org.usfirst.frc.team1592.arch.threads;

/**
 * Periodic Task Thread
 * <p>
 * The class implements a single background thread for which the user assigns one or more
 * periodic tasks at a given rate. This class provides simplified functionality for controlling
 * task behavior. Note that this class also provides the flexibility to change the periodic task
 * rate on the fly, though this can produce some slight lag behavior during the period change.
 * Additionally, if a period change is made while the tasks are paused, the tasks may (but are
 * not guaranteed) run at least once during the changeover before they resume their paused state.
 */
public final class PeriodicTaskThread {

	//===============================//
	//      Internal Components      //
	//===============================//

	private final BackgroundThread thread;
	private final Runnable[] actions;
	
	private long period;
	private boolean started;
	private boolean paused;



	//=======================//
	//      Constructor      //
	//=======================//
	
	/**
	 * Periodic Task thread constructor
	 * <p>
	 * This class runs at approximately 50 Hz and assigns the background thread to {@link Thread.NORM_PRIORITY}.
	 *
	 * @param period  the time in milliseconds between calling actions (i.e. 100 milliseconds is 10 Hz)
	 * @param runnables  the calling actions to run on this thread
	 */
	public PeriodicTaskThread(Runnable... runnables) {
		this(20, Thread.NORM_PRIORITY, runnables);
	}
	
	/**
	 * Periodic Task thread constructor
	 * <p>
	 * This class assigns the background thread to {@link Thread.NORM_PRIORITY}.
	 *
	 * @param period  the time in milliseconds between calling actions (i.e. 100 milliseconds is 10 Hz)
	 * @param runnables  the calling actions to run on this thread
	 */
	public PeriodicTaskThread(long period, Runnable... runnables) {
		this(period, Thread.NORM_PRIORITY, runnables);
	}

	/**
	 * Periodic Task thread constructor
	 *
	 * @param period  the time in milliseconds between calling actions (i.e. 100 milliseconds is 10 Hz)
	 * @param priority  the priority level of the thread
	 * @param runnables  the calling actions to run on this thread
	 */
	public PeriodicTaskThread(long period, int priority, Runnable... runnables) {
		thread = new BackgroundThread(priority);
		this.period = period;
		actions = runnables.clone();
		started = false;
		paused = false;
	}



	//===========================//
	//      Public Methods       //
	//===========================//
	
	/**
	 * Sets the time in milliseconds between subsequent calling actions. Note that this input is 
	 * fixed delay not fixed rate and will vary the exact separation based on the length of time 
	 * needed to run the runnables as well as other CPU considerations.
	 *
	 * @param period  the time in milliseconds between subsequent calling actions
	 * @throws IllegalArgumentException if {@code period <= 0}
	 */
	public void setPeriod(long period) {
		if (period<=0) {throw new IllegalArgumentException();}
		this.period = period;
		if (started) {
			if (paused) {
				stop();
				start();
				pause();
			} else {
				stop();
				start();
			}
		}
	}
	
	/**
	 * Returns the time in milliseconds between subsequent calling actions.
	 */
	public long getPeriod() {
		return period;
	}
	
	/**
	 * Returns whether the background thread is started and therefore available for
	 * scheduling. This is {@code true} after instantiation until the first time that
	 * {@link #close()} is called on this object. After that, this method will always
	 * return {@code false}.
	 *
	 * @return whether the thread is active and therefore available for scheduling
	 */
	public boolean isActive() {
		return thread.isActive();
	}
	
	/**
	 * Returns whether the periodic tasks are running. This returns true after {@link #start()}
	 * has been called and remains true until either {@link #pause()}, {@link #stop()}, or
	 * {@link #close()} is called. If {@link #pause()} is called, this can return to true by
	 * a call to {@link #resume()}.
	 *
	 * @return whether the periodic tasks are running
	 */
	public boolean isRunning() {
		return started && !paused;
	}
	
	/**
	 * Returns whether the periodic tasks are paused. This returns true after {@link #pause()}
	 * and will only become false by either {@link #resume()} or if the tasks are stopped using
	 * {@link #stop()} or {@link #close()}.
	 *
	 * @return Returns whether the periodic tasks are paused
	 */
	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * Starts the periodic tasks, if not already running, otherwise does nothing.
	 */
	public void start() {
		if (!started) {
			started = true;
			for (int i=0; i<actions.length; i++) {
				thread.startPeriodic(Integer.toString(i), this.period, actions[i]);
			}
		}
	}
	
	/**
	 * Pauses the periodic tasks, if running, otherwise does nothing.
	 */
	public void pause() {
		if (started && !paused) {
			paused = true;
			thread.pauseAllPeriodic();
		}
	}
	
	/**
	 * Resumes the periodic tasks, if paused, otherwise does nothing.
	 */
	public void resume() {
		if (started && paused) {
			paused = false;
			thread.resumeAllPeriodic();
		}
	}
	
	/**
	 * Stops the periodic tasks, if running or paused, otherwise does nothing.
	 */
	public void stop() {
		if (started) {
			started = false;
			paused = false;
			thread.stopAllPeriodic();
		}
	}
	
	/**
	 * Closes the thread and stops all periodic tasks. This class will no longer
	 * run any tasks after this method is called.
	 */
	public void close() {
		thread.close();
	}

}

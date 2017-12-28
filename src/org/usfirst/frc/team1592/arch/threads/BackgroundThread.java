package org.usfirst.frc.team1592.arch.threads;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.usfirst.frc.team1592.arch.RobotUtility;

/**
 * Background Thread Class
 * <p>
 * An instantiation of this class initializes a java.util.Timer object, which itself
 * is a facility for scheduling and controlling tasks on a background thread. The thread
 * itself is instantiated by the java.util.Timer object.
 * <p>
 * This class provides a more convenient API for setting up and scheduling runnable tasks
 * as well as for closing the background thread.
 */
public final class BackgroundThread {
	
	//===============================//
	//      Internal Components      //
	//===============================//
	
	private java.util.Timer timer;
	private final Map<String, InternalTask> tasks;
	private final Map<String, TimedRunnable> pausedTasks;
	private boolean active;
	
	
	
	//=======================//
	//      Constructor      //
	//=======================//
	
	/**
	 * Standard thread constructor
	 *
	 * @param priority  the priority level of the thread
	 */
	public BackgroundThread(int priority) {
		Thread v = new Thread(() -> {
			timer = new java.util.Timer();
		});
		v.setPriority(priority);
		v.start();
		try {
			v.join();
		} catch (InterruptedException e) {
			throw new IllegalArgumentException(e);
		}
		tasks = new HashMap<>();
		pausedTasks = new HashMap<>();
		active = true;
	}
	
	
	
	//===========================//
	//      Public Methods       //
	//===========================//
	
	/**
	 * Returns whether the background thread is active and therefore available for
	 * scheduling. This is {@code true} after instantiation until the first time that
	 * {@link #close()} is called on this object. After that, this method will always
	 * return {@code false}.
	 *
	 * @return whether the background thread is active and therefore available for scheduling
	 */
	public final boolean isActive() {
		return active;
	}
	
	/**
	 * Immediately runs the input action method on the background thread.
	 *
	 * @param action  the runnable
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final void run(Runnable action) {
		timer.schedule(new InternalTask(action, 0), 0);
	}
	
	/**
	 * Schedules the input action method on the background thread at the specified 
	 * period and registers that task with the specified input name. If the input 
	 * name already exists, a modified one is generated to ensure uniqueness. The 
	 * actually assigned name, modified as necessary for uniqueness, is returned by 
	 * this method.
	 *
	 * @param name  the requested task name
	 * @param period  the time in milliseconds between method calls (fixed-delay)
	 * @param action  the runnable
	 * @return the name actually stored as reference to this periodic task
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final String startPeriodic(String name, long period, Runnable action) {
		name = RobotUtility.ensureUnique(name, tasks.keySet());
		InternalTask task = new InternalTask(action, period);
		tasks.put(name, task);
		timer.schedule(task, 0, task.period);
		return name;
	}
	
	/**
	 * Stops the periodic task with the given name from continuing to execute as
	 * scheduled on the background thread. This method only succeeds if the input
	 * name matches a currently running named task, otherwise no changes are made.
	 *
	 * @param name  the reference name to a periodic task
	 * @return {@code true} if the name referred to a valid task which was stopped
	 * 			successfully, {@code false} otherwise.
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final boolean pausePeriodic(String name) {
		InternalTask task = tasks.get(name);
		if (task != null) {
			boolean out = task.cancel();
			if (out) {
				pausedTasks.put(name, new TimedRunnable(task.action, task.period));
			}
			return out;
		}
		return false;
	}
	
	/**
	 * Pauses all of the periodic tasks, if any, from continuing to execute as
	 * scheduled on the background thread.
	 * 
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final void pauseAllPeriodic() {
		for (Map.Entry<String, InternalTask> e : tasks.entrySet()) {
			e.getValue().cancel();
			pausedTasks.put(e.getKey(), new TimedRunnable(e.getValue().action, e.getValue().period));
		}
	}
	
	/**
	 * Resumes the periodic task with the given name, if it exists and is paused.
	 *
	 * @param name  the reference name to a periodic task
	 * @return {@code true} if the name referred to a valid task which was paused
	 * 			and is resumed successfully, {@code false} otherwise.
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final boolean resumePeriodic(String name) {
		TimedRunnable action = pausedTasks.remove(name);
		if (action != null) {
			InternalTask task = new InternalTask(action.action, action.period);
			tasks.put(name, task);
			timer.schedule(task, 0, action.period);
			return true;
		}
		return false;
	}
	
	/**
	 * Resumes all of the paused periodic tasks, if any.
	 * 
	 * @return {@code true} if any tasks are resumed successfully, {@code false} otherwise.
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final boolean resumeAllPeriodic() {
		boolean out = false;
		for (Map.Entry<String, TimedRunnable> e : pausedTasks.entrySet()) {
			InternalTask task = new InternalTask(e.getValue().action, e.getValue().period);
			tasks.put(e.getKey(), task);
			timer.schedule(task, 0, e.getValue().period);
			out = true;
		}
		return out;
	}
	
	/**
	 * Stops the periodic task with the given name from continuing to execute as
	 * scheduled on the background thread and removes them from the task list. 
	 * This method only succeeds if the input name matches a currently running 
	 * named task, otherwise no changes are made.
	 *
	 * @param name  the reference name to a periodic task
	 * @return {@code true} if the name referred to a valid task which was stopped
	 * 			successfully, {@code false} otherwise.
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final boolean stopPeriodic(String name) {
		InternalTask task = tasks.remove(name);
		if (task != null) {
			pausedTasks.remove(name);
			return task.cancel();
		}
		return false;
	}
	
	/**
	 * Stops all of the periodic tasks, if any, from continuing to execute as
	 * scheduled on the background thread and removes them from the task list.
	 * @throws IllegalStateException if {@link #isActive()} returns {@code false}
	 */
	public final void stopAllPeriodic() {
		for (TimerTask task : tasks.values()) {
			task.cancel();
		}
		tasks.clear();
		pausedTasks.clear();
	}
	
	/**
	 * Returns an unmodifiable set of the names of the currently active periodic tasks, if any.
	 */
	public final Set<String> getActivePeriodic() {
		Set<String> out = new HashSet<>(tasks.keySet());
		out.removeAll(pausedTasks.keySet());
		return Collections.unmodifiableSet(out);
	}
	
	/**
	 * Returns an unmodifiable set of the names of the currently paused periodic tasks, if any.
	 */
	public final Set<String> getPausedPeriodic() {
		return Collections.unmodifiableSet(pausedTasks.keySet());
	}
	
	/**
	 * Stops all of the periodic tasks, closes the background thread, and marks
	 * the thread as inactive.
	 */
	public final void close() {
		stopAllPeriodic();
		timer.cancel();
	}
	
	
	
	//================================//
	//      Private Final Class       //
	//================================//
	
	/** Internal Task Implementation Wrapper */
	private final class InternalTask extends TimerTask {
		private final Runnable action;
		private final long period;
		InternalTask(Runnable action, long period) {
			if (action==null) {throw new NullPointerException();}
			this.action = action;
			this.period = period;
		}
		@Override public void run() {
			action.run();
		}
	}
	
	/** Timed Runnable Wrapper */
	private final class TimedRunnable {
		private final Runnable action;
		private final long period;
		TimedRunnable(Runnable action, long period) {
			this.action = action;
			this.period = period;
		}
	}

}
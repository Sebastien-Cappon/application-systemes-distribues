package com.openclassrooms.tourguide.util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.TourGuideService;

/**
 * A class that manages a tracker that follows users to record their positions
 * and update rewards every N minutes.
 * 
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
public class UsersTracker extends Thread {
	public static final AtomicBoolean SLEEPINGTRACKER = new AtomicBoolean();
	private static final Logger logger = LoggerFactory.getLogger(UsersTracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	
	private boolean stop = false;

	public UsersTracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;
		executorService.submit(this);
	}

	/**
	 * An override method that run the tracker and logs its statuses.
	 * 
	 * @return <code>void</code>.
	 */
	@Override
	public void run() {
		SLEEPINGTRACKER.set(false);
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			users.forEach(u -> tourGuideService.trackUserLocation(u));
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				SLEEPINGTRACKER.set(true);
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	/**
	 * A method that assures to shut down the Tracker thread.
	 * 
	 * @return <code>void</code>.
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}
}
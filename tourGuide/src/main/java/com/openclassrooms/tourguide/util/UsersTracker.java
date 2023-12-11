package com.openclassrooms.tourguide.util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.ITourGuideService;

/**
 * A class that manages a tracker that follows users to record their positions
 * and update rewards every N minutes.
 * 
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
public class UsersTracker extends Thread {
	
	@Autowired
	private ITourGuideService iTourGuideService; 
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	private static final Logger logger = LoggerFactory.getLogger(UsersTracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	
	private boolean stop = false;

	public static final AtomicBoolean SLEEPINGTRACKER = new AtomicBoolean();
	
	public UsersTracker(ITourGuideService iTourGuideService) {
		this.iTourGuideService = iTourGuideService;
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

			List<User> users = iTourGuideService.getUserList();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			users.forEach(u -> iTourGuideService.trackUserLocation(u));
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
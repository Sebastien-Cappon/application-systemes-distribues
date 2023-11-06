package com.openclassrooms.tourguide.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Constants class that regroups a short list of URI that the logger should
 * ignore. <code>/error</code> is essential in order not to log Spring's
 * automatic masked calls to the <code>/error</code> page when an error is
 * thrown.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public class UriToIgnore {
	public static final String errorURI = "/error";
	public static final String faviconURI = "/favicon.ico";

	public static final List<String> uriToIgnore = new ArrayList<>(Arrays.asList(
			errorURI,
			faviconURI
		));
}
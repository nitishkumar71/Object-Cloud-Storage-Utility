package org.cloud.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Copyright (©) 2017. Athenas Owl. All rights reserved.
 * @author Nitishkumar Singh
 * @Description: Storage Exception Class
 */
public class StorageException extends Exception {

	private static final long serialVersionUID = 1L;
	private int statusCode;
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageException.class);

	/*
	 * Constructor to pass Status code and cause for exception
	 */
	public StorageException(int statusCode, String message) {
		super(message);
		this.setStatusCode(statusCode);
		doLogging(message);
	}

	public StorageException(int statusCode, String message, Throwable cause) {
		super(message, cause);
		this.setStatusCode(statusCode);
		doLogging(message);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/*
	 * do Logging
	 */
	private void doLogging(String message) {
		final StringBuilder logBuilder = new StringBuilder();
		logBuilder.setLength(0);
		logBuilder.append(message);
		LOGGER.info(logBuilder.toString());
	}

}

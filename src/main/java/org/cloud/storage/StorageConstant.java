package org.cloud.storage;

/*
 * Copyright (©) 2017. Athenas Owl. All rights reserved.
 * @author Nitishkumar Singh
 * @Description: Storage Constant
 */

public final class StorageConstant {
	// hide constructor
	private StorageConstant() {
	}

	public static final String BUCKET_REGULAR_EXPRESSION = "";

	public static final String AMAZON_CLOUD_STORAGE_CONSTANT = "s3";

	public static final String GOOGLE_CLOUD_STORAGE_CONSTANT = "gs";
	public static final String BUCKET_SEPERATOR_CONSTANT = "://";

	/*
	 * replace local with local path for development
	 */
	public static final String LOCAL_STORAGE_ROOT = "/tmp";

	public static final String EMPTY_STRING = "";
	public static final String BUCKET_NAME_EXPRESSION = "(://.*?/)";
	public static final String BUCKET_FILE_EXPRESSION = "(://.*)";
	public static final String BUCKET_SEPERATOR_EXPRESSION = "(://)";
	public static final String FOLDER_SEPERATOR_EXPRESSION = "(/)";
	public static final String BUCKET_TYPE_SEPERATOR = "(.*?://)";
	public static final String BUCKET_GUID_EXPRSSION = "(/[^/]*){2}/([^/]*)";
	public static final String DOT = ".";
	public static final int BUCKET_GUID_GROUP = 2;

	public static final int CHUNK_SIZE = 1_000_000;
}

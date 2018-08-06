package org.cloud.storage;

import java.io.IOException;
import java.nio.file.Path;

/*
 * Copyright (©) 2017. Athenas Owl. All rights reserved.
 * @author Nitishkumar Singh
 * @Description: Generic Interface, to expose Storage Services
 */
public interface StorageInterface {

	/*
	 * method to upload file Input: bucketName - Name of the bucket, where we need to upload files bucketFileName - Name
	 * of the file to upload localFilePath - full path of the local file to be upload
	 */
	public void uploadFile(String bucketName, String bucketFileName, Path localFilePath)
			throws StorageException, IOException;

	/*
	 * method to donwload file Input: bucketName - Name of the bucket, where we need to upload files bucketFileName -
	 * Name of the file to download localFilePath - full local path, where path need to be download
	 */
	public void downloadFile(String bucketName, String bucketFileName, Path localFilePath)
			throws StorageException, IOException;

	/**
	 * 
	 * @param <E>
	 * @param bucketType
	 * @param bucketName
	 * @param bucketFileName
	 * @param duration
	 *            in minutes
	 * @param method
	 * @return signed url for object
	 */
	public String getSignedUrl(String bucketName, String bucketFileName, long duration, String method)
			throws StorageException;
}

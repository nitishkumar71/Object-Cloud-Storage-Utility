package org.cloud.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*
 * Copyright (©) 2017. Athenas Owl. All rights reserved.
 * @author Nitishkumar Singh
 * @Description: Storage Repository, which will work as wrapper for all the storage systems
 */

@Repository
public class StorageRepository {
	@Autowired
	GCPStorageComponent googleStorage;

	@Autowired
	AWSStorageComponent amazonStorage;

	/*
	 * Function will download file and decide, which storage service should be used. Input
	 * @url - full path of the storage file
	 */
	public Path downloadFile(String url) throws StorageException, IOException {
		String bucketName = StorageUtility.getBucket(url);
		String bucketFileName = StorageUtility.getBucketFileName(url);
		Path localFilePath = Paths.get(String.join(File.separator, StorageConstant.LOCAL_STORAGE_ROOT, bucketFileName));
		switch (StorageUtility.getBucketStorageType(url)) {
		case StorageConstant.AMAZON_CLOUD_STORAGE_CONSTANT:
			amazonStorage.downloadFile(bucketName, bucketFileName, localFilePath);
			break;
		case StorageConstant.GOOGLE_CLOUD_STORAGE_CONSTANT:
			googleStorage.downloadFile(bucketName, bucketFileName, localFilePath);
			break;
		default:
			throw new StorageException(20000, "Storage not Supported!");
		}
		return localFilePath;
	}

	/*
	 * Function will upload file on the given bucket Input:
	 * @url - Path, where file need to be uploaded
	 * @localFilePath - Local File Path which need to be uploaded
	 */
	public void uploadFile(String url, Path localFilePath) throws StorageException, IOException {
		String bucketName = StorageUtility.getBucket(url);
		String bucketFileName = StorageUtility.getBucketFileName(url);
		switch (StorageUtility.getBucketStorageType(url)) {
		case StorageConstant.AMAZON_CLOUD_STORAGE_CONSTANT:
			amazonStorage.uploadFile(bucketName, bucketFileName, localFilePath);
			break;
		case StorageConstant.GOOGLE_CLOUD_STORAGE_CONSTANT:
			googleStorage.uploadFile(bucketName, bucketFileName, localFilePath);
			break;
		default:
			throw new StorageException(20000, "Storage not Supported!");
		}
	}

	/**
	 * 
	 * @param url
	 * @param duration
	 *            in minutes
	 * @param method
	 * @return signed url
	 */
	public String getSignedUrl(String url, long duration, String method) throws StorageException {
		String bucketName = StorageUtility.getBucket(url);
		String bucketFileName = StorageUtility.getBucketFileName(url);
		String signedUrl = null;
		switch (StorageUtility.getBucketStorageType(url)) {
		case StorageConstant.AMAZON_CLOUD_STORAGE_CONSTANT:
			signedUrl = amazonStorage.getSignedUrl(bucketName, bucketFileName, duration, method);
			break;
		case StorageConstant.GOOGLE_CLOUD_STORAGE_CONSTANT:
			signedUrl = googleStorage.getSignedUrl(bucketName, bucketFileName, duration, method);
			break;
		default:
			throw new StorageException(20000, "Storage not Supported!");
		}
		return signedUrl;
	}
}

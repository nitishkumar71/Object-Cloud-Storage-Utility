package org.cloud.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StorageUtility {

	// hide constructor
	private StorageUtility() {
	}

	private static final String BUCKET_URL_NOT_CORRECT = "Bucket URL is not correct";

	/*
	 * Function will create file, along with the folder structure for Path
	 */
	public static void createLocalFile(Path localFilePath) throws IOException {
		Files.createDirectories(localFilePath.getParent());
		Files.createFile(localFilePath);
	}

	/*
	 * Get Bucket name from given url
	 */
	public static String getBucket(String url) throws StorageException {
		Pattern pattern = Pattern.compile(StorageConstant.BUCKET_NAME_EXPRESSION);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find())
			return matcher.group().replaceAll(StorageConstant.BUCKET_SEPERATOR_EXPRESSION, StorageConstant.EMPTY_STRING)
					.replaceAll(StorageConstant.FOLDER_SEPERATOR_EXPRESSION, StorageConstant.EMPTY_STRING);
		else
			throw new StorageException(20000, BUCKET_URL_NOT_CORRECT);
	}

	/*
	 * Get Bucket File name from given url
	 */
	public static String getBucketFileName(String url) throws StorageException {
		Pattern pattern = Pattern.compile(StorageConstant.BUCKET_FILE_EXPRESSION);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find())
			return matcher.group().replaceAll(StorageConstant.BUCKET_NAME_EXPRESSION, StorageConstant.EMPTY_STRING);
		else
			throw new StorageException(20000, BUCKET_URL_NOT_CORRECT);
	}

	/*
	 * Get Bucket type from given url
	 */
	public static String getBucketStorageType(String url) throws StorageException {
		Pattern pattern = Pattern.compile(StorageConstant.BUCKET_TYPE_SEPERATOR);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find())
			return matcher.group().replaceAll(StorageConstant.BUCKET_SEPERATOR_EXPRESSION,
					StorageConstant.EMPTY_STRING);
		else
			throw new StorageException(20000, BUCKET_URL_NOT_CORRECT);
	}

	/*
	 * Get Bucket GUID
	 */
	public static String getBucketFileGuid(String url) throws StorageException {
		Pattern pattern = Pattern.compile(StorageConstant.BUCKET_GUID_EXPRSSION);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find())
			return matcher.group(StorageConstant.BUCKET_GUID_GROUP);
		else
			throw new StorageException(20000, BUCKET_URL_NOT_CORRECT);
	}

	/*
	 * Get File Extension from bucket path
	 */
	public static String getBucketFileExtension(String url) throws StorageException {
		return url.substring(url.lastIndexOf(StorageConstant.DOT));
	}

	/*
	 * Get File name without Extension from bucket path
	 */
	public static String getBucketFileNameWithoutExtension(String url) throws StorageException {
		String fileName = getBucketFileName(url);
		return fileName.substring(0, fileName.lastIndexOf(StorageConstant.DOT));
	}
}

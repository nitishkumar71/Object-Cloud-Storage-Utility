package org.cloud.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;

/*
 * Copyright (©) 2017. Athenas Owl. All rights reserved.
 * @author Nitishkumar Singh
 * @Description: Implementation of AWS Storage
 */

@Component
public class AWSStorageComponent implements StorageInterface {

	@Autowired
	private AmazonS3 amazonS3;

	private static final TransferManager transferManager = TransferManagerBuilder.standard().build();

	private static final Logger LOGGER = LoggerFactory.getLogger(AWSStorageComponent.class);

	@Override
	public void uploadFile(String bucketName, String bucketFileName, Path localFilePath) throws StorageException {
		try {
			File file = localFilePath.toFile();
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, bucketFileName, file);
			if (file.length() < StorageConstant.CHUNK_SIZE)
				amazonS3.putObject(putObjectRequest);
			else {
				Upload upload = transferManager.upload(putObjectRequest);
				upload.waitForCompletion();
			}
			// PutObjectResult result = amazonS3
			// .putObject(new PutObjectRequest(bucketName, bucketFileName, localFilePath.toFile()));

		} catch (AmazonS3Exception | InterruptedException ex) {
			LOGGER.info(ex.getMessage());
			ex.printStackTrace();
			throw new StorageException(20001, "No Such Bucket Exist");
		}
	}

	@Override
	public void downloadFile(String bucketName, String bucketFileName, Path localFilePath)
			throws IOException, StorageException {
		final GetObjectRequest request = new GetObjectRequest(bucketName, bucketFileName);
		try (S3Object s3object = amazonS3.getObject(new GetObjectRequest(bucketName, bucketFileName));) {

			StorageUtility.createLocalFile(localFilePath);
			if (localFilePath.toFile().length() < StorageConstant.CHUNK_SIZE) {
				Files.write(localFilePath, IOUtils.toByteArray(s3object.getObjectContent()), StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} else {
				Download download = transferManager.download(request, localFilePath.toFile());
				download.waitForCompletion();
			}
			// old code

			// s3object.getObjectMetadata().getContentLength()
			// StorageUtility.createLocalFile(localFilePath);
			// Files.write(localFilePath, IOUtils.toByteArray(s3object.getObjectContent()), StandardOpenOption.WRITE,
			// StandardOpenOption.TRUNCATE_EXISTING);
		} catch (AmazonS3Exception | InterruptedException ex) {
			LOGGER.info(ex.getMessage());
			ex.printStackTrace();
			throw new StorageException(20001, "Bucket File does not exist");
		}

	}

	@Override
	public String getSignedUrl(String bucketName, String bucketFileName, long duration, String method) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
				bucketFileName);
		generatePresignedUrlRequest.setMethod(HttpMethod.valueOf(method)); // Default
		generatePresignedUrlRequest
				.setExpiration(Date.from(LocalDateTime.now().plusMinutes(duration).toInstant(ZoneOffset.UTC)));
		return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}
}

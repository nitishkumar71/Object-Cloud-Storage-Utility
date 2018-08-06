package org.cloud.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/*
 * Copyright (©) 2017. Athenas Owl. All rights reserved.
 * @author Nitishkumar Singh
 * @Description: Implementation of GCP Storage
 */
@Component
public class GCPStorageComponent implements StorageInterface {
	private static Storage storage = StorageOptions.getDefaultInstance().getService();

	private static final Logger LOGGER = LoggerFactory.getLogger(GCPStorageComponent.class);

	@Override
	public void uploadFile(String bucketName, String bucketFileName, Path localFilePath)
			throws IOException, StorageException {
		BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, bucketFileName).build();

		// .setContentType(Files.probeContentType(localFilePath)).build();
		ByteBuffer buffer = ByteBuffer.allocate(StorageConstant.CHUNK_SIZE);
		try (WriteChannel writer = storage.writer(blobInfo);
				ByteChannel inChannel = Files.newByteChannel(localFilePath);) { // write to bucket
			while (inChannel.read(buffer) > 0) {
				buffer.flip();
				writer.write(buffer);
				buffer.clear();
			}
			/*
			 * try { storage.create(blobInfo, Files.readAllBytes(localFilePath));
			 */
		} catch (com.google.cloud.storage.StorageException ex) {
			LOGGER.info(ex.getMessage());
			ex.printStackTrace();
			throw new StorageException(20001, "No such bukcet exist");
		} finally {

		}
	}

	@Override
	public void downloadFile(String bucketName, String bucketFileName, Path localFilePath)
			throws StorageException, IOException {
		try {
			Blob blob = storage.get(BlobId.of(bucketName, bucketFileName));
			// No such object;
			if (blob == null)
				throw new StorageException(20001, "No Such File Exist in bucket");

			// create nested directories
			StorageUtility.createLocalFile(localFilePath);
			if (blob.getSize() < StorageConstant.CHUNK_SIZE) {
				Files.write(localFilePath, blob.getContent(), StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} else {
				// When Blob size is big or unknown use the blob's channel reader.
				try (ReadChannel reader = blob.reader()) {
					WritableByteChannel channel = Channels
							.newChannel(new PrintStream(new FileOutputStream(localFilePath.toFile())));
					ByteBuffer bytes = ByteBuffer.allocate(StorageConstant.CHUNK_SIZE); // download in 1 MB chunk
					while (reader.read(bytes) > 0) {
						bytes.flip();
						channel.write(bytes);
						bytes.clear();
					}
				}

			}
		} catch (com.google.cloud.storage.StorageException ex) {
			LOGGER.info(ex.getMessage());
			ex.printStackTrace();
			throw new StorageException(20001, "No such bukcet exist");
		}
	}

	@Override
	public String getSignedUrl(String bucketName, String bucketFileName, long duration, String method) {
		return storage.signUrl(BlobInfo.newBuilder(bucketName, bucketFileName).build(), duration, TimeUnit.MINUTES,
				Storage.SignUrlOption.httpMethod(HttpMethod.valueOf(method))).toString();
	}
}

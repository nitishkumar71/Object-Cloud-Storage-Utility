package org.cloud.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = SpringBootTestMain.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(SpringTestConstant.activeProfiles)
public class StorageRepositoryTest {

	@Autowired
	StorageRepository repository;

	private final String GCP_BUCKET_FILE_PATH = "gs://b-ao-product-mock/%s/video/%s/%s.mp4";
	private final String AWS_BUCKET_FILE_PATH = "s3://b-product-mock/%s/video/%s/%s.mp4";

	final Path testVideoFilePath = Paths
			.get(getClass().getClassLoader().getResource("a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4").getPath());

	/*
	 * Test to download file from GCP
	 */
	@Test
	public void downloadGCPStorageFile() throws StorageException, IOException {
		try {
			// String bucketPath =
			// "gs://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-3c8b-49d3-92fa-c2864d2a205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
			String bucketPath = submitUpload(testVideoFilePath, GCP_BUCKET_FILE_PATH);
			System.out.println("GCP BUCKET: " + bucketPath);
			// String expectedOutputPath =
			// "/tmp/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-3c8b-49d3-92fa-c2864d2a205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
			String expectedOutputPath = String.join(File.separator, StorageConstant.LOCAL_STORAGE_ROOT,
					StorageUtility.getBucketFileName(bucketPath));
			// delete file, if already exists
			removeFiles(bucketPath);
			repository.downloadFile(bucketPath);
			assertEquals(Paths.get(expectedOutputPath).toFile().exists(), true);
		} catch (StorageException | IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
	}

	/*
	 * Throws StorageException, if file does not exist on GCP Storage
	 */
	@Test
	public void downloadGCPStorageFileThrowsException() throws IOException {
		String bucketPath = "gs://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-5153c8b-49d3-92fa-c2864d2a545205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		assertThrows(StorageException.class, () -> repository.downloadFile(bucketPath));
	}

	/*
	 * Download File from Amazon Storage
	 */
	@Test
	public void downloadAmazonStorageFile() throws StorageException, IOException {
		// String bucketPath =
		// "s3://b-product-mock/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/video/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4";
		String bucketPath = submitUpload(testVideoFilePath, AWS_BUCKET_FILE_PATH);
		System.out.println("AWS BUCKET: " + bucketPath);
		// String expectedOutputPath =
		// "/tmp/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/video/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4";
		String expectedOutputPath = String.join(File.separator, StorageConstant.LOCAL_STORAGE_ROOT,
				StorageUtility.getBucketFileName(bucketPath));
		removeFiles(bucketPath);
		repository.downloadFile(bucketPath);
		assertEquals(Paths.get(expectedOutputPath).toFile().exists(), true);
	}

	/*
	 * Throws StorageException, if file does not exist on Amazon Storage
	 */
	@Test
	public void downloadAmazonStorageFileThrowsException() throws IOException, StorageException {
		String bucketPath = "s3://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-5153c8b-49d3-92fa-c2864d2a545205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		removeFiles(bucketPath);
		assertThrows(StorageException.class, () -> repository.downloadFile(bucketPath));
	}

	/*
	 * Throws StorageException for any other storage type, which is not supported
	 */
	@Test
	public void downloadOtherStorageThrowsException() {
		String bucketPath = "b2://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-5153c8b-49d3-92fa-c2864d2a545205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		assertThrows(StorageException.class, () -> repository.downloadFile(bucketPath));
	}

	/*
	 * upload file to GCP Storage
	 */
	@Test
	public void uploadGCPStorageFile() throws IOException, StorageException {
		String bucketFilePath = "gs://b-ao-product-mock/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/video/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4";
		removeFiles(bucketFilePath);
		Path localFilePath = Paths
				.get(getClass().getClassLoader().getResource("a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4").getPath());
		repository.uploadFile(bucketFilePath, localFilePath);
	}

	/*
	 * if bucket name is incorrect, throws StorageException
	 */
	@Test
	public void uploadGCPStorageFileThrowsException() throws IOException, StorageException {
		String bucketFilePath = "gs://b-ao-product-moc/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/video/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4";
		removeFiles(bucketFilePath);
		Path localFilePath = Paths
				.get(getClass().getClassLoader().getResource("a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4").getPath());
		assertThrows(StorageException.class, () -> repository.uploadFile(bucketFilePath, localFilePath));
	}

	/*
	 * upload file to Amazon Storage
	 */
	@Test
	public void uploadAmazonStorageFile() throws IOException, StorageException {
		String bucketFilePath = "s3://b-product-mock/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/video/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4";
		removeFiles(bucketFilePath);
		Path localFilePath = Paths
				.get(getClass().getClassLoader().getResource("a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4").getPath());
		repository.uploadFile(bucketFilePath, localFilePath);
	}

	/*
	 * if bucket name is incorrect, throws StorageException
	 */
	@Test
	public void uploadAmazonStorageFileThrowsException() throws IOException, StorageException {
		String bucketFilePath = "s3://b-ao-product-moc/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/video/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4";
		removeFiles(bucketFilePath);
		Path localFilePath = Paths
				.get(getClass().getClassLoader().getResource("a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4").getPath());
		assertThrows(StorageException.class, () -> repository.uploadFile(bucketFilePath, localFilePath));
	}

	/*
	 * Throws StorageException for any other storage type, which is not supported
	 */
	@Test
	public void uploadOtherStorageThrowsException() {
		String bucketPath = "b2://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-5153c8b-49d3-92fa-c2864d2a545205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		Path localFilePath = Paths
				.get(getClass().getClassLoader().getResource("a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4").getPath());
		assertThrows(StorageException.class, () -> repository.uploadFile(bucketPath, localFilePath));
	}

	@Test
	public void signedUrl() throws StorageException {
		String bucketFilePath = "gs://b-ao-product-moc/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/video/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4";
		Path localFilePath = Paths
				.get(getClass().getClassLoader().getResource("a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.mp4").getPath());
		assertThrows(StorageException.class, () -> repository.uploadFile(bucketFilePath, localFilePath));
		String signedUrl = repository.getSignedUrl(bucketFilePath, 10, "GET");
		System.out.println("Signed Url: " + signedUrl);
	}

	private String submitUpload(Path localFilePath, String BUCKET_FILE_PATH) throws IOException, StorageException {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		String bucketPath = String.format(BUCKET_FILE_PATH, uuid, uuid, uuid);
		System.out.println(bucketPath);
		repository.uploadFile(bucketPath, localFilePath);
		return bucketPath;
	}

	public void removeFiles(String bucketPath) throws IOException, StorageException {
		// delete file, if already exists
		if (Files.exists(Paths.get(String.join(File.separator, StorageConstant.LOCAL_STORAGE_ROOT,
				StorageUtility.getBucketFileGuid(bucketPath)))))
			Files.walk(Paths.get(String.join(File.separator, StorageConstant.LOCAL_STORAGE_ROOT,
					StorageUtility.getBucketFileGuid(bucketPath))), FileVisitOption.FOLLOW_LINKS)
					.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}

}

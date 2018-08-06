package org.cloud.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(SpringTestConstant.activeProfiles)
public class StorgageUtilityTest {

	/*
	 * Create file on local file system, based on given path
	 */
	@Test
	public void createLocalFileTest() throws IOException {
		Path jsonFilePath = Paths
				.get("/tmp/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207/a66f01d5-8cda-4f7e-8f5e-7b6f3dcd9207.json");
		// delete, if file already exist
		Files.deleteIfExists(jsonFilePath);

		StorageUtility.createLocalFile(jsonFilePath);

		// check if file exists
		assertEquals(jsonFilePath.toFile().exists(), true);
	}

	/*
	 * Test if it returns correct bucket name, for defined bucket storage url
	 */
	@Test
	public void getBucketTest() throws StorageException {
		String bucketPath = "gs://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-3c8b-49d3-92fa-c2864d2a205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		assertEquals(StorageUtility.getBucket(bucketPath), "b-ao-product-mock");
	}

	/*
	 * Throws Storage Exception, if Buket url is not correct
	 */
	@Test
	public void getBucketTestThrowsStorageException() {
		String randomText = "g/o-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/d2a205b.mp4";
		assertThrows(StorageException.class, () -> StorageUtility.getBucket(randomText));
	}

	/*
	 * Test, if method returns bucket file name
	 */
	public void getBucketFileNameTest() throws StorageException {
		String bucketPath = "gs://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-3c8b-49d3-92fa-c2864d2a205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		assertEquals(StorageUtility.getBucketFileName(bucketPath), "963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4");
	}

	/*
	 * Throws Storage Exception, if Buket url is not correct
	 */
	@Test
	public void getBucketFileNameTestThrowsStorageException() {
		String randomText = "g/o-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/d2a205b.mp4";
		assertThrows(StorageException.class, () -> StorageUtility.getBucketFileName(randomText));
	}

	/*
	 * Test, if method returns bucket storage type
	 */
	public void getBucketStorageTypeTest() throws StorageException {
		String bucketPath = "gs://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-3c8b-49d3-92fa-c2864d2a205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		assertEquals(StorageUtility.getBucketStorageType(bucketPath), "gs");
	}

	/*
	 * Throws Storage Exception, if Buket url is not correct
	 */
	@Test
	public void getBucketStorageTypeThrowsStorageException() {
		String randomText = "g/o-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/d2a205b.mp4";
		assertThrows(StorageException.class, () -> StorageUtility.getBucketStorageType(randomText));
	}

	/*
	 * Test, if method returns bucket file name
	 */
	public void getBucketFileGuidTest() throws StorageException {
		String bucketPath = "gs://b-ao-product-mock/963fea17-3c8b-49d3-92fa-c2864d2a205b/video/963fea17-3c8b-49d3-92fa-c2864d2a205b/963fea17-3c8b-49d3-92fa-c2864d2a205b.mp4";
		assertEquals(StorageUtility.getBucketFileGuid(bucketPath), "963fea17-3c8b-49d3-92fa-c2864d2a205b");
	}

	/*
	 * Throws Storage Exception, if Buket url is not correct
	 */
	@Test
	public void getBucketFileGuidThrowsStorageException() {
		String randomText = "g63fea17-3c8b-49d3-92fa-c2864d2a205b/d2a205b";
		assertThrows(StorageException.class, () -> StorageUtility.getBucketFileGuid(randomText));
	}
}

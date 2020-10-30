package com.capgemini;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import org.junit.Test;

import com.capgemini.FileOperations.FileUtils;
import com.capgemini.WatchService.Java8WatchService;

public class NIOFileAPITest {
	private static String HOME = "D:\\Capgemini Training";
	private static String PLAY_WITH_NIO = "TempPlayGround";

	@Test
	public void givenPathWhenCheckedThenConfirm() throws IOException {
		// Check File exists
		Path homePath = Paths.get(HOME);
		assertTrue(Files.exists(homePath));
		System.out.println(homePath);

		// Delete file and check file does not exist
		Path playPath = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		if (Files.exists(playPath))
			FileUtils.deleteFiles(playPath.toFile());
		assertTrue(Files.notExists(playPath));

		// Create a directory
		Files.createDirectory(playPath);
		assertTrue(Files.exists(playPath));

		// Create File
		IntStream.range(1, 10).forEach(cntr -> {
			Path tempFile = Paths.get(playPath + "/temp" + cntr);
			assertTrue(Files.notExists(tempFile));
			try {
				Files.createFile(tempFile);
			} catch (IOException e) {
			}
			assertTrue(Files.exists(tempFile));
		});

		// List files, directories as well as files with extensions
		System.out.println("Files.list");
		Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
		System.out.println("Files.newDirectory");
		Files.newDirectoryStream(playPath).forEach(System.out::println);
		System.out.println("Files.newDirectory with temp");
		Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().contains("temp"))
				.forEach(System.out::println);
	}

	public void givenADirectoryWhenWatchedListsAllTheActivities() throws IOException {
		Path dir = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		Files.list(dir).filter(Files::isRegularFile).forEach(System.out::println);
		new Java8WatchService(dir).processEvents();
	}
}

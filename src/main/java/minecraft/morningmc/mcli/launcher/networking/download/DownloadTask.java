package minecraft.morningmc.mcli.launcher.networking.download;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.networking.Requester;
import minecraft.morningmc.mcli.launcher.settings.NetworkSettings;
import minecraft.morningmc.mcli.utils.annotations.LauncherProcess;
import minecraft.morningmc.mcli.utils.exceptions.DownloadException;
import minecraft.morningmc.mcli.utils.interfaces.UniqueObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Represents a download task.
 * This class is used to download a file from a given URL to a specified destination.
 * It also stores a temporary file for the download process.
 * The destination file will be replaced by the temporary file if the download is successful.
 */
@LauncherProcess("download")
public class DownloadTask implements UniqueObject {
	private static final Logger logger = LogManager.getLogger();
	
	public final URL source;
	public final File destination;
	public final RandomAccessFile temp;
	public final long length;
	private final UUID identifier;
	
	/**
	 * Constructs a new {@link DownloadTask}.
	 *
	 * @param source      the URL of the file to be downloaded.
	 * @param destination the destination file.
	 * @param identifier  the {@link UUID} of the download task.
	 * @throws DownloadException if the download fails.
	 */
	public DownloadTask(URL source, File destination, UUID identifier) throws DownloadException {
		this.source = source;
		this.destination = destination;
		this.identifier = identifier;
		temp = getTemp();
		length = getLength();
	}
	
	/**
	 * Starts the download process.
	 */
	public void start() {
		short threadCount = (short) Math.min(NetworkSettings.maxThreads, length / ( NetworkSettings.minSizePerThread >> 10 /* convert KB to Bytes */ ));
	}
	
	/**
	 * Dispatches the download task to all the threads.
	 *
	 * @param threadCount The number of threads to use for the download.
	 */
	private void dispatch(short threadCount) {
	
	}
	
	@Override
	public UUID identifier() {
		return identifier;
	}
	
	/**
	 * Gets a {@link RandomAccessFile} for the temporary file.
	 * If the temp file cannot be created, it will use the destination file instead.
	 *
	 * @return the temporary file.
	 * @throws DownloadException if the temporary file cannot be created.
	 */
	private RandomAccessFile getTemp() throws DownloadException {
		try {
			File tempFile = new File(FileMetadata.cacheRoot, identifier.toString() + ".download.tmp");
			tempFile.createNewFile();
			return new RandomAccessFile(tempFile, "rwd");
		} catch (Exception e) {
			throw new DownloadException("Failed to create temp file", e);
		}
	}
	
	/**
	 * Gets the length of the file to be downloaded in Bytes.
	 *
	 * @return the length of the file to be downloaded.
	 */
	private long getLength() {
		try {
			return Requester.openConnection(source, Requester.Method.HEAD).getContentLengthLong();
		} catch (IOException e) {
			logger.warn("Failed to get the length of file: {}", e.getMessage());
			return -1;
		}
	}
}

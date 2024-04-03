package minecraft.morningmc.mcli.launcher.networking.download;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends Thread {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final URL source;
	private final RandomAccessFile target;
	private final int start;
	private final int end;
	private DownloadState downloadState;
	
	public DownloadTask(URL source, File target, int start, int end) throws FileNotFoundException {
		this.source = source;
		this.target = new RandomAccessFile(target, "rw");
		this.start = start;
		this.end = end;
	}
	
	public DownloadState getDownloadState() {
		return downloadState;
	}
	
	@Override
	public void run() {
		try {
			HttpURLConnection connection = (HttpURLConnection) source.openConnection();
			connection.setRequestProperty("Range", "bytes=%d-%d".formatted(start, end));
			int responseCode = connection.getResponseCode();
			
		} catch (Exception e) {
			downloadState = DownloadState.FAILED;
		}
	}
	
	public enum DownloadState {
		REQUESTING, RECEIVING, COMPLETE, FAILED, CANCELLED, PAUSED
	}
}

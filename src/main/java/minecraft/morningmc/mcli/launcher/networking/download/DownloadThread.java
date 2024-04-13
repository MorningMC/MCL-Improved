package minecraft.morningmc.mcli.launcher.networking.download;

import minecraft.morningmc.mcli.launcher.GlobalSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final URL source;
	private final RandomAccessFile target;
	private final long start;
	private final long end;
	private DownloadState downloadState;
	
	public DownloadThread(URL source, RandomAccessFile target, long start, long end) {
		this.source = source;
		this.target = target;
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
			connection.setConnectTimeout(GlobalSettings.instance.timeout().get());
			int responseCode = connection.getResponseCode();
			
			target.seek(start);
			
		} catch (Exception e) {
			downloadState = DownloadState.FAILED;
		}
	}
	
	public enum DownloadState {
		REQUESTING, RECEIVING, COMPLETE, FAILED, CANCELLED, PAUSED
	}
}

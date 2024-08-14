package minecraft.morningmc.mcli.launcher.networking.download;

import minecraft.morningmc.mcli.launcher.networking.ConnectionBuilder;
import minecraft.morningmc.mcli.launcher.networking.Requester;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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
			int responseCode = ConnectionBuilder.create(source)
					                   .header("Range", "bytes=%d-%d".formatted(start, end))
					                   .request()
					                   .code();
			
			target.seek(start);
			
		} catch (Exception e) {
			downloadState = DownloadState.FAILED;
		}
	}
	
	public enum DownloadState {
		REQUESTING, RECEIVING, COMPLETE, FAILED, CANCELLED, PAUSED
	}
}

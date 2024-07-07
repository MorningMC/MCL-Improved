package minecraft.morningmc.mcli.launcher.networking.download;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.utils.annotations.LauncherProcess;
import minecraft.morningmc.mcli.utils.interfaces.UniqueObject;

import java.io.*;
import java.net.URL;
import java.util.*;

@LauncherProcess("download")
public class DownloadTask implements UniqueObject {
	public final URL source;
	public final File destination;
	public final RandomAccessFile temp;
	private final UUID identifier;
	
	public DownloadTask(URL source, File destination, UUID identifier) {
		this.source = source;
		this.destination = destination;
		this.identifier = identifier;
		
		try {
			File temp = new File(FileMetadata.cacheRoot, identifier.toString() + ".download.tmp");
			temp.createNewFile();
			this.temp = new RandomAccessFile(temp, "rwd");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public UUID identifier() {
		return identifier;
	}
}

package minecraft.morningmc.mcli.launcher.networking.download;

import java.net.URL;

public record DownloadInfo(URL url, long size, String checksum) {
}

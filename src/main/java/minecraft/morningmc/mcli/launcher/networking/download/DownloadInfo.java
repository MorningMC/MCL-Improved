package minecraft.morningmc.mcli.launcher.networking.download;

import java.io.File;
import java.net.URL;

public record DownloadInfo(URL source, File destination, Long checksum) {
}

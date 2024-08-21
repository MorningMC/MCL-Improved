package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.utils.FileManager;
import minecraft.morningmc.mcli.minecraft.client.resources.Modification;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * Represents a directory for Minecraft-related files.
 */
public class MinecraftDirectory {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link MinecraftDirectory} objects from/to NBT data. */
	public static final NbtLoader<MinecraftDirectory, StringTag> loader = new NbtLoader<>() {
		
		@Override
		public MinecraftDirectory load(StringTag tag) {
			return new MinecraftDirectory(new File(tag.getValue()));
		}

		@Override
		public StringTag save(MinecraftDirectory object) {
			return new StringTag(object.root.getAbsolutePath());
		}
	};
	
	public final File root;
	public Set<Modification> mods = Collections.newSetFromMap(new ConcurrentHashMap<>());
	
	/**
	 * Constructs a {@link MinecraftDirectory} with a specified root directory.
	 *
	 * @param root The root directory for Minecraft-related files.
	 */
	public MinecraftDirectory(File root) {
		this.root = root;
		refresh();
	}
	
	/**
	 * Refreshes the resources.
	 * This method should be called whenever the resources changes or when the resources need to be reloaded.
	 */
	public void refresh() {
		// load mods
		File modsDir = new File(root, "mods");
		try {
			Arrays.stream(Objects.requireNonNull(modsDir.listFiles()))
					.parallel()
					.flatMap(mod -> {
						try {
							return Stream.of(new Modification(mod));
						} catch (Exception e) {
							logger.warn("Failed to parse mod from file {}: {}", mod.getAbsolutePath(), e.getMessage());
							return Stream.empty();
						}
					})
					.forEach(mods::add);
		} catch (Exception ignored) {}
	}
}

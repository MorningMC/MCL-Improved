package minecraft.morningmc.mcli.ui.settings;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.scene.text.Font;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.InputStream;
import java.util.*;

/**
 * Represents the style of fonts in the user interface.
 */
public class FontStyle {
	/** {@link NbtLoader} for loading and saving {@link FontStyle} objects from/to NBT data. */
	public static final NbtLoader<FontStyle, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public FontStyle load(CompoundTag tag) throws IllegalNbtException {
			return new FontStyle(
					NbtLoader.fontLoader.load(tag.getCompound("simple")),
					NbtLoader.fontLoader.load(tag.getCompound("bold")),
					NbtLoader.fontLoader.load(tag.getCompound("title")),
					NbtLoader.fontLoader.load(tag.getCompound("code"))
			);
		}
		
		@Override
		public CompoundTag save(FontStyle object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("simple", NbtLoader.fontLoader.save(object.simple));
			tag.put("bold", NbtLoader.fontLoader.save(object.bold));
			tag.put("title", NbtLoader.fontLoader.save(object.title));
			tag.put("code", NbtLoader.fontLoader.save(object.code));
			
			return tag;
		}
	};
	/** The mapping of builtin fonts and its resource path. */
	public static final Map<String, String> builtinFonts = Map.of(
			"Minecraft Regular", "assets/fonts/minecraft.otf",
			"Minecraft Bold", "assets/fonts/minecraft-bold.otf",
			"Minecraft Italic", "assets/fonts/minecraft-italic.otf",
			"Minecraft Bold Italic", "assets/fonts/minecraft-bold-italic.otf",
			"Minecraft AE Pixel", "assets/fonts/minecraftAE.ttf",
			"Minecraft Ten", "assets/fonts/minecraftTen.ttf"
	);
	/** The default {@link FontStyle} object. */
	public static final FontStyle defaultStyle = new FontStyle(
			loadFont(builtinFonts.get("Minecraft Regular"), 16),
			loadFont(builtinFonts.get("Minecraft Ten"), 16),
			loadFont(builtinFonts.get("Minecraft Ten"), 16),
			Font.font("Consolas", 16)
	);
	
	public Font simple;
	public Font bold;
	public Font title;
	public Font code;
	
	/**
	 * Constructs a new {@link FontStyle} object.
	 *
	 * @param simple The simple font.
	 * @param bold   The bold font.
	 * @param title  The title font.
	 * @param code   The code font.
	 */
	public FontStyle(Font simple, Font bold, Font title, Font code) {
		this.simple = simple;
		this.bold = bold;
		this.title = title;
		this.code = code;
	}
	
	/**
	 * Loads a font from the specified resource path with the given size.
	 *
	 * @param resource The resource path of the font.
	 * @param size     The size of the font.
	 * @return The loaded font, or {@code null} if an error occurred.
	 */
	public static Font loadFont(String resource, double size) {
		try (InputStream in = ClassLoader.getSystemResourceAsStream(resource)) {
			return Font.loadFont(in, size);
		} catch (Exception e) {
			return null;
		}
	}
}

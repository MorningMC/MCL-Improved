package minecraft.morningmc.mcli.ui.settings;

import minecraft.morningmc.mcli.utils.FileManager;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.scene.text.Font;

import dev.dewy.nbt.tags.collection.CompoundTag;

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
	
	public static final Font minecraft = Font.loadFont(ClassLoader.getSystemResourceAsStream("assets/fonts/minecraft.otf"), 16);
	public static final Font minecraftBold = Font.loadFont(ClassLoader.getSystemResourceAsStream("assets/fonts/minecraft-bold.otf"), 16);
	public static final Font minecraftItalic = Font.loadFont(ClassLoader.getSystemResourceAsStream("assets/fonts/minecraft-italic.otf"), 16);
	public static final Font minecraftBoldItalic = Font.loadFont(ClassLoader.getSystemResourceAsStream("assets/fonts/minecraft-bold-italic.otf"), 16);
	public static final Font minecraftAE = Font.loadFont(ClassLoader.getSystemResourceAsStream("assets/fonts/minecraftAE.ttf"), 16);
	public static final Font minecraftTen =  Font.loadFont(ClassLoader.getSystemResourceAsStream("assets/fonts/minecraftTen.ttf"), 16);
	
	public static final FontStyle defaultStyle = new FontStyle(
			minecraft,
			minecraftTen,
			minecraftTen,
			Font.font("Consolas", 16)
	);
	
	public Font simple;
	public Font bold;
	public Font title;
	public Font code;
	
	public FontStyle(Font simple, Font bold, Font title, Font code) {
		this.simple = simple;
		this.bold = bold;
		this.title = title;
		this.code = code;
	}
}

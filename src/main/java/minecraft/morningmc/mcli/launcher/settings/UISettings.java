package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.ui.settings.Background;
import minecraft.morningmc.mcli.ui.settings.ColorStyle;
import minecraft.morningmc.mcli.utils.WindowSize;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.containers.Enumerable;
import minecraft.morningmc.mcli.utils.containers.Switchable;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the UI settings.
 */
@StaticClass
public class UISettings {
	/** {@link NbtLoader} for loading and saving {@link UISettings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Void load(CompoundTag tag) throws IllegalNbtException {
			windowSize = WindowSize.loader.load(tag.getCompound("windowSize"));
			colorStyle = Enumerable.generateLoader(ColorStyle.loader, ColorStyle.Policy.class).load(tag.getCompound("colorStyle"));
			background = Switchable.generateLoader(Background.loader).load(tag.getCompound("background"));
			
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("windowSize", WindowSize.loader.save(windowSize));
			tag.put("colorStyle", Enumerable.generateLoader(ColorStyle.loader, ColorStyle.Policy.class).save(colorStyle));
			tag.put("background", Switchable.generateLoader(Background.loader).save(background));
			
			return tag;
		}
	};
	
	/** The size of the window in pixel */
	public static WindowSize windowSize;
	
	/** The color style. */
	public static Enumerable<ColorStyle, ColorStyle.Policy> colorStyle;
	
	/** The background settings. */
	public static Switchable<Background> background;
	
	/**
	 * Initializes the {@link UISettings} in default.
	 */
	public static void initDefault() {
		windowSize = WindowSize.windowed(1024, 632);
		colorStyle = Enumerable.of(ColorStyle.bright, ColorStyle.Policy.SYSTEM);
		background = Switchable.ofDisabled(Background.of(null, 1, 0));
	}
}
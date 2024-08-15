package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.ui.settings.*;
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
public class UISettings extends Settings {
	/** {@link NbtLoader} for loading and saving {@link UISettings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Void load(CompoundTag tag) throws IllegalNbtException {
			scale = tag.getDouble("scale").getValue();
			defaultWindowSize = WindowSize.loader.load(tag.getCompound("default_window_size"));
			colorStyle = Enumerable.generateLoader(ColorStyle.loader, ColorStyle.Policy.class).load(tag.getCompound("color_style"));
			fontStyle = FontStyle.loader.load(tag.getCompound("font_style"));
			background = Switchable.generateLoader(Background.loader).load(tag.getCompound("background"));
			constants = Constants.loader.load(tag.getCompound("constants"));
			
			init();
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putDouble("scale", scale);
			tag.put("default_window_size", WindowSize.loader.save(defaultWindowSize));
			tag.put("color_style", Enumerable.generateLoader(ColorStyle.loader, ColorStyle.Policy.class).save(colorStyle));
			tag.put("font_style", FontStyle.loader.save(fontStyle));
			tag.put("background", Switchable.generateLoader(Background.loader).save(background));
			tag.put("constants", Constants.loader.save(constants));
			
			return tag;
		}
	};
	
	/** The scaling size. */
	public static double scale;
	
	/** The default window size of the windows. */
	public static WindowSize defaultWindowSize;
	
	/** The color style. */
	public static Enumerable<ColorStyle, ColorStyle.Policy> colorStyle;
	
	/** The font style. */
	public static FontStyle fontStyle;
	
	/** The background settings. */
	public static Switchable<Background> background;
	
	/** The constants of the UI. */
	public static Constants constants;
	
	/**
	 * Initializes the {@link UISettings}.
	 */
	public static void init() {
		colorStyle.switcher = (value, policy) -> switch (policy) {
			case SYSTEM -> ColorStyle.followSystem();
			case BRIGHT -> ColorStyle.bright;
			case DARK -> ColorStyle.dark;
			case CUSTOM -> value;
		};
	}
	
	/**
	 * Initializes the {@link UISettings} in default.
	 */
	public static void initDefault() {
		scale = 1;
		defaultWindowSize = WindowSize.window(1311, 810);
		colorStyle = Enumerable.of(ColorStyle.bright, ColorStyle.Policy.SYSTEM);
		fontStyle = FontStyle.defaultStyle;
		background = Switchable.ofDisabled(Background.of(null, 1, 0));
		constants = Constants.initDefault();
		
		init();
	}
}
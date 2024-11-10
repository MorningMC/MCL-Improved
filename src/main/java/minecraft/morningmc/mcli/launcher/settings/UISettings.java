package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.ui.settings.*;
import minecraft.morningmc.mcli.minecraft.launch.options.WindowSize;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.containers.Enumerable;
import minecraft.morningmc.mcli.utils.containers.Switchable;
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
		public Void load(CompoundTag tag) {
			defaultWindowSize = WindowSize.loader.load(tag.getCompound("default_window_size"));
			colorStyle = Enumerable.generateLoader(ColorStyle.loader, ColorStyle.Policy.class).load(tag.getCompound("color_style"));
			fontStyle = FontStyle.loader.load(tag.getCompound("font_style"));
			background = Switchable.generateLoader(Background.loader).load(tag.getCompound("background"));
			titleHeight = tag.getByte("title_height").getValue();
			resizeAreaThickness = tag.getByte("resize_area_thickness").getValue();
			edgeThickness = tag.getByte("edge_thickness").getValue();
			bottomThickness = tag.getByte("bottom_thickness").getValue();
			iconSizeFactor = tag.getDouble("icon_size_factor").getValue();
			shadowOffset = tag.getByte("shadow_offset").getValue();
			
			init();
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("default_window_size", WindowSize.loader.save(defaultWindowSize));
			tag.put("color_style", Enumerable.generateLoader(ColorStyle.loader, ColorStyle.Policy.class).save(colorStyle));
			tag.put("font_style", FontStyle.loader.save(fontStyle));
			tag.put("background", Switchable.generateLoader(Background.loader).save(background));
			tag.putByte("title_height", titleHeight);
			tag.putByte("resize_area_thickness", resizeAreaThickness);
			tag.putByte("edge_thickness", edgeThickness);
			tag.putByte("bottom_thickness", bottomThickness);
			tag.putDouble("icon_size_factor", iconSizeFactor);
			tag.putByte("shadow_offset", shadowOffset);
			
			return tag;
		}
	};
	
	/** The default window size of the windows. */
	public static WindowSize defaultWindowSize;
	
	/** The color style. */
	public static Enumerable<ColorStyle, ColorStyle.Policy> colorStyle;
	
	/** The font style. */
	public static FontStyle fontStyle;
	
	/** The background settings. */
	public static Switchable<Background> background;
	
	// Window
	/** The height of the title bar in pixel. */
	public static byte titleHeight;
	
	/** The thickness of the resize area of a window. */
	public static byte resizeAreaThickness;
	
	// Button
	/** The thickness of the edge of a button in pixel. */
	public static byte edgeThickness;
	
	/** The thickness of the bottom of a button in pixel. */
	public static byte bottomThickness;
	
	/** The factor of the size of the icon for the button. */
	public static double iconSizeFactor;
	
	// Text
	/** The offset of the shadow of a text in pixel. */
	public static byte shadowOffset;
	
	/**
	 * Initializes the {@link UISettings}.
	 */
	public static void init() {
		colorStyle.switcher = (value, policy) -> switch (policy) {
			case BRIGHT -> ColorStyle.bright;
			case DARK -> ColorStyle.dark;
			case SYSTEM -> ColorStyle.followSystem();
			default -> value;
		};
	}
	
	/**
	 * Initializes the {@link UISettings} in default.
	 */
	public static void initDefault() {
		defaultWindowSize = WindowSize.window(1311, 810);
		colorStyle = Enumerable.of(ColorStyle.bright, ColorStyle.Policy.SYSTEM);
		fontStyle = FontStyle.defaultStyle;
		background = Switchable.ofDisabled(Background.of(null, 1, 0));
		titleHeight = 48;
		resizeAreaThickness = 3;
		edgeThickness = 3;
		bottomThickness = 6;
		iconSizeFactor = 0.5;
		shadowOffset = 2;
		
		init();
	}
}
package minecraft.morningmc.mcli.ui.settings;

import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import javafx.scene.paint.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the color style settings for various UI components in the launcher.
 * This class holds the color configurations for buttons, text, and other elements.
 */
public final class ColorStyle {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link ColorStyle} objects from/to NBT data. */
	public static final NbtLoader<ColorStyle, CompoundTag> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@link ColorStyle} object from the provided {@link CompoundTag}.
		 *
		 * @param tag The NBT tag containing the color style data.
		 * @return A new {@link ColorStyle} instance loaded from the NBT data.
		 * @throws IllegalNbtException if the NBT data is invalid or incomplete.
		 */
		@Override
		public ColorStyle load(CompoundTag tag) throws IllegalNbtException {
			return new ColorStyle(
					ButtonStyle.loader.load(tag.getCompound("simpleButton")),
					ButtonStyle.loader.load(tag.getCompound("background")),
					ButtonStyle.loader.load(tag.getCompound("title")),
					ButtonStyle.loader.load(tag.getCompound("launchButton")),
					ButtonStyle.loader.load(tag.getCompound("accountButton")),
					NbtLoader.colorLoader.load(tag.getCompound("text")),
					NbtLoader.colorLoader.load(tag.getCompound("textShadow")),
					ButtonStyle.loader.load(tag.getCompound("textEntry"))
			);
		}
		
		/**
		 * Saves the provided {@link ColorStyle} object to a {@link CompoundTag}.
		 *
		 * @param object The {@link ColorStyle} object to save.
		 * @return A {@link CompoundTag} containing the color style data.
		 */
		@Override
		public CompoundTag save(ColorStyle object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("simpleButton", ButtonStyle.loader.save(object.simpleButton));
			tag.put("background", ButtonStyle.loader.save(object.background));
			tag.put("title", ButtonStyle.loader.save(object.title));
			tag.put("launchButton", ButtonStyle.loader.save(object.launchButton));
			tag.put("accountButton", ButtonStyle.loader.save(object.accountButton));
			tag.put("text", NbtLoader.colorLoader.save(object.text));
			tag.put("textShadow", NbtLoader.colorLoader.save(object.textShadow));
			tag.put("textEntry", ButtonStyle.loader.save(object.textEntry));
			
			return tag;
		}
	};
	
	/** The preset bright color style. */
	public static final ColorStyle bright = new ColorStyle(
			ButtonStyle.ofSimple(Color.rgb(208, 209, 212)),
			ButtonStyle.ofSimple(Color.rgb(208, 209, 212)),
			ButtonStyle.ofSimple(Color.rgb(208, 209, 212)),
			ButtonStyle.ofSimple(Color.rgb(60, 133, 39)),
			ButtonStyle.ofSimple(Color.rgb(115, 69, 229)),
			Color.rgb(0, 0, 0),
			Color.rgb(255, 255, 255, 0.5),
			ButtonStyle.ofSimple(Color.rgb(185, 186, 189))
	);
	
	/** The preset dark color style. */
	public static final ColorStyle dark = new ColorStyle(
			ButtonStyle.ofSimple(Color.rgb(72, 73, 74)),
			ButtonStyle.ofSimple(Color.rgb(72, 73, 74)),
			ButtonStyle.ofSimple(Color.rgb(72, 73, 74)),
			ButtonStyle.ofSimple(Color.rgb(60, 133, 39)),
			ButtonStyle.ofSimple(Color.rgb(115, 69, 229)),
			Color.rgb(255, 255, 255),
			Color.rgb(255, 255, 255, 0.5),
			ButtonStyle.ofSimple(Color.rgb(49, 50, 51))
	);
	
	public ButtonStyle simpleButton;
	public ButtonStyle background;
	public ButtonStyle title;
	public ButtonStyle launchButton;
	public ButtonStyle accountButton;
	public Color text;
	public Color textShadow;
	public ButtonStyle textEntry;
	
	/**
	 * Constructs a new {@link ColorStyle} instance with the specified styles and colors.
	 *
	 * @param simpleButton  The style for the simple button.
	 * @param background    The style for the background.
	 * @param title         The style for the title.
	 * @param launchButton  The style for the launch button.
	 * @param accountButton The style for the account button.
	 * @param text          The color of the text.
	 * @param textShadow    The color of the text shadow.
	 * @param textEntry     The style for the text entry.
	 */
	public ColorStyle(ButtonStyle simpleButton,
	                  ButtonStyle background,
	                  ButtonStyle title,
	                  ButtonStyle launchButton,
	                  ButtonStyle accountButton,
	                  Color text,
	                  Color textShadow,
	                  ButtonStyle textEntry) {
		this.simpleButton = simpleButton;
		this.background = background;
		this.title = title;
		this.launchButton = launchButton;
		this.accountButton = accountButton;
		this.text = text;
		this.textShadow = textShadow;
		this.textEntry = textEntry;
	}
	
	/**
	 * Gets the color style that matches the system's color scheme.
	 *
	 * @return The color style for the system's color scheme.
	 */
	public static ColorStyle followSystem() {
		try {
			return switch (Platform.system.operatingSystem()) {
				case WINDOWS -> {
					Process process = Runtime.getRuntime().exec(new String[]{ "reg", "query", "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "/v", "AppsUseLightTheme" });
					yield new String(process.getInputStream().readAllBytes()).contains("0x0") ? dark : bright;
				}
				case MACOS -> {
					Process process = Runtime.getRuntime().exec(new String[]{ "defaults", "read", "-g", "AppleInterfaceStyle" });
					yield new String(process.getInputStream().readAllBytes()).toLowerCase().contains("dark") ? dark : bright;
				}
				case LINUX -> {
					Process process = Runtime.getRuntime().exec(new String[]{ "gsettings", "get", "org.gnome.desktop.interface", "gtk-theme" });
					yield new String(process.getInputStream().readAllBytes()).toLowerCase().contains("dark") ? dark : bright;
				}
				default -> {
					logger.warn("Unknown operating system, use bright default.");
					yield bright;
				}
			};
		} catch (Exception e) {
			logger.warn("Unable to determine system color scheme, use bright default: {}", e.getMessage());
			return bright;
		}
	}
	
	/**
	 * Represents a color style for a button.
	 * This record holds information about the major color, edge color, and bottom color of a button,
	 * along with whether the button should use a detailed style or not.
	 *
	 * @param major    The major color of the button. This color is the primary color of the button's appearance.
	 * @param edge     The edge color of the button. This color is used as the border or outline color when the button is in detailed mode.
	 * @param bottom   The bottom color of the button. This color is used for shading or highlighting the bottom portion of the button when in detailed mode.
	 */
	public record ButtonStyle(Color major, Color edge, Color bottom) {
		
		/** {@link NbtLoader} for loading and saving {@link ButtonStyle} objects from/to NBT data. */
		public static final NbtLoader<ButtonStyle, CompoundTag> loader = new NbtLoader<>() {
			
			/**
			 * Loads a {@link ButtonStyle} object from the provided {@link CompoundTag}.
			 *
			 * @param tag The NBT tag containing the button style data.
			 * @return A new {@link ButtonStyle} instance loaded from the NBT data.
			 * @throws IllegalNbtException if the NBT data is invalid or incomplete.
			 */
			@Override
			public ButtonStyle load(CompoundTag tag) throws IllegalNbtException {
				return new ButtonStyle(
						NbtLoader.colorLoader.load(tag.getCompound("major")),
						NbtLoader.colorLoader.load(tag.getCompound("edge")),
						NbtLoader.colorLoader.load(tag.getCompound("bottom"))
				);
			}
			
			/**
			 * Saves the provided {@link ButtonStyle} object to a {@link CompoundTag}.
			 *
			 * @param object The {@link ButtonStyle} object to save.
			 * @return A {@link CompoundTag} containing the button style data.
			 */
			@Override
			public CompoundTag save(ButtonStyle object) {
				CompoundTag tag = new CompoundTag();
				
				tag.put("major", NbtLoader.colorLoader.save(object.major));
				tag.put("edge", NbtLoader.colorLoader.save(object.edge));
				tag.put("bottom", NbtLoader.colorLoader.save(object.bottom));
				
				return tag;
			}
		};
		
		/**
		 * Creates a new {@link ButtonStyle} instance with the specified major color,
		 * detailed mode flag, edge color, and bottom color.
		 *
		 * @param major    The major color of the button.
		 * @param edge     The edge color of the button.
		 * @param bottom   The bottom color of the button.
		 * @return A new {@link ButtonStyle} instance.
		 */
		public static ButtonStyle of(Color major, Color edge, Color bottom) {
			return new ButtonStyle(major, edge, bottom);
		}
		
		/**
		 * Creates a new simple {@link ButtonStyle} instance with the specified major color.
		 * The edge and bottom colors will be derived from the major color.
		 *
		 * @param major The major color of the button.
		 * @return A new simple {@link ButtonStyle} instance.
		 */
		public static ButtonStyle ofSimple(Color major) {
			return new ButtonStyle(major, major.brighter(), major.darker().darker());
		}
	}
	
	/**
	 * Enumerates the different color style policies available.
	 */
	public enum Policy {
		BRIGHT, DARK, SYSTEM, CUSTOM
	}
}
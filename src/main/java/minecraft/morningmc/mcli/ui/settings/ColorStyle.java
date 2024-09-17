package minecraft.morningmc.mcli.ui.settings;

import javafx.scene.Cursor;
import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.Builder;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;

import dev.dewy.nbt.tags.collection.CompoundTag;

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
					ButtonStyle.loader.load(tag.getCompound("simple")),
					ButtonStyle.loader.load(tag.getCompound("launch")),
					ButtonStyle.loader.load(tag.getCompound("account")),
					ButtonStyle.loader.load(tag.getCompound("background")),
					ButtonStyle.loader.load(tag.getCompound("title")),
					ButtonStyle.loader.load(tag.getCompound("close")),
					NbtLoader.colorLoader.load(tag.getInt("text")),
					NbtLoader.colorLoader.load(tag.getInt("text_shadow")),
					ButtonStyle.loader.load(tag.getCompound("text_entry"))
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
			
			tag.put("simple", ButtonStyle.loader.save(object.simple));
			tag.put("launch", ButtonStyle.loader.save(object.launch));
			tag.put("account", ButtonStyle.loader.save(object.account));
			tag.put("background", ButtonStyle.loader.save(object.background));
			tag.put("title", ButtonStyle.loader.save(object.title));
			tag.put("close", ButtonStyle.loader.save(object.close));
			tag.put("text", NbtLoader.colorLoader.save(object.text));
			tag.put("text_shadow", NbtLoader.colorLoader.save(object.textShadow));
			tag.put("text_entry", ButtonStyle.loader.save(object.textEntry));
			
			return tag;
		}
	};
	
	/** The preset bright color style. */
	public static final ColorStyle bright = new ColorStyle(
			ButtonStyle.ofSimple(Color.rgb(208, 209, 212)),
			ButtonStyle.ofSimple(Color.rgb(60, 133, 39)),
			ButtonStyle.ofSimple(Color.rgb(115, 69, 229)),
			ButtonStyle.ofSimple(Color.rgb(146, 146, 148)),
			ButtonStyle.ofSimple(Color.rgb(208, 209, 212)),
			ButtonStyle.ofSimple(Color.rgb(202, 54, 54)),
			Color.rgb(0, 0, 0),
			Color.rgb(0, 0, 0, 0.4),
			ButtonStyle.ofSimple(Color.rgb(185, 186, 189))
	);
	
	/** The preset dark color style. */
	public static final ColorStyle dark = new ColorStyle(
			ButtonStyle.ofSimple(Color.rgb(49, 50, 51)),
			ButtonStyle.ofSimple(Color.rgb(60, 133, 39)),
			ButtonStyle.ofSimple(Color.rgb(115, 69, 229)),
			ButtonStyle.ofSimple(Color.rgb(72, 73, 74)),
			ButtonStyle.ofSimple(Color.rgb(49, 50, 51)),
			ButtonStyle.ofSimple(Color.rgb(202, 54, 54)),
			Color.rgb(255, 255, 255),
			Color.rgb(0, 0, 0, 0.4),
			ButtonStyle.ofSimple(Color.rgb(49, 50, 51))
	);
	
	public ButtonStyle simple;
	public ButtonStyle launch;
	public ButtonStyle account;
	public ButtonStyle background;
	public ButtonStyle title;
	public ButtonStyle close;
	public Color text;
	public Color textShadow;
	public ButtonStyle textEntry;
	
	/**
	 * Constructs a new {@link ColorStyle} instance with the specified styles and colors.
	 *
	 * @param simple        The style for the simple button.
	 * @param launch        The style for the launch button.
	 * @param account       The style for the account button.
	 * @param background    The style for the background.
	 * @param title         The style for the title.
	 * @param close         The style for the close button.
	 * @param text          The color of the text.
	 * @param textShadow    The color of the text shadow.
	 * @param textEntry     The style for the text entry.
	 */
	public ColorStyle(ButtonStyle simple,
	                  ButtonStyle launch,
	                  ButtonStyle account,
	                  ButtonStyle background,
	                  ButtonStyle title,
	                  ButtonStyle close,
	                  Color text,
	                  Color textShadow,
	                  ButtonStyle textEntry) {
		this.simple = simple;
		this.launch = launch;
		this.account = account;
		this.background = background;
		this.title = title;
		this.close = close;
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
					logger.warn("Unknown operating system, use bright style default.");
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
						NbtLoader.colorLoader.load(tag.getInt("major")),
						NbtLoader.colorLoader.load(tag.getInt("edge")),
						NbtLoader.colorLoader.load(tag.getInt("bottom"))
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
			return of(major, major.brighter(), major.darker().darker());
		}
		
		/**
		 * Generates an {@link Renderer} representing the button style.
		 *
		 * @param width  The width of the {@link AnchorPane} in pixel.
		 * @param height The height of the {@link AnchorPane} in pixel.
		 * @return An {@link Renderer} instance.
		 */
		public Renderer render(int width, int height) {
			return new Renderer(this, width, height);
		}
		
		/**
		 * A builder for rendering an {@link AnchorPane} representing the button style.
		 */
		public static class Renderer implements Builder<AnchorPane> {
			private final ButtonStyle style;
			private int width;
			private int height;
			private Image icon = null;
			private String text = "";
			private Font font = Font.getDefault();
			private Cursor cursor = Cursor.DEFAULT;
			private Cursor cursorPressed = null;
			private EventHandler<MouseEvent> clickHandler = event -> {};
			private EventHandler<MouseEvent> pressHandler = event -> {};
			private EventHandler<MouseEvent> dragHandler = event -> {};
			
			/**
			 * Constructs a new {@link Renderer} instance.
			 *
			 * @param style  The button style for the button.
			 * @param width  The width of the button in pixel.
			 * @param height The height of the button in pixel.
			 */
			public Renderer(ButtonStyle style, int width, int height) {
				this.style = style;
				this.width = width;
				this.height = height;
			}
			
			/**
			 * Sets the size of the button.
			 *
			 * @param width  The width of the button in pixel.
			 * @param height The height of the button in pixel.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer size(int width, int height) {
				this.width = width;
				this.height = height;
				return this;
			}
			
			/**
			 * Sets the icon for the button.
			 *
			 * @param icon The icon to be displayed on the button.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer icon(Image icon) {
				this.icon = icon;
				return this;
			}
			
			/**
			 * Sets the text and font for the button.
			 *
			 * @param text The text to be displayed on the button.
			 * @param font The font to be used for the text.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer text(String text, Font font) {
				this.text = text;
				this.font = font;
				return this;
			}
			
			/**
			 * Sets the cursor for the button.
			 *
			 * @param cursor The cursor to be set.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer cursor(Cursor cursor) {
				this.cursor = cursor;
				return this;
			}
			
			/**
			 * Sets the cursor for the button when it is pressed.
			 *
			 * @param cursorPressed The cursor to be set when the button is pressed.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer cursorPressed(Cursor cursorPressed) {
				this.cursorPressed = cursorPressed;
				return this;
			}
			
			/**
			 * Sets the event handler for the button click event.
			 *
			 * @param clickHandler The event handler to be set.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer clickHandler(EventHandler<MouseEvent> clickHandler) {
				this.clickHandler = clickHandler;
				return this;
			}
			
			/**
			 * Sets the event handler for the button press event.
			 *
			 * @param pressHandler The event handler to be set.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer pressHandler(EventHandler<MouseEvent> pressHandler) {
				this.pressHandler = pressHandler;
				return this;
			}
			
			/**
			 * Sets the event handler for the button drag event.
			 *
			 * @param dragHandler The event handler to be set.
			 * @return The current {@link Renderer} instance.
			 */
			public Renderer dragHandler(EventHandler<MouseEvent> dragHandler) {
				this.dragHandler = dragHandler;
				return this;
			}
			
			/**
			 * Builds the {@link AnchorPane} representing the button style.
			 *
			 * @return The {@link AnchorPane} representing the button style.
			 */
			@Override
			public AnchorPane build() {
				// button render
				Button button = new Button();
				button.setPrefSize(width, height);
				button.setOpacity(0); // the button should be hided in order to show the structure behind
				button.setCursor(cursor);
				if (cursorPressed != null) {
					button.setOnMousePressed(event -> {
						pressHandler.handle(event);
						button.setCursor(cursorPressed);
					});
					button.setOnMouseReleased(event -> button.setCursor(cursor));
				} else {
					button.setOnMousePressed(pressHandler);
				}
				button.setOnMouseClicked(clickHandler);
				button.setOnMouseDragged(dragHandler);
				
				// basic shapes
				Rectangle majorRect = new Rectangle(width - UISettings.edgeThickness * 2, height - UISettings.edgeThickness * 2 - UISettings.bottomThickness, style.major);
				Rectangle edgeRect = new Rectangle(width, height - UISettings.bottomThickness, style.edge);
				Rectangle bottomRect = new Rectangle(width, UISettings.bottomThickness, style.bottom);
				
				// text render
				Text textView = new Text(text);
				textView.setFont(font);
				textView.setFill(UISettings.colorStyle.getSwitch().text);
				textView.setTextAlignment(TextAlignment.CENTER);
				StackPane textPane = new StackPane(textView);
				textPane.setPrefSize(width, height - UISettings.bottomThickness);
				
				// text shadow render
				Text shadowView = new Text(text);
				shadowView.setFont(font);
				shadowView.setFill(UISettings.colorStyle.getSwitch().textShadow);
				shadowView.setTextAlignment(TextAlignment.CENTER);
				StackPane shadowPane = new StackPane(shadowView);
				shadowPane.setPrefSize(width - UISettings.shadowOffset * 2, height - UISettings.shadowOffset * 2 - UISettings.bottomThickness);
				
				AnchorPane pane = new AnchorPane(edgeRect, majorRect, bottomRect, shadowPane, textPane, button);
				pane.setPrefSize(width, height);
				AnchorPane.setTopAnchor(majorRect, UISettings.edgeThickness * 1.);
				AnchorPane.setLeftAnchor(majorRect, UISettings.edgeThickness * 1.);
				AnchorPane.setTopAnchor(bottomRect, height - UISettings.bottomThickness * 1.);
				AnchorPane.setTopAnchor(shadowPane, UISettings.shadowOffset * 2.);
				AnchorPane.setLeftAnchor(shadowPane, UISettings.shadowOffset * 2.);
				
				// icon render
				if (icon != null) {
					final double length = Math.min(width, height - UISettings.bottomThickness);
					final double iconSize = length * UISettings.iconSizeFactor;
					
					ImageView iconView = new ImageView(icon);
					iconView.setFitWidth(iconSize);
					iconView.setFitHeight(iconSize);
					pane.getChildren().add(pane.getChildren().size() - 1, iconView);
					AnchorPane.setTopAnchor(iconView, (length - iconSize) / 2);
					AnchorPane.setLeftAnchor(iconView, (length - iconSize) / 2);
				}
				return pane;
			}
		}
	}
	
	/**
	 * Enumerates the different color style policies available.
	 */
	public enum Policy {
		BRIGHT, DARK, SYSTEM, CUSTOM
	}
}
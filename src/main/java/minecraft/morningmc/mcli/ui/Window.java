package minecraft.morningmc.mcli.ui;

import minecraft.morningmc.mcli.launcher.Metadata;
import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.ui.settings.Background;
import minecraft.morningmc.mcli.minecraft.launch.options.WindowSize;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * Represents a customizable window in the MCLI UI.
 * It provides functionality for creating a window, managing its size and position, and handling user interactions such as resizing and dragging.
 */
public class Window {
	private static final Logger logger = LogManager.getLogger();
	
	private final Stage stage;
	private final SizeManager.Token sizeToken;
	private double dragOffsetX;
	private double dragOffsetY;
	private double resizeOldX;
	private double resizeOldY;
	
	/**
	 * Constructs a new {@link Window} instance.
	 *
	 * @param stage      The {@link Stage} instance associated with this window.
	 * @param sizeToken  The token used to manage the window size.
	 * @param title      The title of the window.
	 */
	public Window(Stage stage, SizeManager.Token sizeToken, String title) {
		this.stage = stage;
		this.sizeToken = sizeToken;
		
		stage.setTitle(title);
		stage.getIcons().add(UIManager.icon);
		stage.initStyle(StageStyle.TRANSPARENT);
		
		resize(SizeManager.get(sizeToken));
		
		stage.widthProperty().addListener((obs, old, ne) -> resize(SizeManager.get(sizeToken).width(ne.intValue())));
		stage.heightProperty().addListener((obs, old, ne) -> resize(SizeManager.get(sizeToken).height(ne.intValue())));
		stage.maximizedProperty().addListener((obs, old, ne) -> resize(SizeManager.get(sizeToken).fullScreen(ne)));
		
		stage.show();
	}
	
	/**
	 * Creates a new window.
	 *
	 * @param sizeToken  The token used to manage the window size.
	 * @param title      The title of the window.
	 * @return           A new {@link Window} instance.
	 */
	public static Window create(SizeManager.Token sizeToken, String title) {
		return new Window(new Stage(), sizeToken, title);
	}
	
	/**
	 * Resizes the window based on the specified window size.
	 *
	 * @param size the new size of the window.
	 */
	public void resize(WindowSize size) {
		SizeManager.set(sizeToken, size);
		
		stage.setWidth(size.width());
		stage.setHeight(size.height());
		stage.setMaximized(size.fullScreen());
		
		// render the window
		WindowSize currentSize = SizeManager.get(sizeToken);
		
		// render title bar
		AnchorPane title = UISettings.colorStyle.getSwitch().title.render(currentSize.width(), UISettings.titleHeight)
				                   .text(Metadata.fullName, UISettings.fontStyle.title)
				                   .icon(UIManager.icon)
				                   .cursorPressed(Cursor.MOVE)
				                   .pressHandler(event -> {
									   dragOffsetX = event.getSceneX();
									   dragOffsetY = event.getSceneY();
				                   })
				                   .dragHandler(event -> {
									   stage.setX(event.getScreenX() - dragOffsetX);
									   stage.setY(event.getScreenY() - dragOffsetY);
				                   })
				                   .build();
		AnchorPane close = UISettings.colorStyle.getSwitch().close.render(UISettings.titleHeight, UISettings.titleHeight)
				                   .text("X", UISettings.fontStyle.title)
				                   .cursor(Cursor.HAND)
				                   .clickHandler(event -> stage.close())
				                   .build();
		AnchorPane maximize = UISettings.colorStyle.getSwitch().title.render(UISettings.titleHeight, UISettings.titleHeight)
				                      .text("O", UISettings.fontStyle.title)
				                      .cursor(Cursor.HAND)
				                      .clickHandler(event -> resize(currentSize.fullScreen(!stage.isMaximized())))
				                      .build();
		AnchorPane minimize = UISettings.colorStyle.getSwitch().title.render(UISettings.titleHeight, UISettings.titleHeight)
				                      .text("_", UISettings.fontStyle.title)
				                      .cursor(Cursor.HAND)
				                      .clickHandler(event -> stage.setIconified(true))
				                      .build();
		AnchorPane titleBar = new AnchorPane(title, close, maximize, minimize);
		AnchorPane.setLeftAnchor(close, currentSize.width() - UISettings.titleHeight * 1.);
		AnchorPane.setLeftAnchor(maximize, currentSize.width() - UISettings.titleHeight * 2.);
		AnchorPane.setLeftAnchor(minimize, currentSize.width() - UISettings.titleHeight * 3. + UISettings.edgeThickness);
		
		// render background
		AnchorPane background = UISettings.colorStyle.getSwitch().background.render(currentSize.width(), currentSize.height() - UISettings.titleHeight)
				                   .build();
		ImageView backgroundView = UISettings.background.getIfEnabled(Background.of(null, 0, 0)).render(currentSize.width(), currentSize.height() - UISettings.titleHeight);
		
		AnchorPane root = new AnchorPane(titleBar, background, backgroundView);
		root.setPrefSize(currentSize.width(), currentSize.height());
		AnchorPane.setTopAnchor(background, UISettings.titleHeight * 1.);
		AnchorPane.setTopAnchor(backgroundView, UISettings.titleHeight * 1.);
		
		// render resize area
		if (!currentSize.fullScreen()) { // ignore resize area when maximized
			// render top-left resize area
			Button topLeftArea = renderResizeArea(
					UISettings.resizeAreaThickness,
					UISettings.resizeAreaThickness,
					Cursor.NW_RESIZE,
					event -> {
						dragOffsetX = event.getSceneX();
						dragOffsetY = event.getSceneY();
						resizeOldX = stage.getX();
						resizeOldY = stage.getY();
					},
					event -> {
						stage.setX(event.getScreenX() - dragOffsetX);
						stage.setY(event.getScreenY() - dragOffsetY);
						resize(WindowSize.window(
								currentSize.width() + (int) resizeOldX - (int) event.getScreenX() + (int) dragOffsetX,
								currentSize.height() + (int) resizeOldY - (int) event.getScreenY() + (int) dragOffsetY
						));
					}
			);
			
			// render top resize area
			Button topArea = renderResizeArea(
					currentSize.width() - UISettings.resizeAreaThickness * 2,
					UISettings.resizeAreaThickness,
					Cursor.N_RESIZE,
					event -> {
						dragOffsetY = event.getSceneY();
						resizeOldY = stage.getY();
					},
					event -> {
						stage.setY(event.getScreenY() - dragOffsetY);
						resize(WindowSize.window(
								currentSize.width(),
								currentSize.height() + (int) resizeOldY - (int) event.getScreenY() + (int) dragOffsetY
						));
					}
			);
			
			// render top-right resize area
			Button topRightArea = renderResizeArea(
					UISettings.resizeAreaThickness,
					UISettings.resizeAreaThickness,
					Cursor.NE_RESIZE,
					event -> {
						dragOffsetX = event.getSceneX();
						dragOffsetY = event.getSceneY();
						resizeOldX = stage.getX();
						resizeOldY = stage.getY();
					},
					event -> {
						stage.setY(event.getScreenY() - dragOffsetY);
						resize(WindowSize.window(
								currentSize.width() - (int) resizeOldX + (int) event.getScreenX() - (int) dragOffsetX,
								currentSize.height() + (int) resizeOldY - (int) event.getScreenY() + (int) dragOffsetY
						));
					}
			);
			
			// render right resize area
			Button rightArea = renderResizeArea(
					UISettings.resizeAreaThickness,
					currentSize.height() - UISettings.resizeAreaThickness * 2,
					Cursor.E_RESIZE,
					event -> {
						dragOffsetX = event.getSceneX();
						resizeOldX = stage.getX();
					},
					event -> resize(WindowSize.window(
							currentSize.width() - (int) resizeOldX + (int) event.getScreenX() - (int) dragOffsetX,
							currentSize.height()
					))
			);
			
			// render bottom-right resize area
			Button bottomRightArea = renderResizeArea(
					UISettings.resizeAreaThickness,
					UISettings.resizeAreaThickness,
					Cursor.SE_RESIZE,
					event -> {
						dragOffsetX = event.getSceneX();
						dragOffsetY = event.getSceneY();
						resizeOldX = stage.getX();
						resizeOldY = stage.getY();
					},
					event -> resize(WindowSize.window(
							currentSize.width() - (int) resizeOldX + (int) event.getScreenX() - (int) dragOffsetX,
							currentSize.height() - (int) resizeOldY + (int) event.getScreenY() - (int) dragOffsetY
					))
			);
			
			// render bottom resize area
			Button bottomArea = renderResizeArea(
					currentSize.width() - UISettings.resizeAreaThickness * 2,
					UISettings.resizeAreaThickness,
					Cursor.S_RESIZE,
					event -> {
						dragOffsetY = event.getSceneY();
						resizeOldY = stage.getY();
					},
					event -> resize(WindowSize.window(
							currentSize.width(),
							currentSize.height() - (int) resizeOldY + (int) event.getScreenY() - (int) dragOffsetY
					))
			);
			
			// render bottom-left resize area
			Button bottomLeftArea = renderResizeArea(
					UISettings.resizeAreaThickness,
					UISettings.resizeAreaThickness,
					Cursor.SW_RESIZE,
					event -> {
						dragOffsetX = event.getSceneX();
						dragOffsetY = event.getSceneY();
						resizeOldX = stage.getX();
						resizeOldY = stage.getY();
					},
					event -> {
						stage.setX(event.getScreenX() - dragOffsetX);
						resize(WindowSize.window(
								currentSize.width() + (int) resizeOldX - (int) event.getScreenX() + (int) dragOffsetX,
								currentSize.height() - (int) resizeOldY + (int) event.getScreenY() - (int) dragOffsetY
						));
					}
			);
			
			// render left resize area
			Button leftArea = renderResizeArea(
					UISettings.resizeAreaThickness,
					currentSize.height() - UISettings.resizeAreaThickness * 2,
					Cursor.W_RESIZE,
					event -> {
						dragOffsetX = event.getSceneX();
						resizeOldX = stage.getX();
					},
					event -> {
						stage.setX(event.getScreenX() - dragOffsetX);
						resize(WindowSize.window(
								currentSize.width() + (int) resizeOldX - (int) event.getScreenX() + (int) dragOffsetX,
								currentSize.height()
						));
					}
			);
			
			root.getChildren().addAll(topLeftArea, topArea, topRightArea, rightArea, bottomRightArea, bottomArea, bottomLeftArea, leftArea);
			AnchorPane.setRightAnchor(topLeftArea, currentSize.width() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setBottomAnchor(topLeftArea, currentSize.height() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setLeftAnchor(topArea, UISettings.resizeAreaThickness * 1.);
			AnchorPane.setBottomAnchor(topArea, currentSize.height() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setLeftAnchor(topRightArea, currentSize.width() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setBottomAnchor(topRightArea, currentSize.height() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setTopAnchor(rightArea, UISettings.resizeAreaThickness * 1.);
			AnchorPane.setLeftAnchor(rightArea, currentSize.width() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setTopAnchor(bottomRightArea, currentSize.height() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setLeftAnchor(bottomRightArea, currentSize.width() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setTopAnchor(bottomArea, currentSize.height() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setLeftAnchor(bottomArea, UISettings.resizeAreaThickness * 1.);
			AnchorPane.setTopAnchor(bottomLeftArea, currentSize.height() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setRightAnchor(bottomLeftArea, currentSize.width() - UISettings.resizeAreaThickness * 1.);
			AnchorPane.setTopAnchor(leftArea, UISettings.resizeAreaThickness * 1.);
			AnchorPane.setRightAnchor(leftArea, currentSize.width() - UISettings.resizeAreaThickness * 1.);
		}
		
		stage.setScene(new Scene(root));
	}
	
	/**
	 * Renders a resize area for the window.
	 *
	 * @param width           The width of the resize area in pixel.
	 * @param height          The height of the resize area in pixel.
	 * @param cursor          The cursor to be displayed when hovering over the resize area.
	 * @param pressedHandler  The {@link EventHandler} when the resize area is pressed.
	 * @param releasedHandler The {@link EventHandler} when the resize area is released.
	 * @return The rendered resize area as a {@link Button} instance.
	 */
	private Button renderResizeArea(int width, int height, Cursor cursor, EventHandler<MouseEvent> pressedHandler, EventHandler<MouseEvent> releasedHandler) {
		Button area = new Button();
		area.setPrefSize(width, height);
		area.setOpacity(0); // the button should be hided in order to show the structure behind
		area.setCursor(cursor);
		area.setOnMousePressed(pressedHandler);
		area.setOnMouseReleased(releasedHandler);
		return area;
	}
	
	/**
	 * Manages different types of window sizes.
	 */
	@StaticClass
	public static class SizeManager {
		/** {@link NbtLoader} for loading and saving {@link SizeManager} objects from/to NBT data. */
		public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
			@Override
			public Void load(CompoundTag tag) {
				windowSizes = new ConcurrentHashMap<>(tag.getValue().entrySet().stream()
						                                      .flatMap(entry -> {
																  try {
																	  return Stream.of(Map.entry(
																			  Token.valueOf(entry.getKey()),
																			  WindowSize.loader.load((CompoundTag) entry.getValue())
																	  ));
																  } catch (IllegalNbtException e) {
																	  logger.warn("Illegal window size: {}", e.getMessage());
																	  return Stream.empty();
																  }
						                                      })
						                                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
				);
				return null;
			}
			
			@Override
			public CompoundTag save(Void object) {
				CompoundTag tag = new CompoundTag();
				
				for (Map.Entry<Token, WindowSize> entry : windowSizes.entrySet()) {
					tag.put(entry.getKey().name(), WindowSize.loader.save(entry.getValue()));
				}
				
				return tag;
			}
		};
		
		/** The managed {@link WindowSize} instances. */
		private static Map<Token, WindowSize> windowSizes = new ConcurrentHashMap<>();
		
		/**
		 * Gets the {@link WindowSize} for a given handle.
		 *
		 * @param token The token of the window size.
		 * @return The {@link WindowSize} for the given handle.
		 */
		public static WindowSize get(Token token) {
			return windowSizes.getOrDefault(token, UISettings.defaultWindowSize);
		}
		
		/**
		 * Sets the {@link WindowSize} for a given handle.
		 *
		 * @param token      The token of the window size.
		 * @param windowSize The {@link WindowSize} to be set.
		 */
		public static void set(Token token, WindowSize windowSize) {
			windowSizes.put(token, windowSize);
		}
		
		/**
		 * The tokens for the windows.
		 */
		public enum Token {
			/** The main window. */
			MAIN
		}
	}
}

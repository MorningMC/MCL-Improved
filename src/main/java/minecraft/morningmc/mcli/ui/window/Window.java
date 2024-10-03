package minecraft.morningmc.mcli.ui.window;

import minecraft.morningmc.mcli.launcher.Metadata;
import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.ui.UIManager;
import minecraft.morningmc.mcli.ui.settings.Background;
import minecraft.morningmc.mcli.minecraft.launch.options.WindowSize;
import minecraft.morningmc.mcli.utils.functions.ExceptionUtils;
import minecraft.morningmc.mcli.utils.interfaces.NamedObject;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a customizable window in the user interface.
 * It provides functionality for creating a window, managing its size and position, and handling user interactions such as resizing and dragging.
 */
public class Window implements NamedObject {
	private static final Logger logger = LogManager.getLogger();
	
	private final Stage stage;
	private final SizeManager.Token sizeToken;
	private final WindowElements base;
	private Elements content = null;
	
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
		
		// refresh the size when resize
		stage.widthProperty().addListener((obs, old, ne) -> SizeManager.set(sizeToken, SizeManager.get(sizeToken).width(ne.intValue())));
		stage.heightProperty().addListener((obs, old, ne) -> SizeManager.set(sizeToken, SizeManager.get(sizeToken).height(ne.intValue())));
		stage.maximizedProperty().addListener((obs, old, ne) -> SizeManager.set(sizeToken, SizeManager.get(sizeToken).fullScreen(ne)));
		
		base = new WindowElements(this);
		refresh();
		
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
		try {
			WindowSize oldSize = SizeManager.get(sizeToken);
			WindowSize currentSize = refreshSize(size);
			int width = currentSize.width() != oldSize.width() ? currentSize.width() : 0;
			int height = currentSize.height() != oldSize.height() ? currentSize.height() : 0;
			
			stage.setScene(new Scene(base.render(width, height, false)));
		} catch (Exception e) {
			logger.warn("Exception when window \"{}\" resize: {}", stage.getTitle(), ExceptionUtils.getMessages(e));
		}
	}
	
	/**
	 * Refreshes the content of the window.
	 */
	public void refresh() {
		try {
			WindowSize size = refreshSize(SizeManager.get(sizeToken));
			stage.setScene(new Scene(base.render(size.width(), size.height(), true)));
		} catch (Exception e) {
			logger.warn("Exception when window \"{}\" refresh: {}", stage.getTitle(), ExceptionUtils.getMessages(e));
		}
	}
	
	/**
	 * Applies the specified content to the window.
	 *
	 * @param content the content to be applied.
	 */
	public void apply(Elements content) {
		this.content = content;
		refresh();
	}
	
	/**
	 * Refreshes the window size and returns the new size. This prevents the expected size of the window from being unequal to the actual size.
	 *
	 * @param size The new size of the window which needs to be updated.
	 * @return the new size of the window.
	 */
	private WindowSize refreshSize(WindowSize size) {
		stage.setWidth(size.width());
		stage.setHeight(size.height());
		stage.setMaximized(size.fullScreen());
		
		return SizeManager.get(sizeToken);
	}
	
	@Override
	public String name() {
		return stage.getTitle();
	}
	
	private static class WindowElements implements Elements {
		private final Window window;
		private int width;
		private int height;
		// nodes
		private AnchorPane title;
		private AnchorPane close;
		private AnchorPane maximize;
		private AnchorPane minimize;
		private AnchorPane titleBar;
		private Button topLeftArea;
		private Button topArea;
		private Button topRightArea;
		private Button rightArea;
		private Button bottomRightArea;
		private Button bottomArea;
		private Button bottomLeftArea;
		private Button leftArea;
		// temp values
		private double dragOffsetX;
		private double dragOffsetY;
		private double resizeOldX;
		private double resizeOldY;
		
		public WindowElements(Window window) {
			this.window = window;
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
		
		@Override
		public Parent render(int width, int height, boolean redrawAll) {
			if (width > 0) {
				this.width = width;
			}
			if (height > 0) {
				this.height = height;
			}
			
			// render titlebar
			if (width > 0) {
				title = UISettings.colorStyle.getSwitch().title.render(this.width, UISettings.titleHeight)
						        .text(Metadata.fullName, UISettings.fontStyle.title)
						        .icon(UIManager.icon)
						        .cursorPressed(Cursor.MOVE)
						        .pressHandler(event -> {
							        dragOffsetX = event.getSceneX();
							        dragOffsetY = event.getSceneY();
						        })
						        .dragHandler(event -> {
							        window.stage.setX(event.getScreenX() - dragOffsetX);
							        window.stage.setY(event.getScreenY() - dragOffsetY);
						        })
						        .build();
			}
			if (redrawAll) {
				close = UISettings.colorStyle.getSwitch().close.render(UISettings.titleHeight, UISettings.titleHeight)
						        .text("X", UISettings.fontStyle.title)
						        .cursor(Cursor.HAND)
						        .clickHandler(event -> window.stage.close())
						        .build();
				maximize = UISettings.colorStyle.getSwitch().title.render(UISettings.titleHeight, UISettings.titleHeight)
						           .text("O", UISettings.fontStyle.title)
						           .cursor(Cursor.HAND)
						           .clickHandler(event -> window.resize(SizeManager.get(window.sizeToken).fullScreen(!window.stage.isMaximized())))
						           .build();
				minimize = UISettings.colorStyle.getSwitch().title.render(UISettings.titleHeight, UISettings.titleHeight)
						           .text("_", UISettings.fontStyle.title)
						           .cursor(Cursor.HAND)
						           .clickHandler(event -> window.stage.setIconified(true))
						           .build();
			}
			if (width > 0) {
				titleBar = new AnchorPane(title, close, maximize, minimize);
				AnchorPane.setLeftAnchor(close, this.width - UISettings.titleHeight * 1.);
				AnchorPane.setLeftAnchor(maximize, this.width - UISettings.titleHeight * 2.);
				AnchorPane.setLeftAnchor(minimize, this.width - UISettings.titleHeight * 3. + UISettings.edgeThickness);
			}
			
			// render background
			AnchorPane background = UISettings.colorStyle.getSwitch().background.render(this.width, this.height - UISettings.titleHeight).build();
			ImageView backgroundView = UISettings.background.getIfEnabled(Background.of(null, 0, 0)).render(this.width, this.height - UISettings.titleHeight);
			
			// render root
			AnchorPane root = new AnchorPane(titleBar, background, backgroundView);
			root.setPrefSize(this.width, this.height);
			AnchorPane.setTopAnchor(background, UISettings.titleHeight * 1.);
			AnchorPane.setTopAnchor(backgroundView, UISettings.titleHeight * 1.);
			
			// render content
			if (window.content != null) {
				Parent content = window.content.render(width, Math.max(height - UISettings.titleHeight, 0), redrawAll);
				root.getChildren().add(content);
				AnchorPane.setTopAnchor(content, UISettings.titleHeight * 1.);
			}
			
			// render resize area
			if (!SizeManager.get(window.sizeToken).fullScreen()) { // ignore resize area when maximized
				if (redrawAll) {
					// render top-left resize area
					topLeftArea = renderResizeArea(
							UISettings.resizeAreaThickness,
							UISettings.resizeAreaThickness,
							Cursor.NW_RESIZE,
							event -> {
								dragOffsetX = event.getSceneX();
								dragOffsetY = event.getSceneY();
								resizeOldX = window.stage.getX();
								resizeOldY = window.stage.getY();
							},
							event -> {
								window.stage.setX(event.getScreenX() - dragOffsetX);
								window.stage.setY(event.getScreenY() - dragOffsetY);
								window.resize(WindowSize.window(
										this.width + (int) resizeOldX - (int) event.getScreenX() + (int) dragOffsetX,
										this.height + (int) resizeOldY - (int) event.getScreenY() + (int) dragOffsetY
								));
							}
					);
					
					// render top-right resize area
					topRightArea = renderResizeArea(
							UISettings.resizeAreaThickness,
							UISettings.resizeAreaThickness,
							Cursor.NE_RESIZE,
							event -> {
								dragOffsetX = event.getSceneX();
								dragOffsetY = event.getSceneY();
								resizeOldX = window.stage.getX();
								resizeOldY = window.stage.getY();
							},
							event -> {
								window.stage.setY(event.getScreenY() - dragOffsetY);
								window.resize(WindowSize.window(
										this.width - (int) resizeOldX + (int) event.getScreenX() - (int) dragOffsetX,
										this.height + (int) resizeOldY - (int) event.getScreenY() + (int) dragOffsetY
								));
							}
					);
					
					// render bottom-right resize area
					bottomRightArea = renderResizeArea(
							UISettings.resizeAreaThickness,
							UISettings.resizeAreaThickness,
							Cursor.SE_RESIZE,
							event -> {
								dragOffsetX = event.getSceneX();
								dragOffsetY = event.getSceneY();
								resizeOldX = window.stage.getX();
								resizeOldY = window.stage.getY();
							},
							event -> window.resize(WindowSize.window(
									this.width - (int) resizeOldX + (int) event.getScreenX() - (int) dragOffsetX,
									this.height - (int) resizeOldY + (int) event.getScreenY() - (int) dragOffsetY
							))
					);
					
					// render bottom-left resize area
					bottomLeftArea = renderResizeArea(
							UISettings.resizeAreaThickness,
							UISettings.resizeAreaThickness,
							Cursor.SW_RESIZE,
							event -> {
								dragOffsetX = event.getSceneX();
								dragOffsetY = event.getSceneY();
								resizeOldX = window.stage.getX();
								resizeOldY = window.stage.getY();
							},
							event -> {
								window.stage.setX(event.getScreenX() - dragOffsetX);
								window.resize(WindowSize.window(
										this.width + (int) resizeOldX - (int) event.getScreenX() + (int) dragOffsetX,
										this.height - (int) resizeOldY + (int) event.getScreenY() - (int) dragOffsetY
								));
							}
					);
				}
				
				if (width > 0) {
					// render top resize area
					topArea = renderResizeArea(
							this.width - UISettings.resizeAreaThickness * 2,
							UISettings.resizeAreaThickness,
							Cursor.N_RESIZE,
							event -> {
								dragOffsetY = event.getSceneY();
								resizeOldY = window.stage.getY();
							},
							event -> {
								window.stage.setY(event.getScreenY() - dragOffsetY);
								window.resize(WindowSize.window(
										this.width,
										this.height + (int) resizeOldY - (int) event.getScreenY() + (int) dragOffsetY
								));
							}
					);
					
					// render bottom resize area
					bottomArea = renderResizeArea(
							this.width - UISettings.resizeAreaThickness * 2,
							UISettings.resizeAreaThickness,
							Cursor.S_RESIZE,
							event -> {
								dragOffsetY = event.getSceneY();
								resizeOldY = window.stage.getY();
							},
							event -> window.resize(WindowSize.window(
									this.width,
									this.height - (int) resizeOldY + (int) event.getScreenY() - (int) dragOffsetY
							))
					);
				}
				
				if (height > 0) {
					// render right resize area
					rightArea = renderResizeArea(
							UISettings.resizeAreaThickness,
							this.height - UISettings.resizeAreaThickness * 2,
							Cursor.E_RESIZE,
							event -> {
								dragOffsetX = event.getSceneX();
								resizeOldX = window.stage.getX();
							},
							event -> window.resize(WindowSize.window(
									this.width - (int) resizeOldX + (int) event.getScreenX() - (int) dragOffsetX,
									this.height
							))
					);
					
					// render left resize area
					leftArea = renderResizeArea(
							UISettings.resizeAreaThickness,
							this.height - UISettings.resizeAreaThickness * 2,
							Cursor.W_RESIZE,
							event -> {
								dragOffsetX = event.getSceneX();
								resizeOldX = window.stage.getX();
							},
							event -> {
								window.stage.setX(event.getScreenX() - dragOffsetX);
								window.resize(WindowSize.window(
										this.width + (int) resizeOldX - (int) event.getScreenX() + (int) dragOffsetX,
										this.height
								));
							}
					);
				}
				
				root.getChildren().addAll(topLeftArea, topArea, topRightArea, rightArea, bottomRightArea, bottomArea, bottomLeftArea, leftArea);
				AnchorPane.setRightAnchor(topLeftArea, this.width - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setBottomAnchor(topLeftArea, this.height - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setLeftAnchor(topArea, UISettings.resizeAreaThickness * 1.);
				AnchorPane.setBottomAnchor(topArea, this.height - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setLeftAnchor(topRightArea, this.width - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setBottomAnchor(topRightArea, this.height - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setTopAnchor(rightArea, UISettings.resizeAreaThickness * 1.);
				AnchorPane.setLeftAnchor(rightArea, this.width - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setTopAnchor(bottomRightArea, this.height - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setLeftAnchor(bottomRightArea, this.width - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setTopAnchor(bottomArea, this.height - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setLeftAnchor(bottomArea, UISettings.resizeAreaThickness * 1.);
				AnchorPane.setTopAnchor(bottomLeftArea, this.height - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setRightAnchor(bottomLeftArea, this.width - UISettings.resizeAreaThickness * 1.);
				AnchorPane.setTopAnchor(leftArea, UISettings.resizeAreaThickness * 1.);
				AnchorPane.setRightAnchor(leftArea, this.width - UISettings.resizeAreaThickness * 1.);
			}
			
			return root;
		}
	}
}

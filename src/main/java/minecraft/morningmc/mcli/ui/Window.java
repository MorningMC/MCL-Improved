package minecraft.morningmc.mcli.ui;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import minecraft.morningmc.mcli.launcher.Metadata;
import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.utils.WindowSize;

public class Window {
	private final Stage stage;
	private final WindowSizeManager.Handle sizeHandle;
	private double dragOffsetX;
	private double dragOffsetY;
	
	public Window(Stage stage, WindowSizeManager.Handle sizeHandle, String title) {
		this.stage = stage;
		this.sizeHandle = sizeHandle;
		
		stage.setTitle(title);
		stage.getIcons().add(UIManager.icon);
		stage.initStyle(StageStyle.TRANSPARENT);
		
		resize(WindowSizeManager.get(sizeHandle));
		
		stage.widthProperty().addListener((obs, old, ne) -> resize(WindowSizeManager.get(sizeHandle).width(ne.intValue())));
		stage.heightProperty().addListener((obs, old, ne) -> resize(WindowSizeManager.get(sizeHandle).height(ne.intValue())));
		stage.maximizedProperty().addListener((obs, old, ne) -> resize(WindowSizeManager.get(sizeHandle).fullScreen(ne)));
		
		stage.show();
	}
	
	public static Window create(WindowSizeManager.Handle sizeHandle, String title) {
		return new Window(new Stage(), sizeHandle, title);
	}
	
	public void resize(WindowSize size) {
		WindowSizeManager.set(sizeHandle, size);
		
		stage.setWidth(size.width());
		stage.setHeight(size.height());
		stage.setMaximized(size.fullScreen());
		
		// render title bar
		AnchorPane title = UISettings.colorStyle.getSwitch().title.render(size.width(), UISettings.constants.titleHeight)
				                   .text(Metadata.fullName, UISettings.fontStyle.title)
				                   .icon(UIManager.icon)
				                   .pressHandler(event -> {
									   dragOffsetX = event.getScreenX() - stage.getX();
									   dragOffsetY = event.getScreenY() - stage.getY();
				                   })
				                   .dragHandler(event -> {
									   stage.setX(event.getScreenX() - dragOffsetX);
									   stage.setY(event.getScreenY() - dragOffsetY);
				                   })
				                   .build();
		AnchorPane close = UISettings.colorStyle.getSwitch().close.render(UISettings.constants.titleHeight, UISettings.constants.titleHeight)
				                   .text("X", UISettings.fontStyle.title)
				                   .pressHandler(event -> stage.close())
				                   .build();
		AnchorPane maximize = UISettings.colorStyle.getSwitch().title.render(UISettings.constants.titleHeight, UISettings.constants.titleHeight)
				                   .text("O", UISettings.fontStyle.title)
				                   .pressHandler(event -> {
					                   stage.setMaximized(!stage.isMaximized());
									   resize(WindowSizeManager.get(sizeHandle).fullScreen(stage.isMaximized()));
				                   })
				                   .build();
		AnchorPane minimize = UISettings.colorStyle.getSwitch().title.render(UISettings.constants.titleHeight, UISettings.constants.titleHeight)
				                   .text("_", UISettings.fontStyle.title)
				                   .pressHandler(event -> stage.setIconified(true))
				                   .build();
		AnchorPane titleBar = new AnchorPane(title, close, maximize, minimize);
		AnchorPane.setLeftAnchor(close, size.width() - UISettings.constants.titleHeight * 1.);
		AnchorPane.setLeftAnchor(maximize, size.width() - UISettings.constants.titleHeight * 2.);
		AnchorPane.setLeftAnchor(minimize, size.width() - UISettings.constants.titleHeight * 3. + UISettings.constants.edgeThickness);
		
		// render background
		AnchorPane background = UISettings.colorStyle.getSwitch().background.render(size.width(), size.height() - UISettings.constants.titleHeight)
				                   .build();
		
		VBox root = new VBox(titleBar, background);
		stage.setScene(new Scene(root));
	}
}

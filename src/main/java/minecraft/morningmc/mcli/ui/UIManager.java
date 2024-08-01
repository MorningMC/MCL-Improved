package minecraft.morningmc.mcli.ui;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;
import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.utils.WindowSize;

import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * A class to manage the UI of the launcher.
 */
public class UIManager {
	public static final Image icon = new Image(FileMetadata.getResource("assets/textures/icon.png"));
	
	public final Stage mainStage;
	
	/**
	 * Constructs a new {@link UIManager}.
	 *
	 * @param mainStage the main stage of the launcher.
	 */
	public UIManager(Stage mainStage) {
		this.mainStage = mainStage;
		
		mainStage.setTitle(LauncherMetadata.fullName);
		mainStage.getIcons().add(icon);
		
		refreshSize();
		mainStage.widthProperty().addListener((obs, old, ne) -> UISettings.windowSize = WindowSize.windowed((int) ne, UISettings.windowSize.height()));
		mainStage.heightProperty().addListener((obs, old, ne) -> UISettings.windowSize = WindowSize.windowed(UISettings.windowSize.width(), (int) ne));
		
		mainStage.show();
	}
	
	/**
	 * Refreshes the size of the main stage.
	 */
	public void refreshSize() {
		mainStage.setWidth(UISettings.windowSize.width());
		mainStage.setHeight(UISettings.windowSize.height());
	}
}
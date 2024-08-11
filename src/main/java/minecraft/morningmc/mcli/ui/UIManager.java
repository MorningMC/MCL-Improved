package minecraft.morningmc.mcli.ui;

import minecraft.morningmc.mcli.launcher.Metadata;
import minecraft.morningmc.mcli.launcher.main.FileManager;
import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.utils.WindowSize;

import javafx.stage.Stage;
import javafx.scene.image.Image;

/**
 * A class to manage the UI of the launcher.
 */
public class UIManager {
	public static final Image icon = new Image(FileManager.getResource("assets/textures/icon.png"));
	
	public final Stage mainStage;
	
	/**
	 * Constructs a new {@link UIManager}.
	 *
	 * @param mainStage the main stage of the launcher.
	 */
	public UIManager(Stage mainStage) {
		this.mainStage = mainStage;
		
		mainStage.setTitle(Metadata.fullName);
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
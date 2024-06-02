package minecraft.morningmc.mcli.ui;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A class to manage the UI of the launcher.
 */
public class UIManager {
	public static final Image ICON = new Image(FileMetadata.getResource(
			LauncherMetadata.VERSION.isStable() ? "assets/textures/icons/stable.png" : "assets/textures/icons/dev.png"));
	
	private final Stage mainStage;
	
	/**
	 * Constructs a new UIManager.
	 *
	 * @param mainStage the main stage of the launcher.
	 */
	public UIManager(Stage mainStage) {
		this.mainStage = mainStage;
		
		mainStage.setTitle(LauncherMetadata.FULL_NAME);
		mainStage.getIcons().add(ICON);
		
		mainStage.setWidth(1536);
		mainStage.setHeight(949);
		
		mainStage.show();
	}
	
	/**
	 * Gets the main stage of the launcher.
	 *
	 * @return the main stage of the launcher.
	 */
	public Stage mainStage() {
		return mainStage;
	}
}
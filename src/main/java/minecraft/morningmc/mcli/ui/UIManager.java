package minecraft.morningmc.mcli.ui;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;

import javafx.scene.image.Image;
import javafx.stage.Stage;

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
		
		mainStage.setWidth(1024);
		mainStage.setHeight(632);
		
		mainStage.show();
	}
}
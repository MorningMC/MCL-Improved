package minecraft.morningmc.mcli.ui;

import minecraft.morningmc.mcli.launcher.Metadata;
import minecraft.morningmc.mcli.utils.FileManager;

import javafx.stage.*;
import javafx.scene.image.Image;

/**
 * A class to manage the UI of the launcher.
 */
public class UIManager {
	public static final Image icon = new Image(FileManager.getResource("assets/textures/icon.png"));
	
	public final Window mainWindow;
	
	/**
	 * Constructs a new {@link UIManager}.
	 *
	 * @param mainStage the main stage of the launcher.
	 */
	public UIManager(Stage mainStage) {
		mainWindow = new Window(mainStage, Window.WindowSizeManager.Token.MAIN, Metadata.fullName);
	}
}
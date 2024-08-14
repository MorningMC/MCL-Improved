package minecraft.morningmc.mcli.ui;

import javafx.scene.Scene;
import javafx.stage.StageStyle;
import minecraft.morningmc.mcli.launcher.Metadata;
import minecraft.morningmc.mcli.ui.settings.FontStyle;
import minecraft.morningmc.mcli.utils.FileManager;
import minecraft.morningmc.mcli.launcher.settings.UISettings;

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
		mainStage.initStyle(StageStyle.TRANSPARENT);
		
		refreshSize();
		mainStage.widthProperty().addListener((obs, old, ne) -> UISettings.windowSize = UISettings.windowSize.width(ne.intValue()));
		mainStage.heightProperty().addListener((obs, old, ne) -> UISettings.windowSize = UISettings.windowSize.height(ne.intValue()));
		mainStage.maximizedProperty().addListener((obs, old, ne) -> UISettings.windowSize = UISettings.windowSize.fullScreen(ne));
		
		mainStage.setScene(new Scene(
				UISettings.colorStyle.getSwitch().title.render(UISettings.windowSize.width(), 48)
						.text(Metadata.fullName, FontStyle.minecraftTen)
						.icon(icon)
						.eventHandler(event -> mainStage.close())
						.build()
		));
		mainStage.show();
	}
	
	/**
	 * Refreshes the size of the main stage.
	 */
	public void refreshSize() {
		mainStage.setWidth(UISettings.windowSize.width());
		mainStage.setHeight(UISettings.windowSize.height());
		mainStage.setMaximized(UISettings.windowSize.fullScreen());
	}
}
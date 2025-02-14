package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(LauncherMetadata.FULL_NAME);
	}
}
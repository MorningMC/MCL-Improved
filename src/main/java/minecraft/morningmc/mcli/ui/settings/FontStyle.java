package minecraft.morningmc.mcli.ui.settings;

import javafx.scene.text.Font;
import minecraft.morningmc.mcli.utils.FileManager;

public class FontStyle {
	public static final Font minecraft = Font.loadFont(FileManager.getResource("assets/fonts/minecraft.otf"), 16);
	public static final Font minecraftBold = Font.loadFont(FileManager.getResource("assets/fonts/minecraft-bold.otf"), 16);
	public static final Font minecraftItalic = Font.loadFont(FileManager.getResource("assets/fonts/minecraft-italic.otf"), 16);
	public static final Font minecraftBoldItalic = Font.loadFont(FileManager.getResource("assets/fonts/minecraft-bold-italic.otf"), 16);
	public static final Font minecraftAE = Font.loadFont(FileManager.getResource("assets/fonts/minecraftAE.ttf"), 16);
	public static final Font minecraftTen =  Font.loadFont(FileManager.getResource("assets/fonts/minecraftTen.ttf"), 16);
}

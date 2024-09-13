package minecraft.morningmc.mcli.ui.settings;

import javafx.scene.effect.GaussianBlur;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.*;

/**
 * Represents the background settings, including the background image file, opacity, and blur level.
 *
 * @param background The file representing the background image.
 * @param opacity    The opacity of the background image, between 0 (completely transparent) and 1 (completely opaque).
 * @param blur       The blur radius of the background image.
 */
public record Background(File background, double opacity, int blur) {
	
	/** {@link NbtLoader} for loading and saving {@link Background} objects from/to NBT data. */
	public static final NbtLoader<Background, CompoundTag> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@link Background} object from the provided {@link CompoundTag}.
		 *
		 * @param tag The NBT tag containing the background settings data.
		 * @return A new {@link Background} instance loaded from the NBT data, or {@code null} if loading fails.
		 */
		@Override
		public Background load(CompoundTag tag) {
			File file;
			try {
				file = new File(tag.getString("background").getValue());
			} catch (Exception e) {
				file = null;
			}
			double opacity = tag.getDouble("opacity").getValue();
			int blur = tag.getInt("blur").getValue();
			
			return of(file, opacity, blur);
		}
		
		/**
		 * Saves the provided {@link Background} object to a {@link CompoundTag}.
		 *
		 * @param object The {@link Background} object to save.
		 * @return A {@link CompoundTag} containing the background settings data.
		 */
		@Override
		public CompoundTag save(Background object) {
			CompoundTag tag = new CompoundTag();
			
			if (object.background != null) {
				tag.putString("background", object.background.getAbsolutePath());
			}
			tag.putDouble("opacity", object.opacity);
			tag.putInt("blur", object.blur);
			
			return tag;
		}
	};
	
	/**
	 * Creates a new {@link Background} instance with the specified background image file, opacity, and blur level.
	 *
	 * @param background The file representing the background image.
	 * @param opacity    The opacity of the background image, between 0 (completely transparent) and 1 (completely opaque).
	 * @param blur       The blur radius of the background image.
	 * @return A new {@link Background} instance with the specified settings.
	 * @throws IllegalArgumentException if the opacity value are not between 0 and 1.
	 */
	public static Background of(File background, double opacity, int blur) {
		if (opacity < 0 || opacity > 1) {
			throw new IllegalArgumentException("Opacity must be between 0 and 1");
		}
		
		return new Background(background, opacity, blur);
	}
	
	public ImageView render(int width, int height) {
		ImageView imageView = new ImageView();
		
		try {
			Image image = new Image(new FileInputStream(background));
			imageView.setImage(image);
			imageView.setFitWidth(width);
			imageView.setFitHeight(height);
			imageView.setOpacity(opacity);
			imageView.setEffect(new GaussianBlur(blur));
			
			return imageView;
		} catch (Exception e) {
			return imageView;
		}
	}
}
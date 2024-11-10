package minecraft.morningmc.mcli.utils.interfaces;

import dev.dewy.nbt.tags.array.IntArrayTag;
import dev.dewy.nbt.tags.primitive.IntTag;
import minecraft.morningmc.mcli.ui.settings.FontStyle;

import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;
import dev.dewy.nbt.tags.primitive.StringTag;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

/**
 * Interface for loading and saving objects to and from NBT tags.
 *
 * @param <C> The type of object to load or singleplayer.
 * @param <T> The type of NBT tag.
 */
public interface NbtLoader<C, T extends Tag> {
	/** {@link NbtLoader} for loading and saving {@link String} objects from/to NBT data. */
	NbtLoader<String, StringTag> stringLoader = new NbtLoader<>() {

		/**
		 * Load a string from an NBT string tag.
		 *
		 * @param tag The NBT string tag.
		 * @return The loaded string.
		 */
		@Override
		public String load(StringTag tag) {
			return tag.getValue();
		}

		/**
		 * Save a string to an NBT string tag.
		 *
		 * @param object The string to be saved.
		 * @return The NBT string tag containing the saved string.
		 */
		@Override
		public StringTag save(String object) {
			return new StringTag(object);
		}
	};
	
	/** {@link NbtLoader} for loading and saving a list of strings from/to NBT data. */
	NbtLoader<List<String>, ListTag<StringTag>> stringListLoader = new NbtLoader<>() {
		
		/**
		 * Load a list of strings from an NBT list tag.
		 *
		 * @param tag The NBT list tag containing string elements.
		 * @return The loaded list of strings.
		 */
		@Override
		public List<String> load(ListTag<StringTag> tag) {
			return tag.getValue().stream().map(StringTag::getValue).toList();
		}
		
		/**
		 * Save a list of strings to an NBT list tag.
		 *
		 * @param object The list of strings to be saved.
		 * @return The NBT list tag containing string elements.
		 */
		@Override
		public ListTag<StringTag> save(List<String> object) {
			ListTag<StringTag> tag = new ListTag<>();
			object.stream().map(StringTag::new).forEach(tag::add);
			return tag;
		}
	};
	
	/** {@link NbtLoader} for loading and saving {@link UUID} objects from/to NBT data. */
	NbtLoader<UUID, IntArrayTag> uuidLoader = new NbtLoader<>() {

		/**
		 * Load a {@link UUID} from an NBT string tag.
		 *
		 * @param tag The NBT string tag containing a UUID string.
		 * @return The loaded {@link UUID}.
		 * @throws IllegalArgumentException If there is an issue with the NBT data.
		 */
		@Override
		public UUID load(IntArrayTag tag) {
			try {
				long mostSigBits = (long)tag.getValue()[0] << 32 | (long)tag.getValue()[1] & 0xFFFFFFFFL;
				long leastSigBits = (long)tag.getValue()[2] << 32 | (long)tag.getValue()[3] & 0xFFFFFFFFL;
				
				return new UUID(mostSigBits, leastSigBits);
			} catch (Exception e) {
				throw new IllegalArgumentException("Exception while loading UUID object", e);
			}
		}

		/**
		 * Save a {@link UUID} to an NBT string tag.
		 *
		 * @param object The {@link UUID} to be saved.
		 * @return The NBT string tag containing the saved UUID string.
		 */
		@Override
		public IntArrayTag save(UUID object) {
			return new IntArrayTag(new int[]{
					(int) ( object.getMostSignificantBits() >> 32 ),
					(int) object.getMostSignificantBits(),
					(int) ( object.getLeastSignificantBits() >> 32 ),
					(int) object.getLeastSignificantBits()
			});
		}
	};
	
	/** {@link NbtLoader} for loading and saving a proxy from/to NBT data. */
	NbtLoader<Proxy, CompoundTag> proxyLoader = new NbtLoader<>() {

		/**
		 * Load a proxy from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag containing proxy data.
		 * @return The loaded proxy.
		 * @throws IllegalArgumentException If there is an issue with the NBT data.
		 */
		@Override
		public Proxy load(CompoundTag tag) {
			try {
				Proxy.Type type = Proxy.Type.values()[tag.getInt("type").getValue()];
				
				if (type == Proxy.Type.DIRECT) {
					return Proxy.NO_PROXY;
				}
				String address = tag.getString("address").getValue();
				int port = tag.getInt("port").getValue();
				return new Proxy(type, new InetSocketAddress(address, port));
				
			} catch (Exception e) {
				throw new IllegalArgumentException("Exception while loading Proxy object", e);
			}
		}

		/**
		 * Save a proxy to an NBT compound tag.
		 *
		 * @param object The proxy to be saved.
		 * @return The NBT compound tag containing the saved proxy data.
		 */
		@Override
		public CompoundTag save(Proxy object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putInt("type", object.type().ordinal());
			if (object.type() != Proxy.Type.DIRECT) {
				InetSocketAddress address = (InetSocketAddress) object.address();
				tag.putString("address", address.getHostString());
				tag.putInt("port", address.getPort());
			}
			
			return tag;
		}
	};
	
	/** {@link NbtLoader} for loading and saving {@link Color} objects from/to NBT data. */
	NbtLoader<Color, IntTag> colorLoader = new NbtLoader<>() {
		
		/**
		 * Load a color from an NBT int tag in the format of {@code AARRGGBB}.
		 *
		 * @param tag The NBT tag containing color data.
		 * @return The loaded color.
		 * @throws IllegalArgumentException If there is an issue with the NBT data.
		 */
		@Override
		public Color load(IntTag tag) {
			try {
				// extract alpha, red, green, and blue components using bitwise operations
				int opacity = (tag.getValue() >> 24) & 0xFF;  // extract the alpha channel
				int red = (tag.getValue() >> 16) & 0xFF;      // extract the red channel
				int green = (tag.getValue() >> 8) & 0xFF;     // extract the green channel
				int blue = tag.getValue() & 0xFF;             // extract the blue channel
				
				// convert to JavaFX color (RGB takes 0-255, opacity takes 0.0-1.0)
				return Color.rgb(red, green, blue, opacity / 255.);
			} catch (Exception e) {
				throw new IllegalArgumentException("Exception while loading Color object", e);
			}
		}
		
		/**
		 * Save a color to an NBT int tag in the format of {@code AARRGGBB}.
		 *
		 * @param object The color to be saved.
		 * @return The NBT tag containing the saved color data.
		 */
		@Override
		public IntTag save(Color object) {
			// convert color components to integers (0-255 scale)
			int red = (int) (object.getRed() * 255);
			int green = (int) (object.getGreen() * 255);
			int blue = (int) (object.getBlue() * 255);
			int opacity = (int) (object.getOpacity() * 255);
			
			// combine components into a single 32-bit int (AARRGGBB format)
			return new IntTag((opacity << 24) | (red << 16) | (green << 8) | blue);
		}
	};
	
	/** {@link NbtLoader} for loading and saving {@link Font} objects from/to NBT data. */
	NbtLoader<Font, CompoundTag> fontLoader = new NbtLoader<>() {

		/**
		 * Load a font from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag containing font data.
		 * @return The loaded font.
		 */
		@Override
		public Font load(CompoundTag tag) {
			String family = tag.getString("family").getValue();
			double size = tag.getDouble("size").getValue();
			
			// if the font is built-in in the application
			String builtin = FontStyle.builtinFonts.get(family);
			if (builtin != null) {
				FontStyle.loadFont(builtin, size);
			}

			return Font.font(family, size);
		}

		/**
		 * Save a font to an NBT compound tag.
		 *
		 * @param object The font to be saved.
		 * @return The NBT compound tag containing the saved font data.
		 */
		@Override
		public CompoundTag save(Font object) {
			CompoundTag tag = new CompoundTag();

			tag.putString("family", object.getFamily());
			tag.putDouble("size", object.getSize());

			return tag;
		}
	};
	
	/**
	 * Load an object from an NBT tag.
	 *
	 * @param tag The NBT tag containing data to be loaded.
	 * @return The loaded object.
	 */
	C load(T tag);
	
	/**
	 * Save an object to an NBT tag.
	 *
	 * @param object The object to be saved.
	 * @return The NBT tag containing the saved data.
	 */
	T save(C object);
}
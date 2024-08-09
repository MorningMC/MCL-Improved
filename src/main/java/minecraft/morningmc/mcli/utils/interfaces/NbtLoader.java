package minecraft.morningmc.mcli.utils.interfaces;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;

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
	
	/** {@link NbtLoader} for loading and saving a proxy from/to NBT data. */
	NbtLoader<Proxy, CompoundTag> proxyLoader = new NbtLoader<>() {

		/**
		 * Load a proxy from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag containing proxy data.
		 * @return The loaded proxy.
		 */
		@Override
		public Proxy load(CompoundTag tag) {
			Proxy.Type type = Proxy.Type.valueOf(tag.getString("type").getValue());
			
			if (type == Proxy.Type.DIRECT) {
				return Proxy.NO_PROXY;
			}
			String address = tag.getString("address").getValue();
			int port = tag.getInt("port").getValue();
			
			return new Proxy(type, new InetSocketAddress(address, port));
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
			
			tag.putString("type", object.type().name());
			if (object.type() != Proxy.Type.DIRECT) {
				InetSocketAddress address = (InetSocketAddress) object.address();
				tag.putString("address", address.getHostString());
				tag.putInt("port", address.getPort());
			}
			
			return tag;
		}
	};
	
	/** {@link NbtLoader} for loading and saving {@link Color} objects from/to NBT data. */
	NbtLoader<Color, CompoundTag> colorLoader = new NbtLoader<>() {

		/**
		 * Load a color from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag containing color data.
		 * @return The loaded color.
		 */
		@Override
		public Color load(CompoundTag tag) {
			double red = tag.getDouble("red").getValue();
			double green = tag.getDouble("green").getValue();
			double blue = tag.getDouble("blue").getValue();
			double opacity = tag.getDouble("opacity").getValue();

			return new Color(red, green, blue, opacity);
		}

		/**
		 * Save a color to an NBT compound tag.
		 *
		 * @param object The color to be saved.
		 * @return The NBT compound tag containing the saved color data.
		 */
		@Override
		public CompoundTag save(Color object) {
			CompoundTag tag = new CompoundTag();

			tag.putDouble("red", object.getRed());
			tag.putDouble("green", object.getGreen());
			tag.putDouble("blue", object.getBlue());
			tag.putDouble("opacity", object.getOpacity());

			return tag;
		}
	};
	
	/**
	 * Load an object from an NBT tag.
	 *
	 * @param tag The NBT tag containing data to be loaded.
	 * @return The loaded object.
	 * @throws IllegalNbtException If there is an issue with the NBT data.
	 */
	C load(T tag) throws IllegalNbtException;
	
	/**
	 * Save an object to an NBT tag.
	 *
	 * @param object The object to be saved.
	 * @return The NBT tag containing the saved data.
	 */
	T save(C object);
}
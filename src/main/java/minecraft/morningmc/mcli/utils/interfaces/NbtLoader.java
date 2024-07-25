package minecraft.morningmc.mcli.utils.interfaces;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;

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
			
			for (String s : object) {
				tag.add(new StringTag(s));
			}
			
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
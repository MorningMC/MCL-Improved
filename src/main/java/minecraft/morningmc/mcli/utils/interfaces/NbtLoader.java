package minecraft.morningmc.mcli.utils.interfaces;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;

import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.tags.collection.ListTag;
import dev.dewy.nbt.tags.primitive.StringTag;

import java.util.List;

/**
 * Interface for loading and saving objects to and from NBT tags.
 *
 * @param <C> The type of object to load or save.
 * @param <T> The type of NBT tag.
 */
public interface NbtLoader<C, T extends Tag> {
	
	/** Loader instance for handling lists of strings in NBT format. */
	NbtLoader<List<String>, ListTag<StringTag>> STRING_LIST_LOADER = new NbtLoader<>() {
		
		/**
		 * Load a list of strings from an NBT list tag.
		 *
		 * @param tag The NBT list tag containing string elements.
		 * @return The loaded list of strings.
		 * @throws IllegalNbtException If there is an issue with the NBT data.
		 */
		@Override
		public List<String> loadFromNbt(ListTag<StringTag> tag) throws IllegalNbtException {
			return tag.getValue().stream().map(StringTag::getValue).toList();
		}
		
		/**
		 * Save a list of strings to an NBT list tag.
		 *
		 * @param object The list of strings to be saved.
		 * @return The NBT list tag containing string elements.
		 */
		@Override
		public ListTag<StringTag> saveToNbt(List<String> object) {
			ListTag<StringTag> tag = new ListTag<>();
			
			for (String s : object) {
				tag.add(new StringTag(s));
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
	C loadFromNbt(T tag) throws IllegalNbtException;
	
	/**
	 * Save an object to an NBT tag.
	 *
	 * @param object The object to be saved.
	 * @return The NBT tag containing the saved data.
	 */
	T saveToNbt(C object);
}
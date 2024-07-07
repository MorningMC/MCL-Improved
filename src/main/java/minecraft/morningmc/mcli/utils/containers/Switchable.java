package minecraft.morningmc.mcli.utils.containers;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * A container object that allows to switch on or off and modify the value at the same time.
 *
 * @param <T> The type of the value.
 */
public class Switchable<T> extends Modifiable<T> {
	private boolean enabled;
	
	/**
	 * Constructs a new {@link Switchable} object.
	 *
	 * @param value The value to be stored in the {@link Switchable} object.
	 * @param enabled Whether the {@link Switchable} object is enabled.
	 */
	private Switchable(T value, boolean enabled) {
		super(value);
		this.enabled = enabled;
	}
	
	/**
	 * Constructs a new {@link Switchable} object.
	 *
	 * @param value The value to be stored in the {@link Switchable} object.
	 * @param enabled Whether the {@link Switchable} object is enabled.
	 * @return A new {@link Switchable} object.
	 *
	 * @param <T> The type of the value.
	 */
	public static <T> Switchable<T> of(T value, boolean enabled) {
		return new Switchable<>(value, enabled);
	}
	
	/**
	 * Constructs a new enabled {@link Switchable} object.
	 *
	 * @param value The value to be stored in the {@link Switchable} object.
	 * @return A new {@link Switchable} object.
	 *
	 * @param <T> The type of the value.
	 */
	public static <T> Switchable<T> ofEnabled(T value) {
		return of(value, true);
	}
	
	/**
	 * Constructs a new disabled {@link Switchable} object.
	 *
	 * @param value The value to be stored in the {@link Switchable} object.
	 * @return A new {@link Switchable} object.
	 *
	 * @param <T> The type of the value.
	 */
	public static <T> Switchable<T> ofDisabled(T value) {
		return of(value, false);
	}
	
	/**
	 * Generates a {@link Switchable} loader from a loader.
	 *
	 * @param loader The loader to be wrapped in a {@link Switchable} loader.
	 * @return The {@link Switchable} loader.
	 *
	 * @param <C> The type of the value.
	 * @param <T> The type of the NBT tag.
	 */
	public static <C, T extends Tag> NbtLoader<Switchable<C>, CompoundTag> generateLoader(NbtLoader<C, T> loader) {
		return new NbtLoader<>() {
			
			/**
			 * Load a {@link Switchable} object from an NBT tag.
			 *
			 * @param tag The NBT tag containing data to be loaded.
			 * @return The loaded {@link Switchable} object.
			 * @throws IllegalNbtException If there is an issue with the NBT data.
			 */
			@Override
			public Switchable<C> load(CompoundTag tag) throws IllegalNbtException {
				boolean enabled = tag.getByte("enabled").getValue() != 0;
				C value = loader.load(tag.get("value"));
				
				return of(value, enabled);
			}
			
			/**
			 * Save a {@link Switchable} object to an NBT tag.
			 *
			 * @param object The object to be saved.
			 * @return The NBT tag containing the saved data.
			 */
			@Override
			public CompoundTag save(Switchable<C> object) {
				CompoundTag tag = new CompoundTag();
				
				tag.putByte("enabled", (byte) (object.isEnabled() ? 1 : 0));
				tag.put("value", loader.save(object.get()));
				
				return tag;
			}
		};
	}
	
	/**
	 * Returns the value of the {@link Switchable} object if it is enabled,
	 * or {@code null} otherwise.
	 * <p>
	 * This method is equivalent to {@code getIfEnabled(null)}
	 *
	 * @return The value of the {@link Switchable} object if it is enabled,
	 *         or {@code null} otherwise.
	 * @see #getIfEnabled(T)
	 */
	public T getIfEnabled() {
		return getIfEnabled(null);
	}
	
	/**
	 * Returns the value of the {@link Switchable} object if it is enabled,
	 * or {@code defaultValue} otherwise.
	 *
	 * @param defaultValue The default value to be returned if the {@link Switchable}
	 *        is disabled.
	 * @return The value of the {@link Switchable} object if it is enabled,
	 *         or {@code defaultValue} otherwise.
	 */
	public T getIfEnabled(T defaultValue) {
		return enabled ? value : defaultValue;
	}
	
	/**
	 * Returns whether the {@link Switchable} object is enabled.
	 *
	 * @return Whether the {@link Switchable} object is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Switches the {@link Switchable} object on or off.
	 *
	 * @param enabled Whether the {@link Switchable} object should be enabled.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}

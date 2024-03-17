package minecraft.morningmc.mcli.utils;

/**
 * A container object that allows to switch on or off and modify the value at the same time.
 *
 * @param <T> The type of the value.
 */
public class Switchable <T> {
	private T value;
	private boolean enabled;
	
	/**
	 * Constructs a new {@code Switchable} object.
	 *
	 * @param value The value to be stored in the {@code Switchable} object.
	 * @param enabled Whether the {@code Switchable} object is enabled.
	 */
	private Switchable(T value, boolean enabled) {
		this.value = value;
		this.enabled = enabled;
	}
	
	/**
	 * Constructs a new {@code Switchable} object.
	 *
	 * @param value The value to be stored in the {@code Switchable} object.
	 * @param enabled Whether the {@code Switchable} object is enabled.
	 * @return A new {@code Switchable} object.
	 *
	 * @param <T> The type of the value.
	 */
	public static <T> Switchable<T> of(T value, boolean enabled) {
		return new Switchable<>(value, enabled);
	}
	
	/**
	 * Constructs a new enabled {@code Switchable} object.
	 *
	 * @param value The value to be stored in the {@code Switchable} object.
	 * @return A new {@code Switchable} object.
	 *
	 * @param <T> The type of the value.
	 */
	public static <T> Switchable<T> ofEnabled(T value) {
		return new Switchable<>(value, true);
	}
	
	/**
	 * Constructs a new disabled {@code Switchable} object.
	 *
	 * @param value The value to be stored in the {@code Switchable} object.
	 * @return A new {@code Switchable} object.
	 *
	 * @param <T> The type of the value.
	 */
	public static <T> Switchable<T> ofDisabled(T value) {
		return new Switchable<>(value, false);
	}
	
	/**
	 * Gets the value of the {@code Switchable} object.
	 *
	 * @return The value of the {@code Switchable} object.
	 */
	public T get() {
		return value;
	}
	
	/**
	 * Returns the value of the {@code Switchable} object if it is enabled,
	 * or null otherwise.
	 * <p>
	 * This method is equivalent to {@code getIfEnabled(null)}
	 *
	 * @return The value of the {@code Switchable} object if it is enabled,
	 *         or null otherwise.
	 * @see #getIfEnabled(T)
	 */
	public T getIfEnabled() {
		return getIfEnabled(null);
	}
	
	/**
	 * Returns the value of the {@code Switchable} object if it is enabled,
	 * or {@code defaultObject} otherwise.
	 *
	 * @param defaultObject The default value to be returned if the {@code Switchable}
	 *        is disabled.
	 * @return The value of the {@code Switchable} object if it is enabled,
	 *         or {@code defaultObject} otherwise.
	 */
	public T getIfEnabled(T defaultObject) {
		return enabled ? value : defaultObject;
	}
	
	/**
	 * Modifies the value of the {@code Switchable} object.
	 *
	 * @param value The new value to be stored in the {@code Switchable} object.
	 */
	public void modify(T value) {
		this.value = value;
	}
	
	/**
	 * Returns whether the {@code Switchable} object is enabled.
	 *
	 * @return Whether the {@code Switchable} object is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Switches the {@code Switchable} object on or off.
	 *
	 * @param enabled Whether the {@code Switchable} object should be enabled.
	 */
	public void switchEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}

package minecraft.morningmc.mcli.utils.containers;

/**
 * A container object for a modifiable value.
 *
 * @param <T> The type of the value.
 */
public class Modifiable<T> {
	private T value;
	
	/**
	 * Constructs a new {@code Modifiable} instance with the specified initial value.
	 *
	 * @param value The initial value.
	 */
	public Modifiable(T value) {
		this.value = value;
	}
	
	/**
	 * Creates a new {@code Modifiable} instance with the specified initial value.
	 *
	 * @param value The initial value.
	 * @return A new Modifiable instance.
	 * @param <T> The type of the value.
	 */
	public static <T> Modifiable<T> of(T value) {
		return new Modifiable<>(value);
	}
	
	/**
	 * Gets the current value.
	 *
	 * @return The current value.
	 */
	public T get() {
		return value;
	}
	
	/**
	 * Sets a new value.
	 *
	 * @param value The new value to set.
	 */
	public void set(T value) {
		this.value = value;
	}
}
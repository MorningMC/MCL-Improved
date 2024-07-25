package minecraft.morningmc.mcli.utils.interfaces;

/**
 * Represents a builder pattern for creating instances of type {@code T}.
 *
 * @param <T> the type of object that this builder creates.
 */
public interface Builder<T> {
	
	/**
	 * Creates a builder that returns the provided object when {@link #build()} is called.
	 *
	 * @param object the object to be returned by the builder.
	 * @param <T> the type of the object.
	 * @return A builder that returns the provided object.
	 */
	static <T> Builder<T> of(final T object) {
		return new Builder<>() {
			
			@Override
			public T build() {
				return object;
			}
			
			@Override
			public T buildDefault() {
				return object;
			}
		};
	}
	
	/**
	 * Builds and returns an instance of type {@code T}.
	 *
	 * @return The built object of type {@code T}.
	 */
	T build();
	
	/**
	 * Builds and returns a default instance of type {@code T}.
	 *
	 * @return The default built object of type {@code T}.
	 */
	default T buildDefault() {
		return null;
	}
}
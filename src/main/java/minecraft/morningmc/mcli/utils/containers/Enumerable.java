package minecraft.morningmc.mcli.utils.containers;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.util.function.*;

/**
 * A container object that allows to switch between different enums and modify the value at the same time.
 *
 * @param <T> The type of the value.
 * @param <E> The type of the policy.
 */
public class Enumerable<T, E extends Enum<E>> extends Modifiable<T> {
	public E policy;
	
	/**
	 * Constructs a new {@link Enumerable} object.
	 *
	 * @param value The value to be stored in the {@link Enumerable} object.
	 * @param policy The policy to be used.
	 */
	public Enumerable(T value, E policy) {
		super(value);
		this.policy = policy;
	}
	
	/**
	 * Creates a new {@link Enumerable} object.
	 *
	 * @param value The value to be stored in the {@link Enumerable} object.
	 * @param policy The policy to be used.
	 * @return A new {@link Enumerable} object.
	 *
	 * @param <T> The type of the value.
	 * @param <E> The type of the policy.
	 */
	public static <T, E extends Enum<E>> Enumerable<T, E> of(T value, E policy) {
		return new Enumerable<>(value, policy);
	}
	
	/**
	 * Generates a {@link Enumerable} loader from a loader.
	 *
	 * @param loader The loader to be wrapped in a {@link Enumerable} loader.
	 * @return The {@link Enumerable} loader.
	 *
	 * @param <C> The type of the value.
	 * @param <E> The type of the policy.
	 * @param <T> The type of the NBT tag.
	 */
	public static <C, E extends Enum<E>, T extends Tag> NbtLoader<Enumerable<C, E>, CompoundTag> generateLoader(NbtLoader<C, T> loader, Class<E> enumClass) {
		return new NbtLoader<>() {
			
			/**
			 * Load a {@link Enumerable} object from an NBT tag.
			 *
			 * @param tag The NBT tag containing data to be loaded.
			 * @return The loaded {@link Enumerable} object.
			 * @throws IllegalNbtException If there is an issue with the NBT data.
			 */
			@Override
			public Enumerable<C, E> load(CompoundTag tag) throws IllegalNbtException {
				E policy = Enum.valueOf(enumClass, tag.getString("policy").getValue());
				C value = loader.load(tag.get("value"));
				
				return of(value, policy);
			}
			
			/**
			 * Save a {@link Enumerable} object to an NBT tag.
			 *
			 * @param object The object to be saved.
			 * @return The NBT tag containing the saved data.
			 */
			@Override
			public CompoundTag save(Enumerable<C, E> object) {
				CompoundTag tag = new CompoundTag();
				tag.putString("policy", object.policy.name());
				tag.put("value", loader.save(object.value));

				return tag;
			}
		};
	}
	
	/**
	 * Returns the value based on the switcher.
	 *
	 * @param switcher The switcher to be used.
	 * @return The value based on the switcher.
	 */
	public T get(BiFunction<T, E, T> switcher) {
		return switcher.apply(value, policy);
	}
	
	/**
	 * Returns the value if the policy matches the given policy,
	 * or null otherwise.
	 * <p>
	 * This method is equivalent to {@code getIf(policy, null)}.
	 *
	 * @param policy The policy to check.
	 * @return The value if the policy matches the given policy, otherwise {@code null}.
	 */
	public T getIf(E policy) {
		return getIf(policy, null);
	}
	
	/**
	 * Returns the value if the policy matches the given policy,
	 * or the default value otherwise.
	 *
	 * @param policy The policy to check.
	 * @param defaultValue The default value to be returned if the policy does not match.
	 * @return The value if the policy matches the given policy, otherwise the default value.
	 */
	public T getIf(E policy, T defaultValue) {
		return this.policy.equals(policy) ? this.value : defaultValue;
	}
}

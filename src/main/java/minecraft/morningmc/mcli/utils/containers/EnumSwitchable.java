package minecraft.morningmc.mcli.utils.containers;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * A container object that allows to switch between different enums and modify the value at the same time.
 *
 * @param <T> The type of the value.
 * @param <E> The type of the policy.
 */
public class EnumSwitchable<T, E extends Enum<E>> extends Modifiable<T> {
	private E policy;
	
	/**
	 * Constructs a new {@code EnumSwitchable} object.
	 *
	 * @param value The value to be stored in the {@code EnumSwitchable} object.
	 * @param policy The policy to be used.
	 */
	public EnumSwitchable(T value, E policy) {
		super(value);
		this.policy = policy;
	}
	
	/**
	 * Creates a new {@code EnumSwitchable} object.
	 *
	 * @param value The value to be stored in the {@code EnumSwitchable} object.
	 * @param policy The policy to be used.
	 * @return A new {@code EnumSwitchable} object.
	 *
	 * @param <T> The type of the value.
	 * @param <E> The type of the policy.
	 */
	public static <T, E extends Enum<E>> EnumSwitchable<T, E> of(T value, E policy) {
		return new EnumSwitchable<>(value, policy);
	}
	
	/**
	 * Generates a {@code EnumSwitchable} loader from a loader.
	 *
	 * @param loader The loader to be wrapped in a {@code EnumSwitchable} loader.
	 * @return The {@code EnumSwitchable} loader.
	 *
	 * @param <C> The type of the value.
	 * @param <E> The type of the policy.
	 * @param <T> The type of the NBT tag.
	 */
	public static <C, E extends Enum<E>, T extends Tag> NbtLoader<EnumSwitchable<C, E>, CompoundTag> generateLoader(NbtLoader<C, T> loader, Class<E> enumClass) {
		return new NbtLoader<>() {
			
			/**
			 * Load a {@code EnumSwitchable} object from an NBT tag.
			 *
			 * @param tag The NBT tag containing data to be loaded.
			 * @return The loaded {@code EnumSwitchable} object.
			 * @throws IllegalNbtException If there is an issue with the NBT data.
			 */
			@Override
			public EnumSwitchable<C, E> load(CompoundTag tag) throws IllegalNbtException {
				E policy = Enum.valueOf(enumClass, tag.getString("policy").getValue());
				C value = loader.load(tag.get("value"));
				
				return of(value, policy);
			}
			
			/**
			 * Save a {@code EnumSwitchable} object to an NBT tag.
			 *
			 * @param object The object to be saved.
			 * @return The NBT tag containing the saved data.
			 */
			@Override
			public CompoundTag save(EnumSwitchable<C, E> object) {
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
	public T get(Switcher<T, E> switcher) {
		return switcher.switchPolicy(value, policy);
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
	
	/**
	 * Returns the policy.
	 *
	 * @return The policy.
	 */
	public E getPolicy() {
		return policy;
	}
	
	/**
	 * Sets the policy.
	 *
	 * @param policy The policy to be set.
	 */
	public void setPolicy(E policy) {
		this.policy = policy;
	}
	
	/**
	 * A functional interface that allows to switch the value based on a policy.
	 *
	 * @param <T> The type of the value.
	 * @param <E> The type of the policy.
	 */
	@FunctionalInterface
	public interface Switcher<T, E extends Enum<E>> {
		
		/**
		 * Switches the value based on the policy.
		 *
		 * @param value The value to be switched.
		 * @param policy The policy to be used.
		 * @return The switched value.
		 */
		T switchPolicy(T value, E policy);
	}
}

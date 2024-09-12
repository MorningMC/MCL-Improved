package minecraft.morningmc.mcli.utils.interfaces;

import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;

import java.util.*;

/**
 * An interface representing an object with a UUID.
 */
public interface UniqueObject {
	
	/**
	 * Compares two unique objects.
	 *
	 * @param a The first unique object.
	 * @param b The second unique object.
	 * @return {@code true} if the objects are equal, {@code false} otherwise.
	 */
	static boolean equals(UniqueObject a, UniqueObject b) {
		if (a == null || b == null) {
			return false;
		}
		return a.identifier().equals(b.identifier());
	}
	
	/**
	 * Retrieves the {@link UUID} associated with this object.
	 *
	 * @return the {@link UUID} associated with this object.
	 */
	UUID identifier();
	
	@ObjectCollection
	interface Collection<E extends UniqueObject> {
	
	}
}
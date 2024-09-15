package minecraft.morningmc.mcli.utils;

import java.util.*;

/**
 * Represents a conditional value, which is associated with a set of rules.
 * The value is only applicable if all the rules are satisfied.
 *
 * @param rules A set of rules that must be satisfied for the value to be applicable.
 * @param value The value associated with the conditions.
 * @param <T> The type of the value.
 */
public record Conditional<T>(Set<Rule> rules, T value) {
	
	/**
	 * Creates an {@link Conditional} instance without rules, which always passes the rule check.
	 *
	 * @param value The value associated with the unconditional condition.
	 * @param <T> The type of the value.
	 * @return A {@link Conditional} instance that always passes the rule check.
	 */
	public static <T> Conditional<T> unconditional(T value) {
		return new Conditional<>(Set.of(Rule.allow), value);
	}
	
	/**
	 * Splits a list of values into a list of {@link Conditional} instances, each associated with the given set of rules.
	 *
	 * @param rules  A set of rules that must be satisfied for the values to be applicable.
	 * @param values A list of values to be associated with the given set of rules.
	 * @param <T> The type of the values.
	 * @return A list of {@link Conditional} instances, each associated with the given set of rules.
	 */
	public static <T> List<Conditional<T>> split(Set<Rule> rules, List<T> values) {
		return values.stream().map(value -> new Conditional<>(rules, value)).toList();
	}
	
	/**
	 * Checks if all the rules match the given features, operating system, and architecture.
	 *
	 * @param features A map of features where the key is the feature name, and the value indicates whether it is enabled ({@code true}) or not ({@code false}).
	 * @return {@code true} if all the rules are passed, {@code false} otherwise.
	 */
	public boolean check(Map<String, Boolean> features) {
		return rules.stream().allMatch(rule -> rule.check(features));
	}
	
	/**
	 * Represents a single rule, which can include conditions based on features, operating system, and architecture.
	 *
	 * @param action    A boolean indicating whether the rule allows ({@code true}) or disallows ({@code false}) the condition.
	 * @param features  A map of features where the key is the feature name, and the value indicates whether it is required ({@code true}) or not ({@code false}).
	 * @param os        The operating system that this rule applies to, or {@link Platform.OperatingSystem#UNKNOWN} if not specified.
	 * @param arch      The architecture that this rule applies to, or {@link Platform.Architecture#UNKNOWN} if not specified.
	 */
	public record Rule(boolean action, Map<String, Boolean> features, Platform.OperatingSystem os, Platform.Architecture arch) {
		/** The rule that always passes the check. This means its {@link #check(Map)} method always returns {@code true}. */
		public static final Rule allow = new Rule(true, Map.of(), Platform.OperatingSystem.UNKNOWN, Platform.Architecture.UNKNOWN);
		
		/**
		 * Checks if this rule matches the given features, operating system, and architecture.
		 *
		 * @param features A map of features where the key is the feature name, and the value indicates whether it is enabled ({@code true}) or not ({@code false}).
		 * @return {@code true} if the rule is passed, {@code false} otherwise.
		 */
		public boolean check(Map<String, Boolean> features) {
			boolean passed = true;
			
			passed &= this.features.entrySet().stream().allMatch(entry -> features.getOrDefault(entry.getKey(), false) == entry.getValue());
			passed &= os == Platform.OperatingSystem.UNKNOWN || Platform.system.operatingSystem() == os; // Platform.OperatingSystem.UNKNOWN also refers to any operating system
			passed &= arch == Platform.Architecture.UNKNOWN || Platform.system.architecture() == arch; // Platform.Architecture.UNKNOWN also refers to any architecture
			
			return passed == action;
		}
	}
}
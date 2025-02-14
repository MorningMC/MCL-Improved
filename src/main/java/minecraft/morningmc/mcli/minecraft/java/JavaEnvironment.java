package minecraft.morningmc.mcli.minecraft.java;

public record JavaEnvironment (JavaRuntime runtime,
                               int minMemory,
                               int maxMemory) {

	public static JavaEnvironment create(JavaRuntime runtime) {
		return new JavaEnvironment(runtime, 2048, 4096);
	}
}

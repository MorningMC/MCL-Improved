package minecraft.morningmc.mcli.minecraft.client.resources;

import minecraft.morningmc.mcli.launcher.networking.Requester;
import minecraft.morningmc.mcli.utils.FileManager;
import minecraft.morningmc.mcli.minecraft.client.resources.marker.Marker;

import javafx.scene.image.Image;

import com.moandjiezana.toml.Toml;
import com.google.gson.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;
import java.util.zip.*;

/**
 * Represents a Minecraft modification.
 */
public class Modification {
	private static final Logger logger = LogManager.getLogger();
	
	public final Set<Info> infos;
	public final Set<Loader> loaders;
	public final File file;
	public Marker marker;
	
	/**
	 * Constructs a new {@link Modification} instance from a file.
	 *
	 * @param file The mod file.
	 */
	public Modification(File file) {
		this(file, null);
	}
	
	/**
	 * Constructs a new {@link Modification} instance from a file and a marker.
	 *
	 * @param file The mod file.
	 * @param marker The marker.
	 */
	public Modification(File file, Marker marker) {
		if (!file.isFile()) {
			throw new IllegalArgumentException("Illegal mod file: " + file.getAbsolutePath());
		}
		
		this.file = file;
		this.marker = marker;
		loaders = Loader.infer(file);
		infos = Info.parse(file, loaders);
	}
	
	/**
	 * Checks if a mod file is enabled.
	 *
	 * @return {@code true} if the mod file is enabled, {@code false} otherwise.
	 */
	public boolean isEnabled() {
		return !file.getName().endsWith(".disabled") && !file.getName().endsWith(".disable");
	}
	
	/**
	 * Enables the mod file.
	 * If the mod file is already enabled, this method does nothing.
	 */
	public void enable() {
		if (!isEnabled()) {
			FileManager.renameFile(file, file.getName().substring(0, file.getName().lastIndexOf('.')));
		}
	}
	
	/**
	 * Disables the mod file.
	 * If the mod file is already disabled, this method does nothing.
	 */
	public void disable() {
		if (isEnabled()) {
			FileManager.renameFile(file, file.getName() + ".disabled");
		}
	}
	
	/**
	 * Enumerates the different mod loaders.
	 */
	public enum Loader {
		FORGE, NEOFORGE, FABRIC, QUILT, LITELOADER, RIFT;
		
		/**
		 * Infers the mod loader based on the contents of the mod file.
		 *
		 * @param file The mod file.
		 * @return The inferred {@link Loader}.
		 */
		public static Set<Loader> infer(File file) {
			Set<Loader> loaders = new HashSet<>();
			
			try (JarFile jarFile = new JarFile(file)) {
				if (jarFile.getEntry("META-INF/mods.toml") != null) {
					loaders.add(FORGE);
				}
				if (jarFile.getEntry("META-INF/neoforge.mods.toml") != null) {
					loaders.add(NEOFORGE);
				}
				if (jarFile.getEntry("fabric.mod.json") != null) {
					loaders.add(FABRIC);
				}
				if (jarFile.getEntry("quilt.mod.json") != null) {
					loaders.add(QUILT);
				}
				if (jarFile.getEntry("litemod.json") != null) {
					loaders.add(LITELOADER);
				}
				if (jarFile.getEntry("mcmod.info") != null) {
					loaders.add(RIFT);
				}
				return loaders;
				
			} catch (Exception e) {
				logger.warn("Failed to infer mod loader for file {}: {}", file.getAbsolutePath(), e.getMessage());
				return loaders;
			}
		}
	}
	
	/**
	 * Represents the information of a mod.
	 *
	 * @param lowcode            Whether the mod is lowcode or not. <i>Forge and NeoForge only.</i>
	 * @param fmlVersion         The acceptable version range of the Forge Mod Loader, expressed as a <a href="https://maven.apache.org/enforcer/enforcer-rules/versionRanges.html">Maven Version Range</a>. <i>Forge and NeoForge only.</i>
	 * @param modID              The mod identifier.
	 * @param version            The version of the mod.
	 * @param name               The pretty name of the mod. Used when representing the mod on a screen.
	 * @param description        The description of the mod.
	 * @param icon               The icon of the mod.
	 * @param iconImage          The {@link Image} instance of the icon of the mod.
	 * @param iconBlur           Whether the icon should be blurred or not when trying to scale the icon. <i>Forge and NeoForge only.</i>
	 * @param contributors       The contributors of the mod.
	 * @param group              The group of the mod. <i>Quilt only.</i>
	 * @param contact            The contact information of the mod.
	 * @param license            The license of the mod. <i>Forge, NeoForge and Fabric only.</i>
	 * @param credits            The credits of the mod. <i>Forge and NeoForge only.</i>
	 * @param environment        The environment of the mod. <i>Forge, Fabric and Quilt only.</i>
	 * @param showAsResourcePack Whether the mod should be shown as a resource pack or not. <i>Forge and NeoForge only.</i>
	 * @param showAsDataPack     Whether the mod should be shown as a data pack or not. <i>NeoForge only.</i>
	 * @param usedServices       The used services of the mod. <i>Forge and NeoForge only.</i>
	 * @param entrypoints        The entrypoints of the mod. <i>Fabric and Quilt only.</i>
	 * @param dependencies       The dependencies of the mod.
	 * @param accessWidener      The access widener of the mod. <i>Fabric only.</i>
	 * @param mixins             The mixins of the mod. <i>Fabric and Quilt only.</i>
	 */
	public record Info(boolean lowcode,
					   String fmlVersion,
	                   String modID,
	                   String version,
	                   String name,
	                   String description,
	                   String icon,
	                   Image iconImage,
					   boolean iconBlur,
	                   Map<String, String> contributors,
					   String group,
	                   Map<String, URL> contact,
	                   String license,
					   String credits,
	                   Environment environment,
					   boolean showAsResourcePack,
					   boolean showAsDataPack,
	                   Set<String> usedServices,
	                   Map<String, Set<String>> entrypoints,
	                   Set<Dependency> dependencies,
	                   String accessWidener,
	                   Set<String> mixins) {
		
		/**
		 * Parses a mod file and returns a set of {@link Info} object.
		 *
		 * @param file    The mod file to be parsed.
		 * @param loaders The loaders of the mod file.
		 * @return The parsed {@link Info} objects, or empty {@link Set} if the file could not be parsed.
		 */
		public static Set<Info> parse(File file, Set<Loader> loaders) {
			Set<Info> infos = new HashSet<>();
			
			try (JarFile jarFile = new JarFile(file)) {
				
				// parsing Forge mod info
				if (loaders.contains(Loader.FORGE)) {
					try (InputStream in = getJarInputStream(jarFile, "META-INF/mods.toml")) {
						Toml toml = new Toml().read(in);
						
						boolean lowcode = toml.getString("modLoader").equals("lowcodefml");
						String fmlVersion = toml.getString("loaderVersion");
						String license = toml.getString("license");
						boolean showAsResourcePack = toml.getBoolean("showAsResourcePack", false);
						Set<String> usedServices = new HashSet<>(toml.getList("services", List.of()));
						Map<String, String> properties = Optional.ofNullable(toml.getTable("properties"))
								                                 .map(p -> p.toMap().keySet().stream().collect(Collectors.toMap("${file.%s}"::formatted, p::getString)))
								                                 .orElseGet(HashMap::new);
						properties.put("${file.jarVersion}", Optional.ofNullable(jarFile.getManifest().getMainAttributes().getValue("Implementation-Version")).orElse("0.0NONE"));
						URL issueTracker = Requester.newURL(toml.getString("issueTrackerURL"));
						
						for (Toml mods : toml.getTables("mods")) {
							String modID = mods.getString("modId");
							String version = properties.entrySet().stream().reduce(
									mods.getString("version", "1"),
									(ver, entry) -> ver.replace(entry.getKey(), entry.getValue()),
									(ver1, ver2) -> ver1
							);
							String name = mods.getString("displayName", modID);
							String description = mods.getString("description", "MISSING DESCRIPTION");
							String icon = mods.getString("logoFile", "");
							boolean iconBlur = mods.getBoolean("logoBlur", true);
							Map<String, String> contributors = Arrays.stream(mods.getString("authors", "").split(","))
									                                   .map(String::trim)
									                                   .collect(Collectors.toMap(contributor -> contributor, ignored -> "Developer"));
							String credits = mods.getString("credits", "");
							Environment environment = Environment.parseDisplayTest(mods.getString("displayTest", "MATCH_VERSION"));
							
							// parse dependencies
							Set<Dependency> dependencies = new HashSet<>();
							for (Toml dependency : mods.getTables("dependencies." + modID)) {
								dependencies.add(Dependency.parseForge(dependency));
							}
							
							// parse contact information
							Map<String, URL> contact = new HashMap<>();
							if (issueTracker != null) {
								contact.put("issues", issueTracker);
							}
							URL updateChecker = Requester.newURL(toml.getString("updateJSONURL"));
							if (updateChecker != null) {
								contact.put("update", updateChecker);
							}
							URL displayURL = Requester.newURL(toml.getString("displayURL"));
							if (displayURL != null) {
								contact.put("homepage", displayURL);
							}
							
							logger.trace("Parsed Forge mod info: {}", modID);
							infos.add(new Info(lowcode, fmlVersion, modID, version, name, description, icon, parseIconImage(jarFile, icon), iconBlur, contributors, null, contact, license, credits, environment, showAsResourcePack, false, usedServices, Map.of(), dependencies, null, Set.of()));
						}
					} catch (Exception e) {
						logger.warn("Failed to parse Forge mod info for file {}: {}", file.getAbsolutePath(), e.getMessage());
					}
				}
				
				// parsing NeoForge mod info
				if (loaders.contains(Loader.NEOFORGE)) {
					try (InputStream in = getJarInputStream(jarFile, "META-INF/neoforge.mods.toml")) {
						Toml toml = new Toml().read(in);
						
						boolean lowcode = toml.getString("modLoader").equals("lowcodefml");
						String fmlVersion = toml.getString("loaderVersion");
						String license = toml.getString("license");
						boolean showAsResourcePack = toml.getBoolean("showAsResourcePack", false);
						boolean showAsDataPack = toml.getBoolean("showAsDataPack", false);
						Set<String> usedServices = new HashSet<>(toml.getList("services", List.of()));
						Map<String, String> properties = Optional.ofNullable(toml.getTable("properties"))
								                                 .map(p -> p.toMap().keySet().stream().collect(Collectors.toMap("${file.%s}"::formatted, p::getString)))
								                                 .orElseGet(HashMap::new);
						properties.put("${file.jarVersion}", Optional.ofNullable(jarFile.getManifest().getMainAttributes().getValue("Implementation-Version")).orElse("0.0NONE"));
						URL issueTracker = Requester.newURL(toml.getString("issueTrackerURL"));
						
						for (Toml mods : toml.getTables("mods")) {
							String modID = mods.getString("modId");
							String version = properties.entrySet().stream().reduce(
									mods.getString("version", "1"),
									(ver, entry) -> ver.replace(entry.getKey(), entry.getValue()),
									(ver1, ver2) -> ver1
							);
							String name = mods.getString("displayName", modID);
							String description = mods.getString("description", "MISSING DESCRIPTION");
							String icon = mods.getString("logoFile", "");
							boolean iconBlur = mods.getBoolean("logoBlur", true);
							Map<String, String> contributors = Arrays.stream(mods.getString("authors", "").split(","))
									                                   .map(String::trim)
									                                   .collect(Collectors.toMap(contributor -> contributor, ignored -> "Developer"));
							String credits = mods.getString("credits", "");

							// parse dependencies
							Set<Dependency> dependencies = new HashSet<>();
							for (Toml dependency : mods.getTables("dependencies." + modID)) {
								dependencies.add(Dependency.parseNeoForge(dependency));
							}

							// parse contact information
							Map<String, URL> contact = new HashMap<>();
							if (issueTracker != null) {
								contact.put("issues", issueTracker);
							}
							URL updateChecker = Requester.newURL(toml.getString("updateJSONURL"));
							if (updateChecker != null) {
								contact.put("update", updateChecker);
							}
							URL displayURL = Requester.newURL(toml.getString("displayURL"));
							if (displayURL != null) {
								contact.put("homepage", displayURL);
							}

							logger.trace("Parsed NeoForge mod info: {}", modID);
							infos.add(new Info(lowcode, fmlVersion, modID, version, name, description, icon, parseIconImage(jarFile, icon), iconBlur, contributors, null, contact, license, credits, Environment.UNKNOWN, showAsResourcePack, showAsDataPack, usedServices, Map.of(), dependencies, null, Set.of()));
						}
					} catch (Exception e) {
						logger.warn("Failed to parse NeoForge mod info for file {}: {}", file.getAbsolutePath(), e.getMessage());
					}
				}
				
				// parsing Fabric mod info
				if (loaders.contains(Loader.FABRIC)) {
					try (InputStream in = getJarInputStream(jarFile, "fabric.mod.json")) {
						JsonObject json = JsonParser.parseReader(FileManager.getReader(in)).getAsJsonObject();
						
						String modID = json.get("id").getAsString();
						String version = json.get("version").getAsString();
						String name = Optional.ofNullable(json.get("name").getAsString()).orElse(modID);
						String description = Optional.ofNullable(json.get("description").getAsString()).orElse("");
						String icon = json.get("icon").getAsString();
						Map<String, String> contributors = json.getAsJsonArray("authors").asList().stream()
								                                   .map(JsonElement::getAsString)
								                                   .collect(Collectors.toMap(contributor -> contributor, ignored -> "Developer"));
						Map<String, URL> contact = json.getAsJsonObject("contact").entrySet().stream()
								                           .flatMap(e -> {
									                           try {
										                           return Stream.of(Map.entry(
																		   e.getKey(),
												                           Objects.requireNonNull(Requester.newURL(e.getValue().getAsString()))
										                           ));
									                           } catch (Exception ex) {
										                           return Stream.empty();
									                           }
								                           })
								                           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
						String license = json.get("license").getAsString();
						Environment environment = Optional.ofNullable(json.get("environment").getAsString()).orElse("*").equals("*") ? Environment.BOTH : Environment.valueOf(json.get("environment").getAsString().toUpperCase());
						Map<String, Set<String>> entrypoints = json.getAsJsonObject("entrypoints").entrySet().stream()
								                                       .collect(Collectors.toMap(
										                                       Map.Entry::getKey,
										                                       entry -> entry.getValue().getAsJsonObject().entrySet().stream()
												                                            .map(Map.Entry::getKey)
												                                            .collect(Collectors.toSet())
								                                       ));
						String accessWidener = json.get("accessWidener").getAsString();
						Set<String> mixins = json.getAsJsonArray("mixins").asList().stream().map(JsonElement::getAsString).collect(Collectors.toSet());
						
						// parse dependencies
						Set<Dependency> dependencies = new HashSet<>();
						dependencies.addAll(Dependency.parseFabric(json.getAsJsonObject("depends"), Dependency.Type.REQUIRED));
						dependencies.addAll(Dependency.parseFabric(json.getAsJsonObject("recommends"), Dependency.Type.RECOMMENDS));
						dependencies.addAll(Dependency.parseFabric(json.getAsJsonObject("suggests"), Dependency.Type.OPTIONAL));
						dependencies.addAll(Dependency.parseFabric(json.getAsJsonObject("conflicts"), Dependency.Type.CONFLICTS));
						dependencies.addAll(Dependency.parseFabric(json.getAsJsonObject("breaks"), Dependency.Type.INCOMPATIBLE));
						
						logger.trace("Parsed Fabric mod info: {}", modID);
						infos.add(new Info(false, null, modID, version, name, description, icon, parseIconImage(jarFile, icon), false, contributors, null, contact, license, null, environment, false, false, Set.of(), entrypoints, dependencies, accessWidener, mixins));
					} catch (Exception e) {
						logger.warn("Failed to parse Fabric mod info for file {}: {}", file.getAbsolutePath(), e.getMessage());
					}
				}
				
				// parsing Quilt mod info
				if (loaders.contains(Loader.QUILT)) {
					try (InputStream in = getJarInputStream(jarFile, "quilt.mod.json")) {
						JsonObject json = JsonParser.parseReader(FileManager.getReader(in)).getAsJsonObject();
						
						// parse entries that warped in the "quilt_loader" entry
						JsonObject quilt = json.getAsJsonObject("quilt_loader");
						String group = quilt.get("group").getAsString();
						String modID = quilt.get("id").getAsString();
						String version = quilt.get("version").getAsString();
						
						// parse entries that warped in the "metadata" entry
						JsonObject metadata = quilt.getAsJsonObject("metadata");
						String name = metadata.get("name").getAsString();
						String description = metadata.get("description").getAsString();
						Map<String, String> contributors = metadata.getAsJsonObject("authors").entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAsString()));
						Map<String, URL> contact = metadata.getAsJsonObject("contact").entrySet().stream()
								                           .flatMap(e -> {
									                           try {
										                           return Stream.of(Map.entry(
												                           e.getKey(),
												                           Objects.requireNonNull(Requester.newURL(e.getValue().getAsString()))
										                           ));
									                           } catch (Exception ex) {
										                           return Stream.empty();
									                           }
								                           })
								                           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
						String icon = metadata.get("icon").getAsString();
						// end of the "metadata" entry
						
						Map<String, Set<String>> entrypoints = quilt.getAsJsonObject("entrypoints").entrySet().stream()
								                                       .collect(Collectors.toMap(
																			   Map.Entry::getKey,
										                                       entry -> entry.getValue().getAsJsonObject().entrySet().stream()
												                                            .map(Map.Entry::getKey)
												                                            .collect(Collectors.toSet())
								                                       ));
						
						// parse dependencies
						Set<Dependency> dependencies = new HashSet<>();
						dependencies.addAll(Dependency.parseQuilt(quilt.getAsJsonArray("depends"), Dependency.Type.REQUIRED));
						dependencies.addAll(Dependency.parseQuilt(quilt.getAsJsonArray("recommends"), Dependency.Type.RECOMMENDS));
						dependencies.addAll(Dependency.parseQuilt(quilt.getAsJsonArray("suggests"), Dependency.Type.OPTIONAL));
						dependencies.addAll(Dependency.parseQuilt(quilt.getAsJsonArray("conflicts"), Dependency.Type.CONFLICTS));
						dependencies.addAll(Dependency.parseQuilt(quilt.getAsJsonArray("breaks"), Dependency.Type.INCOMPATIBLE));
						// end of the "quilt_loader" entry
						
						Set<String> mixins = Set.of(json.get("mixin").getAsString());
						
						logger.trace("Parsed Quilt mod info: {}", modID);
						infos.add(new Info(false, null, modID, version, name, description, icon, parseIconImage(jarFile, icon), false, contributors, group, contact, null, null, Environment.UNKNOWN, false, false, Set.of(), entrypoints, dependencies, null, mixins));
					} catch (Exception e) {
						logger.warn("Failed to parse Quilt mod info for file {}: {}", file.getAbsolutePath(), e.getMessage());
					}
					
					// parsing LiteLoader mod info is not supported yet
					if (loaders.contains(Loader.LITELOADER)) {
						logger.warn("LiteLoader parsing is not supported");
					}
					
					// parsing Rift mod info is not supported yet
					if (loaders.contains(Loader.RIFT)) {
						logger.warn("Rift parsing is not supported");
					}
				}
				
				return infos;
				
			} catch (Exception e) {
				logger.warn("Failed to parse mod file {}: {}", file.getAbsolutePath(), e.getMessage());
				return infos;
			}
		}
		
		/**
		 * Gets an {@link InputStream} for a specific entry in the mod file.
		 * 
		 * @param jarFile   A {@link JarFile} instance for the mod file.
		 * @param entryName The name of the entry.
		 * @return An {@link InputStream} for the specified entry.
		 * @throws IOException If an I/O error occurs while getting the {@link InputStream}.
		 */
		private static InputStream getJarInputStream(JarFile jarFile, String entryName) throws IOException {
			ZipEntry entry = jarFile.getEntry(entryName);
			return jarFile.getInputStream(entry);
		}
		
		/**
		 * Parses an {@link Image} instance of the icon from the mod file.
		 *
		 * @param jarFile A {@link JarFile} instance for the mod file.
		 * @param icon    The name of the icon entry.
		 * @return The {@link Image} instance of the icon, or {@code null} if an I/O error occurs.
		 */
		private static Image parseIconImage(JarFile jarFile, String icon) {
			try {
				return new Image(getJarInputStream(jarFile, icon));
			} catch (IOException e) {
				logger.warn("Failed to parse icon image for mod file: {}", e.getMessage());
				return null;
			}
		}
		
		/**
		 * Represents a dependency of a mod.
		 *
		 * @param modID       The mod identifier of the dependency.
		 * @param version     The required version of the dependency. <i>Fabric only.</i>
		 * @param type        The type of the dependency.
		 * @param ordering    Defines if the mod must load before or after this dependency. <i>Forge and NeoForge only.</i>
		 * @param environment The physical side the dependency must be present on. <i>Forge and NeoForge only.</i>
		 */
		public record Dependency(String modID, String version, Type type, Ordering ordering, Environment environment) {
			
			/**
			 * Parses a dependency from a {@link Toml} configuration specific to Forge.
			 *
			 * @param dependency The {@link Toml} object representing the dependency.
			 * @return A parsed {@link Dependency} object.
			 */
			public static Dependency parseForge(Toml dependency) {
				return new Dependency(
						dependency.getString("modId"),
						null,
						dependency.getBoolean("mandatory") ? Type.REQUIRED : Type.OPTIONAL,
						Ordering.valueOf(dependency.getString("ordering", "NONE")),
						Environment.valueOf(dependency.getString("side", "BOTH"))
				);
			}
			
			/**
			 * Parses a dependency from a {@link Toml} configuration specific to NeoForge.
			 *
			 * @param dependency The {@link Toml} object representing the dependency.
			 * @return A parsed {@link Dependency} object.
			 */
			public static Dependency parseNeoForge(Toml dependency) {
				return new Dependency(
						dependency.getString("modId"),
						null,
						switch (dependency.getString("type", "required")) {
							case "optional" -> Type.OPTIONAL;
							case "incompatible" -> Type.INCOMPATIBLE;
							case "discouraged" -> Type.CONFLICTS;
							default -> Type.REQUIRED;
						},
						Ordering.valueOf(dependency.getString("ordering", "NONE")),
						Environment.valueOf(dependency.getString("side", "BOTH"))
				);
			}
			
			/**
			 * Parses a set of dependencies from a {@link JsonObject} specific to Fabric.
			 *
			 * @param json The {@link JsonObject} representing the dependencies.
			 * @param type The type of the dependencies.
			 * @return A set of parsed {@link Dependency} objects.
			 */
			public static Set<Dependency> parseFabric(JsonObject json, Type type) {
				Set<Dependency> dependencies = new HashSet<>();
				
				if (json != null) {
					for (Map.Entry<String, JsonElement> dependency : json.entrySet()) {
						dependencies.add(new Dependency(
								dependency.getKey(),
								dependency.getValue().getAsString(),
								type,
								Ordering.NONE,
								Environment.UNKNOWN
						));
					}
				}
				
				return dependencies;
			}
			
			/**
			 * Parses a set of dependencies from a {@link JsonArray} specific to Quilt.
			 *
			 * @param json The {@link JsonArray} representing the dependencies.
			 * @param type The type of the dependencies.
			 * @return A set of parsed {@link Dependency} objects.
			 */
			public static Set<Dependency> parseQuilt(JsonArray json, Type type) {
				Set<Dependency> dependencies = new HashSet<>();
				
				if (json != null) {
					for (JsonElement dependency : json.asList()) {
						dependencies.add(new Dependency(
								dependency.getAsString(),
								null,
								type,
								Ordering.NONE,
								Environment.UNKNOWN
						));
					}
				}
				
				return dependencies;
			}
			
			/**
			 * Enumerates the different types of dependencies.
			 */
			public enum Type {
				/** For dependencies required to run. Without them a game will crash. */
				REQUIRED,
				
				/** For dependencies not required to run. Without them a game will log a warning. */
				RECOMMENDS,
				
				/** For dependencies not required to run. Use this as a kind of metadata. */
				OPTIONAL,
				
				/** For mods whose together with yours cause some kind of bugs, etc. With them a game will log a warning. */
				CONFLICTS,
				
				/** For mods whose together with yours might cause a game crash. With them a game will crash. */
				INCOMPATIBLE
			}
			
			/**
			 * Enumerates the different ordering options for a dependency.
			 */
			public enum Ordering {
				BEFORE, AFTER, NONE
			}
		}
		
		/**
		 * Enumerates the different mod environments.
		 */
		public enum Environment {
			CLIENT, SERVER, BOTH, UNKNOWN;
			
			/**
			 * Parses the {@code displayTest} statement in {@code META-INF/mods.toml} and infers the corresponding {@link Environment}.
			 *
			 * @param displayTest The {@code displayTest} statement in {@code META-INF/mods.toml}.
			 * @return The inferred {@link Environment}.
			 */
			public static Environment parseDisplayTest(String displayTest) {
				return switch (displayTest) {
					case "IGNORE_SERVER_VERSION" -> CLIENT;
					case "IGNORE_ALL_VERSION" -> SERVER;
					case "NONE" -> UNKNOWN;
					default -> BOTH;
				};
			}
		}
	}
}
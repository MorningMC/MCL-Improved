package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * The {@link Translation} class manages language translations for the application.
 * It loads translation files and provides methods to retrieve translated strings.
 */
public class Translation {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link Translation} objects from/to NBT data. */
	public static final NbtLoader<Translation, StringTag> loader = new NbtLoader<>() {
		
		@Override
		public Translation load(StringTag tag) {
			init(tag.getValue());
			return instance;
		}
		
		@Override
		public StringTag save(Translation object) {
			return new StringTag(object.language);
		}
	};
	
	/** The singleton instance of the {@link Translation} class. */
	public static Translation instance = null;
	
	/** The current language of the translations. */
	private final String language;
	
	/** A map storing the translation mappings. */
	private final Map<String, String> translations = new HashMap<>();
	
	/**
	 * Constructs a new {@link Translation} object and loads the translations for the specified language.
	 *
	 * @param language the language code (e.g., "en", "zh") for which to load translations.
	 */
	public Translation(String language) {
		this.language = language;
		
		// Load translations
		logger.info("Loading translations for language: {}", language);
		
		try (BufferedReader reader = FileMetadata.getReader(FileMetadata.getResource(
				"assets/lang/lang_%s.properties".formatted(language)))) {
			for (String line; (line = reader.readLine()) != null; ) {
				Matcher matcher = Pattern.compile("(?<key>.*)=(?<value>.*)").matcher(line);
				if (matcher.matches()) {
					logger.trace("Loading translations key: {}", matcher.group("key"));
					translations.put(matcher.group("key"), matcher.group("value"));
				}
			}
			
		} catch (IOException e) {
			logger.error("Failed to load translations for language: {}", language, e);
		}
	}
	
	/**
	 * Initializes the {@link Translation} instance with the specified language.
	 *
	 * @param language the language code for which to initialize translations.
	 * @throws IllegalStateException if the {@link Translation} instance is already initialized.
	 */
	public static void init(String language) {
		if (instance != null) {
			throw new IllegalStateException("Translation already initialized");
		}
		instance = new Translation(language);
	}
	
	/**
	 * Retrieves the current language of the translations.
	 *
	 * @return the language code of the current translations.
	 */
	public static String getLanguage() {
		return instance.language;
	}
	
	/**
	 * Sets the current language to the specified language and reloads the translations.
	 *
	 * @param language the new language code for which to load translations.
	 */
	public static void setLanguage(String language) {
		instance = new Translation(language);
	}
	
	/**
	 * Retrieves the translation for the specified key.
	 *
	 * @param key the key for which to retrieve the translation.
	 * @return the translated string, or the key itself if no translation is found.
	 */
	public static String get(String key) {
		String value =  instance.translations.get(key);
		if (value == null) {
			logger.warn("Translation key not found: {}", key);
            return key;
		}
		return value;
	}
	
	/**
	 * Retrieves the translation for the specified key and formats it with the provided arguments.
	 *
	 * @param key the key for which to retrieve the translation.
	 * @param args the arguments to format the translation string.
	 * @return the formatted translated string, or the key itself if no translation is found.
	 */
	public static String get(String key, Object... args) {
		return get(key).formatted(args);
	}
}
package tw.mayortw.mydungeon.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import tw.mayortw.mydungeon.MYDungeon;

public class I18n {
	public static final HashMap<String, I18n> dictionaries = new HashMap<>();
	
	HashMap<String, String> dictionary;
	
	private I18n (HashMap<String, String> dictIn) {
		dictionary = dictIn;
	}
	
	public String translate (String key) {
		String translated = this.dictionary.get(key);
		return translated == null ? key : translated;
	}
	
	public boolean has (String key) {
		return this.dictionary.containsKey(key);
	}
	
	public static I18n get () {
		return I18n.get(Locale.getDefault());
	}
	
	public static I18n get (Locale localeIn) {
		return get(localeIn.toString());
	}
	
	public static I18n get (String localeStrIn) {
		I18n i18n = dictionaries.get(localeStrIn.toLowerCase());
		return i18n != null ? i18n : dictionaries.get("en_us");
	}
	
	@SuppressWarnings("unchecked")
	public static void init (Map<String, InputStream> streams) {
		Yaml yaml = new Yaml();
		
		for (Map.Entry<String, InputStream> stream : streams.entrySet()) {
			HashMap<String, String> loaded = (HashMap<String, String>) yaml.load(stream.getValue());
			dictionaries.put(stream.getKey().replaceAll("lang/(.*)\\.yml", "$1"), new I18n (loaded));
			try {
				stream.getValue().close();
			} catch (IOException e) {
				MYDungeon.LOG.warning("Failed to close input streams of lang files.");
				MYDungeon.LOG.warning(e.toString());
			}
		}
	}
}

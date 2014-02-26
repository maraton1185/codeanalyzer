package codeanalyzer.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.PreferenceStore;
import org.osgi.service.prefs.Preferences;

public abstract class PreferenceSupplier {

	private static PreferenceStore preferenceStore;
	private static final String prefFileName = "codeanalyzer.prefs";

	static {
		
		preferenceStore = new PreferenceStore(prefFileName);
		
		preferenceStore.setDefault("P_LOGIN", "demo");
		preferenceStore.setDefault("P_NTPSERVER", "ptbtime1.ptb.de");
		
		try {
			preferenceStore.load();
		} catch (IOException e) {
			// Ignore
		}
	}
	
	public static PreferenceStore getPreferenceStore() {		
        return preferenceStore;
    }
	public static void save() {
		try {
			preferenceStore.save();
		} catch (IOException e) {
			// Ignore
		}		
	}
	
	public static void remove(String id) {
		preferenceStore.setToDefault(id);		
	}
	
	public static String get(String key) {
		return preferenceStore.getString(key);		
	}
	
	public static void set(String key, String value) {
		preferenceStore.setValue(key, value);		
	}
	
	public static List<String> getBaseList() {
		
		List<String> result = new ArrayList<String>();
		
		for (String key : preferenceStore.preferenceNames()) {
			if(key.contains("db."))
				result.add(key);
		}
		
		return result;
	}
	
	public static Preferences getScoupNode() {
		return ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
	}
	
	//******************************************************************
	
	public static final String NTPSERVER = "P_NTPSERVER";
	
	public static final String DEFAULT_DIRECTORY = "P_DEFAULT_DIRECTORY";
	
	public static final String BASE_ACTIVE = "P_BASE_ACTIVE";
	public static final String BASE_COMPARE = "P_BASE_COMPARE";
	
		
}

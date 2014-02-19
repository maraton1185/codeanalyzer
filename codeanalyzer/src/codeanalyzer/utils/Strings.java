package codeanalyzer.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Strings {

	static ResourceBundle resourceBundle = ResourceBundle.getBundle("strings"); //$NON-NLS-1$
	
	public static String get(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}			
	}
}

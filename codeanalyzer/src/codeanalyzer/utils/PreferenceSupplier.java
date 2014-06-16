package codeanalyzer.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.FontData;
import org.osgi.service.prefs.Preferences;

public abstract class PreferenceSupplier {

	private static PreferenceStore preferenceStore;
	private static final String prefFileName = Strings.get("P_FILE_NAME");

	// ******************************************************************

	public static final String NTPSERVER = "P_NTPSERVER";

	public static final String DEFAULT_DIRECTORY = "P_DEFAULT_DIRECTORY";
	public static final String DEFAULT_BOOK_DIRECTORY = "P_DEFAULT_BOOK_DIRECTORY";

	public static final String INIT_EXECUTION = "P_INIT_EXECUTION";
	public static final String BASE_ACTIVE = "P_BASE_ACTIVE";
	public static final String BASE_COMPARE = "P_BASE_COMPARE";
	// public static final String SHOW_START_PAGE = "P_SHOW_START_PAGE";
	public static final String SHOW_BOOK_PERSPECTIVE = "P_SHOW_BOOK_PERSPECTIVE";
	public static final String OPEN_BOOK_ON_STARTUP = "OPEN_BOOK_ON_STARTUP";
	public static final String BOOK_ON_STARTUP = "BOOK_ON_STARTUP";
	public static final String FONT = "FONT";
	public static final String NOT_OPEN_SECTION_START_VIEW = "NOT_OPEN_SECTION_START_VIEW";
	public static final String MINIMIZE_TO_TRAY = "MINIMIZE_TO_TRAY";
	public static final String MINIMIZE_TO_TRAY_ON_STARTUP = "MINIMIZE_TO_TRAY_ON_STARTUP";

	public static final String SELECTED_BOOK = "SELECTED_BOOK";
	public static final String SELECTED_USER = "SELECTED_USER";

	public static final String REMOTE_PORT = "REMOTE_PORT";

	// ******************************************************************

	static {

		preferenceStore = new PreferenceStore(prefFileName);

		preferenceStore.setDefault(PreferenceSupplier.REMOTE_PORT, 80);

		// preferenceStore.setDefault(PreferenceSupplier.FONT, null);

		preferenceStore.setDefault(PreferenceSupplier.SELECTED_BOOK, 1);
		preferenceStore.setDefault(PreferenceSupplier.SELECTED_USER, 1);

		preferenceStore.setDefault(PreferenceSupplier.NTPSERVER,
				"ptbtime1.ptb.de");
		// preferenceStore.setDefault(PreferenceSupplier.SHOW_START_PAGE, true);

		preferenceStore.setDefault(PreferenceSupplier.DEFAULT_DIRECTORY,
				ResourcesPlugin.getWorkspace().getRoot().getLocation()
						.toString());

		preferenceStore.setDefault(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY,
				ResourcesPlugin.getWorkspace().getRoot().getLocation()
						.toString());

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

	public static FontData[] getFontData(String key) {
		FontData[] data = PreferenceConverter.getFontDataArray(preferenceStore,
				key);
		// if (data == null)
		// data = JFaceResources.getDialogFont().getFontData();

		return data;
	}

	public static Boolean getBoolean(String key) {
		return preferenceStore.getBoolean(key);
	}

	public static int getInt(String key) {
		return preferenceStore.getInt(key);
	}

	public static void set(String key, String value) {
		preferenceStore.setValue(key, value);
	}

	public static void set(String key, Boolean value) {
		preferenceStore.setValue(key, value);
	}

	public static void set(String key, int value) {
		preferenceStore.setValue(key, value);
	}

	public static List<String> getBaseList() {

		List<String> result = new ArrayList<String>();

		for (String key : preferenceStore.preferenceNames()) {
			if (key.contains("db."))
				result.add(key);
		}

		return result;
	}

	public static Preferences getScoupNode() {
		return ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
	}

}

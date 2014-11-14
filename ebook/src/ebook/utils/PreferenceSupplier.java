package ebook.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.osgi.service.prefs.Preferences;

import ebook.core.App.Perspectives;

public abstract class PreferenceSupplier {

	private static PreferenceStore preferenceStore;
	private static final String prefFileName = Strings.pref("P_FILE_NAME");

	// ******************************************************************

	public static final String NTPSERVER = "P_NTPSERVER";

	public static final String DEFAULT_CONF_DIRECTORY = "P_DEFAULT_DIRECTORY";
	public static final String DEFAULT_BOOK_DIRECTORY = "P_DEFAULT_BOOK_DIRECTORY";

	public static final String INIT_EXECUTION = "P_INIT_EXECUTION";
	public static final String BASE_ACTIVE = "P_BASE_ACTIVE";
	public static final String BASE_COMPARE = "P_BASE_COMPARE";

	// public static final String OPEN_BOOK_ON_STARTUP = "OPEN_BOOK_ON_STARTUP";
	// public static final String BOOK_ON_STARTUP = "BOOK_ON_STARTUP";
	public static final String FONT = "FONT";

	public static final String START_PERSPECTIVE = "START_PERSPECTIVE";

	public static final String MINIMIZE_TO_TRAY = "MINIMIZE_TO_TRAY";
	public static final String MINIMIZE_TO_TRAY_ON_STARTUP = "MINIMIZE_TO_TRAY_ON_STARTUP";

	public static final String SELECTED_BOOK = "SELECTED_BOOK";
	public static final String SELECTED_USER = "SELECTED_USER";
	public static final String SELECTED_CONF = "SELECTED_CONF";
	public static final String SELECTED_LIST = "SELECTED_LIST";

	public static final String REMOTE_PORT = "REMOTE_PORT";
	public static final String START_JETTY = "START_JETTY";
	public static final String IMAGE_WIDTH = "IMAGE_WIDTH";
	public static final String EXTERNAL_JETTY_BASE = "EXTERNAL_JETTY_BASE";
	// public static final String JETTY_BASE = "JETTY_BASE";
	public static final String APP_BRAND = "APP_BRAND";
	public static final String SESSION_TIMEOUT = "SESSION_TIMEOUT";
	// public static final String UPDATE_SITE = "UPDATE_SITE";
	public static final String SHOW_ABOUT_ON_STARTUP = "SHOW_ABOUT_ON_STARTUP";
	public static final String CHECK_UPDATE_ON_STARTUP = "CHECK_UPDATE_ON_STARTUP";
	public static final String DEFAULT_IMAGE_DIRECTORY = "DEFAULT_IMAGE_DIRECTORY";
	public static final String BOOKMARK_LENGTH = "BOOKMARK_LENGTH";
	public static final String CONF_LIST_VIEW_COMPARISON = "CONF_LIST_VIEW_COMPARISON";
	public static final String IMAGE_TITLE = "IMAGE_TITLE";
	public static final String INIT_SECTION_HTML = "INIT_SECTION_HTML";
	// public static final String APP_HOST = "APP_HOST";
	public static final String WINDOW_SIZE = "WINDOW_SIZE";
	public static final String LOAD_EDITOR_TEMPLATES_ON_GET = "LOAD_EDITOR_TEMPLATES_ON_GET";
	// public static final String EDITOR_TEMPLATES_FOLDER =
	// "EDITOR_TEMPLATES_FOLDER";
	public static final String SECTION_REF_TARGET = "SECTION_REF_TARGET";
	public static final String SECTION_REF_TITLE = "SECTION_REF_TITLE";

	// ******************************************************************

	static {

		preferenceStore = new PreferenceStore(prefFileName);

		preferenceStore.setDefault(PreferenceSupplier.SECTION_REF_TITLE,
				"ссылка");

		preferenceStore.setDefault(PreferenceSupplier.SECTION_REF_TARGET, true);

		// preferenceStore.setDefault(PreferenceSupplier.EDITOR_TEMPLATES_FOLDER,
		// ResourcesPlugin.getWorkspace().getRoot().getLocation()
		// .toString());

		preferenceStore.setDefault(
				PreferenceSupplier.LOAD_EDITOR_TEMPLATES_ON_GET, false);

		// preferenceStore.setDefault(PreferenceSupplier.APP_HOST,
		// "http://codeanalyzer.ru");
		//
		// // "file://d:/Marat/java/temp/update_site/";
		// preferenceStore.setDefault(PreferenceSupplier.UPDATE_SITE,
		// "http://codeanalyzer.ru/media/repository");

		preferenceStore
				.setDefault(
						PreferenceSupplier.INIT_SECTION_HTML,
						"<h2>"
								+ "Заголовок &quot;&quot; ()</h2>"
								+ "<table border=\"1\" cellpadding=\"10\" cellspacing=\"1\" style=\"width: 100%;\">"
								+ "		<tbody>" + "			<tr>" + "				<td>"
								+ "					&nbsp;</td>" + "				<td>"
								+ "					&nbsp;</td>" + "			</tr>" + "			<tr>"
								+ "				<td>" + "					&nbsp;</td>" + "				<td>"
								+ "					&nbsp;</td>" + "			</tr>" + "			<tr>"
								+ "				<td>" + "					&nbsp;</td>" + "				<td>"
								+ "					&nbsp;</td>" + "			</tr>"
								+ "		</tbody>" + "	</table>" + "	<p>"
								+ "		&nbsp;</p>");

		preferenceStore.setDefault(PreferenceSupplier.IMAGE_TITLE,
				Strings.value("image"));

		preferenceStore.setDefault(
				PreferenceSupplier.CONF_LIST_VIEW_COMPARISON, 0);

		preferenceStore.setDefault(PreferenceSupplier.BOOKMARK_LENGTH, 40);

		preferenceStore.setDefault(PreferenceSupplier.CHECK_UPDATE_ON_STARTUP,
				true);

		preferenceStore.setDefault(PreferenceSupplier.SHOW_ABOUT_ON_STARTUP,
				true);

		preferenceStore.setDefault(PreferenceSupplier.SESSION_TIMEOUT, 60);

		preferenceStore.setDefault(PreferenceSupplier.START_PERSPECTIVE,
				Perspectives.main.toString());

		preferenceStore.setDefault(PreferenceSupplier.APP_BRAND,
				"Анализатор кода 1С");

		preferenceStore.setDefault(PreferenceSupplier.IMAGE_WIDTH, 200);
		preferenceStore
				.setDefault(PreferenceSupplier.EXTERNAL_JETTY_BASE, true);
		// preferenceStore.setDefault(PreferenceSupplier.JETTY_BASE,
		// ResourcesPlugin.getWorkspace().getRoot().getLocation()
		// .toString());

		preferenceStore.setDefault(PreferenceSupplier.START_JETTY, true);
		preferenceStore.setDefault(PreferenceSupplier.REMOTE_PORT, 80);

		PreferenceConverter.setDefault(preferenceStore,
				PreferenceSupplier.FONT, new FontData("Arial", 12, SWT.NONE));
		// preferenceStore.setDefault(PreferenceSupplier.FONT, "Arial");

		preferenceStore.setDefault(PreferenceSupplier.SELECTED_BOOK, 1);
		preferenceStore.setDefault(PreferenceSupplier.SELECTED_USER, 1);
		preferenceStore.setDefault(PreferenceSupplier.SELECTED_CONF, 1);
		preferenceStore.setDefault(PreferenceSupplier.SELECTED_LIST, "");

		preferenceStore.setDefault(PreferenceSupplier.NTPSERVER,
				"ptbtime1.ptb.de");
		// preferenceStore.setDefault(PreferenceSupplier.SHOW_START_PAGE, true);

		preferenceStore.setDefault(PreferenceSupplier.DEFAULT_CONF_DIRECTORY,
				ResourcesPlugin.getWorkspace().getRoot().getLocation()
						.toString());

		preferenceStore.setDefault(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY,
				ResourcesPlugin.getWorkspace().getRoot().getLocation()
						.toString());

		preferenceStore.setDefault(PreferenceSupplier.DEFAULT_IMAGE_DIRECTORY,
				ResourcesPlugin.getWorkspace().getRoot().getLocation()
						.toString());

		// preferenceStore.setDefault(PreferenceSupplier.OPEN_BOOK_ON_STARTUP,
		// false);

		// preferenceStore.setDefault(PreferenceSupplier.BOOK_ON_STARTUP,
		// Utils.getAboutBookPath() + Const.DEFAULT_DB_EXTENSION);

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
		return ConfigurationScope.INSTANCE.getNode(Strings.pref("P_NODE"));
	}

}

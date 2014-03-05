package codeanalyzer.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Strings {

	static ResourceBundle resourceStrings = ResourceBundle.getBundle("strings");

	public static String get(String key) {
		try {
			return resourceStrings.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	static ResourceBundle resourceKeywords = ResourceBundle
			.getBundle("keywords");

	public static boolean keyword(String key) {

		return resourceKeywords.containsKey(key);
		// for (String s : fgKeywords) {
		// if (s.equalsIgnoreCase(v))
		// return true;
		// }
		// return false;
	}

	static ResourceBundle resourceCalls = ResourceBundle
			.getBundle("standart_calls");

	public static boolean standart_call(String key) {

		return resourceCalls.containsKey(key.toLowerCase());
		// for (String s : fgKeywords) {
		// if (s.equalsIgnoreCase(v))
		// return true;
		// }
		// return false;
	}

	// private static String[] fgKeywords = { "�����", "�����", "������",
	// "�����",
	// "�����������������", "���", "���", "������", "��", "����",
	// "������", "���", "�", "�������������", "������������",
	// "��������������������", "�������������������������", "����������",
	// "�������", "����", "������", "������", "�����������", "����",
	// "���", "��������������", "��������", "��������", "��������",
	// "���������", "�����", "�����", "������������", "���������",
	// "��������������", "�������", "������������", "�������", "�������",
	// "����", "�����", "�����", "���������", "���������", "���������",
	// "���������", "�������", "����������", "������������",
	// "����������������", "�����������������������",
	// "������������������", "���", "�������", "��", "����", "����",
	// "����������", "��������", "����������" };
}

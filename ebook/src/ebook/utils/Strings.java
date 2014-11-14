package ebook.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Strings {

	public static final String host = "http://codeanalyzer.ru";
	public static final String updateSite = "http://codeanalyzer.ru/media/repository";
	public static final String templates = "templates";
	public static final String xul = "xulrunner";
	public static final String jetty = "webroot";
	// static ResourceBundle resourceStrings =
	// ResourceBundle.getBundle("strings");
	//
	// public static String _get(String key) {
	// try {
	// return resourceStrings.getString(key);
	// } catch (MissingResourceException e) {
	// return key;
	// } catch (NullPointerException e) {
	//			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
	// }
	// }

	static ResourceBundle modelStrings = ResourceBundle.getBundle("model");

	public static String model(String key) {
		try {
			return modelStrings.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	static ResourceBundle messageStrings = ResourceBundle.getBundle("message");

	public static String msg(String key) {
		try {
			return messageStrings.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	static ResourceBundle valueStrings = ResourceBundle.getBundle("value");

	public static String value(String key) {
		try {
			return valueStrings.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	static ResourceBundle prefStrings = ResourceBundle.getBundle("pref");

	public static String pref(String key) {
		try {
			return prefStrings.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	static ResourceBundle titleStrings = ResourceBundle.getBundle("title");

	public static String title(String key) {
		try {
			return titleStrings.getString(key);
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

	// private static String[] fgKeywords = { "СокрЛ", "СокрП", "СокрЛП",
	// "Число",
	// "ЗначениеЗаполнено", "Окр", "Цел", "Формат", "Не", "Ложь",
	// "Истина", "Или", "И", "ПолучитьМакет", "ОткрытьФорму",
	// "ОткрытьФормуМодульно", "ЗаполнитьЗначенияСвойство", "Оповестить",
	// "Закрыть", "НСТР", "Пустая", "Вопрос", "СтрЗаменить", "Прав",
	// "Лев", "Предупреждение", "Сообщить", "ЭтоНовый", "КонецДня",
	// "НачалоДня", "Перем", "Новый", "Неопределено", "Процедура",
	// "КонецПроцедуры", "Функция", "КонецФункции", "Возврат", "Экспорт",
	// "Если", "Тогда", "Иначе", "КонецЕсли", "Конецесли", "конецЕсли",
	// "ИначеЕсли", "Попытка", "Исключение", "КонецПопытки",
	// "НачатьТранзакцию", "ЗафиксироватьТранзакцию",
	// "ОтменитьТранзакцию", "Для", "Каждого", "Из", "Пока", "Цикл",
	// "КонецЦикла", "Прервать", "Продолжить" };
}

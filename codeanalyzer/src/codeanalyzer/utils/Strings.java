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
	
	
	public static boolean keyword(String v) {
		for(String s:fgKeywords)
		{
			if(s.equalsIgnoreCase(v))
				return true;
		}
		return false;
	}

	private static String[] fgKeywords = { "СокрЛ", "СокрП", "СокрЛП", "Число",
			"ЗначениеЗаполнено", "Окр", "Цел", "Формат", "Не",
			"Ложь", "Истина", "Или", "И", "ПолучитьМакет", "ОткрытьФорму",
			"ОткрытьФормуМодульно", "ЗаполнитьЗначенияСвойство", "Оповестить",
			"Закрыть", "НСТР", "Пустая", "Вопрос", "СтрЗаменить",
			"Прав", "Лев", "Предупреждение", "Сообщить", "ЭтоНовый",
			"КонецДня", "НачалоДня", "Перем", "Новый", "Неопределено",
			"Процедура", "КонецПроцедуры", "Функция", "КонецФункции",
			"Возврат", "Экспорт", "Если", "Тогда", "Иначе", "КонецЕсли", "Конецесли", "конецЕсли",
			"ИначеЕсли", "Попытка", "Исключение", "КонецПопытки",
			"НачатьТранзакцию", "ЗафиксироватьТранзакцию",
			"ОтменитьТранзакцию", "Для", "Каждого", "Из", "Пока", "Цикл",
			"КонецЦикла", "Прервать", "Продолжить" };
}

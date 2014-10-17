package ebook.utils;

public class Const {

	// VERSION *******************************************************
	// MUST не забываем ставить версию!
	// public static final int versionYear = 2013;
	// public static final int versionMonth = 10;
	// public static final int versionDate = 07;

	public static final int FREE_TREE_ITEMS_COUNT = 2;
	public static final int FREE_BOOK_ITEMS_COUNT = 10;
	// @password_hash SA test
	public static final String FREE_DB_PASSWORD = "1b49e74abcc2a598e0cb50f565304bee16377636073b86529dedc2beda5131a0";

	// HOST ***********************************************************

	// MUST хост
	// private static String host = "http://192.168.3.254/";
	private static String host() {
		return PreferenceSupplier.get(PreferenceSupplier.APP_HOST);
	};

	// private static final String host = PreferenceSupplier
	// .get(PreferenceSupplier.APP_HOST);
	// private static final String host = "http://localhost:4921";
	// public static final String UPDATE_SITE =

	// public static String URL_exportConf1CLinkOpen() {
	// return host() + "/download";
	// }

	// public static String URL_registrationLinkOpen() {
	// return host() + "/profile";
	// }

	public static String URL_proLinkOpen() {
		return host() + "/pro";
	}

	public static String URL_docLinkOpen() {
		return host() + "/documentation";
	}

	// public static String URL_using() {
	// return host() + "/using";
	// }

	// public static String URL_download() {
	// return host() + "/download";
	// }

	// public static String URL_CHECK_UPDATE() {
	// return host() + "/check";
	// }
	public static String URL_CONTACT() {
		return host() + "/contact";
	};

	public static String URL_ACTIVATE() {
		return host() + "/activate";
	};

	// DB *******************************************************
	public static final String DEFAULT_DB_NAME = "base";
	public static final String DEFAULT_DB_EXTENSION = ".h2.db";
	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	public static final int DEFAULT_FREE_FILES_COUNT = 50;
	public static final String DB1 = "db1";
	public static final String DB2 = "db2";
	public static final String SYSTEM_DB_NAME = "db";

	// ERRORS *******************************************************

	public static final String ERROR_NO_ADRESS = "	ошибка доступа к сайту\n ";
	public static final String ERROR_CRYPT = "	ошибка обращения к сайту \n	скачайте последнюю версию программы.";
	public static final String ERROR_SITE_CRYPT = "	ошибка обращения к сайту \n	скачайте последнюю версию программы.";
	// public static final String ERROR_MESSAGE =
	// "	ошибка обращения к сайту \n	скачайте последнюю версию программы.";
	public static final String ERROR_SITE_ACCESS = "	Сайт не доступен";

	public static final String ERROR_PRO_ACCESS = "Функционал доступен только в pro-версии";

	// MESSAGES *******************************************************
	public static final String MSG_CONTACT_FAIL = "Ошибка отправки сообщения: \n";
	public static final String MSG_FREE = "Доступна free-версия \n";
	public static final Object MSG_FREE_SHORT = "free";
	public static final String MSG_PRO = "Доступна pro-версия \n";
	public static final String MSG_PRO_SHORT = "pro";
	public static final String MSG_ACTIVATE_FAIL = "Ошибка активации: \n";
	public static final String MSG_NO_FREE_DEVICES = "	Недостаточно свободных лицензий для активации.";

	public static final String MSG_ACTIVATE_OK = "Активировано: \n";
	public static final String MSG_ACTIVATED = "	приобретено лицензий: %1$s\n "
			+ "	активировано: %2$s\n" + "	доступно для активации: %3$s";
	public static final String MSG_LOGIN = "	неверные логин, пароль (должны быть такие же, как на сайте) \n";
	// public static final String MSG_ALREADY_ACTIVATED =
	// "	устройство уже активировано";
	public static final String MSG_GETID = "Невозможно идентифицировать компьютер. \n";
	public static final String MSG_NO_CREDENTIALS = "Не указаны логин, пароль. \n";
	public static final String MSG_ID = "Неверный серийный номер компьютера. \n";
	public static final String MSG_EMPTY_SERIAL = "Серийный номер не указан. Активируйте плагин. \n";
	public static final String MSG_INCORRECT_SERIAL = "Некорректный серийный номер \n(скачайте последнюю версию плагина и активируйте его повторно). \n";
	public static final String MSG_SEND_EMAIL_TO = "Для активации напишите, пожалуйста, на email: mail@codeanalyzer.ru \n"
			+ "В письме укажите следующие данные: \n    - логин \n    - серийный номер компьютера UUID";
	public static final String MSG_SEND_EMAIL_TO_MSG = "Напишите, пожалуйста, на email: mail@codeanalyzer.ru \n";
	// CONFIG MESSAGES *******************************************************

	public static final String MSG_CONFIG_CHECK = "Проверка структуры базы данных...";
	public static final String MSG_CONFIG_FILL_LINK_TABLE = "Формирование таблицы вызовов...";
	public static final String MSG_CONFIG_CLEAR_LINK_TABLE = "Очистка  таблицы взаимных вызовов процедур...";
	public static final String MSG_CONFIG_NOT_LOADED = "Не загружена конфигурация";
	public static final String MSG_CONFIG_TOPIC_NOT_FOUND = "Не найдены связанные темы";
	public static final String MSG_CONFIG_HIERARCHY_EMPTY = "Не найдена процедура для перехода\n или процедура нигде не вызывается и ничего не вызывает";
	public static final String MSG_CONFIG_PROC_NOT_FOUND = "Не найдена процедура для перехода";
	public static final String MSG_CONFIG_SEARCH = "Поиск вхождений: ";
	public static final String MSG_CONFIG_SEARCH_NOT_FOUND = "Ничего не найдено";
	public static final String MSG_CONFIG_CLEAR = "Удаление подчинённых тем...";
	public static final String MSG_CONFIG_BUILD = "Построение...";
	public static final String MSG_CONFIG_QUERY = "Выполнение запроса...";

	// CONFIG ERRORS *******************************************************

	public static final String ERROR_CONFIG_PATH = "\n Каталог конфигурации не существует";
	public static final String ERROR_CONFIG_EMPTY = "\n Каталог конфигурации пуст";
	public static final String ERROR_CONFIG_LOADED = "\n Конфигурация не загружена";
	public static final String ERROR_LINK_LOADED = "\n Таблица вызовов уже сформирована";
	public static final String ERROR_CONFIG_INTERRUPT = "\n Отменено пользователем";
	public static final String ERROR_CONFIG_READFILE = "\n Ошибка загрузки файла: \n";
	public static final String ERROR_CONFIG_READOBJECT = "\n Ошибка загрузки объекта: \n";
	public static final String ERROR_CONFIG_CREATE_DATABASE = "\n Ошибка создания базы данных";
	public static final String ERROR_CONFIG_OPEN_DATABASE = "\n Ошибка подключения к базе данных";
	public static final String ERROR_CONFIG_FILL_LINK_TABLE = "\n Ошибка заполнения таблицы взаимных вызовов процедур";
	public static final String ERROR_CONFIG_CREATE_TOPIC = "Ошибка построения темы";
	public static final String ERROR_CONFIG_CLEAR = "Ошибка удаления подчинённых тем";

	// STRINGS *******************************************************

	public static final String STRING_CONFIG_ROOT = "Конфигурация";
	public static final String STRING_CONFIG_TEXT_ROOT = "текст";
	public static final String STRING_PARAMETERS_NAME = "параметры";
	public static final String STRING_CALLED_LIST_NAME = "вызывает";
	public static final String STRING_CALLS_LIST_NAME = "вызывается";
	public static final String STRING_SEARCH_LIST_NAME = "Результаты поиска";
	public static final String STRING_MESSAGE_TITLE = "Конфигурация 1С";

	public static final String STRING_INIT = "РАЗДЕЛИНИЦИАЦИИ";
	public static final String STRING_INIT_TITLE = "раздел инициации";
	public static final String STRING_VARS = "РАЗДЕЛОПИСАНИЯПЕРЕМЕННЫХ";
	public static final String STRING_VARS_TITLE = "раздел описания переменных";

	// CONFIG CONNECTION ERRORS
	// *******************************************************

	public static final String ERROR_CONFIG_CONNECTION_EXE = "\n На найден файл 1cestart.exe";
	public static final String ERROR_CONFIG_CONNECTION_CHECK = "\n Ошибка получения данных из 1С";
	public static final String MESSAGE_CONFIG_CONNECTION_CHECK = "Соединение успешно установлено";

	// MARKERS *******************************************************

	public static final String MARKER_SEARCH_TEXT = "ebook-search_text";
	public static final String MARKER_SEARCH_META = "ebook-search-meta";
	public static final String MARKER_SEARCH_PROC = "ebook-search-procs";

	public static final String MARKER_ROOT = "ebook-root";
	public static final String MARKER_OBJECT = "ebook-object";
	public static final String MARKER_MODULE = "ebook-module";
	public static final String MARKER_PROC = "ebook-proc";

	public static final String TOPIC_EXTENSION = "ebook_topic_extension";

	// COMPARE *******************************************************

	public static final String COMPARE_EQUALS = "одинаковые";
	public static final String COMPARE_REMOVED = "есть в активной";
	public static final String COMPARE_ADDED = "есть в текущей";
	public static final String COMPARE_CHANGED = "изменённые";
	public static final String MODULE_NOT_FOUND = "//Модуль не найден...";
	public static final String PROC_NOT_FOUND = "//Процедура не найдена...";
	public static final String COMPARE_WORK = "Сравнение объектов...";
	public static final String COMPARE_WORK_ACTIVE = "Анализ объектов активной конфигурации...";
	public static final String COMPARE_WORK_NON_ACTIVE = "Анализ объектов не активной конфигурации...";
	public static final String COMPARE_TEXT_MARKER = "<COMPARE> ";
	public static final String COMPARE_ADDED_MARKER = "<+> ";
	public static final String COMPARE_REMOVED_MARKER = "<-> ";
	public static final String COMPARE_CHANGED_MARKER = "<!> ";

	// PATTERN *******************************************************

	public static final String PATTERN_PROCEDURE = "(ФУНКЦИЯ|ПРОЦЕДУРА)\\s+([А-Я]|[A-Z]|_|Ё)+[А-ЯA-Z0-9_Ё]*\\s*\\(";
	public static final String PATTERN_PROCEDURE_IN_STRING = "(\\.)*([А-Я]|[A-Z]|_|Ё)+[А-ЯA-Z0-9_Ё]*\\s*\\(";
	public static final String PATTERN_MODULE = "([А-Я]|[A-Z]|_|Ё)+[А-ЯA-Z0-9_Ё]*\\s*";

}

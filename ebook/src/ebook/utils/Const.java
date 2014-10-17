package ebook.utils;

public class Const {

	// VERSION *******************************************************
	// MUST �� �������� ������� ������!
	// public static final int versionYear = 2013;
	// public static final int versionMonth = 10;
	// public static final int versionDate = 07;

	public static final int FREE_TREE_ITEMS_COUNT = 2;
	public static final int FREE_BOOK_ITEMS_COUNT = 10;
	// @password_hash SA test
	public static final String FREE_DB_PASSWORD = "1b49e74abcc2a598e0cb50f565304bee16377636073b86529dedc2beda5131a0";

	// HOST ***********************************************************

	// MUST ����
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

	public static final String ERROR_NO_ADRESS = "	������ ������� � �����\n ";
	public static final String ERROR_CRYPT = "	������ ��������� � ����� \n	�������� ��������� ������ ���������.";
	public static final String ERROR_SITE_CRYPT = "	������ ��������� � ����� \n	�������� ��������� ������ ���������.";
	// public static final String ERROR_MESSAGE =
	// "	������ ��������� � ����� \n	�������� ��������� ������ ���������.";
	public static final String ERROR_SITE_ACCESS = "	���� �� ��������";

	public static final String ERROR_PRO_ACCESS = "���������� �������� ������ � pro-������";

	// MESSAGES *******************************************************
	public static final String MSG_CONTACT_FAIL = "������ �������� ���������: \n";
	public static final String MSG_FREE = "�������� free-������ \n";
	public static final Object MSG_FREE_SHORT = "free";
	public static final String MSG_PRO = "�������� pro-������ \n";
	public static final String MSG_PRO_SHORT = "pro";
	public static final String MSG_ACTIVATE_FAIL = "������ ���������: \n";
	public static final String MSG_NO_FREE_DEVICES = "	������������ ��������� �������� ��� ���������.";

	public static final String MSG_ACTIVATE_OK = "������������: \n";
	public static final String MSG_ACTIVATED = "	����������� ��������: %1$s\n "
			+ "	������������: %2$s\n" + "	�������� ��� ���������: %3$s";
	public static final String MSG_LOGIN = "	�������� �����, ������ (������ ���� ����� ��, ��� �� �����) \n";
	// public static final String MSG_ALREADY_ACTIVATED =
	// "	���������� ��� ������������";
	public static final String MSG_GETID = "���������� ���������������� ���������. \n";
	public static final String MSG_NO_CREDENTIALS = "�� ������� �����, ������. \n";
	public static final String MSG_ID = "�������� �������� ����� ����������. \n";
	public static final String MSG_EMPTY_SERIAL = "�������� ����� �� ������. ����������� ������. \n";
	public static final String MSG_INCORRECT_SERIAL = "������������ �������� ����� \n(�������� ��������� ������ ������� � ����������� ��� ��������). \n";
	public static final String MSG_SEND_EMAIL_TO = "��� ��������� ��������, ����������, �� email: mail@codeanalyzer.ru \n"
			+ "� ������ ������� ��������� ������: \n    - ����� \n    - �������� ����� ���������� UUID";
	public static final String MSG_SEND_EMAIL_TO_MSG = "��������, ����������, �� email: mail@codeanalyzer.ru \n";
	// CONFIG MESSAGES *******************************************************

	public static final String MSG_CONFIG_CHECK = "�������� ��������� ���� ������...";
	public static final String MSG_CONFIG_FILL_LINK_TABLE = "������������ ������� �������...";
	public static final String MSG_CONFIG_CLEAR_LINK_TABLE = "�������  ������� �������� ������� ��������...";
	public static final String MSG_CONFIG_NOT_LOADED = "�� ��������� ������������";
	public static final String MSG_CONFIG_TOPIC_NOT_FOUND = "�� ������� ��������� ����";
	public static final String MSG_CONFIG_HIERARCHY_EMPTY = "�� ������� ��������� ��� ��������\n ��� ��������� ����� �� ���������� � ������ �� ��������";
	public static final String MSG_CONFIG_PROC_NOT_FOUND = "�� ������� ��������� ��� ��������";
	public static final String MSG_CONFIG_SEARCH = "����� ���������: ";
	public static final String MSG_CONFIG_SEARCH_NOT_FOUND = "������ �� �������";
	public static final String MSG_CONFIG_CLEAR = "�������� ���������� ���...";
	public static final String MSG_CONFIG_BUILD = "����������...";
	public static final String MSG_CONFIG_QUERY = "���������� �������...";

	// CONFIG ERRORS *******************************************************

	public static final String ERROR_CONFIG_PATH = "\n ������� ������������ �� ����������";
	public static final String ERROR_CONFIG_EMPTY = "\n ������� ������������ ����";
	public static final String ERROR_CONFIG_LOADED = "\n ������������ �� ���������";
	public static final String ERROR_LINK_LOADED = "\n ������� ������� ��� ������������";
	public static final String ERROR_CONFIG_INTERRUPT = "\n �������� �������������";
	public static final String ERROR_CONFIG_READFILE = "\n ������ �������� �����: \n";
	public static final String ERROR_CONFIG_READOBJECT = "\n ������ �������� �������: \n";
	public static final String ERROR_CONFIG_CREATE_DATABASE = "\n ������ �������� ���� ������";
	public static final String ERROR_CONFIG_OPEN_DATABASE = "\n ������ ����������� � ���� ������";
	public static final String ERROR_CONFIG_FILL_LINK_TABLE = "\n ������ ���������� ������� �������� ������� ��������";
	public static final String ERROR_CONFIG_CREATE_TOPIC = "������ ���������� ����";
	public static final String ERROR_CONFIG_CLEAR = "������ �������� ���������� ���";

	// STRINGS *******************************************************

	public static final String STRING_CONFIG_ROOT = "������������";
	public static final String STRING_CONFIG_TEXT_ROOT = "�����";
	public static final String STRING_PARAMETERS_NAME = "���������";
	public static final String STRING_CALLED_LIST_NAME = "��������";
	public static final String STRING_CALLS_LIST_NAME = "����������";
	public static final String STRING_SEARCH_LIST_NAME = "���������� ������";
	public static final String STRING_MESSAGE_TITLE = "������������ 1�";

	public static final String STRING_INIT = "���������������";
	public static final String STRING_INIT_TITLE = "������ ���������";
	public static final String STRING_VARS = "������������������������";
	public static final String STRING_VARS_TITLE = "������ �������� ����������";

	// CONFIG CONNECTION ERRORS
	// *******************************************************

	public static final String ERROR_CONFIG_CONNECTION_EXE = "\n �� ������ ���� 1cestart.exe";
	public static final String ERROR_CONFIG_CONNECTION_CHECK = "\n ������ ��������� ������ �� 1�";
	public static final String MESSAGE_CONFIG_CONNECTION_CHECK = "���������� ������� �����������";

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

	public static final String COMPARE_EQUALS = "����������";
	public static final String COMPARE_REMOVED = "���� � ��������";
	public static final String COMPARE_ADDED = "���� � �������";
	public static final String COMPARE_CHANGED = "���������";
	public static final String MODULE_NOT_FOUND = "//������ �� ������...";
	public static final String PROC_NOT_FOUND = "//��������� �� �������...";
	public static final String COMPARE_WORK = "��������� ��������...";
	public static final String COMPARE_WORK_ACTIVE = "������ �������� �������� ������������...";
	public static final String COMPARE_WORK_NON_ACTIVE = "������ �������� �� �������� ������������...";
	public static final String COMPARE_TEXT_MARKER = "<COMPARE> ";
	public static final String COMPARE_ADDED_MARKER = "<+> ";
	public static final String COMPARE_REMOVED_MARKER = "<-> ";
	public static final String COMPARE_CHANGED_MARKER = "<!> ";

	// PATTERN *******************************************************

	public static final String PATTERN_PROCEDURE = "(�������|���������)\\s+([�-�]|[A-Z]|_|�)+[�-�A-Z0-9_�]*\\s*\\(";
	public static final String PATTERN_PROCEDURE_IN_STRING = "(\\.)*([�-�]|[A-Z]|_|�)+[�-�A-Z0-9_�]*\\s*\\(";
	public static final String PATTERN_MODULE = "([�-�]|[A-Z]|_|�)+[�-�A-Z0-9_�]*\\s*";

}

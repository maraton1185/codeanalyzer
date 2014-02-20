package ru.codeanalyzer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Const {

	//VERSION *******************************************************
	//MUST �� �������� ������� ������!
	public static final int versionYear = 2013;
	public static final int versionMonth = 10;
	public static final int versionDate = 07;

	//HOST ***********************************************************

	//MUST ����
	private static final String host = "http://www.codeanalyzer.ru";
	//private static final String host = "http://localhost:4921";
	
	public static final String URL_exportConf1CLinkOpen = host + "/download";
	public static final String URL_registrationLinkOpen = host + "/profile";
	public static final String URL_proLinkOpen = host + "/pro";
	public static final String URL_using = host + "/using";
//	public static final String URL_updateConfigDescription = "www.yandex.ru";
	public static final String URL_download = host + "/download";
	
	public static final String URL_CHECK_UPDATE = host + "/check";
	public static final String URL_ACTIVATE = host + "/activate";

	//DB *******************************************************
	public static final String DEFAULT_DB_NAME = "base";
	public static final String DEFAULT_DB_EXTENSION = ".h2.db";
	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	public static final int DEFAULT_FREE_FILES_COUNT = 50;
	public static final String DB1 = "db1";
	public static final String DB2 = "db2";

	/**
	 * ���������� ������� ������ �������
	 * @param days - ���������� ����, ���������� �� ������� ������
	 * @return
	 */	
	public static String GetVersion()
	{
		Calendar c = Calendar.getInstance();
		c.set(Const.versionYear, Const.versionMonth-1, Const.versionDate, 1, 0, 0);
		//c.add(Calendar.DAY_OF_MONTH, days);
		Date d = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(d);
	}
	
	//ERRORS *******************************************************

	public static final String ERROR_NO_ADRESS = "������ ������� � �����\n ";
	public static final String ERROR_CRYPT = "������ ��������� � �����: \n�������� ��������� ������ �������.";
	public static final String ERROR_SITE_CRYPT = "������ ��������� � �����: \n�������� ��������� ������ �������.";
	public static final String ERROR_MESSAGE = "������ ��������� � �����: \n�������� ��������� ������ �������.";
	public static final String ERROR_SITE_ACCESS = "���� �� ��������";	

	public static final String ERROR_PRO_ACCESS = "���������� �������� ������ � pro-������";
	public static final String ERROR_PRO_ACCESS_LOAD = "\n ��� free-������ �������� ������ � ����� ������, \n ���������� �� �����, ��� " + Const.DEFAULT_FREE_FILES_COUNT + " ��������";
	
	//MESSAGES *******************************************************

	public static final String MSG_FREE = "�������� free-������ \n";
	public static final Object MSG_FREE_SHORT = "free";
	public static final String MSG_PRO = "�������� pro-������ \n";
	public static final String MSG_PRO_SHORT = "pro";
	public static final String MSG_ACTIVATE_FAIL = "������ ���������: \n";
	public static final String MSG_ACTIVATE_OK = "������������: \n";
	public static final String MSG_LOGIN = "�� ������ �����, ������ (������ ���� ����� ��, ��� �� �����) \n";
	public static final String MSG_GETID = "���������� ���������������� ���������. \n";
	public static final String MSG_NO_CREDENTIALS = "�� ������� �����, ������. \n";
	public static final String MSG_ID = "�� ������ �������� ����� ����������. \n";
	public static final String MSG_NTP = "��� ���������� � NTP-�������� \n";
	public static final String MSG_EXPIRED = "����� ���� ������������� ������� \n";
	public static final String MSG_EMPTY_SERIAL = "�������� ����� �� ������. ����������� ������. \n";
	public static final String MSG_INCORRECT_SERIAL = "�� ���������� �������� ����� \n(�������� ��������� ������ ������� � ����������� ��� ��������). \n";
	public static final String MSG_SEND_EMAIL_TO = "��� ��������� ��������, ����������, �� email: mail@codeanalyzer.ru \n"
			+ "� ������ ������� ��������� ������: \n    - ����� \n    - �������� ����� ���������� UUID";

	//CONFIG MESSAGES *******************************************************

	public static final String MSG_CONFIG_CHECK = "�������� ��������� ���� ������...";
	public static final String MSG_CONFIG_FILL_LINK_TABLE = "������������ ������� �������� ������� ��������...";
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
	
	//CONFIG ERRORS *******************************************************

	public static final String ERROR_CONFIG_PATH = "\n ������� ������������ �� ����������";
	public static final String ERROR_CONFIG_EMPTY = "\n ������� ������������ ����";
	public static final String ERROR_CONFIG_INTERRUPT = "\n �������� �������������";
	public static final String ERROR_CONFIG_READFILE = "\n ������ �������� �����: \n";
	public static final String ERROR_CONFIG_READOBJECT = "\n ������ �������� �������: \n";
	public static final String ERROR_CONFIG_CREATE_DATABASE = "\n ������ �������� ���� ������";
	public static final String ERROR_CONFIG_OPEN_DATABASE = "\n ������ ����������� � ���� ������";
	public static final String ERROR_CONFIG_FILL_LINK_TABLE = "\n ������ ���������� ������� �������� ������� ��������";
	public static final String ERROR_CONFIG_CREATE_TOPIC = "������ ���������� ����";	
	public static final String ERROR_CONFIG_CLEAR = "������ �������� ���������� ���";
	
	//STRINGS *******************************************************
	
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
	
	//CONFIG CONNECTION ERRORS *******************************************************

	public static final String ERROR_CONFIG_CONNECTION_EXE = "\n �� ������ ���� 1cestart.exe";
	public static final String ERROR_CONFIG_CONNECTION_CHECK = "\n ������ ��������� ������ �� 1�";
	public static final String MESSAGE_CONFIG_CONNECTION_CHECK = "���������� ������� �����������";
	
	//MARKERS *******************************************************
	
	public static final String MARKER_SEARCH_TEXT = "codeanalyzer-search_text";
	public static final String MARKER_SEARCH_META = "codeanalyzer-search-meta";
	public static final String MARKER_SEARCH_PROC = "codeanalyzer-search-procs";
	
	public static final String MARKER_ROOT = "codeanalyzer-root";
	public static final String MARKER_OBJECT = "codeanalyzer-object";
	public static final String MARKER_MODULE = "codeanalyzer-module";
	public static final String MARKER_PROC = "codeanalyzer-proc";
	
	public static final String TOPIC_EXTENSION = "codeanalyzer_topic_extension";

	//COMPARE *******************************************************
	
	public static final String COMPARE_EQUALS = "����������";
	public static final String COMPARE_ADDED = "���� � ��������";
	public static final String COMPARE_REMOVED = "���� � �� ��������";
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

	//PATTERN *******************************************************
	
	public static final String PATTERN_PROCEDURE = "(�������|���������)\\s+([�-�]|[A-Z]|_|�)+[�-�A-Z0-9_�]*\\s*\\(";
	public static final String PATTERN_PROCEDURE_IN_STRING = "(\\.)*([�-�]|[A-Z]|_|�)+[�-�A-Z0-9_�]*\\s*\\(";
	public static final String PATTERN_MODULE = "([�-�]|[A-Z]|_|�)+[�-�A-Z0-9_�]*\\s*";
	

		
	
	
	

}

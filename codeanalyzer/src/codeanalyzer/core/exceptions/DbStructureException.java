package codeanalyzer.core.exceptions;

public class DbStructureException extends Exception {

	private static final long serialVersionUID = 4555098102309242996L;

	@Override
	public String getMessage() {
		return "Ошибка структуры данных. \nНеобходима повторная загрузка конфигурации.";
	}

}

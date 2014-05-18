package codeanalyzer.db.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public interface IDbService {

	void init(boolean createNew) throws InvocationTargetException;

	Connection getConnection() throws IllegalAccessException;

}

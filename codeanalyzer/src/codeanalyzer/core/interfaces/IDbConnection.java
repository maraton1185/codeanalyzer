package codeanalyzer.core.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public interface IDbConnection {

	void create() throws InvocationTargetException;

	void check() throws InvocationTargetException;

	Connection makeConnection(boolean exist) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException;

	void openConnection() throws InvocationTargetException;

	Connection getConnection() throws IllegalAccessException;

}

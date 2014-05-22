package codeanalyzer.module.db.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public interface IDbService {

	void init(boolean createNew) throws InvocationTargetException;

	Connection getConnection() throws IllegalAccessException;

	Connection makeConnection(boolean exist) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException;

}

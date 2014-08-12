package ebook.core.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

import ebook.core.exceptions.DbStructureException;

public interface IDbStructure {

	void createStructure(Connection con) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException;

	void checkSructure(Connection con) throws DbStructureException,
			SQLException;

	void updateSructure(Connection con) throws SQLException;

}

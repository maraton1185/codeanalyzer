package ebook.core.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import ebook.core.exceptions.DbLicenseException;
import ebook.module.tree.ITreeItemInfo;

public interface IDbConnection {

	void create() throws InvocationTargetException;

	void check() throws InvocationTargetException, DbLicenseException;

	Connection makeConnection(boolean exist) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			DbLicenseException;

	// void openConnection() throws InvocationTargetException;

	Connection getConnection() throws IllegalAccessException,
			DbLicenseException;

	ITreeItemInfo getTreeItem();

	String getName();
}

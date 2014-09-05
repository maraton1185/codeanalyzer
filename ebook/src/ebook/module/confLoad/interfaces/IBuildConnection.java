package ebook.module.confLoad.interfaces;

import java.sql.Connection;

public interface IBuildConnection {

	Connection getConnection() throws IllegalAccessException;

}

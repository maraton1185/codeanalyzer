package codeanalyzer.db.interfaces;

import java.lang.reflect.InvocationTargetException;

public interface IDbService {

	void init(boolean createNew) throws InvocationTargetException;

}

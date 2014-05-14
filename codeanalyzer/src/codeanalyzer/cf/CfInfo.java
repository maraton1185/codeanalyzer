package codeanalyzer.cf;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.eclipse.core.runtime.Path;

import codeanalyzer.cf.interfaces.ILoaderManager.operationType;
import codeanalyzer.utils.PreferenceSupplier;

public class CfInfo implements Serializable {

	public String name;
	public operationType type;
	public String path;
	public String db_path;
	public Boolean auto_name;
	public Boolean deleteSourceFiles;

	public SQLConnection sql;

	public class SQLConnection implements Serializable {

		// jdbc:microsoft:sqlserver://192.168.0.1:1433;databaseName=work;selectMethod=cursor
		public SQLConnection(String path, String user, String password) {
			this.path = path;
			this.user = user;
			this.password = password;
		}

		private static final long serialVersionUID = 9219264161306294280L;

		public String getConnectionString() {
			String _p = path.replace('\\', '-');
			String[] _path = _p.split("-");
			if (_path.length != 2)
				return "";

			return "jdbc:sqlserver://" + _path[0] + ";databaseName=" + _path[1]
					+ ";selectMethod=cursor";
		}

		public String user;
		public String password;
		public String path;
	}

	public String getName() {

		final class helper {

			private int segments;

			public helper(int segments) {
				this.segments = segments;
			}

			public String getName(String path) {
				String result = "";
				Path p = new Path(path);
				if (p.segmentCount() < segments)
					result = p.toString();
				for (int i = p.segmentCount() - segments; i >= 0
						&& i < p.segmentCount(); i++) {
					result = result.concat(p.segment(i)).concat("/");
				}
				return result;
			}
		}

		if (!auto_name)
			return name;

		String result = "";
		switch (type) {
		case update:

			result = new helper(3).getName(db_path);
			break;

		case fromDb:

			result = new helper(3).getName(db_path);
			break;
		case fromSQL:
			result = sql == null ? "Новая конфигурация" : sql.path;
			break;
		default:
			result = new helper(2).getName(path);
			break;
		}
		return result;
	}

	public CfInfo() {
		super();
		for (Field f : this.getClass().getFields()) {
			try {
				if (f.getType().isAssignableFrom(Boolean.class))
					f.set(this, false);
				else if (f.getType().isAssignableFrom(operationType.class))
					f.set(this, operationType.fromDirectory);
				else
					f.set(this, "");
			} catch (Exception e) {
			}
		}

		String defaultPath = PreferenceSupplier
				.get(PreferenceSupplier.DEFAULT_DIRECTORY);

		auto_name = true;
		path = defaultPath;
		db_path = defaultPath;
		type = operationType.fromDirectory;

	}

	private static final long serialVersionUID = -6530408724026513483L;

}

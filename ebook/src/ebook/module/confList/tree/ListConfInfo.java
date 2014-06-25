package ebook.module.confList.tree;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.core.models.DbOptions;
import ebook.module.conf.interfaces.ILoaderManager.operationType;
import ebook.module.confList.tree.ConfInfo.SQLConnection;
import ebook.module.tree.TreeItemInfo;
import ebook.utils.Utils;

public class ListConfInfo extends TreeItemInfo {

	public static enum DbState {
		notLoaded, Loaded
	}

	ConfInfo data;

	public ListConfInfo(ListConfInfoOptions options) {
		super(options);
		data = getOptions().info;
	}

	public ListConfInfo() {
		super(null);
	}

	@Override
	public String getSuffix() {
		return getDbName();
	}

	@Override
	public ListConfInfoOptions getOptions() {
		return (ListConfInfoOptions) super.getOptions();
	}

	@Override
	public void setOptions(DbOptions options) {
		super.setOptions(options);
		data = getOptions().info;
	}

	private DbState status = DbState.notLoaded;
	private DbState link_status = DbState.notLoaded;

	public String status() {

		String op = "";
		switch (data.type) {
		case fromDb:
			op = "Подключить " + data.db_full_path;
			break;
		case fromDirectory:
			op = "Загрузить из " + data.load_path;
			break;
		case update:
			op = "Обновить из " + data.load_path;
			break;
		case fromSQL:
			op = data.sql == null ? "-" : "Загрузить из " + data.sql.path;
			break;
		default:
			op = App.mng.cm().getOperationName(data.type);
			break;
		}
		return data.name + " : " + op;
	}

	public boolean isLoaded() {
		return getState() != DbState.notLoaded;
	}

	public void setType(operationType type) {
		data.type = type;
	}

	@Override
	public String getTitle() {
		if (isGroup())
			return super.getTitle();
		else
			return data.getName();
	}

	@Override
	public void setTitle(String title) {
		if (!isGroup() && data != null)
			data.name = title;
		else
			super.setTitle(title);

	}

	public operationType getType() {
		return data.type;
	}

	public void setAutoName(boolean value) {
		data.auto_name = value;
	}

	public boolean getAutoName() {
		return data.auto_name;
	}

	public void setLoadPath(String path) {
		data.load_path = path;
	}

	public IPath getLoadPath() {
		return data.load_path == null ? new Path("") : Utils
				.getAbsolute(new Path(data.load_path));
	}

	public void setName(String name) {
		data.name = name;
	}

	public String getName() {
		return data.getName();
	}

	public void setDbFullPath(String path) {
		data.db_full_path = path;
		data.db_path = new Path(path).removeLastSegments(1).toString();
		data.db_name = new Path(path).removeFileExtension()
				.removeFileExtension().lastSegment();
	}

	public IPath getDbFullPath() {
		return data.db_full_path == null ? new Path("") : Utils
				.getAbsolute(new Path(data.db_full_path));
	}

	public String getDbPath() {
		return data.db_path == null ? "" : Utils.getAbsolute(
				new Path(data.db_path)).toString();
	}

	public String getDbName() {
		return data.db_name;
	}

	public void setState(DbState status) {
		this.status = status;
		link_status = DbState.notLoaded;
	}

	public void setLinkState(DbState status) {
		this.link_status = status;
	}

	public DbState getState() {
		return status;
	}

	public DbState getLinkState() {
		return link_status;
	}

	public void setSQL(String path, String user, String password) {
		data.sql = data.new SQLConnection(path, user, password);
	}

	public SQLConnection getSQL() {
		return data.sql == null ? getDefaultSQL() : data.sql;
	}

	public SQLConnection getDefaultSQL() {
		return data.new SQLConnection("server\\base", "sa", "");
	}

	public Boolean getDeleteSourceFiles() {
		return data.deleteSourceFiles;
	}

	public void setDeleteSourceFiles(Boolean deleteSourceFiles) {
		data.deleteSourceFiles = deleteSourceFiles;
	}
}

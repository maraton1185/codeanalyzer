package ebook.module.db;

import java.util.ArrayList;
import java.util.List;

import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.module.userList.tree.UserInfo;

public class ACLService {

	protected IDbConnection db;
	private final String tableName;
	private final String updateEvent;

	public ACLService() {

		this.db = pico.get(IDbConnection.class);
		this.tableName = "ACL";
		this.updateEvent = "";
	}

	public List<UserInfo> get(Integer book) {

		List<UserInfo> result = new ArrayList<UserInfo>();

		UserInfo item;
		item = new UserInfo();
		item.setId(1);
		item.setTitle("test");
		result.add(item);

		// item = new UserInfo();
		// item.setId(1);
		// item.setTitle("test1");
		// result.add(item);

		return result;
	}
}

package ebook.core.models;

import ebook.core.interfaces.IManagerFactory;
import ebook.module.bookList.BookListManager;
import ebook.module.conf.ConfManager;
import ebook.module.confList.ConfListManager;
import ebook.module.userList.UserManager;

public class ManagerFactory implements IManagerFactory {

	BookListManager bls;

	@Override
	public BookListManager blm() {
		if (bls == null)
			bls = new BookListManager();
		return bls;
	}

	UserManager um;

	@Override
	public UserManager um() {
		if (um == null)
			um = new UserManager();
		return um;
	}

	ConfListManager clm;

	@Override
	public ConfListManager clm() {
		if (clm == null)
			clm = new ConfListManager();
		return clm;
	}

	ConfManager cm;

	@Override
	public ConfManager cm() {
		if (cm == null)
			cm = new ConfManager();
		return cm;
	}
}

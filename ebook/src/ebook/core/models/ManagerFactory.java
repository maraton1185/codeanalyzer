package ebook.core.models;

import ebook.core.pico;
import ebook.core.interfaces.IManagerFactory;
import ebook.module.bookList.BookListManager;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.ConfManager;
import ebook.module.conf.ListManager;
import ebook.module.conf.tree.ListInfo;
import ebook.module.confList.ConfListManager;
import ebook.module.confLoad.interfaces.ILoaderManager;
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

	@Override
	public ILoaderManager lm() {
		return pico.get(ILoaderManager.class);
	}

	@Override
	public ConfManager cm(ConfConnection con, ListInfo list) {
		return new ConfManager(con, list);
	}

	@Override
	public ListManager clm(ConfConnection con) {
		return new ListManager(con);
	}
}

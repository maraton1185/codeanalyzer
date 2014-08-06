package ebook.core.interfaces;

import ebook.module.bookList.BookListManager;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.ConfManager;
import ebook.module.conf.ListManager;
import ebook.module.conf.tree.ListInfo;
import ebook.module.confList.ConfListManager;
import ebook.module.confLoad.interfaces.ILoaderManager;
import ebook.module.userList.UserManager;

public interface IManagerFactory {

	BookListManager blm();

	UserManager um();

	ConfListManager clm();

	ILoaderManager lm();

	ListManager clm(ConfConnection con);

	ConfManager cm(ConfConnection con, ListInfo list);
}

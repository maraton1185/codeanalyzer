package ebook.core.interfaces;

import ebook.module.bookList.BookListManager;
import ebook.module.conf.ConfManager;
import ebook.module.confList.ConfListManager;
import ebook.module.userList.UserManager;

public interface IManagerFactory {

	BookListManager blm();

	UserManager um();

	ConfListManager clm();

	ConfManager cm();
}

package ebook.core.interfaces;

import ebook.module.bookList.BookListManager;
import ebook.module.confList.ConfManager;
import ebook.module.userList.UserManager;

public interface IManagerFactory {

	BookListManager blm();

	UserManager um();

	ConfManager cm();
}

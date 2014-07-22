package ebook.core.interfaces;

import ebook.module.book.BookConnection;
import ebook.module.book.BookService;
import ebook.module.bookList.BookListService;
import ebook.module.confList.ConfListService;
import ebook.module.db.ACLService;
import ebook.module.userList.UserService;

public interface IServiceFactory {

	ACLService acl();

	UserService us();

	BookListService bl();

	BookService bk(BookConnection bookConnection);

	ConfListService cl();

}

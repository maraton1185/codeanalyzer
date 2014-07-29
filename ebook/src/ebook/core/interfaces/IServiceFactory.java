package ebook.core.interfaces;

import ebook.module.acl.ACLService;
import ebook.module.book.BookConnection;
import ebook.module.book.BookService;
import ebook.module.bookList.BookListService;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.ConfService;
import ebook.module.confList.ConfListService;
import ebook.module.userList.UserService;

public interface IServiceFactory {

	ACLService acl();

	UserService us();

	BookListService bl();

	BookService bk(BookConnection con);

	ConfListService cl();

	ConfService cf(ConfConnection con);

}

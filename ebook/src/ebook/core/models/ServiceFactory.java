package ebook.core.models;

import ebook.core.interfaces.IServiceFactory;
import ebook.module.acl.ACLService;
import ebook.module.book.BookConnection;
import ebook.module.book.BookService;
import ebook.module.bookList.BookListService;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.ConfService;
import ebook.module.confList.ConfListService;
import ebook.module.userList.UserService;

public class ServiceFactory implements IServiceFactory {

	ACLService acl;

	@Override
	public ACLService acl() {
		if (acl == null)
			acl = new ACLService();
		return acl;
	}

	UserService us;

	@Override
	public UserService us() {
		if (us == null)
			us = new UserService();
		return us;
	}

	BookListService bl;

	@Override
	public BookListService bl() {
		if (bl == null)
			bl = new BookListService();
		return bl;
	}

	@Override
	public BookService bk(BookConnection bookConnection) {
		return new BookService(bookConnection);
	}

	ConfListService cls;

	@Override
	public ConfListService cl() {

		if (cls == null)
			cls = new ConfListService();
		return cls;
	}

	@Override
	public ConfService cf(ConfConnection con) {
		return new ConfService(con);
	}

}

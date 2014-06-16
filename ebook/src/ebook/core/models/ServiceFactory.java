package ebook.core.models;

import ebook.core.interfaces.IServiceFactory;
import ebook.module.book.BookConnection;
import ebook.module.book.BookService;
import ebook.module.bookList.BookListService;
import ebook.module.confList.ConfListService;
import ebook.module.userList.UserService;

public class ServiceFactory implements IServiceFactory {

	UserService us;

	@Override
	public UserService us() {
		if (us == null)
			us = new UserService();
		return us;
	}

	BookListService bls;

	@Override
	public BookListService bls() {
		if (bls == null)
			bls = new BookListService();
		return bls;
	}

	@Override
	public BookService bs(BookConnection bookConnection) {
		return new BookService(bookConnection);
	}

	ConfListService cls;

	@Override
	public ConfListService cls() {

		if (cls == null)
			cls = new ConfListService();
		return cls;
	}

}

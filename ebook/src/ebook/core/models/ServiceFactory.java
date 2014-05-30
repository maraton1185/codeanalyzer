package ebook.core.models;

import ebook.core.interfaces.IServiceFactory;
import ebook.module.books.BookConnection;
import ebook.module.books.BookService;
import ebook.module.booksList.BookListService;
import ebook.module.users.UserService;

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

}

package codeanalyzer.core.models;

import codeanalyzer.core.interfaces.IServiceFactory;
import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.BookService;
import codeanalyzer.module.booksList.BookListService;
import codeanalyzer.module.users.UserService;

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

package codeanalyzer.core.interfaces;

import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.BookService;
import codeanalyzer.module.booksList.BookListService;
import codeanalyzer.module.users.UserService;

public interface IServiceFactory {

	UserService us();

	BookListService bls();

	BookService bs(BookConnection bookConnection);

}

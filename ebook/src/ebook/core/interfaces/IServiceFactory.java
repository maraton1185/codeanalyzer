package ebook.core.interfaces;

import ebook.module.books.BookConnection;
import ebook.module.books.BookService;
import ebook.module.booksList.BookListService;
import ebook.module.users.UserService;

public interface IServiceFactory {

	UserService us();

	BookListService bls();

	BookService bs(BookConnection bookConnection);

}

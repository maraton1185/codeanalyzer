package ebook.core.interfaces;

import ebook.module.book.BookConnection;
import ebook.module.book.BookService;
import ebook.module.bookList.BookListService;
import ebook.module.confList.ConfListService;
import ebook.module.userList.UserService;

public interface IServiceFactory {

	UserService us();

	BookListService bls();

	BookService bs(BookConnection bookConnection);

	ConfListService cls();

}

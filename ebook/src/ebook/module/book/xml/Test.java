/**
 * 
 */
package ebook.module.book.xml;

import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class Test {

	@Execute
	public void execute(BookConnection book, @Active SectionInfo section,
			Shell shell) {

		IPath p = Utils.browseFile(book.getPath(), shell,
				Strings.get("appTitle"), "*.xml");
		if (p == null)
			return;

		// book.srv().saveToFile(p, section);

		ArrayList<SectionXML> bookList = new ArrayList<SectionXML>();

		// create books
		SectionXML book1 = new SectionXML();
		book1.setTitle("978-0060554736");
		bookList.add(book1);

		SectionXML book2 = new SectionXML();
		book2.setTitle("978-3832180577");
		bookList.add(book2);

		// create bookstore, assigning book
		SectionXML bookstore = new SectionXML();
		bookstore.setTitle("Fraport Bookstore");
		bookstore.setChild(bookList);
		try {
			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext.newInstance(SectionXML.class);
			Marshaller m = context.createMarshaller();

			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out
			m.marshal(bookstore, System.out);

			// Write to File
			m.marshal(bookstore, p.toFile());

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}

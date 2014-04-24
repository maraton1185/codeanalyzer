package codeanalyzer.views.books;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_CONTENT_VIEW_DATA;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class SectionView {

	FormToolkit toolkit;
	ScrolledForm form;
	Composite body;

	// Section bookSection;
	// Composite bookSectionClient;
	// HyperlinkAdapter bookSectionHandler;

	@Inject
	@Active
	BookInfo book;

	BookSection section;
	private List<BookSection> sectionsList;
	private ECommandService cs;
	private EHandlerService hs;
	private MWindow window;

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Const.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_CONTENT_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs) {

		if (book != data.book)
			return;

		if (!data.parent.equals(section))
			return;

		fillBody();
	}

	@Inject
	public SectionView() {
		// TODO Your code here
	}

	private void fillBody() {

		Hyperlink hlink;
		Button button;
		Label label;
		GridData gd;

		for (Control ctrl : body.getChildren()) {
			ctrl.dispose();
		}
		// *************************************************************

		sectionsList = book.sections().getChildren(section);

		for (BookSection sec : sectionsList) {

			Composite comp = toolkit.createComposite(body);
			GridLayout gl = new GridLayout(2, false);
			comp.setLayout(gl);
			// comp.setLayoutData(new RowdaGridData(GridData.FILL_BOTH));
			// if (sec.block) {
			//
			// } else {

			hlink = toolkit.createHyperlink(comp, sec.title, SWT.WRAP);
			hlink.setHref(sec);
			hlink.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {
					window.getContext().set(BookSection.class,
							(BookSection) e.getHref());
					Utils.executeHandler(hs, cs,
							Strings.get("command.id.ShowSection"));
					// super.linkActivated(e);
				}

			});
			gd = new GridData();
			gd.horizontalSpan = 2;
			hlink.setLayoutData(gd);
			// }
		}

		// *************************************************************
		body.layout(true);
		form.getBody().layout(true);
	}

	@PostConstruct
	public void postConstruct(Composite parent, BookSection section,
			final ECommandService cs, final EHandlerService hs,
			@Active final MWindow window) {

		this.section = section;
		this.cs = cs;
		this.hs = hs;
		this.window = window;

		ImageHyperlink link;

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		// form.setSize(448, 377);
		// form.setLocation(0, 0);
		RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		// layout.maxNumColumns = 2;
		form.getBody().setLayout(layout);

		form.setText(section.title);

		body = toolkit.createComposite(form.getBody());
		body.setLayout(layout);

		fillBody();

		// IMAGEHYPERLINKS
		// *******************************************************

		link = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		// link.setImage(Utils.getImage("add_book.png"));
		link.setText("Добавить блок текста");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Utils.executeHandler(hs, cs,
						Strings.get("command.id.AddSectionsBlock"));
				super.linkActivated(e);
			}

		});

	}
}
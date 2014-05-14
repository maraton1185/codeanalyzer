package codeanalyzer.views.main;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.books.book.CurrentBookInfo;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;

public class BookInfoView {

	private ScrolledForm form;
	Text desc_text;
	Text path;
	WritableValue bookValue;
	ImageHyperlink title;

	@Inject
	MDirtyable dirty;

	IChangeListener listener = new IChangeListener() {
		@Override
		public void handleChange(ChangeEvent event) {
			if (dirty != null)
				dirty.setDirty(true);
		}
	};

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOK_INFO(
			@UIEventTopic(Const.EVENT_UPDATE_BOOK_INFO) Object o,
			@Optional CurrentBookInfo book, IBookManager bm, final EHandlerService hs,
			final ECommandService cs) {
		if (book == null) {
			title.setText(Strings.get("bookInfoViewTitle"));
			return;
		}

		title.setText(book.getName());
		title.setImage(book.getImage());

		path.setText(book.getFullName());
		path.setEnabled(true);

		form.reflow(true);

		bookValue.setValue(book);

		dirty.setDirty(false);
	}

	@Persist
	public void save(@Optional CurrentBookInfo book, IBookManager bm, Shell shell) {
		if (book == null)
			return;

		if (bm.saveBook(book, shell))
			dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		Label label;

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		// form.setText(Strings.get("appTitle"));
		form.getBody().setLayout(new GridLayout(3, false));

		title = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3,
				1));
		title.setText(Strings.get("bookInfoViewTitle"));
		title.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				AppManager.br.post(Const.EVENT_SHOW_BOOK, null);

				super.linkActivated(e);
			}

		});

		Button radio1 = toolkit.createButton(form.getBody(), "Просмотр",
				SWT.RADIO);
		radio1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		Button radio2 = toolkit.createButton(form.getBody(), "Редактор",
				SWT.RADIO);
		radio2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		label = toolkit.createLabel(form.getBody(), "Описание:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3,
				1));

		desc_text = toolkit.createText(form.getBody(), "", SWT.BORDER
				| SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		GridData gd_desc_text = new GridData(GridData.FILL_BOTH);
		gd_desc_text.horizontalSpan = 3;
		desc_text.setLayoutData(gd_desc_text);

		label = toolkit.createLabel(form.getBody(), "Путь:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		path = toolkit.createText(form.getBody(), "", SWT.BORDER | SWT.WRAP
				| SWT.SINGLE | SWT.READ_ONLY);
		GridData gd_path = new GridData(GridData.FILL_HORIZONTAL);
		gd_path.horizontalSpan = 2;
		path.setLayoutData(gd_path);
		path.setEnabled(false);

		bookValue = new WritableValue();
		// create new Context
		DataBindingContext ctx = new DataBindingContext();

		// define the IObservables
		IObservableValue target, target1, target2;
		IObservableValue model;

		target = WidgetProperties.enabled().observe(desc_text);
		target1 = WidgetProperties.enabled().observe(radio1);
		target2 = WidgetProperties.enabled().observe(radio2);
		model = BeanProperties.value(CurrentBookInfo.class, "opened").observeDetail(
				bookValue);
		ctx.bindValue(target, model);
		ctx.bindValue(target1, model);
		ctx.bindValue(target2, model);

		target = WidgetProperties.selection().observe(radio1);
		model = BeanProperties.value(CurrentBookInfo.class, "viewMode").observeDetail(
				bookValue);
		ctx.bindValue(target, model);

		target = WidgetProperties.selection().observe(radio2);
		model = BeanProperties.value(CurrentBookInfo.class, "editMode").observeDetail(
				bookValue);
		ctx.bindValue(target, model);

		target = WidgetProperties.text(SWT.Modify).observe(desc_text);
		model = BeanProperties.value(CurrentBookInfo.class, "description")
				.observeDetail(bookValue);
		ctx.bindValue(target, model);

		for (Object o : ctx.getBindings()) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}

		bookValue.setValue(new CurrentBookInfo());

		dirty.setDirty(false);

	}
}
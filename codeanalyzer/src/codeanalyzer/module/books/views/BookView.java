package codeanalyzer.module.books.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.core.Events;
import codeanalyzer.module.books.interfaces.IBookManager;
import codeanalyzer.module.books.list.BookInfo;
import codeanalyzer.module.books.list.BookService;
import codeanalyzer.utils.Strings;

public class BookView {

	ScrolledForm form;
	WritableValue dataValue;
	BookService bs = new BookService();
	BookViewModel model;

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
			@UIEventTopic(Events.EVENT_UPDATE_BOOK_INFO) Object o,
			@Optional BookInfo data, IBookManager bm, final EHandlerService hs,
			final ECommandService cs) {
		if (data == null) {
			form.setText(Strings.get("bookInfoViewTitle"));
			// title.setText(Strings.get("bookInfoViewTitle"));
			return;
		}

		model = new BookViewModel((BookInfo) bs.get(data.id));

		form.reflow(true);

		dataValue.setValue(model);

		dirty.setDirty(false);
	}

	@Persist
	public void save(IBookManager bm, Shell shell) {
		if (model == null)
			return;

		if (bm.save(model.getData(), shell))
			dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		Label label;
		Text text;
		GridData gd;

		dataValue = new WritableValue();
		DataBindingContext ctx = new DataBindingContext();

		// define the IObservables
		IObservableValue target;
		IObservableValue field_model;

		// ********************************************
		parent.setLayout(new FillLayout());
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		// form.setText(Strings.get("appTitle"));
		form.getBody().setLayout(new GridLayout(3, false));

		// ПОЛЯ *******************************************
		// title = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		// title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
		// 3,
		// 1));
		// title.setText(Strings.get("bookInfoViewTitle"));
		// title.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		//
		// AppManager.br.post(Events.EVENT_SHOW_BOOK, null);
		//
		// super.linkActivated(e);
		// }
		//
		// });
		//
		// Button radio1 = toolkit.createButton(form.getBody(), "Просмотр",
		// SWT.RADIO);
		// radio1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
		// 1, 1));
		// Button radio2 = toolkit.createButton(form.getBody(), "Редактор",
		// SWT.RADIO);
		// radio2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
		// 1, 1));
		//
		// label = toolkit.createLabel(form.getBody(), "Описание:", SWT.LEFT);
		// label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
		// 3,
		// 1));
		//
		// desc_text = toolkit.createText(form.getBody(), "", SWT.BORDER
		// | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		// GridData gd_desc_text = new GridData(GridData.FILL_BOTH);
		// gd_desc_text.horizontalSpan = 3;
		// desc_text.setLayoutData(gd_desc_text);
		//
		// label = toolkit.createLabel(form.getBody(), "Путь:", SWT.LEFT);
		// label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
		// 1,
		// 1));
		//
		// path = toolkit.createText(form.getBody(), "", SWT.BORDER | SWT.WRAP
		// | SWT.SINGLE | SWT.READ_ONLY);
		// GridData gd_path = new GridData(GridData.FILL_HORIZONTAL);
		// gd_path.horizontalSpan = 2;
		// path.setLayoutData(gd_path);
		// path.setEnabled(false);
		//
		// bookValue = new WritableValue();
		// // create new Context
		// DataBindingContext ctx = new DataBindingContext();
		//
		// // define the IObservables
		// IObservableValue target, target1, target2;
		// IObservableValue model;
		//
		// target = WidgetProperties.enabled().observe(desc_text);
		// target1 = WidgetProperties.enabled().observe(radio1);
		// target2 = WidgetProperties.enabled().observe(radio2);
		// model = BeanProperties.value(WindowBookInfo.class, "opened")
		// .observeDetail(bookValue);
		// ctx.bindValue(target, model);
		// ctx.bindValue(target1, model);
		// ctx.bindValue(target2, model);
		//
		// target = WidgetProperties.selection().observe(radio1);
		// model = BeanProperties.value(WindowBookInfo.class, "viewMode")
		// .observeDetail(bookValue);
		// ctx.bindValue(target, model);
		//
		// target = WidgetProperties.selection().observe(radio2);
		// model = BeanProperties.value(WindowBookInfo.class, "editMode")
		// .observeDetail(bookValue);
		// ctx.bindValue(target, model);
		//
		// target = WidgetProperties.text(SWT.Modify).observe(desc_text);
		// model = BeanProperties.value(WindowBookInfo.class, "description")
		// .observeDetail(bookValue);
		// ctx.bindValue(target, model);

		// *******************************************

		for (Object o : ctx.getBindings()) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}

		// bookValue.setValue(new WindowBookInfo());

		dirty.setDirty(false);

	}
}
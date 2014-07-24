package ebook.module.confList.view;

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
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Events;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class ConfView {

	ScrolledForm form;
	WritableValue dataValue;
	// BookListService bs = new BookListService();
	ConfViewModel model = new ConfViewModel(new ListConfInfo());

	Composite stack;
	StackLayout stackLayout;
	Composite groupComp;
	Composite itemComp;
	ComboViewer combo;
	@Inject
	EHandlerService hService;
	@Inject
	ECommandService comService;

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
	public void EVENT_UPDATE_CONF_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_CONF_INFO) Object o,
			@Optional ListConfInfo data, final EHandlerService hs,
			final ECommandService cs) {
		if (data == null) {
			// form.setText(Strings.get("bookInfoViewTitle"));
			// title.setText(Strings.get("bookInfoViewTitle"));
			return;
		}

		model = new ConfViewModel((ListConfInfo) App.srv.cl().get(data.getId()));

		dataValue.setValue(model);

		if (model.isGroup())
			stackLayout.topControl = groupComp;
		else
			stackLayout.topControl = itemComp;
		stack.layout();
		itemComp.layout(true);
		groupComp.layout(true);
		form.reflow(true);

		dirty.setDirty(false);
	}

	@Persist
	public void save(Shell shell) {
		if (model == null)
			return;
		if (App.mng.clm().save(model.getData(), shell))
			dirty.setDirty(false);

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		dataValue = new WritableValue();
		DataBindingContext ctx = new DataBindingContext();

		// ********************************************
		parent.setLayout(new FillLayout());
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		// form.setText(Strings.get("appTitle"));
		form.getBody().setLayout(new GridLayout(2, false));

		// ИМЯ *******************************************
		fNAME(toolkit, ctx, parent);

		// СТЕК *******************************************

		stack = toolkit.createComposite(form.getBody(), SWT.BORDER_DASH);
		stackLayout = new StackLayout();
		stack.setLayout(stackLayout);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.heightHint = 150;
		stack.setLayoutData(gd);

		// ПОЛЯ ГРУПП *******************************************
		fGROUP(toolkit, ctx);

		// ПОЛЯ ЭЛЕМЕНТОВ *******************************************
		fITEM(toolkit, ctx);

		// *******************************************

		for (Object o : ctx.getBindings()) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}

		dirty.setDirty(false);

		App.br.post(Events.EVENT_CONF_LIST_SET_SELECTION, null);

	}

	private void fITEM(FormToolkit toolkit, DataBindingContext ctx) {
		Label label;
		Text text;

		IObservableValue target;
		IObservableValue field_model;

		itemComp = toolkit.createComposite(stack);
		itemComp.setLayout(new GridLayout(2, false));
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2,
		// 1));

		ifLoadFolder(toolkit, ctx, itemComp);

		ifPATH(toolkit, ctx, itemComp);

		ifDbFileName(toolkit, ctx, itemComp);

		ifDbStatus(toolkit, ctx, itemComp);

		ifDbCommands(toolkit, ctx, itemComp);

		label = toolkit.createLabel(itemComp, "Описание", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2,
				1));

		text = toolkit.createText(itemComp, "", SWT.BORDER | SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1)
				.hint(SWT.DEFAULT, 40).applyTo(text);

		target = WidgetProperties.text(SWT.Modify).observe(text);
		field_model = BeanProperties.value(model.getClass(), "description")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

	private void ifPATH(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

		Label label;
		GridData gd;
		Text text;
		Composite comp;

		IObservableValue target;
		IObservableValue field_model;

		comp = toolkit.createComposite(parent);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		label = toolkit.createLabel(comp, "Каталог базы данных:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		text = toolkit.createText(comp, "", SWT.MULTI | SWT.WRAP
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 30;
		text.setLayoutData(gd);

		target = WidgetProperties.text().observe(text);
		field_model = BeanProperties.value(model.getClass(), "dbPath")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		// target = WidgetProperties.visible().observe(comp);
		// field_model = BeanProperties.value(model.getClass(), "item")
		// .observeDetail(dataValue);
		// ctx.bindValue(target, field_model);

	}

	private void ifLoadFolder(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

		Label label;
		GridData gd;
		Text text;
		Composite comp;

		IObservableValue target;
		IObservableValue field_model;

		comp = toolkit.createComposite(parent);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		label = toolkit.createLabel(comp, "Каталог для загрузки:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		text = toolkit.createText(comp, "", SWT.MULTI | SWT.WRAP
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 30;
		text.setLayoutData(gd);

		target = WidgetProperties.text().observe(text);
		field_model = BeanProperties.value(model.getClass(), "loadFolder")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

	private void ifDbFileName(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

		Label label;
		GridData gd;
		Text text;
		Composite comp;

		IObservableValue target;
		IObservableValue field_model;

		comp = toolkit.createComposite(parent);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		label = toolkit.createLabel(comp, "Файл базы данных:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		text = toolkit.createText(comp, "", SWT.MULTI | SWT.WRAP
				| SWT.READ_ONLY | SWT.BOLD);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 30;
		text.setLayoutData(gd);
		FontData fontDatas[] = text.getFont().getFontData();
		FontData data = fontDatas[0];
		Font font = new Font(Display.getCurrent(), data.getName(),
				data.getHeight(), SWT.BOLD);
		text.setFont(font);

		target = WidgetProperties.text().observe(text);
		field_model = BeanProperties.value(model.getClass(), "dbFileName")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		// target = WidgetProperties.visible().observe(comp);
		// field_model = BeanProperties.value(model.getClass(), "item")
		// .observeDetail(dataValue);
		// ctx.bindValue(target, field_model);

	}

	private void ifDbStatus(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

		Label label;
		Composite comp;

		IObservableValue target;
		IObservableValue field_model;

		comp = toolkit.createComposite(parent);
		comp.setLayout(new GridLayout(1, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		label = toolkit.createLabel(comp, "", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		target = WidgetProperties.text().observe(label);
		field_model = BeanProperties.value(model.getClass(), "status")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		label = toolkit.createLabel(comp, "", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		target = WidgetProperties.text().observe(label);
		field_model = BeanProperties.value(model.getClass(), "linkStatus")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

	private void ifDbCommands(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

		// Label label;
		Composite comp;
		ImageHyperlink link;

		// IObservableValue target;
		// IObservableValue field_model;

		comp = toolkit.createComposite(parent);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		link = toolkit.createImageHyperlink(itemComp, SWT.LEFT);
		link.setText("Загрузить");
		link.setImage(Utils.getImage("import.png"));
		link.setUnderlined(false);
		link.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Utils.executeHandler(hService, comService,
						Strings.get("command.id.LoadConfiguration"));
			}
		});

		// link = toolkit.createImageHyperlink(itemComp, SWT.LEFT);
		// link.setText("Открыть");
		// // link.setImage(Utils.getImage("load.png"));
		// link.setUnderlined(false);
		// link.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false,
		// 1,
		// 1));
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// Utils.executeHandler(hService, comService,
		// Strings.get("command.id.ShowConfFromList"));
		// }
		// });

	}

	private void fNAME(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

		Label label;

		IObservableValue target;
		IObservableValue field_model;

		gap(toolkit);

		label = toolkit.createLabel(form.getBody(), "", SWT.WRAP | SWT.CENTER);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1)
				.hint(50, SWT.DEFAULT).applyTo(label);

		FontData fontDatas[] = parent.getFont().getFontData();
		FontData data = fontDatas[0];
		int height = data.getHeight();
		height = (int) (height + 0.5 * height);
		Font font = new Font(Display.getCurrent(), data.getName(), height,
				SWT.BOLD);
		label.setFont(font);

		target = WidgetProperties.text().observe(label);
		field_model = BeanProperties.value(model.getClass(), "title")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

	private void gap(FormToolkit toolkit) {
		Label label = toolkit.createLabel(form.getBody(), "", SWT.CENTER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

	}

	private void fGROUP(FormToolkit toolkit, DataBindingContext ctx) {

		Label label;
		Text text;

		IObservableValue target;
		IObservableValue field_model;

		groupComp = toolkit.createComposite(stack);
		groupComp.setLayout(new GridLayout(2, false));

		label = toolkit.createLabel(groupComp, "Описание группы", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2,
				1));

		text = toolkit.createText(groupComp, "", SWT.BORDER | SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1)
				.hint(SWT.DEFAULT, 40).applyTo(text);

		target = WidgetProperties.text(SWT.Modify).observe(text);
		field_model = BeanProperties.value(model.getClass(), "description")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

}
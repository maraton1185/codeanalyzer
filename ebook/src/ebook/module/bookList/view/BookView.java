package ebook.module.bookList.view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.core.App;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.userList.views.RoleViewModel;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class BookView {

	ScrolledForm form;
	WritableValue dataValue;
	// BookListService bs = new BookListService();
	BookViewModel model = new BookViewModel();

	Composite stack;
	StackLayout stackLayout;
	Composite groupComp;
	Composite itemComp;
	// ComboViewer roles;
	CheckboxTableViewer roles;

	DataBindingContext ctx;

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
	public void EVENT_UPDATE_USER_ROLES(
			@UIEventTopic(Events.EVENT_UPDATE_USER_ROLES) Object o) {
		// roles.setInput(App.srv.us().getBookRoles());
		model.setRoles();
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOK_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_BOOK_INFO) Object o,
			@Optional ListBookInfo data, final EHandlerService hs,
			final ECommandService cs) {
		if (data == null) {
			// form.setText(Strings.get("bookInfoViewTitle"));
			// title.setText(Strings.get("bookInfoViewTitle"));
			return;
		}

		model = new BookViewModel();

		model.setData((ListBookInfo) App.srv.bl().get(data.getId()));
		model.setRoles();
		ViewerSupport.bind(roles, BeansObservables.observeList(model, "roles",
				RoleViewModel.class), BeanProperties.value(RoleViewModel.class,
				"title"));
		ctx.bindSet(ViewersObservables.observeCheckedElements(roles,
				RoleViewModel.class), BeansObservables.observeSet(model,
				"activeRoles", RoleViewModel.class), new UpdateSetStrategy(
				UpdateSetStrategy.POLICY_NEVER), new UpdateSetStrategy(
				UpdateSetStrategy.POLICY_UPDATE));

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

		if (App.mng.blm().save(model.getData(), shell))
			dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		dataValue = new WritableValue();
		ctx = new DataBindingContext();

		// ********************************************
		parent.setLayout(new FillLayout());
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		// form.setText(Strings.get("appTitle"));
		// TableWrapLayout layout = new TableWrapLayout();
		// layout.numColumns = 2;
		form.getBody().setLayout(new GridLayout(2, false));

		// ИМЯ *******************************************
		nameField(toolkit, parent);

		// СТЕК *******************************************

		stack = toolkit.createComposite(form.getBody());
		stackLayout = new StackLayout();
		stack.setLayout(stackLayout);

		stack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
		// ПОЛЯ ГРУПП *******************************************
		groupFields(toolkit);

		// ПОЛЯ ЭЛЕМЕНТОВ *******************************************
		itemFields(toolkit);

		// *******************************************

		for (Object o : ctx.getBindings()) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}

		dirty.setDirty(false);

		App.br.post(Events.EVENT_BOOK_LIST_SET_SELECTION, null);

	}

	private void itemFields(FormToolkit toolkit) {
		// Label label;
		GridData gd;
		Text text;

		IObservableValue target;
		IObservableValue field_model;

		itemComp = toolkit.createComposite(stack);
		// TableWrapLayout layout = new TableWrapLayout();
		// layout.numColumns = 2;
		itemComp.setLayout(new GridLayout(2, false));
		// itemComp.setLayout(layout);
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2,
		// 1));

		pathField(toolkit, itemComp);

		Hyperlink link = toolkit.createHyperlink(itemComp, "Описание книги",
				SWT.RIGHT);
		link.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				try {
					IPath p = model.data.getPath();
					if (p == null)
						return;
					File temp = new File(p.addFileExtension("txt").toString());
					if (!temp.exists()) {
						temp.createNewFile();
					}
					PrintWriter writer = new PrintWriter(temp, "UTF-8");
					writer.println(model.getDescription());
					writer.close();

					java.awt.Desktop.getDesktop().open(temp);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		text = toolkit.createText(itemComp, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1)
				.hint(SWT.DEFAULT, 40).applyTo(text);

		target = WidgetProperties.text(SWT.Modify).observe(text);
		field_model = BeanProperties.value(model.getClass(), "description")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		Composite panel = toolkit.createComposite(itemComp);
		panel.setLayout(new RowLayout());
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.grabExcessHorizontalSpace = true;
		panel.setLayoutData(gd);

		ImageHyperlink hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setText("Картинка книги");

		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				final IPath p = Utils.browseFile(
						new Path(PreferenceSupplier
								.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
						itemComp.getShell(), Strings.get("appTitle"),
						"*.png;*.bmp");
				if (p == null)
					return;

				App.srv.bl().addImage(model.getId(), p);
			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("delete.png"));
		hlink.setToolTipText("Удалить");

		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				App.srv.bl().deleteImage(model.getId());
			}
		});

		Label label = toolkit.createLabel(itemComp, "");

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		target = WidgetProperties.image().observe(label);
		field_model = BeanProperties.value(model.getClass(), "image")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

	private void groupFields(FormToolkit toolkit) {

		Label label;
		Text text;

		IObservableValue target;
		IObservableValue field_model;

		groupComp = toolkit.createComposite(stack);
		groupComp.setLayout(new GridLayout(2, false));
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2,
		// 1));

		addRoles(toolkit);

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

	private void pathField(FormToolkit toolkit, Composite parent) {

		Label label;
		GridData gd;
		Text text;
		Composite comp;

		IObservableValue target;
		IObservableValue field_model;

		comp = toolkit.createComposite(parent);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));

		// TableWrapData td = new TableWrapData(SWT.FILL, SWT.FILL, 2, 1);
		// td.grabHorizontal = false;
		// td.grabVertical = true;

		label = toolkit.createLabel(comp, "Путь:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		text = toolkit.createText(comp, "", SWT.MULTI | SWT.WRAP
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 30;
		text.setLayoutData(gd);

		target = WidgetProperties.text().observe(text);
		field_model = BeanProperties.value(model.getClass(), "path")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		// target = WidgetProperties.visible().observe(comp);
		// field_model = BeanProperties.value(model.getClass(), "item")
		// .observeDetail(dataValue);
		// ctx.bindValue(target, field_model);

	}

	private void nameField(FormToolkit toolkit, Composite parent) {

		Label label;
		GridData gd;

		IObservableValue target;
		IObservableValue field_model;

		gap(toolkit);

		label = toolkit.createLabel(form.getBody(), "", SWT.CENTER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

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

	private void addRoles(FormToolkit toolkit) {

		Label label;
		GridData gd;

		// IObservableValue target;
		// IObservableValue field_model;

		label = toolkit.createLabel(groupComp, "Доступ по ролям:", SWT.LEFT);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(label);
		// label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
		// 2,
		// 1));

		// Composite rolesComposite = new Composite(groupComp, SWT.NONE);
		// toolkit.adapt(rolesComposite, true, true);
		Composite rolesComposite = toolkit.createComposite(groupComp);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 1;
		gd.heightHint = 150;
		// gd.horizontalSpan = 2;
		rolesComposite.setLayoutData(gd);
		// GridDataFactory.fillDefaults().applyTo(rolesComposite);
		TableColumnLayout rolesColumnLayout = new TableColumnLayout();
		rolesComposite.setLayout(rolesColumnLayout);

		roles = CheckboxTableViewer.newCheckList(rolesComposite, SWT.SINGLE
				| SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);

		Table rolesTable = roles.getTable();

		rolesTable.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					model.setActiveRoles(roles.getCheckedElements());
				}
			}
		});
		rolesTable.setHeaderVisible(true);
		rolesTable.setLinesVisible(true);
		TableColumn titleColumn = new TableColumn(rolesTable, SWT.NONE);
		titleColumn.setText("Роль");
		rolesColumnLayout.setColumnData(titleColumn, new ColumnWeightData(1));

		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(roles.getTable());

		toolkit.adapt(roles.getControl(), true, true);

		// ViewerSupport.bind(roles, BeansObservables.observeList(model,
		// "roles",
		// RoleViewModel.class), BeanProperties.value(RoleViewModel.class,
		// "title"));
		//
		// ctx.bindSet(ViewersObservables.observeCheckedElements(roles,
		// RoleViewModel.class), BeansObservables.observeSet(model,
		// "activeRoles", RoleViewModel.class));
		// IViewerObservableValue selected = ViewerProperties.singleSelection()
		// .observe(friendsViewer);
		//
		// field_model = BeanProperties.value(model.getClass(), "role",
		// UserInfo.class).observeDetail(dataValue);
		// ctx.bindValue(selected, field_model);

		// target = WidgetProperties.enabled().observe(roles.getControl());
		// field_model = BeanProperties.value(model.getClass(), "showRole")
		// .observeDetail(dataValue);
		// ctx.bindValue(target, field_model);

	}

	private void gap(FormToolkit toolkit) {
		Label label = toolkit.createLabel(form.getBody(), "", SWT.CENTER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

	}

}
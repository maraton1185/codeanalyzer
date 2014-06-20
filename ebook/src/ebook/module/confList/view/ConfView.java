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
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.userList.tree.UserInfo;
import ebook.utils.Events;

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

		model = new ConfViewModel((ListConfInfo) App.srv.cls()
				.get(data.getId()));

		dataValue.setValue(model);

		if (model.isGroup())
			stackLayout.topControl = groupComp;
		else
			stackLayout.topControl = itemComp;
		stack.layout();

		form.reflow(true);

		dirty.setDirty(false);
	}

	@Persist
	public void save(Shell shell) {
		if (model == null)
			return;

		// if (bm.save(model.getData(), shell))
		// dirty.setDirty(false);
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

		// ��� *******************************************
		nameField(toolkit, ctx, parent);

		// ���� *******************************************

		stack = toolkit.createComposite(form.getBody());
		stackLayout = new StackLayout();
		stack.setLayout(stackLayout);
		stack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));

		// ���� ����� *******************************************
		groupFields(toolkit, ctx);

		// ���� ��������� *******************************************
		itemFields(toolkit, ctx);

		// *******************************************

		for (Object o : ctx.getBindings()) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}

		dirty.setDirty(false);

		App.br.post(Events.EVENT_CONF_LIST_SET_SELECTION, null);

	}

	private void itemFields(FormToolkit toolkit, DataBindingContext ctx) {
		// Label label;
		GridData gd;
		Text text;

		IObservableValue target;
		IObservableValue field_model;

		itemComp = toolkit.createComposite(stack);
		itemComp.setLayout(new GridLayout(2, false));
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2,
		// 1));

		pathField(toolkit, ctx, itemComp);

		// Hyperlink link = toolkit.createHyperlink(itemComp, "�������� �����",
		// SWT.RIGHT);
		// link.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2,
		// 1));
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// // try {
		// // IPath p = model.data.getPath();
		// // if (p == null)
		// // return;
		// // File temp = new File(p.addFileExtension("txt").toString());
		// // if (!temp.exists())
		// // temp.createNewFile();
		// // java.awt.Desktop.getDesktop().open(temp);
		// // } catch (IOException e1) {
		// // e1.printStackTrace();
		// // }
		// }
		// });

		// label = toolkit.createLabel(itemComp, "�������� �����:", SWT.LEFT);
		// label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
		// 2,
		// 1));

		// text = toolkit.createText(itemComp, "", SWT.WRAP | SWT.MULTI
		// | SWT.READ_ONLY);
		// gd = new GridData(GridData.FILL_BOTH);
		// gd.horizontalSpan = 2;
		// text.setLayoutData(gd);
		//
		// target = WidgetProperties.text().observe(text);
		// field_model = BeanProperties.value(model.getClass(),
		// "bookDescription")
		// .observeDetail(dataValue);
		// ctx.bindValue(target, field_model);

	}

	private void groupFields(FormToolkit toolkit, DataBindingContext ctx) {

		Label label;
		GridData gd;
		Text text;

		IObservableValue target;
		IObservableValue field_model;

		groupComp = toolkit.createComposite(stack);
		groupComp.setLayout(new GridLayout(2, false));
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2,
		// 1));

		// addComboRoles(toolkit, ctx);

		label = toolkit.createLabel(groupComp, "�������� ������", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2,
				1));

		text = toolkit.createText(groupComp, "", SWT.BORDER | SWT.WRAP
				| SWT.MULTI);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		text.setLayoutData(gd);

		target = WidgetProperties.text(SWT.Modify).observe(text);
		field_model = BeanProperties.value(model.getClass(), "description")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

	private void pathField(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

		Label label;
		GridData gd;
		Text text;
		Composite comp;

		IObservableValue target;
		IObservableValue field_model;

		comp = toolkit.createComposite(parent);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		label = toolkit.createLabel(comp, "����:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		text = toolkit.createText(comp, "", SWT.WRAP | SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
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

	private void nameField(FormToolkit toolkit, DataBindingContext ctx,
			Composite parent) {

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

	private void addComboRoles(FormToolkit toolkit, DataBindingContext ctx) {

		Label label;
		GridData gd;

		IObservableValue target;
		IObservableValue field_model;

		label = toolkit.createLabel(groupComp, "����:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		combo = new ComboViewer(groupComp, SWT.READ_ONLY);
		combo.setContentProvider(ArrayContentProvider.getInstance());
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((UserInfo) element).getTitle();
			}
		});

		combo.setInput(App.srv.us().getBookRoles());

		toolkit.adapt(combo.getControl(), true, true);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		combo.getControl().setLayoutData(gd);

		IViewerObservableValue selected = ViewerProperties.singleSelection()
				.observe(combo);

		field_model = BeanProperties.value(model.getClass(), "role",
				UserInfo.class).observeDetail(dataValue);
		ctx.bindValue(selected, field_model);

		target = WidgetProperties.enabled().observe(combo.getCombo());
		field_model = BeanProperties.value(model.getClass(), "showRole")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

	}

	private void gap(FormToolkit toolkit) {
		Label label = toolkit.createLabel(form.getBody(), "", SWT.CENTER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

	}

}
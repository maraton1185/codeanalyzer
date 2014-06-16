package codeanalyzer.module.users.views;

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

import codeanalyzer.core.App;
import codeanalyzer.module.booksList.IBookListManager;
import codeanalyzer.module.users.interfaces.IUserManager;
import codeanalyzer.module.users.tree.UserInfo;
import codeanalyzer.utils.Events;

public class UserView {

	ScrolledForm form;
	WritableValue dataValue;
	// UserService us = new UserService();
	UserViewModel model = new UserViewModel(new UserInfo());

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
	public void EVENT_UPDATE_USER_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_USER_INFO) Object o,
			@Optional UserInfo data, IBookListManager bm,
			final EHandlerService hs, final ECommandService cs) {

		if (data == null) {
			// form.setText(Strings.get("UserViewTitle"));
			return;
		}

		model = new UserViewModel((UserInfo) App.srv.us().get(data.getId()));

		form.reflow(true);

		dataValue.setValue(model);

		dirty.setDirty(false);
	}

	@Persist
	public void save(IUserManager um, Shell shell) {
		if (model == null)
			return;

		if (um.save(model.getData(), shell))
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
		form.getBody().setLayout(new GridLayout(2, false));

		// »Ãﬂ *******************************************

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

		// œ¿–ŒÀ‹ *******************************************

		gap(toolkit);

		label = toolkit.createLabel(form.getBody(), "œ‡ÓÎ¸:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		text = toolkit.createText(form.getBody(), "", SWT.BORDER | SWT.WRAP
				| SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gd);
		text.setEchoChar('*');

		target = WidgetProperties.text(SWT.Modify).observe(text);
		field_model = BeanProperties.value(model.getClass(), "password")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		target = WidgetProperties.visible().observe(label);
		field_model = BeanProperties.value(model.getClass(), "group")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);
		target = WidgetProperties.visible().observe(text);
		field_model = BeanProperties.value(model.getClass(), "group")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		// Œœ»—¿Õ»≈ *******************************************

		label = toolkit.createLabel(form.getBody(), "ŒÔËÒ‡ÌËÂ:", SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));

		text = toolkit.createText(form.getBody(), "", SWT.BORDER | SWT.WRAP
				| SWT.MULTI);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		// gd.grabExcessVerticalSpace = true;
		text.setLayoutData(gd);
		// text.setEchoChar('*');

		target = WidgetProperties.text(SWT.Modify).observe(text);
		field_model = BeanProperties.value(model.getClass(), "description")
				.observeDetail(dataValue);
		ctx.bindValue(target, field_model);

		// *******************************************

		for (Object o : ctx.getBindings()) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}

		dirty.setDirty(false);

		App.br.post(Events.EVENT_BOOK_LIST_SET_SELECTION, null);
	}

	private void gap(FormToolkit toolkit) {
		Label label = toolkit.createLabel(form.getBody(), "", SWT.CENTER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

	}

}
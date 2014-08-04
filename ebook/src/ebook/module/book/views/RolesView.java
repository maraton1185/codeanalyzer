package ebook.module.book.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.core.App;
import ebook.module.acl.AclViewModel;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;
import ebook.utils.Utils;

public class RolesView {

	DataBindingContext ctx;
	FormToolkit toolkit;
	ScrolledForm form;

	Composite stack;
	StackLayout stackLayout;
	Composite groupComp;
	Composite itemComp;

	CheckboxTableViewer roles;

	SectionViewModel model = new SectionViewModel(null, null, null);

	@Inject
	@Active
	BookConnection book;

	// Integer book_id;

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_INFO) Object o,
			@Active @Optional SectionInfo data, final EHandlerService hs,
			final ECommandService cs) {
		if (data == null) {
			return;
		}

		model = new SectionViewModel(book, roles, book.srv().get(data.getId()));

		model.setRoles();

		ViewerSupport.bind(roles, BeansObservables.observeList(model, "roles",
				AclViewModel.class), BeanProperties.value(AclViewModel.class,
				"title"));

		ctx.bindSet(ViewersObservables.observeCheckedElements(roles,
				AclViewModel.class), BeansObservables.observeSet(model,
				"activeRoles", AclViewModel.class), new UpdateSetStrategy(
				UpdateSetStrategy.POLICY_NEVER), new UpdateSetStrategy(
				UpdateSetStrategy.POLICY_UPDATE));

		// dataValue.setValue(model);
		//
		if (data.isGroup())
			stackLayout.topControl = groupComp;
		else
			stackLayout.topControl = itemComp;
		stack.layout();
		itemComp.layout(true);
		groupComp.layout(true);
		form.reflow(true);

		// dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		// book_id = book.getTreeItem().getId();

		ctx = new DataBindingContext();

		// ********************************************
		parent.setLayout(new FillLayout());
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.getBody().setLayout(new GridLayout(2, false));

		// ÑÒÅÊ *******************************************

		stack = toolkit.createComposite(form.getBody());
		stackLayout = new StackLayout();
		stack.setLayout(stackLayout);

		stack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		// ÏÎËß ÃÐÓÏÏ *******************************************
		groupFields();

		// ÏÎËß ÝËÅÌÅÍÒÎÂ *******************************************
		itemFields();

		App.br.post(Events.EVENT_UPDATE_SECTION_INFO, null);

	}

	private void itemFields() {
		itemComp = toolkit.createComposite(stack);
		itemComp.setLayout(new GridLayout(2, false));

	}

	private void groupFields() {
		Label label;

		groupComp = toolkit.createComposite(stack);
		groupComp.setLayout(new GridLayout(2, false));
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2,
		// 1));

		GridData gd;

		// IObservableValue target;
		// IObservableValue field_model;

		label = toolkit.createLabel(groupComp, "Äîñòóï ïî ðîëÿì:", SWT.LEFT);
		GridDataFactory.fillDefaults().applyTo(label);
		// label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
		// 2,
		// 1));

		Composite panel = toolkit.createComposite(groupComp);
		panel.setLayout(new RowLayout());
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.grabExcessHorizontalSpace = true;
		panel.setLayoutData(gd);

		ImageHyperlink hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("set.png"));
		hlink.setToolTipText("Óñòàíîâèòü ôëàæêè");
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				roles.setAllChecked(true);
				model.setActiveRoles(roles.getCheckedElements());
			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("unset.png"));
		hlink.setToolTipText("Ñíÿòü ôëàæêè");

		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				roles.setAllChecked(false);
				model.setActiveRoles(roles.getCheckedElements());
				model.setRoles();
			}
		});

		// Composite rolesComposite = new Composite(groupComp, SWT.NONE);
		// toolkit.adapt(rolesComposite, true, true);
		Composite rolesComposite = toolkit.createComposite(groupComp);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 1;
		// gd.heightHint = 150;
		gd.horizontalSpan = 2;
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
		titleColumn.setText("Ðîëü");
		rolesColumnLayout.setColumnData(titleColumn, new ColumnWeightData(1));

		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(roles.getTable());

		toolkit.adapt(roles.getControl(), true, true);

	}

}
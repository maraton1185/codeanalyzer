package ebook.module.book.views.section;

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
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.module.acl.AclViewModel;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;

public class SectionRolesView {

	DataBindingContext ctx;
	FormToolkit toolkit;
	ScrolledForm form;

	CheckboxTableViewer roles;

	SectionViewModel model = new SectionViewModel(null, null);

	@Inject
	@Active
	BookConnection book;

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_INFO) Object o,
			@Optional SectionInfo data, final EHandlerService hs,
			final ECommandService cs) {
		if (data == null) {
			return;
		}

		model = new SectionViewModel(roles, book.srv().get(data.getId()));

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
		// if (model.isGroup())
		// stackLayout.topControl = groupComp;
		// else
		// stackLayout.topControl = itemComp;
		// stack.layout();
		// itemComp.layout(true);
		// groupComp.layout(true);
		// form.reflow(true);
		//
		// dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		ctx = new DataBindingContext();

		// ********************************************
		parent.setLayout(new FillLayout());
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.getBody().setLayout(new GridLayout(2, false));

	}

}
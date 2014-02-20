package codesnippetapp.views;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class test {
	private Text text;
	private Text text_1;
	private Text text_2;

	public test() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);

		lblNewLabel.setText("Snippet Name:");
		
		text = new Text(composite, SWT.BORDER);

		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);

		lblNewLabel_1.setText("New Label");
		
		text_1 = new Text(composite, SWT.BORDER);

		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);

		lblNewLabel_2.setText("New Label");
		
		text_2 = new Text(composite, SWT.BORDER);

		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("New Button");

	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
}

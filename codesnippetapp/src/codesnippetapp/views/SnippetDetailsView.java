 
package codesnippetapp.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import codesnippetapp.CodeSnippetAppConstants;
import codesnippetapp.data.SnippetData;

public class SnippetDetailsView {
	
	
	private Text snippetText, codeText, descText;
	private Button saveButton;
	private SnippetData snippet; //Currently displayed snippet data
	
	@Inject
	IEventBroker broker;
	
	@Inject
	public SnippetDetailsView() {
		//TODO Your code here
	}
	
	@Inject	@Optional
	public void onSelectSnippet(@UIEventTopic(CodeSnippetAppConstants.SELECT_SNIPPET_EVENT)SnippetData snippet)
	{
//		snippetText.setText(data.toString());		
//		codeText.setText(data.code);
//		descText.setText(data.description);
		if (snippet != null && snippet.name != null)
			snippetText.setText(snippet.name);
		else
			snippetText.setText("");
		if (snippet != null && snippet.code != null)
			codeText.setText(snippet.code);
		else
			codeText.setText("");
		if (snippet != null && snippet.description != null)
			descText.setText(snippet.description);
		else
			descText.setText("");
		this.snippet= snippet;
		if (snippet != null)
		{
			saveButton.setEnabled(true);
			snippetText.setFocus();
			int caretPos = snippetText.getText().length();
			snippetText.setSelection(caretPos, caretPos);
		}
		else
			saveButton.setEnabled(false);
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
				
		Composite snippetDetailsComposite = new Composite(parent, SWT.None);
		//We will use GridLayout with two columns.
		//The second argument to GridLayout constructor below
		//is a flag to indicate if columns should be of eual width
		snippetDetailsComposite.setLayout(new GridLayout(2, false));
		
		GridData gridData;
		//Create label and text box for Snippet Name
		Label snippetLabel = new Label(snippetDetailsComposite, SWT.None);
		snippetLabel.setText("Snippet Name:");
		snippetText = new Text(snippetDetailsComposite, SWT.BORDER);
		//Make snippet text box to grab horizontal space
		gridData = new GridData(GridData.GRAB_HORIZONTAL);
		gridData.horizontalAlignment = SWT.FILL;
		snippetText.setLayoutData(gridData);
		
		//Create label and text box for Snippet Code
		Label codeLabel = new Label(snippetDetailsComposite, SWT.None);
		codeLabel.setText("Code:");
		//We want code text box to have multiple lines. 
		//So use SWT.MULTI style
		codeText = new Text(snippetDetailsComposite,SWT.MULTI 
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		//Make codeText text box to grab horizontal and vertical space
		gridData = new GridData(GridData.GRAB_HORIZONTAL 
				| GridData.GRAB_VERTICAL);
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		codeText.setLayoutData(gridData);
		
		//Create label and text box for description
		Label descLabel = new Label(snippetDetailsComposite, SWT.None);
		descLabel.setText("Description:");
		descText = new Text(snippetDetailsComposite, SWT.MULTI 
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		//Make descText to grab horizontal space
		gridData = new GridData(GridData.GRAB_HORIZONTAL);
		gridData.horizontalAlignment = SWT.FILL;
		//Set height of descText to 60 pixels
		gridData.heightHint = 60;
		descText.setLayoutData(gridData);
		
		//Create Save button
		saveButton = new Button(snippetDetailsComposite, SWT.BORDER);
		saveButton.setText("Save");
		saveButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				snippet.name = snippetText.getText();
				snippet.code = codeText.getText();
				snippet.description = descText.getText();
				broker.post(CodeSnippetAppConstants.SAVE_SNIPPET_EVENT, null);
			}
			
		});
	}
	
	
	
	
}
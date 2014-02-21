 
package codeanalyzer.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BookmarkView {
	@Inject
	public BookmarkView() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("New Label");
	}
	
	
	
	
}
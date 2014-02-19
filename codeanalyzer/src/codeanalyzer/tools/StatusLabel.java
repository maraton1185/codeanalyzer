package codeanalyzer.tools;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;

public class StatusLabel {
	  @PostConstruct
	  public void createControls(Composite parent) {
	    final Composite comp = new Composite(parent, SWT.NONE);
	    comp.setLayout(new RowLayout(SWT.HORIZONTAL));
	    
	    Label lblNewLabel = new Label(comp, SWT.BORDER);
//	    lblNewLabel.setBounds(0, 0, 224, 298);
	    lblNewLabel.setText("home1");

	  }
	} 
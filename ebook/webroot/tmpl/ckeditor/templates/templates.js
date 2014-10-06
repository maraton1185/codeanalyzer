// Register a template definition set named "default".
CKEDITOR.addTemplates( 'default',
{
	// The name of the subfolder that contains the preview images of the templates.
	//imagesPath : CKEDITOR.getUrl( CKEDITOR.plugins.getPath( 'templates' ) + 'templates/images/' ),
 
	// Template definitions.
	templates :
		[
			{
				title: 'Документ',
				description: 'Шаблон описания документа.',
				html:
					'<h2>Template 1</h2>' +
					'<p><img src="/logo.png" style="float:left" />Type your text here.</p>'
			},
			{
				title: 'My Template 2',
				html:
					'<h3>Template 2</h3>' +
					'<p>Type your text here.</p>'
			}
		]
});

CKEDITOR.editorConfig = function( config )
{
	config.toolbar = 'MyToolbar';
 	config.language = 'ru';
	config.toolbar_MyToolbar =
	[
		{ name: 'document', items : [ 'Source', 'Templates'] },
		{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
		{ name: 'editing', items : [ 'Find','Replace','-','SelectAll' ] },
		{ name: 'insert', items : ['Table','HorizontalRule','Smiley','SpecialChar'] },
                '/',
//		{ name: 'styles', items : [ 'Format' ] },
		{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','-','RemoveFormat' ] },
		{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'] },
		{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
		{ name: 'styles', items: [ 'Format', 'Font', 'FontSize' ] },
		{ name: 'colors', items: [ 'TextColor', 'BGColor' ] },
			{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
//			{ name: 'tools', items : [ 'Maximize','-','About' ] }
	];

	config.templates_files = [ ];

	
};
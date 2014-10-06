package ebook.web.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ebook.utils.PreferenceSupplier;
import ebook.utils.Utils;

public class EditorController {

	public String getModel() {

		String folder = PreferenceSupplier
				.get(PreferenceSupplier.EDITOR_TEMPLATES_FOLDER);

		File tmpl_dir = new File(folder);

		String tmpl = "";
		if (!tmpl_dir.exists())
			return "";

		if (!tmpl_dir.isDirectory())
			return "";

		File[] files = new File(folder).listFiles();

		tmpl = "<script type=\"text/javascript\"> CKEDITOR.addTemplates( 'default',{templates:[";

		String sText = "";
		for (File f : files) {

			sText = "";

			try (BufferedReader br = new BufferedReader(new FileReader(
					f.getAbsolutePath()))) {

				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					sText += "'" + sCurrentLine + "'+";
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			tmpl += "{title: '" + Utils.getFileName(f) + "',html:" + sText
					+ "''},";
		}

		tmpl += "]});</script>";

		return tmpl;
	}
}

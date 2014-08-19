package ebook.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {
	List<String> fileList;
	private String FOLDER;

	ZipHelper(String SOURCE_FOLDER) {
		fileList = new ArrayList<String>();
		this.FOLDER = SOURCE_FOLDER;
	}

	public static void zip(String SOURCE_FOLDER, String OUTPUT_ZIP_FILE)
			throws IOException {

		ZipHelper appZip = new ZipHelper(SOURCE_FOLDER);
		appZip.generateFileList(new File(SOURCE_FOLDER));
		appZip.zipIt(OUTPUT_ZIP_FILE);
	}

	public static void unzip(String INPUT_ZIP_FILE, String OUTPUT_FOLDER) {
		ZipHelper unZip = new ZipHelper(OUTPUT_FOLDER);
		unZip.unZipIt(INPUT_ZIP_FILE);
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output folder
	 */
	public void unZipIt(String zipFile) {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			File folder = new File(FOLDER);
			if (!folder.exists()) {
				folder.mkdir();
			}
			folder.deleteOnExit();

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(FOLDER + File.separator + fileName);
				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				File dirs = new File(newFile.getParent());
				dirs.mkdirs();
				dirs.deleteOnExit();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				newFile.deleteOnExit();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			// //System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Zip it
	 * 
	 * @param zipFile
	 *            output ZIP file location
	 * @throws IOException
	 */
	public void zipIt(String zipFile) throws IOException {

		byte[] buffer = new byte[1024];

		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);

		// //System.out.println("Output to Zip : " + zipFile);

		for (String file : this.fileList) {

			// System.out.println("File Added : " + file);
			ZipEntry ze = new ZipEntry(file);
			zos.putNextEntry(ze);

			FileInputStream in = new FileInputStream(FOLDER + File.separator
					+ file);

			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			in.close();
		}

		zos.closeEntry();
		// remember close it
		zos.close();

		// //System.out.println("Done");

	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public void generateFileList(File node) {

		node.deleteOnExit();
		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}

	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private String generateZipEntry(String file) {
		return file.substring(FOLDER.length() + 1, file.length());
	}
}
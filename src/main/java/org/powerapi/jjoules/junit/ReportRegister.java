/**
 * 
 */
package org.powerapi.jjoules.junit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;

/**
 * @author spirals
 *
 */
public class ReportRegister{

	private String filename;

	public void setFilename(String newFilename) {
		this.filename = newFilename;
	}

	
	public void jsonRegistreReport(Map<String, Long> reports) {


		File reportsDir = new File("target","jjoules-reports");

		if(! reportsDir.exists()) {
			reportsDir.mkdir();
		}

		Gson gson = new Gson();

		File file = new File(reportsDir,filename+".json");

		if(! file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			gson.toJson(gson.toJsonTree(reports),fw);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

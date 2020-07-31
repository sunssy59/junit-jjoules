/**
 * 
 */
package org.powerapi.jjoules.callgraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author spirals
 *
 */
public class CallGraph {
	
	private static boolean state = false;
	
	private static String CALLGRAPH_DIR = "target/call-graphs";
	
	public static void createDirectory() {
		File callgraphDir = new File(CALLGRAPH_DIR);
		callgraphDir.mkdir();
	}
	
	public static void generateJarFile(String className) throws Exception {
		Process proc = null;
		
		String[] classNameSplited = className.split("\\.");
		String filename = classNameSplited[classNameSplited.length - 1]; 
		String classPathName = "target/test-classes/" + className.replace('.', '/') + ".class";
		
		String jarcmd = "jar cvf "+ filename + ".jar "+ classPathName;
		String mvcmd = "mv " + filename + ".jar target/";
		try {
			proc = Runtime.getRuntime().exec(jarcmd);
			if (proc.exitValue() == 0) {
				Runtime.getRuntime().exec(mvcmd);
				generateCallGraphFile("target/" + filename + ".jar",filename + ".txt");
			}
				
			else
				throw new Exception("error => jar / cvf");
		} catch(IOException e1) {
			
		}
		
	}
	
	public static void generateCallGraphFile(String jarFilename, String outputFilename) {
		
		String callgraphcmd = "java -jar javacg-0.1-SNAPSHOT-static.jar " + jarFilename;
		try {
			Process proc = Runtime.getRuntime().exec(callgraphcmd);
			readProcessus(proc,outputFilename);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static void readProcessus(Process p, String outputFilename) throws IOException {
    	
    	createDirectory();
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        File file = new File(CALLGRAPH_DIR + "/" + outputFilename);
        
        if (! file.exists()) {
        	file.createNewFile();
        }
        
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
      
        while ((line = reader.readLine()) != null) {
            bw.write(line+"\n");
        }
        
        bw.close();
        fw.close();
        
        reader.close();
    }
	
	
	
}

/**
 * 
 */
package org.powerapi.jjoules.callgraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * 
 *
 */
@Mojo(name = "generate-matrix",
defaultPhase = LifecyclePhase.TEST)
public class CallGraphMojo extends AbstractMojo{

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;
	
	@Parameter(defaultValue = "${project.build.directory}/call-graph", required = true)
	private String outputDir;

	@Parameter(defaultValue = "callgraph-matrix.txt" , required = true)
	private String outputFilename;

	@Parameter(defaultValue = "tests-callgraph.txt", required = true)
	private String callgraphFilename;

	private Map<String,Collection<String>> methodsCallgraph;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		File outputDirFile = new File(outputDir);
		if (! outputDirFile.exists())
			outputDirFile.mkdir();

		File outputFile = new File(outputDir,outputFilename);
		File callgraphFile = new File(outputDir,callgraphFilename);

		this.executes(callgraphFile,outputFile);
	}

	private void executes(File callgraphFile, File outputFile) {

		getLog().info("Read tests call graph in file {"+callgraphFilename+"} ...");
		this.readCallgraphFile(callgraphFile);

		getLog().info("Create call graph matrix and dump it in file {"+outputFilename+"} ...");
		this.createCallgraphMatrix(outputFile);
	}

	private void readCallgraphFile(File callgraphFile) {
		this.methodsCallgraph = new HashMap<String,Collection<String>>();

		FileReader fr;
		String line = "";

		try {
			fr = new FileReader(callgraphFile);
			BufferedReader br = new BufferedReader(fr);
			String[] lineSplited;

			while((line = br.readLine()) != null) {
				lineSplited = line.split(" ");

				String src = lineSplited[0];
				String tgt = lineSplited[1];

				if (src.startsWith("M") && 
						(tgt.contains(project.getGroupId())) &&
						(tgt.startsWith(("(M")) || 
								tgt.startsWith(("(S")) )) 
				{	
					String key = src.substring(2, src.length());
					String value = tgt.substring(3, tgt.length());
					if (! this.methodsCallgraph.containsKey(key)) 
						this.methodsCallgraph.put(key, new ArrayList<String>());

					this.methodsCallgraph.get(key).add(value);
				}
			}
		} catch (IOException e) {
			getLog().error("You may have to move your file {"+callgraphFilename+"} in directory {"+outputDir+"}");
		}
	}

	private void createCallgraphMatrix(File outputFile) {

		FileWriter fw;
		try {

			fw = new FileWriter(outputFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// keys -> tests methods
			Set<String> keys = this.methodsCallgraph.keySet();

			// values -> all methods called in tests methods
			Collection<String> values = new HashSet<String>();

			for(Collection<String> val : this.methodsCallgraph.values()) {
				values.addAll(val);
			}

			// writing first line (tests methods)
			for(String key : keys){
				bw.write(key + " ");
			}
			bw.newLine();

			// writing all values lines
			for(String value : values) {
				bw.write(value + " ");
				for(String key : keys) {
					if(this.methodsCallgraph.get(key).contains(value))
						bw.write("true ");
					else
						bw.write("false ");
				}
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}     
	}	
}

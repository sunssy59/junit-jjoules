/**
 * 
 */
package org.powerapi.junitjjoules.mesureit;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.powerapi.jjoules.energy.display.EnergyDisplayHandler;
import org.powerapi.jjoules.energy.display.EnergyPrinter;
import org.powerapi.jjoules.energy.display.EnergyRegisterCSV;
import org.powerapi.jjoules.energy.display.EnergyRegisterJson;
import org.powerapi.jjoules.energy.display.utils.Result;
import org.powerapi.jjoules.energy.domain.EnergyDomain;
import org.powerapi.jjoules.energy.domain.rapl.RaplPackageDomain;

/**
 * @author sanoussy
 *
 */
public class EnergyMesureExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback,BeforeAllCallback {
	
	public static boolean FIRST_TEST_PASSED = false;
	private static int NB_TESTS = 0;
	private static int NB_TESTS_PASSED = 0;

	private static final Logger LOGGER = Logger.getLogger(EnergyMesureExtension.class.getName());
	private static final EnergyDomain domain = new RaplPackageDomain(0);
	private static EnergyMesureIt ENERGY_MESURE_IT = new EnergyMesureIt(domain);
	private static Map<String,Result> resultEnergyConsumed;
	
	public void getNbTestsClasses(File classesDir) {
		  File currentFile = classesDir;
		  File[] allFiles = currentFile.listFiles();
		  for(File file : allFiles) {
			  if(file.exists()) {
				  if(file.isFile()) {
					  NB_TESTS++;
				  }else if(file.isDirectory()) {
					  getNbTestsClasses(file);
				  }
			  }

		  }
	}
	
	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		NB_TESTS_PASSED++;
		if(!FIRST_TEST_PASSED) {
			getNbTestsClasses(new File("src/test"));
			FIRST_TEST_PASSED = true;
			//EnergyDisplayHandler.initDir();
		}
			
		EnergyMesureIt.ENERGY_MESURE_IT.setEnergyDomain(domain);
		resultEnergyConsumed =  new HashMap<String,Result>();
	}
	
	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		ENERGY_MESURE_IT.begin();
		getMethodStore(context).put(LaunchEnergyKey.METHOD_TEST_ENERGY,ENERGY_MESURE_IT.getEnergyBefore());
		getMethodStore(context).put(LaunchEnergyKey.METHOD_TEST_DURATION,System.currentTimeMillis());
	}
	
	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		Method testMethod = context.getRequiredTestMethod();
		double startEnergy = getMethodStore(context).remove(LaunchEnergyKey.METHOD_TEST_ENERGY, double.class);
		long startTime = getMethodStore(context).remove(LaunchEnergyKey.METHOD_TEST_DURATION, long.class);
		double end = ENERGY_MESURE_IT.end();
		long duration = System.currentTimeMillis() - startTime;
		
		// end can be replace by ENERGY_MESURE_IT.getEnergyAfter()-startEnergy
		this.resultEnergyConsumed.put(testMethod.getName(), new Result(end,duration));
		
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		// printing result
		EnergyPrinter.ENERGY_PRINTER.ALL_RESULTS = resultEnergyConsumed;
		EnergyPrinter.ENERGY_PRINTER.displayIt();
		
		String className = context.getRequiredTestClass().getSimpleName();
		
		//saving result in CSV intermediate register 
		EnergyRegisterCSV.CURRENT_CLASS_NAME = className;
		LOGGER.info("Saving result in CSV intermediate register for test => "+ className);
		EnergyDisplayHandler.saveResultOfClass(resultEnergyConsumed,EnergyRegisterCSV.CURRENT_CLASS_NAME,EnergyRegisterCSV.ALL_DATA);
		
		
		//saving result in JSON intermediate register 
		EnergyRegisterJson.CURRENT_CLASS_NAME = className;
		LOGGER.info("Saving result in JSON intermediate register for test => "+ className);
		EnergyDisplayHandler.saveResultOfClass(resultEnergyConsumed,EnergyRegisterJson.CURRENT_CLASS_NAME,EnergyRegisterJson.ALL_DATA);
		
		// saving all result in CSV and JSON files if all tests are finished;
		if(NB_TESTS == NB_TESTS_PASSED) {
			LOGGER.info("Saving result in CSV file in : "+EnergyRegisterCSV.ENERGY_REGISTER_CSV.getFileName());
			EnergyRegisterCSV.ENERGY_REGISTER_CSV.displayIt();
			LOGGER.info("Saving result in JSON file in : "+EnergyRegisterJson.ENERGY_REGISTER_Json.getFileName());
			EnergyRegisterJson.ENERGY_REGISTER_Json.displayIt();
		}
	}
	
	private Store getMethodStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
    }
	
	private Store getClassStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestClass()));
    }

	private enum LaunchEnergyKey {
		METHOD_TEST_ENERGY, CLASS_TEST_ENERGY,
		METHOD_TEST_DURATION, CLASS_TEST_DURATION
	}

}

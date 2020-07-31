/**
 * 
 */
package org.powerapi.jjoules.junit;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.powerapi.jjoules.EnergySample;
import org.powerapi.jjoules.callgraph.CallGraph;
import org.powerapi.jjoules.rapl.RaplDevice;

/**
 * JUnit extension for logging the energy consumption of tests.
 *
 */
public class EnergyTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback{
	private final Namespace NAMESPACE = Namespace.create(getClass());

	private final Store getStore(final ExtensionContext context) {
		return context.getStore(this.NAMESPACE);
	}

	@Override
	public void beforeTestExecution(final ExtensionContext context) throws Exception {
		getStore(context).put(context.getRequiredTestMethod(), RaplDevice.RAPL.recordEnergy());
	}

	@Override
	public void afterTestExecution(final ExtensionContext context) throws Exception {
		Map<String, Long> report = getStore(context).get(context.getRequiredTestMethod(), EnergySample.class).stop();
		for (Entry<String, Long> value : report.entrySet()) {
			context.publishReportEntry(value.getKey(), value.getValue().toString());
		}
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		CallGraph.generateJarFile(context.getRequiredTestClass().getName());
		
	}
}

/**
 * 
 */
package org.powerapi.junitjjoules.mesureit;

import org.powerapi.jjoules.energy.domain.EnergyDomain;

/**
 * class for mesuring an energy with two methods <em>begin</em> and <em>end</em>
 * @author sanoussy
 *
 */
public class EnergyMesureIt {
	
	public static final EnergyMesureIt ENERGY_MESURE_IT = new EnergyMesureIt();
	
	private double energyBefore;
	private double energyAfter;
	
	private EnergyDomain domain;
	
	private EnergyMesureIt() {
	}
	
	public EnergyMesureIt(EnergyDomain domain) {
		this.domain = domain;
	}
	
	public EnergyDomain getEnergyDomain() {
		return this.domain;
	}
	public void setEnergyDomain(EnergyDomain newDomain) {
		this.domain = newDomain;
	}
	/**
	 * @return the energy consumed before checking 
	 */
	public double getEnergyBefore() {
		return this.energyBefore;
	}
	
	/**
	 * @return the energy consumed after checking 
	 */
	public double getEnergyAfter() {
		return this.energyAfter;
	}
	/**
	 * 
	 */
	public void begin() {
		this.energyBefore = this.domain.getEneregyConsumed();
	}
	/**
	 * 
	 */
	public double end() {
		this.energyAfter = this.domain.getEneregyConsumed();
		return this.getEnergyAfter() - this.getEnergyBefore();
	}

}

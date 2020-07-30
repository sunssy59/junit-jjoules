/**
 * 
 */
package org.powerapi.junitjjoules.mesureit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerapi.jjoules.energy.domain.EnergyDomain;

/**
 * @author sanoussy
 *
 */
public class EnergyMesureItTest {
	
	private MokeEnergyDomain mokeDomain;
	
	@BeforeEach
	public void init() {
		this.mokeDomain = new MokeEnergyDomain();
		EnergyMesureIt.ENERGY_MESURE_IT.setEnergyDomain(mokeDomain);
	}
	

	/**
	 * Test method for {@link org.powerapi.junitjjoules.mesureit.EnergyMesureIt#getEnergyDomain()}.
	 */
	@Test
	public void testGetEnergyDomain() {
		assertThat(this.mokeDomain.getEneregyConsumed()).isEqualTo(59);
	}

	/**
	 * Test method for {@link org.powerapi.junitjjoules.mesureit.EnergyMesureIt#begin()}.
	 */
	@Test
	public void testBegin() {
		EnergyMesureIt.ENERGY_MESURE_IT.begin();
		assertThat(this.mokeDomain.getEnergyConsumedCalled).isTrue();
	}

	/**
	 * Test method for {@link org.powerapi.junitjjoules.mesureit.EnergyMesureIt#end()}.
	 */
	@Test
	public void testEnd() {
		EnergyMesureIt.ENERGY_MESURE_IT.begin();
		assertThat(this.mokeDomain.getEnergyConsumedCalled).isTrue();
	}
	
	
	class MokeEnergyDomain extends EnergyDomain{
		
		protected boolean getEnergyConsumedCalled = false;

		@Override
		public String getDeviceType() {
			return null;
		}

		@Override
		public double getEneregyConsumed() {
			getEnergyConsumedCalled = true;
			return 59;
		}

		@Override
		public String getDomainName() {
			return "MokedDomain";
		}

		@Override
		public String domainPath() {
			return null;
		}
		
	}

}

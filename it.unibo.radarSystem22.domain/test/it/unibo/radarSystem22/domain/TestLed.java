package it.unibo.radarSystem22.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.mock.LedMock;
 


public class TestLed {
	
	@Test 
	public void testLedMockOn() {
		System.out.println("testLedMockOn");
		
		ILed led =  new LedMock();
		led.turnOn();
		
		assertTrue(led.getState());	
		
	}	
	
	@Test
	public void testLedMockOff() {
		System.out.println("testLedMockOff");
		
		ILed led =  new LedMock();
		led.turnOff();
		
		assertTrue(!led.getState());	
	}
 
}

package com.sap.ewm.core.sapjco;

import com.sap.conn.jco.*;

public class JcoClient {

	private String destination;

	public JcoClient( String destination ){
		this.destination = destination;
	}

	public JCoDestination getDestination(){
		JCoDestination jcoDestination = null;
		
		try {
			jcoDestination = JCoDestinationManager.getDestination( destination );
		} catch (JCoException e) {
			e.printStackTrace();
		}
		return jcoDestination;
	}
	
	public JCoRepository getJCoRepository() {
		JCoRepository repository = null;
		JCoDestination jcoDestination = getDestination();
		synchronized (jcoDestination) {
			try {
				repository = jcoDestination.getRepository();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return repository;
	} 
	
	public JCoFunction getFunction( String rfcName ){
		JCoFunction jcoFunction = null;
		JCoRepository repository = null;
		repository = getJCoRepository();
		try {
			jcoFunction = repository.getFunction( rfcName );
		} catch (JCoException e) {
			e.printStackTrace();
		}
		
		return jcoFunction;
	}
}

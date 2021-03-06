package KeepAway;

import java.util.Vector;

import kaMessages.KAStateResponse;

public class VizLogger {
	Vector<String> latestLogs=null;
	
	public VizLogger(){
		latestLogs=new Vector<String>();
	}
	
	public Vector<String> getLogs(){
		return latestLogs;
	}
	
	public void newLogs(){
		latestLogs=new Vector<String>();
	}
	
	public void addToLog(SoccerPitch P){
		String thisSerializedMoment=KAStateResponse.makePayLoadFor(P);
		latestLogs.add(thisSerializedMoment);
	}
}

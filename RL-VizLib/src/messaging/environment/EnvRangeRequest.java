package messaging.environment;

import java.util.Vector;

import messaging.AbstractMessage;
import messaging.GenericMessage;
import messaging.MessageUser;
import messaging.MessageValueType;
import messaging.NotAnRLVizMessageException;
import rlglue.Environment;
import rlglue.RLGlue;
import visualization.interfaces.getEnvMaxMinsInterface;
import visualization.interfaces.getEnvObsForStateInterface;

public class EnvRangeRequest extends EnvironmentMessages{

	public EnvRangeRequest(GenericMessage theMessageObject) {
		super(theMessageObject);
	}

	public static EnvRangeResponse Execute(){
		String theRequest=AbstractMessage.makeMessage(
				MessageUser.kEnv.id(),
				MessageUser.kBenchmark.id(),
				EnvMessageType.kEnvQueryVarRanges.id(),
				MessageValueType.kNone.id(),
		"NULL");

		String responseMessage=RLGlue.RL_env_message(theRequest);

		EnvRangeResponse theResponse;
		try {
			theResponse = new EnvRangeResponse(responseMessage);
		} catch (NotAnRLVizMessageException e) {
			System.err.println("In EnvRangeRequest, the response was not RL-Viz Compatible");
			theResponse = null;
		}

		return theResponse;

	}

	@Override
	public boolean canHandleAutomatically(Object theReceiver) {
		return (theReceiver instanceof getEnvMaxMinsInterface);
	}

	@Override
	public String handleAutomatically(Environment theEnvironment) {
		
		getEnvMaxMinsInterface castedEnv = (getEnvMaxMinsInterface)theEnvironment;
		//			//Handle a request for the ranges
		Vector<Double> mins = new Vector<Double>();
		Vector<Double> maxs = new Vector<Double>();

		int numVars=castedEnv.getNumVars();

		for(int i=0;i<numVars;i++){
			mins.add(castedEnv.getMinValueForQuerableVariable(i));
			maxs.add(castedEnv.getMaxValueForQuerableVariable(i));
		}

		EnvRangeResponse theResponse=new EnvRangeResponse(mins, maxs);

		return theResponse.makeStringResponse();

	}

}

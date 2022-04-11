package unibo.actor22.common;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.ProtocolType;

public class ApplData {
	public static final String raspAddr = "localhost";

	public static final String ledName = "led";
	public static final String sonarName = "sonar";
	public static final String controllerName = "controller";
	public static final String observerName = "observer";

	public static final String comdLedon = "turnOn";
	public static final String comdLedoff = "turnOff";
	public static final String reqLedState = "getState";

	public static final String cmdActivate = "activate";
	public static final String cmdDectivate = "deactivate";
	public static final String reqSonarDistance = "getDistance";
	public static final String reqSonarActive = "isActive";

	public static final String evEndWork = "endWork";
	public static final String evDistance = "distance";

	public static final int ctxPort = 8018;
	public static final ProtocolType protocol = ProtocolType.tcp;

	public static final IApplMessage turnOnLed = Qak22Util.buildDispatch(controllerName, "cmd", comdLedon, ledName);
	public static final IApplMessage turnOffLed = Qak22Util.buildDispatch(controllerName, "cmd", comdLedoff, ledName);

	public static final IApplMessage activateCrtl = Qak22Util.buildDispatch("main", "cmd", cmdActivate, controllerName);
	public static final IApplMessage activateSonar = Qak22Util.buildDispatch(controllerName, "cmd", cmdActivate,
			sonarName);
	public static final IApplMessage deactivateSonar = Qak22Util.buildDispatch(controllerName, "cmd", cmdDectivate,
			sonarName);
	public static final IApplMessage isActiveSonar = Qak22Util.buildRequest(controllerName, "cmd", reqSonarActive, sonarName);
	public static final IApplMessage getSonarDistance = Qak22Util.buildRequest(controllerName, "cmd", reqSonarDistance,
			sonarName);

	public static final IApplMessage endWorkEvent = Qak22Util.buildEvent(controllerName, evEndWork, evEndWork);

}

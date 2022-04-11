package unibo.actor22.common;

import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Util;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

/*
 * Il controller conosce SOLO I NOMI dei dispositivi 
 * (non ha riferimenti ai dispositivi-attori)
 */
public class ControllerActor extends QakActor22 {
	protected int numIter = 1;
	protected IApplMessage getStateRequest;
	protected boolean on = true;
	private static final int nIteractions = 100;
	private static final int timeBetweenIteractions = 100;
	protected boolean sonarActive = false;
	protected boolean ledRequiredState;
	protected IDistance dist = null;

	public ControllerActor(String name) {
		super(name);
		getStateRequest = Qak22Util.buildRequest(name,"ask", ApplData.reqLedState, ApplData.ledName);
	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		if (msg.isReply()) {
			elabAnswer(msg);
		} else {
			elabCmd(msg);
		}
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl(getName() + " | elabCmd=" + msgCmd, ColorsOut.GREEN);
		switch (msgCmd) {
		case ApplData.cmdActivate: {
			start();
			break;
		}
		default:
			break;
		}
	}

	protected void wrongBehavior() {
		// WARNING: Inviare un treno di messaggi VA EVITATO
		// mantiene il controllo del Thread degli attori (qaksingle)
		for (int i = 1; i <= 3; i++) {
			forward(ApplData.turnOffLed);
			CommUtils.delay(500);
			forward(ApplData.turnOnLed);
			CommUtils.delay(500);
		}
		forward(ApplData.turnOffLed);
	}

	protected void doControllerWork(IApplMessage msg) {
		// Controllo che sonar è attivo e nel caso ottengo la distanza
		if (numIter == 0) {
			if (msg.msgSender() == ApplData.sonarName) {
				if (msg.msgContent() == "false") {
					ColorsOut.outappl(getName() + " | sonar initialization failed ", ColorsOut.RED);
					stop();
				}
				numIter = 1;
				sonarActive = true;
				request(ApplData.getSonarDistance);
			} else {
				ColorsOut.outappl(getName() + " | AAunexpected message ", ColorsOut.RED);
			}
		}
		// Una volta fatta inizializzazione procedo come previsto
		else if (numIter >= 0) {
			if (sonarActive) {
				if (msg.msgSender() == ApplData.sonarName) {
					if (numIter <= nIteractions) {
						numIter++;
						dist = new Distance(msg.msgContent());

						if (dist.getVal() < DomainSystemConfig.DLIMIT) {
							forward(ApplData.turnOnLed); // accesione
							ledRequiredState = true;
						}
//								else {
//									ledRequiredState = false;
//									forward( ApplData.turnOffLed ); //spegnimento
//								}
						request(getStateRequest);
					} else
						stop();
				} else if (msg.msgSender() == ApplData.ledName) {
					String LedState = msg.msgContent();
					if (LedState.equals("" + ledRequiredState)) {
						ColorsOut.outappl(
								"Controller | distance=" + dist + " iteration=" + numIter + " led=" + LedState,
								ColorsOut.GREEN);
						BasicUtils.delay(timeBetweenIteractions);
						request(ApplData.getSonarDistance);
					} else {
						ColorsOut.outappl(getName() + " | error led state ", ColorsOut.RED);
						stop();
					}
				} else {
					ColorsOut.outappl(getName() + " | unexpected message ", ColorsOut.RED);
				}
			}
		}

	}

	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl(getName() + " | elabAnswer numIter=" + numIter + " " + msg, ColorsOut.MAGENTA);
		//CommUtils.delay(500);
		doControllerWork(msg);
	}

	protected void start() {
		if (numIter == -1) {
			CommUtils.aboutThreads(getName() + " |  Before doControllerWork on=" + on);
			forward(ApplData.turnOffLed);
			forward(ApplData.activateSonar);
			request(ApplData.isActiveSonar);
			numIter = 0;
		}
	}

	protected void stop() {
		ColorsOut.outappl(getName() + " | Sonar Off", ColorsOut.YELLOW);
		forward(ApplData.deactivateSonar);
		System.exit(0);
	}

}

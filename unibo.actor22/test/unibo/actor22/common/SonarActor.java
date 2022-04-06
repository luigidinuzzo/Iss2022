package unibo.actor22.common;

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

/*
 * Funge da interprete di comandi e richieste
 */
public class SonarActor extends QakActor22 {
	private ISonar sonar;

	public SonarActor(String name) {
		super(name);
		sonar = DeviceFactory.createSonar();
	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		CommUtils.aboutThreads(getName() + " |  Before doJob - ");
		ColorsOut.out(getName() + " | doJob " + msg, ColorsOut.CYAN);
		if (msg.isRequest())
			elabRequest(msg);
		else
			elabCmd(msg);
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		switch (msgCmd) {
		case ApplData.cmdActivate:
			sonar.activate();
			;
			break;
		case ApplData.cmdDectivate:
			sonar.deactivate();
			;
			break;
		default:
			ColorsOut.outerr(getName() + " | unknown " + msgCmd);
		}
	}

	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		switch (msgReq) {
		case ApplData.reqSonarActive: {
			boolean b = sonar.isActive();
			IApplMessage reply = MsgUtil.buildReply(getName(), "sonarState", "" + b, msg.msgSender());
			ColorsOut.out(getName() + " | sendAnswer reply=" + reply, ColorsOut.CYAN);
			sendReply(msg, reply);
			break;
		}
		case ApplData.reqSonarDistance: {
			int d = sonar.getDistance().getVal();
			IApplMessage reply = MsgUtil.buildReply(getName(), "distance", "" + d, msg.msgSender());
			ColorsOut.out(getName() + " | sendAnswer reply=" + reply, ColorsOut.CYAN);
			sendReply(msg, reply);
			break;
		}
		default:
			ColorsOut.outerr(getName() + " | unknown " + msgReq);
		}
	}

}

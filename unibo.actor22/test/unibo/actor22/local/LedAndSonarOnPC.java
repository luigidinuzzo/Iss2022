package unibo.actor22.local;

import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Util;
import unibo.actor22.common.ApplData;
import unibo.actor22.common.ControllerActor;
import unibo.actor22.common.LedActor;
import unibo.actor22.common.SonarActor;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

public class LedAndSonarOnPC {

	public void doJob() {
		ColorsOut.outappl(this.getClass().getName() + " | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		// CommUtils.waitTheUser();
		execute();
		terminate();
	}

	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = false;					
		CommSystemConfig.tracing        = false;
		DomainSystemConfig.DLIMIT 		= 70;


		new LedActor(ApplData.ledName);
		new ControllerActor(ApplData.controllerName);
		new SonarActor(ApplData.sonarName);

	}

	protected void execute() {
		Qak22Util.sendAMsg(ApplData.activateCrtl);
	}

	public void terminate() {
		CommUtils.aboutThreads("Before exit - ");
		CommUtils.delay(30000);
		System.exit(0);
	}

	public static void main(String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new LedAndSonarOnPC().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}
package unibo.wenvUsage22.wshttp;

import java.util.Observable;

import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.common.ApplData;

public class BoundaryWalkerWs implements IObserver {
	private final String localHostName = "localhost"; // "localhost"; 192.168.1.7
	private final int port = 8090;
	private final String WsURL = "ws://" + localHostName + ":" + port + "/api/move";

	private Interaction2021 conn;
	private String answer = "";

	protected void doJob() throws Exception {
		conn = WsConnection.create("localhost:8091");
		((WsConnection) conn).addObserver(this);
	
		boolean obstacle = false;

		for (int i = 1; i <= 4; i++) {
			while (!obstacle) {

				conn.forward(ApplData.moveForward(500));
				ColorsOut.outappl(i + " answer= " + answer, ColorsOut.BLACK);
				obstacle = answer.contains("collision");
				CommUtils.delay(800);
			}
			obstacle = false;
			conn.forward(ApplData.turnLeft(300));
			CommUtils.delay(800);
			
		}

		ColorsOut.outappl("answer= " + answer, ColorsOut.BLACK);
	}

	/*
	 * MAIN
	 */
	public static void main(String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		new BoundaryWalkerWs().doJob();
		CommUtils.aboutThreads("At end - ");
	}

	@Override
	public void update(Observable source, Object data) {
		ColorsOut.out("BoundaryWalkerWs update/2 receives:" + data);
		answer = data.toString();
	}

	@Override
	public void update(String data) {
		ColorsOut.out("BoundaryWalkerWs update receives:" + data);

	}

}

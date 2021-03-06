package com.cj4dplex.test.sconnect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.cj4dplex.test.controller.Controller;
import com.cj4dplex.test.controller.Controller;
import com.cj4dplex.test.inoutmsg.ServerService;
import com.cj4dplex.test.server.ServerResource;
import com.cj4dplex.test.serverif.ServerInterface;

public class ServerConnect extends Thread {
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	int clientNum = 1;
	private Controller controller = null;

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(8889);
			controller = new Controller();

			System.out.println("---server waiting---");

			while (!this.isInterrupted()) {
				socket = serverSocket.accept();
				ServerResource.getInstance().getClientList().put(clientNum, socket);
				final ServerInterface serverService = new ServerService(clientNum);
				controller.setServer(serverService);
				controller.setUserArea(ServerResource.getInstance().getClientList());
				clientNum++;
			
			}

		} catch (IOException e) {
			try {

				ServerStop();

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void ServerStop() throws InterruptedException {
		if (null != this) {
			do {
				this.interrupt();
				this.sleep(50);
			} while (this.isAlive());
		}

		for (Integer i : ServerResource.getInstance().getClientList().keySet()) {
			try {
				ServerResource.getInstance().getClientList().get(i).close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


}

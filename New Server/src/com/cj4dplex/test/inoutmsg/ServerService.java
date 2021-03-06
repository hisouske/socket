package com.cj4dplex.test.inoutmsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JTextArea;

import com.cj4dplex.test.server.ServerResource;
import com.cj4dplex.test.serverif.ServerInterface;

public class ServerService implements ServerInterface {
	private int clientNum;
	private Socket socket = null;
	private InputStreamReader inputStreamReader = null;
	private BufferedReader bufferedReader = null;
	private PrintWriter printWriter = null;
	private JTextArea textArea = null;
	private JTextArea userArea = null;

	private Thread thread = null;

	public ServerService(int cNum) throws IOException {
		this.clientNum = cNum;
		this.socket = ServerResource.getInstance().getClientList().get(clientNum);
		this.inputStreamReader = new InputStreamReader(socket.getInputStream());
		this.bufferedReader = new BufferedReader(inputStreamReader);
		thread = new Thread(msgReceive(), "TCP Server msgReceive");
		thread.start();
	}

	@Override
	public void msgSend(String msg) {

		for (Integer i : ServerResource.getInstance().getClientList().keySet()) {
			try {
				printWriter = new PrintWriter(ServerResource.getInstance().getClientList().get(i).getOutputStream(),
						true);
				printWriter.write(clientNum + "번 ] " + msg + "\n");
				printWriter.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void setJText(JTextArea textArea, JTextArea userArea) {
		this.textArea = textArea;
		this.userArea = userArea;
	}

	@Override
	public Runnable msgReceive() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					String msg = null;
					while ((msg = bufferedReader.readLine()) != null ) {
						System.out.println("@@Server msgReceive = " + msg);
						System.out.println(msg);
						if (msg.equals("#종료#")) {
							textArea.append("--" + clientNum + " Exit--" + "\n");
							msgSend("--" + clientNum + " Exit--");
							ServerResource.getInstance().getClientList().remove(clientNum);
							userArea.setText("");
							for (Integer i : ServerResource.getInstance().getClientList().keySet()) {
							userArea.append(i + " 번 " + "\n");
							}
							
					
						}

						else {
							textArea.append(clientNum + "번 :" + msg + "\n");
							msgSend(msg);
						}
					}
				} catch (IOException e) {
				thread.interrupt();
				}
			}
		};

	}

	@Override
	public void serverStop() throws IOException {
	
		if (null != inputStreamReader) {
			inputStreamReader.close();
		}
		if (null != printWriter) {
			printWriter.close();
		}
		if (null != bufferedReader) {
			bufferedReader.close();
		}
		if (null != socket) {
			socket.close();
		}
		// if (null != ServerResource.getInstance().getClientList()) {
		// ServerResource.getInstance().getClientList().remove(clientNum);
		// System.out.println("#check server =" +
		// ServerResource.getInstance().getClientList());
		// }
	}

	public void AllStop() throws InterruptedException {
		for (Integer i : ServerResource.getInstance().getClientList().keySet()) {
			try {
				ServerResource.getInstance().getClientList().get(i).close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

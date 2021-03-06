package udpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpUdpSend {
	private static String serverIP = "localhost";
	private static byte[] bt = null;

	public static final void UdpSend(String udpMsg, int port) {
		try {
			DatagramSocket UdpSocket = new DatagramSocket();
			bt = udpMsg.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(bt, bt.length, new InetSocketAddress(serverIP, port));
			UdpSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void TcpSend(String tcpMsg, Socket TcpSocket) {
		try {
			bt = tcpMsg.getBytes("UTF-8");
			OutputStream os = TcpSocket.getOutputStream();
			os.write(bt);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

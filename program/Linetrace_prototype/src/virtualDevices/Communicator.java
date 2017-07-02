package virtualDevices;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Communicator {
	private static final int	SOCKET_PORT	= 7360; // PCと接続するポート

	private static ServerSocket		server = null;
    private static Socket			client = null;
    private static InputStream		inputStream = null;
    private static DataInputStream	dataInputStream = null;

	public Communicator(){

	}

	public void establishConnection(){
		if (server == null) { // 未接続
            try {
                server = new ServerSocket(SOCKET_PORT);
                client = server.accept();
                inputStream = client.getInputStream();
                dataInputStream = new DataInputStream(inputStream);
            } catch (IOException ex) {
                ex.printStackTrace();
                server = null;
                dataInputStream = null;
            }
        }
	}

	public int readCode(){
		int code = 0;
		if (server == null) { // 未接続
            establishConnection();
        } else { //接続済み
            try {
                if (dataInputStream.available() > 0) {
                    code = dataInputStream.readInt();
                }
            } catch (IOException ex) {
            	ex.printStackTrace();
            }
        }
		return code;
	}
}

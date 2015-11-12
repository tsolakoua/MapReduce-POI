
import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;

    public ChatServerThread(ChatServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();
    }

    public void send(String msg) {
        try {
            streamOut.writeUTF(msg);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();
        }
    }
    public String receive() {
    	
    	String answer="";
        try {
            answer=streamIn.readUTF();
            
        } catch (IOException ioe) {
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();
        }
        return answer;
    }
    
    

    public int getID() {
        return ID;
    }

    public void run() {  /////////////////   This function controls the main procedure  /////////////////////
        System.out.println("Server Thread " + ID + " running.");
      
            try {
            	String type=streamIn.readUTF();
            	server.handleConnectionType(ID, type);  // Our connection (MapWorker , ReduceWorker , Client)
            	                                       //  sends us his identification!!!!!!!
      
            	String message=streamIn.readUTF();  // message that comes from our connection
           
                
                if(type.compareTo("Client")==0){
                	String answer= server.handle(ID,message );
                	send(answer);  //our answer will be sent from here
                }
            
            
      
            } catch (IOException ioe) {
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
   
    }
    
    
    
    public void open() throws IOException {
        streamIn = new DataInputStream((socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }
}
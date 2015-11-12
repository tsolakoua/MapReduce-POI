

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ChatServer implements Runnable {

    private ChatServerThread clients[] = new ChatServerThread[50];
    private List <DirectoryType>ConnectionTypes = new ArrayList<DirectoryType>();
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    
    private String DatabaseFiles[]= {"place1.kml" , "place2.kml" , "place3.kml"};

    public ChatServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    public synchronized String handle(int ID, String input) {  // input includes the message!!!! 
    	System.out.println(input);
    	System.out.println(input.length());
    	String answer=""; // this is the answer that we'll sent to the clients
    	
        if (input.equals(".bye")) {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        } else {
        	
        	ShowConnectionTypes();
        	int fileNumber=0;
        	String finalMessage="";
        	for(int i=0;i<ConnectionTypes.size();i++){
        		if((ConnectionTypes.get(i).GETType().compareTo("MapWorker")==0)&&(fileNumber<DatabaseFiles.length)){
        			fileNumber++;
        			int j=findClient(ConnectionTypes.get(i).GETID());
        			clients[i].send(DatabaseFiles[fileNumber]);
        			clients[i].send(input);
        			String answ=clients[i].receive();
        			System.out.println("the answer from thread in position "+ConnectionTypes.get(i).GETID()+"is"+answ);
        		    finalMessage=finalMessage.concat(answ);
        		    finalMessage=finalMessage.concat("@");
        		}
        	}
        	finalMessage=finalMessage.substring(0, (finalMessage.length()-1));
        	System.out.println("The answer from mapWorkers is : "+finalMessage);
        	
            answer="We could't find an answer for you!!"; // this is the answer that we'll sent to the clients
        	
        	for(int i=0;i<ConnectionTypes.size();i++){
        		if(ConnectionTypes.get(i).GETType().compareTo("ReduceWorker")==0){
    
        			int j=findClient(ConnectionTypes.get(i).GETID());
      
        			clients[i].send(finalMessage);
        			answer=clients[i].receive();
        			System.out.println("the answer from thread(ReduceWorker) in position "+ConnectionTypes.get(i).GETID()+"is"+answer);
        		  
        		}
        	}
        	
        	
        }// else , our search for the client is done
        return answer;
    }
    
    public synchronized void handleConnectionType(int ID , String ConnectionType){
    	
    	ConnectionTypes.add(new DirectoryType(ID,ConnectionType));
    }

    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ChatServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }
    
    public void ShowConnectionTypes(){
    	
    	for(int i=0;i<ConnectionTypes.size();i++){
    		
    		System.out.println(ConnectionTypes.get(i).GETID()+" "+ConnectionTypes.get(i).GETType());
    	}
    }

    public static void main(String args[]) {
        ChatServer server = null;
        int port = 4455;
       
            server = new ChatServer(port);
        
    }
}

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {
    private frontEndServer gui;
    private int port;
    private ServerSocket serverSocket;
    private int connectionCounter;
    private int gameCounter, totalClients;
    private int clientSelection;
    //private ArrayList<ServerConnection> clientConnections = new ArrayList<ServerConnection>();
    private ArrayList<Client>  listOfClientConnections = new ArrayList<Client>();
    private ArrayList<Game> games  = new ArrayList<Game>();
    Socket clientSocket;

    public Server(int portNum, frontEndServer gui){
        this.gui = gui;
        this.port = portNum;
        connectionCounter = 0;
        gameCounter = 0;
        clientSelection = 0;
        totalClients = 0;
    }

    @Override
    public void run(){
        int cID;
        try{
            serverSocket = new ServerSocket(port);
            while (true){

                //if(connectionCounter != 2) {
                    System.out.println("connections: " + connectionCounter);

                    //waiting for client to connect
                    clientSocket = serverSocket.accept();
                    cID = connectionCounter;

                    //create instance of Server Connection for every thread
                    ServerConnection connection = new ServerConnection(clientSocket, this);

                    //--create a Client record
                    Client client = new Client(cID, connection);

                    //add client to list of connections
                    listOfClientConnections.add(client);
                    setTotalClients(true);

                    //login player to server
                    loginPlayer(client);

                    //update client with opponent list
                    updateClientWithOppList(client);

                    //update all clients with new client
                    updateAllClientsWithNewClient(client);



                    //initiate connection logistics
                    connection.start();

//                    clientConnections.add(connection);

                    //increase  connection counter
                    connectionCounter++;
                //}


            }

        }
        catch (IOException ex){
           // try {
                //clientSocket.close();

           // }
            //catch (IOException e){
            //    e.printStackTrace();
           // }
            ex.printStackTrace();
        }
    }

    private void updateList(String userName) {
        int i = 0;
        int k = 0;
        int size = 0;

        for( i = 0; i< listOfClientConnections.size(); i++) {

            listOfClientConnections.get(i).setOpponentList(listOfClientConnections);
            for(k = 0; k < listOfClientConnections.get(i).opponentList.size(); k++) {
                    if( !(listOfClientConnections.get(i).opponentList.get(k).userName.equalsIgnoreCase(listOfClientConnections.get(i).userName)) ) {
                        System.out.println(listOfClientConnections.get(i).opponentList.get(k).userName);
                    }

                }
        }
    }

    private void updateClientWithOppList(Client c) {
        int i = 0;
        for(i = 0; i<listOfClientConnections.size(); i++){
            if(!(listOfClientConnections.get(i).userName.equalsIgnoreCase(c.userName))){
                c.sendMsgToClient("add " + listOfClientConnections.get(i).userName);
            }
        }
    }

    private void updateAllClientsWithNewClient(Client c){
        int i = 0;
        for(i = 0; i<listOfClientConnections.size(); i++){
            if(!(listOfClientConnections.get(i).userName.equalsIgnoreCase(c.userName))){
                listOfClientConnections.get(i).sendMsgToClient("add " + c.userName);
            }
        }
    }

    private void loginPlayer(Client client) {

        String successfulMsg = "You have logged in.";
        Scanner parser;
        String clientMessage;
        String clientName;

        clientMessage = client.getMsgFromClient();
        //parse the message to input data into list of client connections
        parser = new Scanner(clientMessage);
        clientName = parser.next();
        //set user name
        client.userName = clientName;
        client.sendMsgToClient(successfulMsg);

    }

    public int getPortNum(){return port;}

    public ServerSocket getServerSocket() {return serverSocket;}


    public void setConnectionCounter(int num) {
        connectionCounter = connectionCounter + num;
    }

    public int getConnectionCounter() {return connectionCounter;}

    public void setClientSelection(int num) {
        clientSelection = clientSelection + num;
    }
    public int getClientSelection() {return clientSelection;}

    public void setTotalClients(boolean increment){
        if(increment)
            totalClients++;
        else
            totalClients--;
    }

    public int getTotalClients(){return totalClients;}

    public frontEndServer getGUI(){return gui;}

//    public ArrayList<ServerConnection> getClientConnections() {
//        return clientConnections;
//    }

    public ArrayList<Client> getListOfClientConnections() {
        return listOfClientConnections;
    }

    public boolean createConnectionRecord() {
        return false;
    }

    public ArrayList<Game> getListofGames() {
        return games;
    }

    public void createGame(int p1Index, int p2Index) {
        Game newgame = new Game(p1Index, p2Index, gameCounter, listOfClientConnections);
        games.add(newgame);
        gameCounter++;
    }

}

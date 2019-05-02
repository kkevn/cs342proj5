import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ServerConnection extends Thread {

    private Socket clientSocket;
    private Server server;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ServerConnection(Socket clientSocket, Server server){


        try {
            this.clientSocket = clientSocket;
            this.server = server;
            //used to read in bytes from a connection, in this case the client socket
            this.inputStream = new DataInputStream(clientSocket.getInputStream());
            //connected to client destination
            this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void run(){
        int i = 0;
        Scanner parser;
        String gameNumber;
        String clientMessage;
        String command;
        String userName;
        String opponentName;

        String lobbyName;

        try {
        while(true) {
                clientMessage = inputStream.readUTF();
                parser = new Scanner(clientMessage);
                command = parser.next();


                //--TODO RECEIVING CREATE_LOBBY COMMAND
                if(command.equalsIgnoreCase("create_lobby")){

                    //parse lobby name and create the lobby
                    lobbyName = parser.next();
                    Lobby new_lobby = new Lobby(lobbyName, "JOIN", server.getLobbyList().size());
                    server.getLobbyList().add(new_lobby);

                    System.out.println("Lobby with name: " + server.getLobbyList().get(server.getLobbyList().size()-1).getLobbyName() +
                            " added to the client lobbyList");

                    //Message all connected clients to create their new psuedo-lobby
                    for(i = 0; i<server.getListOfClientConnections().size(); i++){
                        server.getListOfClientConnections().get(i).getClientsServerConnection().msgClientToMakeLobby(lobbyName);
                    }

                }

                //--TODO RECEIVING JOIN_LOBBY COMMAND
                else if(command.equalsIgnoreCase("join")){
                    userName = parser.next();
                    lobbyName = parser.next();

                    server.getLobbyList().get(getLobbyIndexFromName(lobbyName)).addUsers(userName);
                    System.out.println(userName + " added to lobby " + lobbyName);

                    //send update to all clients about who joined what lobby
                    for(Client c : server.getListOfClientConnections())
                    {
                        c.getClientsServerConnection().msgClientToJoinPlayer(lobbyName, userName);
                    }
                }

                //--TODO RECEIVING TYPED COMMAND



            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try{
                clientSocket.close();
                inputStream.close();
                outputStream.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }





    public void messageClient(String message){
        try{
            outputStream.writeUTF(message);
            outputStream.flush();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public DataInputStream getInputStream(){
        return inputStream;
    }
    public DataOutputStream getOutputStream(){
        return outputStream;
    }


    //--TODO - SEND MAKE LOBBY COMMAND TO CLIENT
    private void msgClientToMakeLobby(String lobName){
        messageClient("create_lobby " + lobName);
    }

    //--TODO - SEND JOIN PLAYER TO LOBBY COMMAND TO CLIENT
    public void msgClientToJoinPlayer(String lobbyname, String username){
        messageClient("join " + username + " " + lobbyname);
    }

    //--TODO - SEND GAME STARTED COMMAND TO CLIENT

    //--TODO - SEND GAME ENDED COMMAND TO CLIENT

    //--TODO - SEND GAME GUI UPDATE POINTS COMMAND TO CLIENT

    //--TODO - SEND GAME NEW WORD TO CLIENT


    //Function to get lobby index from name
    public int getLobbyIndexFromName(String lobName){
        for(Lobby l : server.getLobbyList()){
            if(l.getLobbyName().equalsIgnoreCase(lobName)){
                return l.lobbyIndex;
            }
        }
        return -1; //if lobbyname not found
    }

}
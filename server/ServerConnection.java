import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReferenceArray;


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
        } catch (IOException e) {
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

        try {
        while(true) {
                clientMessage = inputStream.readUTF();
                parser = new Scanner(clientMessage);
                command = parser.next();
                if(command.equalsIgnoreCase("challenge")) {
                    int userOneIndex = -1;
                    int userTwoIndex = -1;
                    userName = parser.next();
                    opponentName = parser.next();

                    //check if opponent is available

                    //if available then set up game and start game
                    for(i = 0; i< server.getListOfClientConnections().size(); i++) {
                        if(userName.equalsIgnoreCase(server.getListOfClientConnections().get(i).userName)) {
                            userOneIndex = server.getListOfClientConnections().get(i).connectionID;
                        }

                        if(opponentName.equalsIgnoreCase(server.getListOfClientConnections().get(i).userName)) {
                            userTwoIndex = server.getListOfClientConnections().get(i).connectionID;
                        }
                    }
                    System.out.println("Creating game with players: " + userOneIndex + " and "+ userTwoIndex);
                    server.createGame(userOneIndex, userTwoIndex);
                }

                else if(command.equalsIgnoreCase("choice")) {
                    gameNumber = parser.next();
                    int gameIndex = Integer.parseInt(gameNumber);
                    String playerName = parser.next();
                    String playerChoice = parser.next();
                    int userIndex = -1;

                    //get players index from username
                    for(i = 0; i< server.getListOfClientConnections().size(); i++) {
                        if (playerName.equalsIgnoreCase(server.getListOfClientConnections().get(i).userName)) {
                            userIndex = server.getListOfClientConnections().get(i).connectionID;
                        }
                    }

                    //command to send to game function
                    server.getListofGames().get(gameIndex).sendChoiceToGame(userIndex, playerChoice);
                }


                else if(command.equalsIgnoreCase("quit")){
                    String playerName = parser.next();
                        for(i = 0; i< server.getListOfClientConnections().size(); i++) {
                            if (playerName.equalsIgnoreCase(server.getListOfClientConnections().get(i).userName)) {
                                //user disconnected
                                server.getListOfClientConnections().get(i).score = -999;
                            }
                        }
                        server.setTotalClients(false);
                        break;
                }
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



}
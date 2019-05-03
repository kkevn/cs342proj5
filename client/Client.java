import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread{

    private String userName;
    private String ipAddress;
    private int port;
    private Socket sock;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private FrontEndClient gui;
    private ArrayList<Lobby> lobbyList = new ArrayList<>();
    private Lobby connected_lobby;
    private int gameID;
    private boolean gameOver = false;


    public Client(String address, int portNum, String userName, FrontEndClient gui) {
        this.ipAddress = address;
        this.port = portNum;
        this.userName = userName;
        this.gui = gui;

    }

//    public static void main(String[] args) {
//        Client client = new Client("localhost", 5555);
//        client.connect();
//
//    }

    @Override
    public void run() {
        String serverMessage;
        try {
            this.sock = new Socket(ipAddress, port);
            this.inputStream = new DataInputStream(sock.getInputStream());
            this.outputStream = new DataOutputStream(sock.getOutputStream());
            System.out.println("CONNECTED");

            login();

            //wait for another client to join
            /*if(ready() == true){
                //now start listening
                listen();
            }
            */
            if(ready() == true/* && gameOver == false*/){
                //now start listening
                listen();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO CONNECT");
        }
    }

    private void login() {
        String loginMessage = userName + " ready";
        String serverMessage;
        //set login-ready message to server
        try{
            outputStream.writeUTF(loginMessage);
            outputStream.flush();
            serverMessage=inputStream.readUTF();//--update GUI with connected message
            gui.setServerText(serverMessage);
            System.out.println(serverMessage);
        }
        catch (IOException e) {
        }
    }


    public boolean ready() {
        String serverMessage;
        Scanner parser;
        String command;
        String gameNumber;
        String lobbyName;
        String username;
        while(true) {

            try {
                serverMessage=inputStream.readUTF(); //--update opponent list on GUI

                parser = new Scanner(serverMessage);
                command = parser.next();

                //--TODO - RECEIVE COMMAND TO CREATE_LOBBY (DONE)
                if(command.equalsIgnoreCase("create_lobby")){

                    //parse lobby name and create the lobby
                    lobbyName = parser.next();
                    Lobby new_lobby = new Lobby(lobbyName, "JOIN", lobbyList.size());
                    lobbyList.add(new_lobby);
                    System.out.println("Lobby with name: " + lobbyList.get(lobbyList.size()-1).getLobbyName() +
                            " added to the client lobbyList");

                    //update lobby screen
                    this.connected_lobby = new_lobby;
                    gui.updateLobbyList();

                }

                //--TODO - RECEIVE COMMAND TO JOIN SOMEONE TO A LOBBY (DONE)
                else if(command.equalsIgnoreCase("join")){

                    //parse lobby and username, and add the user to that lobby name
                    username = parser.next();
                    lobbyName = parser.next();
                    lobbyList.get(getLobbyIndexFromLobbyName(lobbyName)).addUsers(username);


                    //confirm user was added to lobby
                    System.out.println(username + "joined lobby: " +
                            lobbyList.get(getLobbyIndexFromLobbyName(lobbyName)).getLobbyName());

                    //update lobby screen
                    this.connected_lobby = lobbyList.get(getLobbyIndexFromLobbyName(lobbyName));
                    gui.updateLobbyList();

                }

                //--TODO - RECEIVE COMMAND TO START GAME (DONE)
                //--GAME STARTED, BREAK READY AND GO TO LISTEN
                else if(command.equalsIgnoreCase("start_game")) {

                    break;
                }


                else if(command.equalsIgnoreCase("add")) {
                    //add to arraylist
                    lobbyName = parser.next();
                    
                    //TODO
                    // figure out how to parse a list of all lobby objects to client
                    // (1) probably have server send lobby.print() and carefully parse
                    // all outputs of print() into new lobby object
                    // OR (and this is better idea, i think) (2) is to use
                    // objectoutputstream because i think that can parse objects
                    // if using (2), lobbyName should not be a string, but lobby object
                    
                    //lobbyList.add(lobbyName);
                    
                    System.out.println("user: " + lobbyName + " added");
                    gui.updateLobbyList();
                }




            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void listen() {
        //String userinput;
        String serverMessage;
        Scanner readIn = new Scanner(System.in);
        Scanner parser;
        String opponentPlayed;
        String score, score2;
        String result;
        String winner;
        String newWord;
        int parseCount = 0;

        String command = "";
        String lobbyName;
        String username;

        //TODO - DON'T LET CLIENTS START TYPING IN GUI UNTIL GAME STARTS (IN THIS LISTEN() METHOD)


        //When game starts update score list
        //TODO - THIS SHOULD WORK? USED TO UPDATE SCORE LIST WITH LOBBY CLIENTS NAMES
        gui.updateScoreList();

        while(true) {

            try {
//

                //now receive data from server
                serverMessage = inputStream.readUTF();
                System.out.println("original msg: " + serverMessage);

                //parse the message
                parser = new Scanner(serverMessage);
                command = parser.next();


                //--TODO - RECEIVE COMMAND TO CREATE_LOBBY (DONE)
                if(command.equalsIgnoreCase("create_lobby")){

                    //parse lobby name and create the lobby
                    lobbyName = parser.next();
                    Lobby new_lobby = new Lobby(lobbyName, "JOIN", lobbyList.size());
                    lobbyList.add(new_lobby);

                    System.out.println("Lobby with name: " + lobbyList.get(lobbyList.size()-1).getLobbyName() +
                            " added to the client lobbyList");

                    //update lobby screen
                    gui.updateLobbyList();

                }

                //--TODO - RECEIVE COMMAND TO JOIN SOMEONE TO A LOBBY (DONE)
                else if(command.equalsIgnoreCase("join")){

                    //parse lobby and username, and add the user to that lobby name
                    username = parser.next();
                    lobbyName = parser.next();
                    lobbyList.get(getLobbyIndexFromLobbyName(lobbyName)).addUsers(username);


                    //confirm user was added to lobby
                    System.out.println(username + "joined lobby: " +
                            lobbyList.get(getLobbyIndexFromLobbyName(lobbyName)).getLobbyName());

                    //update lobby screen
                    gui.updateLobbyList();

                }

                //--TODO - RECEIVE COMMAND TO UPDATE ROUND WINNER -- to update (DONE)
                else if(command.equalsIgnoreCase("winner")){

                    //parse username and update gui scores
                    String winnerName = parser.next();
                    String clientsLobbyName = parser.next();

                    //update lobby with winner score
                    int clientsLobbyIndex = getLobbyIndexFromLobbyName(clientsLobbyName);
                    lobbyList.get(clientsLobbyIndex).updateWinnerScore(winnerName);


                    //update scores (called when new word)
                    //gui.updateScoreList();

                }

                //--TODO - RECEIVE COMMAND TO UPDATE NEW WORD (DONE)
                //--TODO - NEW_WORD COMMAND SHOULD ALSO SEND GAME STATUS
                else if(command.equalsIgnoreCase("new_word")){

                    //parse username and update gui scores
                    newWord = parser.next();
                    String roundResult = parser.next();
                    int roundResultInt = Integer.parseInt(roundResult);

                    //update word label
                    //zero if game is still ongoing
                    //--TODO - RECEIVE STATUS IN MESSAGE TO END GAME OR CONTINUE
                    gui.updateGameInfo(newWord, roundResultInt);

                    //if the client wins or loses then return to the ready state for the next game
                    if(roundResultInt == 3 || roundResultInt == 4){
                        resetClientGame();
                    }

                }






                
                System.out.println("SERVER REPLY MESSAGE: " + serverMessage);

                if(gameOver == true){
                    this.resetClientGame();
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

        }
        //close down the connection if out of loop
        try {
            sock.close();
            inputStream.close();
            outputStream.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendChoiceToGame(String choice) {
        //output stream write out to server: <command> <gameID> <thisClientsName> <choice>
        try {
            outputStream.writeUTF("choice " + this.gameID + " " + this.userName + " " + choice);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // notify server that client finished typing word
    public void setDone() {
        
        try {
            outputStream.writeUTF("done " + this.gameID + " " + this.userName);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //send game choice to the server
    public void sendMessage(String message) {

        //output stream write out to server: <userName> <message>
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //function used in conjunction with the gui opponent list buttons
    public void challengeOpponent(String opponentUserName) {

        try {
            outputStream.writeUTF("challenge " + this.userName + " " + opponentUserName);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // notify server that client wants to join a lobby
    public void joinLobby(String lobby_name) {

        try {
            outputStream.writeUTF("join " + this.userName + " " + lobby_name);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // notify server that client created a server
    public void setupLobby(String lobby_name) {

        try {
            outputStream.writeUTF("create_lobby " + lobby_name);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // notify server that client logged out
    public void logout() {

        try {
            outputStream.writeUTF("logout " + this.userName);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Lobby getLobby() {
        return connected_lobby;
    }
    
    public ArrayList<Lobby> getLobbyList() {
        return lobbyList;
    }

    public void resetClientGame(){
        gameOver = false;
        gui.score = 0; gui.word = ""; gui.rounds = 1; gui.status = -1;
        //TODO - RETURN CLIENT TO LOBBY SCREEN
        ready();
    }

    public FrontEndClient getGui() {return gui;}

    public String getUserName() {return userName;}

    public String  getIpAddress() {return ipAddress;}



    //Gui function to tell server that player is done typing
    //--TODO - EXECUTE THIS FUNCTION IN GUI WHEN PLAYER IS DONE TYPING (KEVIN)
    public void GuiPlayerDoneTyping(String userName){
        sendMessage("done_typing " + this.userName);
    }

    //Gui function to create new lobby
    public void GuiCreateLobby(String lobName){
        sendMessage("create_lobby " + lobName);
    }

    //Gui function to create new lobby
    public void GuiJoinLobby(String lobName){
        sendMessage("join " + this.userName + " " + lobName);
    }



    //Function to get lobby index from name
    public int getLobbyIndexFromLobbyName(String lobName){
        for(Lobby l : lobbyList){
            if(l.getLobbyName().equals(lobName)){
                return l.lobbyIndex;
            }
        }

        return -1; //if lobbyname not found
    }



}

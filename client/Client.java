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
    private ArrayList<String> opponentList = new ArrayList<String>();
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
        String userName;
        String gameNumber;
        while(true) {

            try {
                serverMessage=inputStream.readUTF(); //--update opponent list on GUI

                parser = new Scanner(serverMessage);
                command = parser.next();


                if(command.equalsIgnoreCase("add")) {
                    //add to arraylist
                    userName = parser.next();
                    opponentList.add(userName);
                    System.out.println("user: " + userName + " added");
                    gui.updateClientList();
                }
                else if(command.equalsIgnoreCase("challenged")) {
                    userName = parser.next();
                    gameNumber = parser.next();
                    this.gameID = Integer.parseInt(gameNumber);
                    gameOver = false;
                    System.out.println("user: " + userName + " has challenged you");
                    //--update GUI with connected message
                    gui.setServerText(command);
                    gui.setConnectedTo(userName);
                    
                    break;
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
        int parseCount = 0;

        while(true) {

            try {
//                //break out of loop with quit
//                if(userinput.equalsIgnoreCase("quit")){break;}

                //now receive data from server
                serverMessage = inputStream.readUTF();
                System.out.println("original msg: " + serverMessage);

                //parse the message
                parser = new Scanner(serverMessage);
                opponentPlayed = parser.next();
                score = parser.next();
                score2 = parser.next();
                //result = parser.nextLine();
                winner = parser.nextLine();
                //winner.trim();
                winner = winner.replaceAll("\\s+","");
                System.out.println("'" + winner + "'");
                System.out.println(opponentPlayed + " " + score + /*" " + result +*/ " " + winner);
                
                // game end reached (win, loss, or tie)
                if (Integer.parseInt(winner) == 3 || Integer.parseInt(winner) == 4 || Integer.parseInt(winner) == 5) {
                    gameOver = true;
                }

                //--update GUI with total score, opponent played, server message
                gui.setTotalpointsText(score, score2);
                gui.setOpponentplayedText(opponentPlayed);
                //gui.setServerText(result);
                gui.updateGameInfo(Integer.parseInt(winner));
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

    //send game choice to the server
    public void sendMessage(String message) {

        //output stream write out to server: <userName> <message>
        try {
            outputStream.writeUTF(this.userName + " " + message);
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

    public ArrayList<String> getOpponentList() {
        return opponentList;
    }

    public void resetClientGame(){
        gameOver = false;
        gui.score = 0; gui.opponent_score = 0; gui.rounds = 1; gui.status = -1;
        ready();
    }

    public FrontEndClient getGui() {return gui;}

    public String getUserName() {return userName;}

    public String  getIpAddress() {return ipAddress;}
}
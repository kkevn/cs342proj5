import java.io.IOException;
import java.util.ArrayList;

public class Client {
    int connectionID;
    String userName;
    String messageToSend; //your decision rpsls
    String roundResult;
    String roundWinner;
    String gameResult;
    int score;
    ServerConnection connection;
    String connectionStatus;
    String gameStatus;
    ArrayList<Client> opponentList = new ArrayList<>();

    //TODO -- add lobby number, change messages to send to server connection


    public Client(int ID, ServerConnection connection) {
        this.connectionID = ID;
        this.userName = " ";
        this.messageToSend = " "; //choice
        this.roundResult = " ";
        this.roundWinner = " ";
        this.gameResult = " ";
        this.score = 0;
        this.connection = connection;
        this.connectionStatus = "connected";
        this.gameStatus = "pending";
    }

    public void setScore(int num){
        score = score + num;
        System.out.println("set score for player: " + connectionID + " to: " + score);

    }
    
    public void setRoundWinner(int winner) {
        // winner = 1 if player 1 won round
        // winner = 2 if player 2 won round
        // winner = 0 if players tied round
        roundWinner = "" + winner;
    }

    public void sendMsgToClient(String msg){
        try {
            connection.getOutputStream().writeUTF(msg);
            connection.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMsgFromClient(){
        try {
            return connection.getInputStream().readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public void setOpponentList(ArrayList<Client> clientList) {
        opponentList = clientList;
        //opponentList.remove(connectionID);

    }

}
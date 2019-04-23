import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class Game {
    private int p1Index, p2Index, choiceCounter;
    ArrayList<Client> cc;
    int gameID;

    public Game(int p1, int p2, int gameID, ArrayList<Client> cc) {

        this.p1Index = p1;
        this.p2Index = p2;
        this.gameID = gameID;
        this.cc = cc;
        this.choiceCounter = 0;

        setupGame();

    }

    private void setupGame() {
        cc.get(p1Index).gameStatus = "playing";
        cc.get(p2Index).gameStatus = "playing";

        cc.get(p1Index).connection.messageClient("challenged " + cc.get(p2Index).userName + " " + gameID);
        cc.get(p2Index).connection.messageClient("challenged " + cc.get(p1Index).userName + " " + gameID);
    }

    public void sendChoiceToGame(int playerIndex, String choice){

        //set players choice
        cc.get(playerIndex).messageToSend = choice;
        choiceCounter++;

        //if both choices have been made, execute game logic
        if(choiceCounter == 2) {

            //--update GUI with their selection
            //server.getGUI().setplayedText(cc.get(0).messageToSend, cc.get(1).messageToSend);

            awardPoints();

            //someone won a game
            if(cc.get(p1Index).score == 3 || cc.get(p2Index).score == 3){
                //distribute game results
                if(cc.get(p1Index).score == 3 && cc.get(p2Index).score < 3 ) {
                    //player 1 won the game
                    //cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + "YOU WON";
                    //cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + "YOU LOST";
                    
                    cc.get(p1Index).setRoundWinner(3);
                    cc.get(p2Index).setRoundWinner(4);
                    
                    //cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + cc.get(p1Index).roundWinner;
                    //cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + cc.get(p2Index).roundWinner;
                    
                    cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + cc.get(p2Index).score + " " + cc.get(p1Index).roundWinner;
                    cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + cc.get(p1Index).score + " " + cc.get(p2Index).roundWinner;



                    //--update GUI with winner
                    //server.getGUI().setWinnerText(cc.get(p1Index).userName);
                    cc.get(p1Index).score = 0;
                    cc.get(p2Index).score = 0;
                }
                else if(cc.get(p2Index).score == 3 && cc.get(p1Index).score < 3) {
                    //player 2 won the game
                    //cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + "YOU LOST";
                    //cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + "YOU WON";
                    
                    cc.get(p1Index).setRoundWinner(4);
                    cc.get(p2Index).setRoundWinner(3);
                    
                    //cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + cc.get(p1Index).roundWinner;
                    //cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + cc.get(p2Index).roundWinner;
                    
                    cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + cc.get(p2Index).score + " " + cc.get(p1Index).roundWinner;
                    cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + cc.get(p1Index).score + " " + cc.get(p2Index).roundWinner;

                    //--update GUI with winner
                    //server.getGUI().setWinnerText(cc.get(p2Index).userName);
                    cc.get(p1Index).score = 0;
                    cc.get(p2Index).score = 0;
                }
                else {
                    //tied game
                    //cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + "GAME WAS A TIE";
                    //cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + "GAME WAS A TIE";
                    
                    cc.get(p1Index).setRoundWinner(5);
                    cc.get(p2Index).setRoundWinner(5);
                    
                    //cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + cc.get(p1Index).roundWinner;
                    //cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + cc.get(p2Index).roundWinner;
                    
                    cc.get(p1Index).gameResult = cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + cc.get(p2Index).score + " " + cc.get(p1Index).roundWinner;
                    cc.get(p2Index).gameResult = cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + cc.get(p1Index).score + " " + cc.get(p2Index).roundWinner;

                    //--update GUI with winner
                    //server.getGUI().setWinnerText("TIE");
                    cc.get(p1Index).score = 0;
                    cc.get(p2Index).score = 0;
                }


                //sending game results to clients
                cc.get(p1Index).connection.messageClient(cc.get(p1Index).gameResult);
                cc.get(p2Index).connection.messageClient(cc.get(p2Index).gameResult);

                cc.get(p1Index).score = 0;
                cc.get(p2Index).score = 0;
            }
            else {
                //set round message
                //cc.get(p1Index).roundResult =  cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + "PLAYING";
                //cc.get(p2Index).roundResult =  cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + "PLAYING";
                cc.get(p1Index).roundResult =  cc.get(p2Index).messageToSend + " " + cc.get(p1Index).score + " " + cc.get(p2Index).score + " " + cc.get(p1Index).roundWinner;
                cc.get(p2Index).roundResult =  cc.get(p1Index).messageToSend + " " + cc.get(p2Index).score + " " + cc.get(p1Index).score + " " + cc.get(p2Index).roundWinner;

                //send round result to clients
                cc.get(p1Index).connection.messageClient(cc.get(p1Index).roundResult);
                cc.get(p2Index).connection.messageClient(cc.get(p2Index).roundResult);

            }

            choiceCounter = 0;
        }

    }

    public void awardPoints() {
        String user1Choice = cc.get(p1Index).messageToSend;
        Client user1 = cc.get(p1Index);
        String user2Choice = cc.get(p2Index).messageToSend;
        Client user2 = cc.get(p2Index);

        if(user1Choice.equalsIgnoreCase(user2Choice)){
            //tie game
            //should we award a point for a tie though?
            user1.setScore(1);
            user2.setScore(1);
        
            user1.setRoundWinner(0);
            user2.setRoundWinner(0);
        }
        else if(user1Choice.equalsIgnoreCase("rock") && (user2Choice.equalsIgnoreCase("scissors") || user2Choice.equalsIgnoreCase("lizard"))){
            user1.setScore(1);
        
            user1.setRoundWinner(1);
            user2.setRoundWinner(1);
        }
        else if(user1Choice.equalsIgnoreCase("paper") && (user2Choice.equalsIgnoreCase("rock") || user2Choice.equalsIgnoreCase("spock"))){
            user1.setScore(1);
        
            user1.setRoundWinner(1);
            user2.setRoundWinner(1);
        }
        else if(user1Choice.equalsIgnoreCase("scissors") && (user2Choice.equalsIgnoreCase("paper") || user2Choice.equalsIgnoreCase("lizard"))){
            user1.setScore(1);
        
            user1.setRoundWinner(1);
            user2.setRoundWinner(1);
        }
        else if(user1Choice.equalsIgnoreCase("lizard") && (user2Choice.equalsIgnoreCase("paper") || user2Choice.equalsIgnoreCase("spock"))){
            user1.setScore(1);
        
            user1.setRoundWinner(1);
            user2.setRoundWinner(1);
        }
        else if(user1Choice.equalsIgnoreCase("spock") && (user2Choice.equalsIgnoreCase("scissors") || user2Choice.equalsIgnoreCase("rock"))){
            user1.setScore(1);
        
            user1.setRoundWinner(1);
            user2.setRoundWinner(1);
        }
        else {
            user2.setScore(1);
            
            user1.setRoundWinner(2);
            user2.setRoundWinner(2);
        }

        /*
        if(user1Choice.equalsIgnoreCase("rock")){
            if(user2Choice.equalsIgnoreCase("rock")) {
                //tie both awarded 1 point
                user1.setScore(1);
                user2.setScore(1);
                
                user1.setRoundWinner(0);
                user2.setRoundWinner(0);
            }
            else if(user2Choice.equalsIgnoreCase("paper"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
            else if(user2Choice.equalsIgnoreCase("scissors"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("lizard"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("spock"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
        }
        if(user1Choice.equalsIgnoreCase("paper")){
            if(user2Choice.equalsIgnoreCase("paper")) {
                //tie both awarded 1 point
                user1.setScore(1);
                user2.setScore(1);
                
                user1.setRoundWinner(0);
                user2.setRoundWinner(0);
            }
            else if(user2Choice.equalsIgnoreCase("rock"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("scissors"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
            else if(user2Choice.equalsIgnoreCase("lizard"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
            else if(user2Choice.equalsIgnoreCase("spock"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
        }
        if(user1Choice.equalsIgnoreCase("scissors")){
            if(user2Choice.equalsIgnoreCase("scissors")) {
                //tie both awarded 1 point
                user1.setScore(1);
                user2.setScore(1);
                
                user1.setRoundWinner(0);
                user2.setRoundWinner(0);
            }
            else if(user2Choice.equalsIgnoreCase("rock"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
            else if(user2Choice.equalsIgnoreCase("paper"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("lizard"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("spock"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
        }
        if(user1Choice.equalsIgnoreCase("lizard")){
            if(user2Choice.equalsIgnoreCase("lizard")) {
                //tie both awarded 1 point
                user1.setScore(1);
                user2.setScore(1);
                
                user1.setRoundWinner(0);
                user2.setRoundWinner(0);
            }
            else if(user2Choice.equalsIgnoreCase("rock"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
            else if(user2Choice.equalsIgnoreCase("paper"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("scissors"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
            else if(user2Choice.equalsIgnoreCase("spock"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
        }
        if(user1Choice.equalsIgnoreCase("spock")){
            if(user2Choice.equalsIgnoreCase("spock")) {
                //tie both awarded 1 point
                user1.setScore(1);
                user2.setScore(1);
                
                user1.setRoundWinner(0);
                user2.setRoundWinner(0);
            }
            else if(user2Choice.equalsIgnoreCase("rock"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("paper"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
            else if(user2Choice.equalsIgnoreCase("scissors"))
            {
                //player 1 wins round
                user1.setScore(1);
                
                user1.setRoundWinner(1);
                user2.setRoundWinner(1);
            }
            else if(user2Choice.equalsIgnoreCase("lizard"))
            {
                //player 2 wins round
                user2.setScore(1);
                
                user1.setRoundWinner(2);
                user2.setRoundWinner(2);
            }
        }
        */
        //--update GUI score
        //server.getGUI().setscoreText(user1.score, user2.score);

    }

    public void resetClientScores(){

    }



}
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private int p1Index, p2Index, p3Index, p4Index, gameRound;
    ArrayList<Client> cc;
    //int gameID;
    private String roundWord;
    private ArrayList<String> words;
    private ArrayList<Integer> clientsDone = new ArrayList<>();
    Scanner scanner;

    public Game(int p1, int p2, int p3, int p4, ArrayList<Client> cc) {

        this.p1Index = p1;
        this.p2Index = p2;
        this.p3Index = p3;
        this.p4Index = p4;
        this.gameRound = 1;
        this.cc = cc;

        setupGame();

    }

    public void sendWord(){
        //send a random word from a list of strings
        Random rand = new Random();
        roundWord = words.get(rand.nextInt(words.size()));

        //send the random word to each client
        cc.get(p1Index).connection.messageClient(roundWord);
        cc.get(p2Index).connection.messageClient(roundWord);
        cc.get(p3Index).connection.messageClient(roundWord);
        cc.get(p4Index).connection.messageClient(roundWord);

    }

    public void createWords(){
        try {
            scanner = new Scanner(new File("src/words.txt"));
            words = new ArrayList<String>();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        while(scanner.hasNext()){
            words.add(scanner.nextLine());
        }

    }

    private void setupGame() {
        createWords();

        cc.get(p1Index).gameStatus = "playing";
        cc.get(p2Index).gameStatus = "playing";
        cc.get(p3Index).gameStatus = "playing";
        cc.get(p4Index).gameStatus = "playing";

        gameRound = 1;
        sendWord();
    }

    public void setGameWinner(int index){
        //notify clients of the game winner
        //...



        //reset the scores
        resetClientScores();
    }
    
    //Called when a player successfully types the round word
    public void wordTyped(int index){
       clientsDone.add(index);

       //once all four players have typed the word inform the clients and update score
       if(clientsDone.size() == 4){
           cc.get(p1Index).connection.messageClient("winner " + clientsDone.get(0));
           cc.get(p2Index).connection.messageClient("winner " + clientsDone.get(0));
           cc.get(p3Index).connection.messageClient("winner " + clientsDone.get(0));
           cc.get(p4Index).connection.messageClient("winner " + clientsDone.get(0));
       }
    }

    public void determineWinner(){
        int score = cc.get(p1Index).score;
        int index = p1Index;

        if(cc.get(p2Index).score > score){
            score = cc.get(p2Index).score;
            index = p2Index;
        }

        if(cc.get(p3Index).score > score){
            score = cc.get(p3Index).score;
            index = p3Index;
        }

        if(cc.get(p4Index).score > score){
            score = cc.get(p4Index).score;
            index = p4Index;
        }

        setGameWinner(index);
    }

    public void sendChoiceToGame(int playerIndex, String choice){

        //set players choice
        cc.get(playerIndex).messageToSend = choice;

        if(choice.equalsIgnoreCase(roundWord)){
            //this player was first to get the word
            awardPoints(playerIndex, 1);

            //new round
            gameRound++;
            if(gameRound < 11)
                sendWord();
            //game is over
            else
                determineWinner();
        }
    }

    public void awardPoints(int index, int award) {
        Client user = cc.get(index);

        user.setScore(award);
    }

    public void resetClientScores(){
        cc.get(p1Index).score = 0;
        cc.get(p2Index).score = 0;
        cc.get(p3Index).score = 0;
        cc.get(p4Index).score = 0;
    }
}

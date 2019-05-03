import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lobby {

    // lobby data
    String lobby_name;
    String lobby_status;
    HashMap<String, Integer> connected_clients = new HashMap<>();
    int p1, p2, p3, p4;
    int player_count;

    //--TODO - ADD LOBBY INDEX, ADD VARIABLES FOR PLAYER INDEX
    int lobbyIndex;
    ArrayList<Client> listOfClientConnections;
    Game lobbyGame;


    //--TODO - ADD LOBBY FUNCTION THAT CAN UPDATE GAME GUI (MAYBE MULTIPLE FUNCTIONS?)
    //send to each client from the game.java


    /* initializes all data */
    public Lobby(String name, String status, int index, ArrayList<Client> clientConnections) {
        lobby_name = name;
        lobby_status = status;
        lobbyIndex = index;
        player_count = 0;
        listOfClientConnections = clientConnections;
    }

    /* updates the lobby name */
    public void setName(String s) {
        lobby_name = s;
    }

    /* updates the lobby status */
    public void setFull(boolean b) {
        lobby_status = b == false ? "JOIN" : "FULL";
    }

    /* returns the lobby status */
    public boolean isFull() {
        return lobby_status.equals("JOIN") ? false : true;
    }

    /* return a string list of all users in lobby */
    public String getUsers() {
        String users = "";

        for (Map.Entry<String, Integer> entry : connected_clients.entrySet()) {
            String user = entry.getKey();

            users += ("\n\t- " + user);
        }

        return users;
    }

    /* return all connected clients */
    public HashMap<String, Integer> getClients() {
        return connected_clients;
    }

    /* returns highest points in lobby */
    public int getFirstPlace() {

        int max = -1;

        for (Map.Entry<String, Integer> entry : connected_clients.entrySet()) {
            int points = entry.getValue();

            if (points > max) {
                max = points;
            }
        }
        return max;
    }

    /* add the specified client(s) by username to the lobby */
    public void addUsers(String... users) {
        for (String s : users) {
            connected_clients.put(s, 0);

            //set player index's
            if(player_count == 0){ p1 = this.getClientIndexFromUsername(s);}
            else if(player_count == 1){ p2 = this.getClientIndexFromUsername(s);}
            else if(player_count == 2){ p3 = this.getClientIndexFromUsername(s);}
            else if(player_count == 3){ p4 = this.getClientIndexFromUsername(s);}

        }
        player_count = connected_clients.size();


        if(player_count == 4){
            setFull(true);
            createGame();
        }
    }

    /* remove the specified client(s) by username from the lobby */
    public void removeUsers(String... users) {
        for (String s : users) {
            connected_clients.remove(s);
        }
        player_count = connected_clients.size();
    }

    /* returns the lobby size */
    public int getLobbySize() {
        return player_count;
    }

    /* clears the lobby */
    public void clearLobby() {
        lobby_name = "[cleared]";
        lobby_status = "FULL";
        connected_clients.clear();
        player_count = 0;
    }

    /* print lobby data as string to console */
    private void print() {
        String result = lobby_name + "\t|\t" + lobby_status + "\t|\t" + player_count + " connected";

        for (Map.Entry<String, Integer> entry : connected_clients.entrySet()) {
            String user = entry.getKey();
            int points = entry.getValue();

            result += ("\n\t- " + user + ", " + points + " points");
        }

        System.out.println(result);
    }

    public String getLobbyName(){ return lobby_name; }

    //returns a list of usernames in the lobby
    public ArrayList<String> getLobbyUserNames(){
        ArrayList<String> usernames = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : connected_clients.entrySet()) {
            String user = entry.getKey();

            usernames.add(user);
        }

        return usernames;
    }

    private void createGame(){
        lobbyGame = new Game(p1, p2, p3, p4, listOfClientConnections, lobby_name);
    }

    //Function to get clients ID index given a username
    public int getClientIndexFromUsername(String uName){
        for(int i = 0; i<listOfClientConnections.size(); i++){
            if(uName.equals(listOfClientConnections.get(i).userName)){
                return listOfClientConnections.get(i).connectionID;
            }
        }
        return -1; //player name not found
    }


    //lobby sends done_typing index to game
    public void sendUserIndexToGame(int playerIndex){
        lobbyGame.sendDoneTyping(playerIndex);
    }

}

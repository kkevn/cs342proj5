import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lobby {
    
    // lobby data
    String lobby_name;
    String lobby_status;
    HashMap<String, Integer> connected_clients = new HashMap<>();
    int player_count;

    int lobbyIndex;


    //--TODO - ADD LOBBY FUNCTION THAT CAN UPDATE GAME GUI
    public void updateWinnerScore(String winnerName){

        //get winners current score and increment it by 1
        int winnersNewScore = connected_clients.get(winnerName) + 1;
        connected_clients.put(winnerName, winnersNewScore);
        System.out.println(winnerName + "'s new score is " + connected_clients.get(winnerName));

    }

        
    /* initializes all data */
    public Lobby(String name, String status, int index) {
        lobby_name = name;
        lobby_status = status;
        lobbyIndex = index;
        player_count = 0;
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
    
    /* returns top three scores in lobby */
    public int[] getTopScores() {

        int first = 0, second = 0, third = 0;
        
        for (Map.Entry<String, Integer> entry : connected_clients.entrySet()) {
            int points = entry.getValue();
            
            // new first place score found
            if (points > first) {
                third = second; 
                second = first; 
                first = points; 
            } 
        
            // new second place score found
            else if (points > second) {
                third = second; 
                second = points; 
            } 
        
            // new third place score found
            else if (points > third) {
                third = points;
            }
        }
        
        return new int[] {first, second, third};
    }
    
    /* add the specified client(s) by username to the lobby */
    public void addUsers(String... users) {
        for (String s : users) {
            connected_clients.put(s, 0);
        }
        player_count = connected_clients.size();

        if(player_count == 4){
            setFull(true);
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

}

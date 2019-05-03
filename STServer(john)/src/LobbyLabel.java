import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class LobbyLabel {
    
    // text components
    final Text t_label;
    final Text t_status;
    final Text t_open;
    final Text t_count;
    final Text t_slash;
    final Text t_min;
    final Text t_close;
        
    // styles
    Font font_default = Font.font("Calibri", FontPosture.REGULAR, 24);
    Color c_label = Color.NAVY;
    Color c_status1 = Color.RED;
    Color c_status2 = Color.GREEN;
    Color c_num = Color.NAVY;
    Color c_mark = Color.BLACK;
        
    /* initializes all components */
    public LobbyLabel(Lobby l) {
        t_label = new Text("\t" + l.lobby_name);
            t_label.setFont(font_default);
            t_label.setFill(c_label);
        t_status = new Text(l.lobby_status);
            t_status.setFont(font_default);
            t_status.setFill(l.lobby_status.equals("FULL") ? c_status1 : c_status2);
        t_open = new Text("[ ");
            t_open.setFont(font_default);
            t_open.setFill(c_mark);
        t_count = new Text("" + l.player_count);
            t_count.setFont(font_default);
            t_count.setFill(c_num);
        t_slash = new Text(" / ");
            t_slash.setFont(font_default);
            t_slash.setFill(c_mark);
        t_min = new Text("4");
            t_min.setFont(font_default);
            t_min.setFill(c_num);
        t_close = new Text(" ]");
            t_close.setFont(font_default);
            t_close.setFill(c_mark);
            
        print();
    }
    
    /* print lobby as string to console */
    private void print() {
        String result = t_label.getText() + "\t" + t_status.getText() + "\t" +
                        t_open.getText() + t_count.getText() + t_slash.getText() + 
                        t_min.getText() + t_close.getText(); 
        
        System.out.println(result);
    }
    
    /* returns the lobby as an array of its text objects */
    public Text[] getAsTextArray() {
        Text[] t_arr = new Text[7];
        
        t_arr[0] = t_label;
        t_arr[1] = t_status;
        t_arr[2] = t_open;
        t_arr[3] = t_count;
        t_arr[4] = t_slash;
        t_arr[5] = t_min;
        t_arr[6] = t_close;
        
        return t_arr;
    }

    public void updateLobbyToFull(Lobby l){
        t_status.setFill(l.lobby_status.equals("FULL") ? c_status1 : c_status2);
    }
    
}

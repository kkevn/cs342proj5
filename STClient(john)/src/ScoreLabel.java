import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class ScoreLabel {
    
    // text components
    final Text t_name;
    final Text t_status;
    final Text t_points;
        
    // styles
    Font font_default = Font.font("Calibri", FontPosture.REGULAR, 24);
    Color c_label = Color.NAVY;
    Color c_status1 = Color.ORANGE;
    Color c_status2 = Color.BLUE;
    Color c_num = Color.NAVY;
        
    /* initializes all components */
    public ScoreLabel(String name, boolean done, int score) {
        t_name = new Text(name);
            t_name.setFont(font_default);
            t_name.setFill(c_label);
        t_status = new Text(done == false ? "Typing" : " Done ");
            t_status.setFont(font_default);
            t_status.setFill(done == false ? c_status1 : c_status2);
        t_points = new Text(score + " points");
            t_points.setFont(font_default);
            t_points.setFill(c_num);
            
        print();
    }
    
    /* returns point value of this score label */
    public int getPoints() {
        return Integer.parseInt(t_points.getText().replace(" points", ""));
    }
    
    /* print score as string to console */
    private void print() {
        String result = t_name.getText() + "\t" + t_status.getText() + "\t" + t_points.getText();
        
        System.out.println(result);
    }
    
    /* returns the score as an array of its text objects */
    public Text[] getAsTextArray() {
        Text[] t_arr = new Text[3];
        
        t_arr[0] = t_name;
        t_arr[1] = t_status;
        t_arr[2] = t_points;
        
        return t_arr;
    }
    
}

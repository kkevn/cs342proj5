/******************************************************************************
 * CS 342 - Project 5, Server GUI - Speed Typing Game in JavaFX
 ******************************************************************************
 * Kevin Kowalski  - kkowal28@uic.edu
 * John Oshana     - joshan3@uic.edu
 * Frankie Ramirez - framir23@uic.edu
 * Alec Thomas     - athoma86@uic.edu
 ******************************************************************************
 * Description:
 *  Creates the server's GUI using JavaFX components.
 ******************************************************************************/

import java.io.Serializable;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class frontEndServer extends Application {

    /* useful variables */
    private int default_port = 5555;
    private String default_ip = "127.0.0.1";
    private int lobby_count = 0;
    Server server;

    /* connection object */
    //private NetworkConnection conn = createServer(default_port);

    /* universal elements */
    private Stage mainStage;
    private Scene s_connect, s_search;
    private Font font, t_font, tf_font;
    private BackgroundImage bi_wallpaper;

    /* setup screen components */
    private Text t_port;
    private TextField tf_port;
    private Button b_connect;
    private HBox hb_port;
    private VBox vb_connect;

    /* view screen components */
    private Text t_lobbies;
    private ListView lv_lobbies;
    private Button b_update;
    private HBox hb_title;
    private VBox vb_search;

    /* creates the scene for the setup screen */
    private Parent createSetupScene() {

        // port field
        t_port = new Text("Port: ");
        t_port.setFill(Color.WHITE);
        t_port.setFont(tf_font);
        tf_port = new TextField("" + default_port);
        tf_port.setFont(tf_font);
        tf_port.setMaxWidth(180);
        hb_port = new HBox();
        hb_port.getChildren().addAll(t_port, tf_port);
        hb_port.setAlignment(Pos.CENTER);

        // connect button
        b_connect = new Button("Setup Server");
        b_connect.setFont(font);
        b_connect.setAlignment(Pos.CENTER);
        b_connect.setMinHeight(50);
        b_connect.setMinWidth(180);
        b_connect.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_connect);
        b_connect.setOnMouseEntered(e -> setButtonHoverStyle(b_connect));
        b_connect.setOnMouseExited(e -> resetButtonStyle(b_connect));
        b_connect.setOnMousePressed(e -> setButtonPressedStyle(b_connect));
        b_connect.setOnAction(e -> connect());

        // layout for connect screen
        vb_connect = new VBox();
        vb_connect.getChildren().addAll(hb_port, b_connect);
        vb_connect.setAlignment(Pos.CENTER);
        vb_connect.setMinSize(300, 200);
        vb_connect.setMargin(hb_port, new Insets(0, 0, 16, 0));
        vb_connect.setMargin(b_connect, new Insets(16, 0, 0, 0));
        vb_connect.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 800px by 800px
        //s_connect = new Scene(vb_connect, 800, 800);

        return vb_connect;
    }

    /* creates the scene for the viewing lobbies */
    private void createViewScene() {

        // text label
        t_lobbies = new Text("Online Lobbies:\t(" + lobby_count + ")");
        t_lobbies.setFill(Color.WHITE);
        t_lobbies.setFont(tf_font);
        hb_title = new HBox();
        hb_title.getChildren().add(t_lobbies);
        hb_title.setAlignment(Pos.BASELINE_RIGHT);

        // list view for clients
        lv_lobbies = new ListView();
        lv_lobbies.getItems().add(new Text("test"));
        lv_lobbies.setStyle("-fx-control-inner-background: rgba(200, 200, 200);"
                + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 25%);");

        // connect button
        b_update = new Button("Update Lobby List");
        b_update.setFont(font);
        b_update.setAlignment(Pos.CENTER);
        b_update.setMinHeight(50);
        b_update.setMinWidth(180);
        b_update.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_update);
        b_update.setOnMouseEntered(e -> setButtonHoverStyle(b_update));
        b_update.setOnMouseExited(e -> resetButtonStyle(b_update));
        b_update.setOnMousePressed(e -> setButtonPressedStyle(b_update));
        b_update.setOnAction(e -> updateLobbyList());

        // layout for connect screen
        vb_search = new VBox();
        vb_search.getChildren().addAll(hb_title, lv_lobbies, b_update);
        vb_search.setAlignment(Pos.CENTER);
        vb_search.setPadding(new Insets(16, 32, 16, 32));
        vb_search.setMargin(t_lobbies, new Insets(10, 0, 16, 0));
        vb_search.setMargin(lv_lobbies, new Insets(0, 0, 16, 0));
        vb_search.setMargin(b_update, new Insets(16, 0, 0, 0));
        vb_search.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 600px by 600px
        s_search = new Scene(vb_search, 600, 600);

        //return vb_search;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // store refernce to primaryStage
        mainStage = primaryStage;

        // initialize random background image
        bi_wallpaper = new BackgroundImage(new Image("bg" + (new Random().nextInt(2) + 1) + ".png", 0, 0, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        // initialize UI fonts
        font = Font.font("Trebuchet MS", FontPosture.ITALIC, 64);
        t_font = Font.font("Calibri", FontPosture.REGULAR, 24);
        tf_font = Font.font("Lucida Console", FontPosture.REGULAR, 24);

        // load all three scenes and jump to connect scene
        s_connect = new Scene(createSetupScene());
        createViewScene();
        mainStage.setTitle("[Server] - Setup Server");
        mainStage.setScene(s_connect);
        mainStage.setResizable(false);
        mainStage.show();
        b_connect.requestFocus();
    }
    
    /* sets the GUI to the specified mode */
    public void setCurrentScene(int mode) {

        // title variable for each scene
        String title = "[Server] - ";

        // set scene according to specified parameter
        switch (mode) {

            // mode 0 -> set to setup screen
            case 0:
                title += "Setup Server";
                mainStage.setScene(s_connect);
                break;

            // mode 1 -> set to clients screen
            case 1:
                title += "View Connected Lobbies";
                mainStage.setScene(s_search);
                updateLobbyList();
                lv_lobbies.getSelectionModel().clearSelection();
                break;
        }

        // set scene title, prevent resizing of window, and show the scene
        mainStage.setTitle(title);
        mainStage.setResizable(false);
        mainStage.show();
    }

   /* resets the specified button's style */
    private void resetButtonStyle(Button b) {
        b.setStyle("-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);"
                + "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );"
                + "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 15 15 15;"
                + "-fx-text-fill: ghostwhite;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Calibri;"
                + "-fx-font-size: 28px;");
    }
    
    /* adjusts the specified button's style for hover event */
    private void setButtonHoverStyle(Button b) {
        b.setStyle("-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #ea7f4b, #c54e2c);"
                + "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );"
                + "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 15 15 15;"
                + "-fx-text-fill: ghostwhite;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Calibri;"
                + "-fx-font-size: 28px;");
    }
    
    /* adjusts the specified button's style for press event */
    private void setButtonPressedStyle(Button b) {
        b.setStyle("-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);"
                + "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );"
                + "-fx-background-insets: 2 0 0 0, 2 0 3 0, 2 0 4 0, 2 0 5 0;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 10 15 13 15;"
                + "-fx-text-fill: ghostwhite;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Calibri;"
                + "-fx-font-size: 28px;");
    }

    /* sets up a server on valid text field input */
    private void connect() {

        try {

            // get port from text field
            int port = Integer.parseInt(tf_port.getText());

            // setup new server connection
            server = new Server(port, this);
            server.start();

            // set to search scene
            setCurrentScene(1);
        }
        catch (Exception e) {
            System.out.println("> Bad port [" + tf_port.getText() + "]");
        }
    }

    /* updates the list view with all available clients */
    private void updateLobbyList() {

        // clear the list before populating it
        lv_lobbies.getItems().clear();
        lobby_count = 0;
        
        // TODO - erase if getTotalLobbies is made
        t_lobbies.setText("Online Lobbies:\t(" + server.getTotalClients() + ")");
        
        // need getTotalLobbies function - TODO
        //t_lobbies.setText("Online Lobbies:\t(" + server.getTotalLobbies() + ")");
        
        
        // TODO
        // the below loop needs to be reworked for iterating through all
        // lobby objects known by the server
        
        if (server.getListOfClientConnections().size() >= 1) {
            for (int i = 0; i < server.getListOfClientConnections().size(); i++) {
                if (server.getListOfClientConnections().get(i).score != -999)
                    lv_lobbies.getItems().add(new Text(server.getListOfClientConnections().get(i).userName));
                
                    /* TODO
                    remove the above line and uncomment the next three lines. 
                
                    String name = server.getListOfClientConnections().get(i).lobbyName;
                    String status = server.getListOfClientConnections().get(i).lobbyStatus;
                    
                    addLobby(new Lobby(name, status, server.getListOfClientConnections().get(i).clients));
                    */
            }
        }
    }
    
    /* apppend specified lobby object to list view */
    public void addLobby(Lobby l) {
        
        // store lobby into a label-able version
        Text[] t_arr = new LobbyLabel(l).getAsTextArray();
        
        // pane region to hold custom lobby object
        FlowPane fp = new FlowPane();
        fp.getChildren().addAll(t_arr[0], new Text("\t\t\t\t"), t_arr[1], 
                                new Text("\t\t\t\t"), t_arr[2], t_arr[3], 
                                t_arr[4], t_arr[5], t_arr[6]);
        
        // tooltip object to list connected players
        String hoverText = "Players in " + t_arr[0].getText().trim() + ":" + l.getUsers();       
        Tooltip tt = new Tooltip(hoverText);
        tt.setFont(t_font);
        Tooltip.install(fp, tt);
        
        // add pane region to cell in list view
        lv_lobbies.getItems().add(fp);
    }

}
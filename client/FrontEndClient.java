/******************************************************************************
 * CS 342 - Project 5, Client GUI - Speed Typing Game in JavaFX
 ******************************************************************************
 * Kevin Kowalski  - kkowal28@uic.edu
 * John Oshana     - joshan3@uic.edu
 * Frankie Ramirez - framir23@uic.edu
 * Alec Thomas     - athoma86@uic.edu
 ******************************************************************************
 * Description:
 *  Creates the client's GUI using JavaFX components.
 ******************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FrontEndClient extends Application {

    /* useful variables */
    private int default_port = 5555;
    private String default_ip = "127.0.0.1";
    private String username = "";
    public int score = 0, rounds = 1, status = -1;
    public String word = "";
    ArrayList<String> lobby_players;
    private Lobby connected_lobby;
    
    private Client client;

    /* universal elements */
    private Stage mainStage;
    private Scene s_connect, s_search, s_setup, s_game;
    private Font font, font2, t_font, tf_font, round_font;
    private BackgroundImage bi_wallpaper;

    /* login screen components */
    private int title_pos = 0;
    private boolean title_blink = true;
    private DropShadow ds;
    private Text t_title, t_username, t_port, t_ip;
    private Timeline tl_animate, tl_blink;
    private TextField tf_username, tf_port, tf_ip;
    private Button b_connect;
    private HBox hb_title, hb_username, hb_port, hb_ip;
    private VBox vb_title, vb_inputs, vb_connect;

    /* search screen components */
    private Text t_search;
    private ListView<FlowPane> lv_lobbies;
    private Button b_create, b_play, b_quit;
    private HBox hb_label, hb_buttons;
    private VBox vb_list, vb_search;
    
    /* lobby screen components */
    private Text t_create, t_lobby, t_mode;
    private TextField tf_lobby;
    private ComboBox cb_mode;
    private Button b_setup, b_return;
    private HBox hb_create, hb_lobby, hb_mode;
    private VBox vb_create, vb_lobby, vb_setup, vb_buttons;

    /* game screen components */
    private BorderPane bp_layout;
    private HBox hb_rounds;
    private VBox vb_game, vb_scores;
    private Text t_player, t_word, t_rounds;
    private TextField tf_word;
    private ListView<FlowPane> lv_scores;

    /* creates the scene for the login screen */
    private Parent createConnectScene() {

        // drop shadow effect for title text
        ds = new DropShadow();
        ds.setOffsetY(1.0f);
        ds.setOffsetX(5.0f);
        ds.setColor(Color.BLACK);

        // title text
        t_title = new Text("Speed Typer");
        t_title.setEffect(ds);
        t_title.setCache(true);
        t_title.setFill(Color.GOLD);
        t_title.setFont(font);
        animateTitle();

        // layout for title text components
        hb_title = new HBox();
        hb_title.getChildren().add(t_title);
        hb_title.setAlignment(Pos.CENTER);
        vb_title = new VBox();
        vb_title.getChildren().addAll(hb_title);

        // username field
        t_username = new Text("\t   Username: ");
        t_username.setFill(Color.GHOSTWHITE);
        t_username.setFont(t_font);
        t_username.setStyle("-fx-font-weight: bold");
        tf_username = new TextField();
        tf_username.setFont(tf_font);
        tf_username.setMaxWidth(180);
        tf_username.setOnKeyTyped(e -> enableConnectOnInputs());
        hb_username = new HBox();
        hb_username.getChildren().addAll(t_username, tf_username);
        hb_username.setAlignment(Pos.CENTER);

        // ip field
        t_ip = new Text("\tI.P. Address: ");
        t_ip.setFill(Color.GHOSTWHITE);
        t_ip.setFont(t_font);
        t_ip.setStyle("-fx-font-weight: bold");
        tf_ip = new TextField(default_ip);
        tf_ip.setFont(tf_font);
        tf_ip.setMaxWidth(180);
        tf_ip.setOnKeyTyped(e -> enableConnectOnInputs());
        hb_ip = new HBox();
        hb_ip.getChildren().addAll(t_ip, tf_ip);
        hb_ip.setAlignment(Pos.CENTER);

        // port field
        t_port = new Text("\tServer Port: ");
        t_port.setFill(Color.GHOSTWHITE);
        t_port.setFont(t_font);
        t_port.setStyle("-fx-font-weight: bold");
        tf_port = new TextField("" + default_port);
        tf_port.setFont(tf_font);
        tf_port.setMaxWidth(180);
        tf_port.setOnKeyTyped(e -> enableConnectOnInputs());
        hb_port = new HBox();
        hb_port.getChildren().addAll(t_port, tf_port);
        hb_port.setAlignment(Pos.CENTER);
        
        // layout for inputs
        vb_inputs = new VBox();
        vb_inputs.getChildren().addAll(hb_username, hb_ip, hb_port);
        vb_inputs.setMaxWidth(450);
        vb_inputs.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50, 0.5), new CornerRadii(24), new Insets(-8))));
        vb_inputs.setMargin(hb_username, new Insets(10, 0, 16, 0));
        vb_inputs.setMargin(hb_ip, new Insets(0, 0, 16, 0));
        vb_inputs.setMargin(hb_port, new Insets(0, 0, 16, 0));

        // connect button
        b_connect = new Button("Connect to Server");
        b_connect.setFont(font);
        b_connect.setAlignment(Pos.CENTER);
        b_connect.setMinHeight(50);
        b_connect.setMinWidth(300);
        b_connect.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_connect);
        b_connect.setOnMouseEntered(e -> setButtonHoverStyle(b_connect));
        b_connect.setOnMouseExited(e -> resetButtonStyle(b_connect));
        b_connect.setOnMousePressed(e -> setButtonPressedStyle(b_connect));
        b_connect.setOnAction(e -> connect());
        b_connect.setDisable(true);

        // layout for connect screen
        vb_connect = new VBox();
        vb_connect.getChildren().addAll(vb_title, vb_inputs, b_connect);
        vb_connect.setAlignment(Pos.CENTER);
        vb_connect.setMinSize(600, 400);
        vb_connect.setMargin(vb_title, new Insets(0, 0, 16, 0));
        vb_connect.setMargin(vb_inputs, new Insets(0, 0, 16, 0));
        vb_connect.setMargin(b_connect, new Insets(16, 0, 0, 0));
        vb_connect.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 600px by 400px
        //s_connect = new Scene(vb_connect, 600, 400);
        return vb_connect;
    }

    /* creates the scene for searching clients */
    private void createSearchScene() {

        // text label
        t_search = new Text("Logged in as:\t" + username + "\n\nLobby Name | Status | Player Count");
        t_search.setFill(Color.GHOSTWHITE);
        t_search.setFont(tf_font);
        hb_label = new HBox();
        hb_label.getChildren().add(t_search);
        hb_label.setAlignment(Pos.CENTER);

        // list view for clients
        lv_lobbies = new ListView<FlowPane>();
        lv_lobbies.setStyle("-fx-control-inner-background: rgba(200, 200, 200);"
                + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 25%);");
        lv_lobbies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FlowPane>() {

            // set listener for list view that only enables connect button on a selection
            @Override
            public void changed(ObservableValue<? extends FlowPane> observable, FlowPane oldValue, FlowPane newValue) {
                b_play.setDisable(false);
            }
        });
        
        // create button
        b_create = new Button("New Lobby");
        b_create.setFont(font);
        b_create.setAlignment(Pos.CENTER);
        b_create.setMinHeight(50);
        b_create.setMinWidth(180);
        b_create.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_create);
        b_create.setOnMouseEntered(e -> setButtonHoverStyle(b_create));
        b_create.setOnMouseExited(e -> resetButtonStyle(b_create));
        b_create.setOnMousePressed(e -> setButtonPressedStyle(b_create));
        b_create.setOnAction(e -> setCurrentScene(2));
        
        // connect button
        b_play = new Button("Join Lobby");
        b_play.setFont(font);
        b_play.setAlignment(Pos.CENTER);
        b_play.setMinHeight(50);
        b_play.setMinWidth(180);
        b_play.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_play);
        b_play.setOnMouseEntered(e -> setButtonHoverStyle(b_play));
        b_play.setOnMouseExited(e -> resetButtonStyle(b_play));
        b_play.setOnMousePressed(e -> setButtonPressedStyle(b_play));
        b_play.setOnAction(e -> play());
        b_play.setDisable(true);

        // quit button
        b_quit = new Button("Logout");
        b_quit.setFont(font);
        b_quit.setAlignment(Pos.CENTER);
        b_quit.setMinHeight(50);
        b_quit.setMinWidth(180);
        b_quit.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_quit);
        b_quit.setOnMouseEntered(e -> setButtonHoverStyle(b_quit));
        b_quit.setOnMouseExited(e -> resetButtonStyle(b_quit));
        b_quit.setOnMousePressed(e -> setButtonPressedStyle(b_quit));
        b_quit.setOnAction(e -> logout());
        
        hb_buttons = new HBox();
        hb_buttons.getChildren().addAll(b_create, b_play, b_quit);
        hb_buttons.setAlignment(Pos.BASELINE_LEFT);
        hb_buttons.setMargin(b_create, new Insets(8));
        hb_buttons.setMargin(b_play, new Insets(8));
        hb_buttons.setMargin(b_quit, new Insets(8));
        
        // layout for lobby list
        vb_list = new VBox();
        vb_list.getChildren().addAll(hb_label, lv_lobbies);
        vb_list.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50, 0.75), new CornerRadii(12), new Insets(-8))));
        vb_list.setMargin(t_search, new Insets(10, 0, 16, 0));
        vb_list.setMargin(lv_lobbies, new Insets(0, 0, 16, 0));
        
        // layout for connect screen
        vb_search = new VBox();
        vb_search.getChildren().addAll(vb_list, hb_buttons);
        vb_search.setAlignment(Pos.CENTER);
        vb_search.setPadding(new Insets(16, 32, 16, 32));
        vb_search.setMargin(hb_buttons, new Insets(16, 0, 0, 0));
        vb_search.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 600px by 600px
        s_search = new Scene(vb_search, 600, 600);

        //return vb_search;
    }

    /* creates the scene for the lobby creation screen */
    private void createNewLobbyScene() {

        // title text
        t_create = new Text("Create a Lobby");
        t_create.setEffect(ds);
        t_create.setCache(true);
        t_create.setFill(Color.GOLD);
        t_create.setFont(font2);

        // layout for title text components
        hb_create = new HBox();
        hb_create.getChildren().add(t_create);
        hb_create.setAlignment(Pos.CENTER);
        vb_create = new VBox();
        vb_create.getChildren().addAll(hb_create);

        // lobby name field
        t_lobby = new Text("Lobby Name: ");
        t_lobby.setFill(Color.GHOSTWHITE);
        t_lobby.setFont(t_font);
        t_lobby.setStyle("-fx-font-weight: bold");
        tf_lobby = new TextField();
        tf_lobby.setFont(tf_font);
        tf_lobby.setMaxWidth(180);
        tf_lobby.setOnKeyTyped(e -> enableButtonOnTextField(b_setup, tf_lobby));
        hb_lobby = new HBox();
        hb_lobby.getChildren().addAll(t_lobby, tf_lobby);
        hb_lobby.setAlignment(Pos.CENTER);
        
        // layout for mode field
        t_mode = new Text("Game Mode: ");
        t_mode.setFill(Color.GHOSTWHITE);
        t_mode.setFont(t_font);
        t_mode.setStyle("-fx-font-weight: bold");
        cb_mode = new ComboBox();
        cb_mode.setPromptText("Select Mode");
        cb_mode.getItems().addAll("10-Rounds", "Time Attack");
        cb_mode.setMinWidth(180);
        cb_mode.setStyle("-fx-font-size: 18px; -fx-background-color: white;");
        hb_mode = new HBox();
        hb_mode.getChildren().addAll(t_mode, cb_mode);
        hb_mode.setAlignment(Pos.CENTER);
        //cb_mode.setDisable(true);
        
        // layout for inputs
        vb_lobby = new VBox();
        vb_lobby.getChildren().addAll(hb_lobby, hb_mode);
        vb_lobby.setMaxWidth(400);
        vb_lobby.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50, 0.5), new CornerRadii(24), new Insets(-8))));
        vb_lobby.setMargin(hb_lobby, new Insets(10, 0, 16, 0));
        vb_lobby.setMargin(hb_mode, new Insets(0, 0, 16, 0));

        // setup button
        b_setup = new Button("Create New Lobby");
        b_setup.setFont(font);
        b_setup.setAlignment(Pos.CENTER);
        b_setup.setMinHeight(50);
        b_setup.setMinWidth(300);
        b_setup.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_setup);
        b_setup.setOnMouseEntered(e -> setButtonHoverStyle(b_setup));
        b_setup.setOnMouseExited(e -> resetButtonStyle(b_setup));
        b_setup.setOnMousePressed(e -> setButtonPressedStyle(b_setup));
        b_setup.setOnAction(e -> createLobby());
        b_setup.setDisable(true);
        
        // return button
        b_return = new Button("Return to Lobbies");
        b_return.setFont(font);
        b_return.setAlignment(Pos.CENTER);
        b_return.setMinHeight(50);
        b_return.setMinWidth(300);
        b_return.setPadding(new Insets(16, 16, 16, 16));
        resetButtonStyle(b_return);
        b_return.setOnMouseEntered(e -> setButtonHoverStyle(b_return));
        b_return.setOnMouseExited(e -> resetButtonStyle(b_return));
        b_return.setOnMousePressed(e -> setButtonPressedStyle(b_return));
        b_return.setOnAction(e -> setCurrentScene(1));

        vb_buttons = new VBox();
        vb_buttons.getChildren().addAll(b_setup, b_return);
        vb_buttons.setAlignment(Pos.CENTER);
        vb_buttons.setMargin(b_setup, new Insets(8));
        vb_buttons.setMargin(b_return, new Insets(8));
        
        // layout for setup screen
        vb_setup = new VBox();
        vb_setup.getChildren().addAll(vb_create, vb_lobby, vb_buttons);
        vb_setup.setAlignment(Pos.CENTER);
        vb_setup.setMinSize(600, 400);
        vb_setup.setMargin(vb_title, new Insets(0, 0, 16, 0));
        vb_setup.setMargin(vb_inputs, new Insets(0, 0, 16, 0));
        vb_setup.setMargin(vb_buttons, new Insets(16, 0, 0, 0));
        vb_setup.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 800px by 800px
        s_setup = new Scene(vb_setup, 500, 400);
        
        //return vb_setup;
    }
    
    /* creates the game for the playing clients */
    private void createGameScene() {

        // player name label
        t_player = new Text(username);
        t_player.setFill(Color.WHITE);
        t_player.setFont(tf_font);

        // rounds label
        t_rounds = new Text("Round:\t" + rounds);
        t_rounds.setFill(Color.WHITE);
        t_rounds.setFont(round_font);
        
        // layout for score list
        hb_rounds = new HBox();
        hb_rounds.getChildren().add(t_rounds);
        hb_rounds.setAlignment(Pos.TOP_CENTER);
        hb_rounds.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50, 0.75), new CornerRadii(12), new Insets(8))));
        hb_rounds.setMargin(t_rounds, new Insets(24));
        
        // word label
        word = "Waiting for clients...";
        t_word = new Text(word);
        t_word.setFill(Color.WHITE);
        t_word.setFont(round_font);
        
        tf_word = new TextField();
        tf_word.setFont(tf_font);
        tf_word.setPromptText(word);
        tf_word.setDisable(true);
        tf_word.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);"
                        + "-fx-prompt-text-fill: rgb(70, 70, 70);"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;");
        //tf_word.setMaxWidth(180);
        tf_word.setOnKeyPressed(new EventHandler<KeyEvent>() {
            
            @Override
            public void handle(KeyEvent event) {
                
                // clear input on 'escape' key
                if (event.getCode() == KeyCode.ESCAPE) {
                    tf_word.clear();
                }
            }
        });
        
        // adjust color based on input accuracy
        tf_word.textProperty().addListener((observable, oldValue, newValue) -> {
            colorText();
        });
        
        // layout for game screen
        vb_game = new VBox();
        vb_game.getChildren().addAll(hb_rounds, t_word, tf_word);
        vb_game.setAlignment(Pos.CENTER);
        vb_game.setPadding(new Insets(32, 32, -32, 32));
        vb_game.setMargin(t_word, new Insets(100, 0, 120, 0));
        vb_game.setMargin(tf_word, new Insets(0, 0, 120, 0));
        
        // list view for scores
        lv_scores = new ListView<FlowPane>();
        lv_scores.setStyle("-fx-control-inner-background: rgba(200, 200, 200);"
                + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 25%);");
        lv_scores.setMinHeight(440);
        
        // layout for score list
        vb_scores = new VBox();
        vb_scores.getChildren().addAll(t_player, lv_scores);
        vb_scores.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50, 0.75), new CornerRadii(12), new Insets(8))));
        vb_scores.setMargin(t_player, new Insets(32, 0, 0, 32));
        vb_scores.setMargin(lv_scores, new Insets(24));
        
        // main layout of game scene
        bp_layout = new BorderPane();
        bp_layout.setLeft(vb_scores);
        bp_layout.setCenter(vb_game);
        bp_layout.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 800px by 800px
        s_game = new Scene(bp_layout, 750, 550);

        //return bp_layout;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        // store refernce to primaryStage
        mainStage = primaryStage;
        
        // initialize random background image
        bi_wallpaper = new BackgroundImage(new Image("bg" + (new Random().nextInt(2) + 1) + ".png", 0, 0, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        // initialize UI fonts
        font = Font.font("Trebuchet MS", FontPosture.ITALIC, 64);
        font2 = Font.font("Trebuchet MS", FontPosture.REGULAR, 48);
        t_font = Font.font("Calibri", FontPosture.REGULAR, 24);
        tf_font = Font.font("Lucida Console", FontPosture.REGULAR, 24);
        round_font = Font.font("Showcard Gothic", FontPosture.REGULAR, 48);

        // load all three scenes and jump to connect scene
        s_connect = new Scene(createConnectScene());
        createSearchScene();
        createNewLobbyScene();
        createGameScene();
        mainStage.setTitle("[Client] - Connect to Server");
        mainStage.setScene(s_connect);
        mainStage.setResizable(false);
        mainStage.show();
        b_connect.requestFocus();
    }

    //validate port function
    public boolean validatePortNum(String pNum) {
        int pn = 0;
        try {
            pn = Integer.parseInt(pNum);

            if (!(pn >= 1024 && pn <= 49151)) {
                System.out.println("Incorrect Range: " + pNum + " is not in 1024-49151 range");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Incorrect Format: " + pNum + " is not a integer");
            return false;
        }
    }

    /* prints to console messages received from server */
    public void setServerText(String message) {
        System.out.println("Server Sent:\n" + message);
    }
    
    /* connects to a server on valid text field inputs */
    private void connect() {

        try {

            // get username, IP, and port from text fields
            username = tf_username.getText();
            String ip = tf_ip.getText();
            int port = Integer.parseInt(tf_port.getText());

            // check if specified port is in range
            if (!(port >= 1024 && port <= 49151)) {
                System.out.println("> Selected port [" + port + "] is not in 1024-49151 range");
            }

            // setup new client connection
            client = new Client(ip, port, username, this);
            client.start();
            System.out.println("> Connected as '" + username + "' on " + ip + " : " + port);
            
            // update search scene label with username
            t_search.setText("Logged in as:\t" + username + "\n\nLobby Name | Status | Player Count");
            
            // set to search scene
            setCurrentScene(1);
        } catch (Exception e) {
            System.out.println("> Bad IP [" + tf_ip.getText() + "] or bad port [" + tf_port.getText() + "]");
        }
    }
    
    /* log user out from server and return to login screen */
    private void logout() {

        try {
            
            // logout current client connection
            client.logout();
            
            System.out.println("> Logged user [" + username + "] out of server");

            // reset default login inputs and return to login scene
            tf_username.clear();
            tf_ip.setText(default_ip);
            tf_port.setText("" + default_port);
            setCurrentScene(0);
            
        } catch (Exception e) {
            System.out.println("> Failed to log user [" + username + "] out of server");
        }
    }

    /* connects an available player to a game */
    private void play() {

        try {

            // get the selected lobby to join
            String selected_lobby = extractString(0);
            
            // get the selected lobby's status
            String selected_lobby_status = extractString(2);
            
            // check if selected lobby is availble and connect if so
            if (selected_lobby_status.equals("JOIN")) {
                
                client.joinLobby(selected_lobby);
                
                System.out.println("> Player [" + tf_username.getText() + "] joined [" + selected_lobby + "]");

                // updates username and opponent's name to UI of game screen - TODO
                t_player.setText(username);
                
                // store the connected lobby - TODO
                connected_lobby = client.getLobby();

                //update lobby list
                updateLobbyList();

                // set to game scene
                setCurrentScene(3);
            }
            else {
                notifyLobbyFull(selected_lobby);
                System.out.println("> Selected lobby [" + selected_lobby + "] is FULL");
            }

            
        } catch (Exception e) {
            System.out.println("> Seleced lobby [" + extractString(0) + "] unavailble");
        }
    }
    
    /* creates a new lobby */
    private void createLobby() {

        try {
            
            String new_lobby = tf_lobby.getText();
            
            // have server setup new lobby
            client.setupLobby(new_lobby);

            System.out.println("> New lobby [" + new_lobby + "] created by [" + username + "]");
            
            // join created lobby and store it - TODO
            client.joinLobby(new_lobby);
            connected_lobby = client.getLobby();
            updateLobbyList();
            
            // set to game scene
            setCurrentScene(3);
            
        } catch (Exception e) {
            System.out.println("> Failed creating a new lobby");
        }
    }

    /* tells server client successfully typed word */
    private void setClientDone() {

        try {
            //client.setDone(); - TODO
            client.GuiPlayerDoneTyping(this.client.getUserName());

            System.out.println("> Successfully typed [" + word + "]");
        } catch (Exception e) {
            System.out.println("> Failed to notify server of successfully typing [" + word + "]");
        }
    }

    /* sets the GUI to the specified mode */
    public void setCurrentScene(int mode) {

        // title variable for each scene
        String title = "[Client] - ";

        // set scene according to specified parameter
        switch (mode) {

            // mode 0 -> set to login screen
            case 0:
                title += "Connect to Server";
                mainStage.setScene(s_connect);
                break;

            // mode 1 -> set to search screen
            case 1:
                title += "Search for a Lobby";
                mainStage.setScene(s_search);
                updateLobbyList();
                lv_lobbies.getSelectionModel().clearSelection();
                b_play.setDisable(true);
                break;
                
            // mode 2 -> set to lobby screen
            case 2:
                title += "Create a Lobby";
                mainStage.setScene(s_setup);
                break;
            
            // mode 3 -> set to game screen
            case 3:
                title += "Playing against other Clients";
                mainStage.setScene(s_game);
                updateScoreList();
                // set focus to text field
                tf_word.requestFocus();
                break;
        }

        updateLobbyList();

        // set scene title, prevent resizing of window, and show the scene
        mainStage.setTitle(title);
        mainStage.setResizable(false);
        mainStage.show();
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
    
    /* apppend specified scorelabel object to list view */
    public void addScore(ScoreLabel sl, int first) {
        
        // store score into a label-able version
        Text[] t_arr = sl.getAsTextArray();
        
        // pane region to hold custom score object
        FlowPane fp = new FlowPane();
        fp.getChildren().addAll(t_arr[0], new Text("\t"), /*t_arr[1],*/
                                new Text("\t"), t_arr[2]);
        
        // mark as gold if in first
        if (sl.getPoints() == first) {
            fp.setStyle("-fx-background-color: linear-gradient(to right, gold, white);");
        }
        
        // add pane region to cell in list view
        lv_scores.getItems().add(fp);
    }
    
    /* updates the list view with all available lobbies */
    public void updateLobbyList() {

        // get data structure containing all lobbies - TODO
        //ArrayList<Lobby> available_lobbies = client.getLobbyList();
        
        //HashMap<String, Integer> test_clients = new HashMap<>();
        //test_clients.put("kevin", 0);
        //test_clients.put("john", 0);
        //test_clients.put("frankie", 0);
        //test_clients.put("alec", 0);
        
        // update client list (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // clear the list before populating it
                lv_lobbies.getItems().clear();

                // for each lobby in data structure - TODO
                for (Lobby l : client.getLobbyList()) {

                    // add current lobby to the list view
                    addLobby(l);
                }
                
                for (int i = 0; i < 6; i++) {
                   // addLobby(new Lobby("lobby_" + i, i < 3 ? "JOIN" : "FULL", i));
                }
            }
        });

    }
    
    /* updates the list view with all updated scores */
    public void updateScoreList() {

        // get current lobby data - TODO
        connected_lobby = client.getLobby();
        
        // get first place's point value = TODO
         //int first_place = connected_lobby.getFirstPlace();
        int first_place = 1;
        
        //HashMap<String, Integer> test_clients = new HashMap<>();
        //test_clients.put("kevin", 0);
        //test_clients.put("john", 0);
        //test_clients.put("frankie", 0);
        //test_clients.put("alec", 0);
        //test_clients.put(username, 1);
        
        // update score list (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // reset the list before populating it
                lv_scores.getItems().clear();
                lv_scores.setStyle("-fx-control-inner-background: rgba(200, 200, 200);"
                    + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 25%);");
                
                // for each player in lobby - TODO -- hopefully this updates the scores (Alec and Frankie)
                for (Map.Entry<String, Integer> entry : connected_lobby.getClients().entrySet()) {
                    String user = entry.getKey();
                    int points = entry.getValue();
                    
                    // add to score list
                    addScore(new ScoreLabel(user, false, points), first_place);
                }
                
                /*for (Map.Entry<String, Integer> entry : test_clients.entrySet()) {
                    String user = entry.getKey();
                    int points = entry.getValue();

                    addScore(new ScoreLabel(user, false, points), first_place);
                }*/
            }
        });

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
    
    /* animate title text to give typing effect */
    private void animateTitle() {
        
        // store title text and length
        String title = t_title.getText();
        int title_length = title.length();
        
        // create a typing animation that updates every 150ms
        tl_animate = new Timeline(new KeyFrame(Duration.millis(150), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                // display title text up to current poition marker
                if (title_pos <= title_length)
                    t_title.setText(title.substring(0, title_pos));
                
                // update current position marker
                title_pos++;
            }
        }));
        
        // play typing animation for each character in title text
        tl_animate.setCycleCount(title_length);
        tl_animate.play();
        
        // do a blinking cursor animation when typing animation finishes
        tl_animate.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                t_title.setText(title + " ");
                
                // create a blinkin cursor animation that blinks every 500ms
                tl_blink = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        
                        // show cursor if last frame did not
                        if (title_blink) {
                            t_title.setText(" " + title + "|");
                            title_blink = false;
                        }
                        
                        // otherwise hide the cursor
                        else {
                            t_title.setText(title + " ");
                            title_blink = true;
                        }
                    }
                }));
                
                // play blinking cursor animation indefinitely
                tl_blink.setCycleCount(Timeline.INDEFINITE);
                tl_blink.play();
                
                // reset position marker
                title_pos = 0;
            }
        });
    }
    
    /* extracts lobby name as string from selected lobby in the list view */
    private String extractString(int i) {
        String selection = lv_lobbies.getSelectionModel().getSelectedItem().getChildren().get(i).toString();
        selection = selection.substring(selection.indexOf("\"") + 1);
        selection = selection.substring(0, selection.indexOf("\""));

        return selection.trim();
    }
    
    /* enable the specified button only if the given textfield is not empty */
    private void enableButtonOnTextField(Button b, TextField tf) {
        if (tf.getText().length() > 0)
            b.setDisable(false);
        else
            b.setDisable(true);
    }
    
    /* enable the setup lobbby button only if the login inputs are not empty */
    private void enableConnectOnInputs() {
        
        if (tf_username.getText().length() > 0 && tf_ip.getText().length() > 0 && tf_port.getText().length() > 0)
            b_connect.setDisable(false);
        else
            b_connect.setDisable(true);
    }

    /* updates the game UI after every round */
    public void updateGameInfo(String newWord, int roundWinner) {
        
        // update word to type
        word = newWord;
        t_word.setText(word);
        tf_word.setPromptText(word);
        
        // update round counter
        t_rounds.setText("Round: " + ++rounds);

        status = roundWinner;
        System.out.println("yolo");
        
        // update score list every round
        updateScoreList();
        
        // set focus to text field
        tf_word.requestFocus();
        
        // update score counters based on round winner
        switch (status) {

            // client tied round
            case 0:
                //t_score.setText("" + ++score);
                //t_opponent_score.setText("" + ++opponent_score);
                break;

            // client won round
            case 1:
                //t_score.setText("" + ++score);
                break;

            // client lost round
            case 2:
                //t_opponent_score.setText("" + ++opponent_score);
                break;

            // client won game
            case 3:
                System.out.println("> Game won!");
                notifyGameOver("Game won!");
                break;

            // client lost game
            case 4:
                System.out.println("> Game lost!");
                notifyGameOver("Game lost!");
                break;

            // client tied game
            case 5:
                System.out.println("> Game tied!");
                notifyGameOver("Game tied!");
                break;
        }
        
        tf_word.setDisable(false);
    }
    
    /* display dialog box to confirm a game over */
    private void notifyGameOver(String status) {

        // spawn a dialog box (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // spawn the dialog box
                Alert alert = new Alert(AlertType.NONE, status + "\n\nClick OK to return to lobby view.", ButtonType.OK);
                alert.setTitle("Game Over");
                alert.setResizable(false);
                alert.showAndWait();

                // accept confirmation and reset game
                if (alert.getResult() == ButtonType.OK) {
                    resetGame();
                }
            }
        });
    }
    
    /* display dialog box to notify client that the lobby is full */
    private void notifyLobbyFull(String lobby) {

        // spawn a dialog box (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // spawn the dialog box
                Alert alert = new Alert(AlertType.INFORMATION, "The selected lobby '" + lobby + "' is full.", ButtonType.OK);
                alert.setTitle("Server is FULL");
                alert.setHeaderText("Failed to Join");
                alert.setResizable(false);
                alert.showAndWait();

                // accept confirmation and update lobby list
                if (alert.getResult() == ButtonType.OK) {
                    updateLobbyList();
                }
            }
        });
    }

    /* resets game variables and returns user to lobby scene */
    private void resetGame() {

        // reset game status
        status = -1;
        
        // reset word
        word = "";
        t_word.setText(word);
        tf_word.setPromptText(word);
        
        // reset round info
        rounds = 1;
        t_rounds.setText("Round:" + rounds);

        // reset score info
        score = 0;

        // take player back to search scene (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setCurrentScene(1);
            }
        });

    }
    
    /* highlights the textfield based on word accuracy */
    private void colorText() {
        
        // get lowercase sensitivty of text field
        String input = tf_word.getText().toLowerCase();
        
        // input matches word, notify server and prevent more typing
        if (input.equals(word)) {
            t_word.setFill(Color.GREEN);
            
            Platform.runLater(() -> { 
                tf_word.clear(); 
            });
            setClientDone();
            
            // TODO, remove updateGameInfo below and uncomment setDisable
            //updateGameInfo(randomWord(), 0);
            tf_word.setDisable(true);
        }
        
        // input is correct so far
        else if (input.length() > 0 && input.length() < word.length() && input.equals(word.substring(0, input.length()))) {
            t_word.setFill(Color.YELLOW);
        }
        
        // input is incorrect
        else {
            t_word.setFill(Color.RED);
        }
    }
    
    /* generates a random word */
    private String randomWord() {
        
        ArrayList<String> words = new ArrayList<>();
        words.add("hello");
        words.add("homework");
        words.add("project");
        words.add("thanos");
        words.add("avengers");
        words.add("computer");
        words.add("coding");
        words.add("exponential");
        words.add("generate");
        
        return words.get(new Random().nextInt(words.size()));
        
    }

}

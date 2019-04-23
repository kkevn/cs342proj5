
/** ****************************************************************************
 * CS 342 - Project 4, Client GUI - Updated R, P, S, L, S in JavaFX
 ******************************************************************************
 * Kevin Kowalski  - kkowal28@uic.edu
 * John Oshana     - joshan3@uic.edu
 * Frankie Ramirez - framir23@uic.edu
 * Alec Thomas     - athoma86@uic.edu
 ******************************************************************************
 * Description:
 *  Creates the client's GUI using JavaFX components.
 ***************************************************************************** */
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FrontEndClient extends Application {

    /*useful variables */
    private int default_port = 5555;
    private String default_ip = "127.0.0.1";
    private String username = "";
    private boolean isChallenger = false;
    public int score = 0, opponent_score = 0, rounds = 1, status = -1;

    private Client client;

    /* universal elements */
    private Stage mainStage;
    private Scene s_connect, s_search, s_game;
    private Font font, tf_font, score_font, round_font;
    private BackgroundImage bi_wallpaper;

    /* login screen components */
    private DropShadow ds;
    private Text t_rock, t_paper, t_scissors, t_lizard, t_spock, t_username, t_port, t_ip;
    private TextField tf_username, tf_port, tf_ip;
    private Button b_connect;
    private HBox hb_rps, hb_ls, hb_username, hb_port, hb_ip;
    private VBox vb_title, vb_connect;

    /* search screen components */
    private Text t_clients;
    private ListView lv_clients;
    private Button b_play, b_quit;
    private HBox hb_title, hb_buttons;
    private VBox vb_search;

    /* game screen components */
    private HBox hb_scores, hb_moves;
    private VBox vb_game, vb_player, vb_opponent;
    private Button b_rock, b_paper, b_scissors, b_lizard, b_spock;
    private Text t_player, t_score, t_last_move, t_opponent, t_opponent_score, t_opponent_last_move, t_rounds;
    private Tooltip tt_rock, tt_paper, tt_scissors, tt_lizard, tt_spock;

    /* creates the scene for the login screen */
    private Parent createConnectScene() {

        // drop shadow effect for title text
        ds = new DropShadow();
        ds.setOffsetY(1.0f);
        ds.setOffsetX(5.0f);
        ds.setColor(Color.BLACK);

        // rock text
        t_rock = new Text("Rock, ");
        t_rock.setEffect(ds);
        t_rock.setCache(true);
        t_rock.setFill(Color.SADDLEBROWN);
        t_rock.setFont(font);
        t_rock.setOnMouseEntered(e -> adjustText(true, t_rock));
        t_rock.setOnMouseExited(e -> adjustText(false, t_rock));

        // paper text
        t_paper = new Text("Paper, ");
        t_paper.setEffect(ds);
        t_paper.setCache(true);
        t_paper.setFill(Color.WHITESMOKE);
        t_paper.setFont(font);
        t_paper.setOnMouseEntered(e -> adjustText(true, t_paper));
        t_paper.setOnMouseExited(e -> adjustText(false, t_paper));

        // scissors text
        t_scissors = new Text("Scissors,");
        t_scissors.setEffect(ds);
        t_scissors.setCache(true);
        t_scissors.setFill(Color.SILVER);
        t_scissors.setFont(font);
        t_scissors.setOnMouseEntered(e -> adjustText(true, t_scissors));
        t_scissors.setOnMouseExited(e -> adjustText(false, t_scissors));

        // lizard text
        t_lizard = new Text("Lizard, ");
        t_lizard.setEffect(ds);
        t_lizard.setCache(true);
        t_lizard.setFill(Color.DARKOLIVEGREEN);
        t_lizard.setFont(font);
        t_lizard.setOnMouseEntered(e -> adjustText(true, t_lizard));
        t_lizard.setOnMouseExited(e -> adjustText(false, t_lizard));

        // spock text
        t_spock = new Text("Spock!");
        t_spock.setEffect(ds);
        t_spock.setCache(true);
        t_spock.setFill(Color.CADETBLUE);
        t_spock.setFont(font);
        t_spock.setOnMouseEntered(e -> adjustText(true, t_spock));
        t_spock.setOnMouseExited(e -> adjustText(false, t_spock));

        // layout for above title text components
        hb_rps = new HBox();
        hb_rps.getChildren().addAll(t_rock, t_paper, t_scissors);
        hb_rps.setAlignment(Pos.CENTER);
        hb_ls = new HBox();
        hb_ls.getChildren().addAll(t_lizard, t_spock);
        hb_ls.setAlignment(Pos.CENTER);
        vb_title = new VBox();
        vb_title.getChildren().addAll(hb_rps, hb_ls);

        // username field
        t_username = new Text("    Username: ");
        t_username.setFill(Color.WHITE);
        t_username.setFont(tf_font);
        tf_username = new TextField();
        tf_username.setFont(tf_font);
        tf_username.setMaxWidth(180);
        hb_username = new HBox();
        hb_username.getChildren().addAll(t_username, tf_username);
        hb_username.setAlignment(Pos.CENTER);

        // ip field
        t_ip = new Text("I.P. Address: ");
        t_ip.setFill(Color.WHITE);
        t_ip.setFont(tf_font);
        tf_ip = new TextField(default_ip);
        tf_ip.setFont(tf_font);
        tf_ip.setMaxWidth(180);
        hb_ip = new HBox();
        hb_ip.getChildren().addAll(t_ip, tf_ip);
        hb_ip.setAlignment(Pos.CENTER);

        // port field
        t_port = new Text("\tPort: ");
        t_port.setFill(Color.WHITE);
        t_port.setFont(tf_font);
        tf_port = new TextField("" + default_port);
        tf_port.setFont(tf_font);
        tf_port.setMaxWidth(180);
        hb_port = new HBox();
        hb_port.getChildren().addAll(t_port, tf_port);
        hb_port.setAlignment(Pos.CENTER);

        // connect button
        b_connect = new Button("Connect");
        b_connect.setFont(font);
        b_connect.setAlignment(Pos.CENTER);
        b_connect.setMinHeight(50);
        b_connect.setMinWidth(180);
        b_connect.setPadding(new Insets(16, 16, 16, 16));
        b_connect.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 18px;");
        b_connect.setOnMouseEntered(e -> adjustButton(true, b_connect, 6.0));
        b_connect.setOnMouseExited(e -> adjustButton(false, b_connect, 6.0));
        b_connect.setOnAction(e -> connect());

        // layout for connect screen
        vb_connect = new VBox();
        vb_connect.getChildren().addAll(vb_title, hb_username, hb_ip, hb_port, b_connect);
        vb_connect.setAlignment(Pos.CENTER);
        vb_connect.setMinSize(600, 400);
        vb_connect.setMargin(vb_title, new Insets(0, 0, 16, 0));
        vb_connect.setMargin(hb_username, new Insets(10, 0, 16, 0));
        vb_connect.setMargin(hb_ip, new Insets(0, 0, 16, 0));
        vb_connect.setMargin(hb_port, new Insets(0, 0, 16, 0));
        vb_connect.setMargin(b_connect, new Insets(16, 0, 0, 0));
        vb_connect.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 800px by 800px
        //s_connect = new Scene(vb_connect, 800, 800);
        return vb_connect;
    }

    /* creates the scene for the searching clients */
    private void createSearchScene() {

        // text label
        t_clients = new Text("Online Clients:");
        t_clients.setFill(Color.WHITE);
        t_clients.setFont(tf_font);
        hb_title = new HBox();
        hb_title.getChildren().add(t_clients);
        hb_title.setAlignment(Pos.BASELINE_LEFT);

        // list view for clients
        lv_clients = new ListView();
        lv_clients.getItems().add(new Text("me"));
        lv_clients.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Text>() {

            @Override
            public void changed(ObservableValue<? extends Text> observable, Text oldValue, Text newValue) {
                b_play.setDisable(false);
            }
        });

        // connect button
        b_play = new Button("Play Selected Client");
        b_play.setFont(font);
        b_play.setAlignment(Pos.CENTER);
        b_play.setMinHeight(50);
        b_play.setMinWidth(180);
        b_play.setPadding(new Insets(16, 16, 16, 16));
        b_play.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 18px;");
        b_play.setOnMouseEntered(e -> adjustButton(true, b_play, 2.0));
        b_play.setOnMouseExited(e -> adjustButton(false, b_play, 2.0));
        b_play.setOnAction(e -> play());
        b_play.setDisable(true);

        // quit button
        /*
        b_quit = new Button("Exit Application");
        b_quit.setFont(font);
        b_quit.setAlignment(Pos.CENTER);
        b_quit.setMinHeight(50);
        b_quit.setMinWidth(180);
        b_quit.setPadding(new Insets(16, 16, 16, 16));
        b_quit.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 18px;");
        b_quit.setOnMouseEntered(e -> adjustButton(true, b_quit, 2.0));
        b_quit.setOnMouseExited(e -> adjustButton(false, b_quit, 2.0));
        b_quit.setOnAction(e -> client.sendChoiceToGame("quit"));
        
        hb_buttons = new HBox();
        hb_buttons.getChildren().addAll(b_play, b_quit);
        hb_buttons.setAlignment(Pos.BASELINE_LEFT);
        hb_buttons.setMargin(b_play, new Insets(8));
        hb_buttons.setMargin(b_quit, new Insets(8));
         */
        // layout for connect screen
        vb_search = new VBox();
        //vb_search.getChildren().addAll(hb_title, lv_clients, hb_buttons);
        vb_search.getChildren().addAll(hb_title, lv_clients, b_play);
        vb_search.setAlignment(Pos.CENTER);
        vb_search.setPadding(new Insets(16, 32, 16, 32));
        vb_search.setMargin(t_clients, new Insets(10, 0, 16, 0));
        vb_search.setMargin(lv_clients, new Insets(0, 0, 16, 0));
        vb_search.setMargin(b_play, new Insets(16, 0, 0, 0));
        //vb_search.setMargin(hb_buttons, new Insets(16, 0, 0, 0));
        vb_search.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 600px by 600px
        s_search = new Scene(vb_search, 600, 600);

        //return vb_search;
    }

    /* creates the game for the playing clients */
    private void createGameScene() {

        // player info labels
        t_player = new Text(username);
        t_player.setFill(Color.WHITE);
        t_player.setFont(tf_font);
        t_score = new Text("" + score);
        t_score.setFill(Color.WHITE);
        t_score.setFont(score_font);
        t_last_move = new Text("Last Move: ");
        t_last_move.setFill(Color.WHITE);
        t_last_move.setFont(tf_font);
        vb_player = new VBox();
        vb_player.getChildren().addAll(t_player, t_score, t_last_move);
        vb_player.setAlignment(Pos.BASELINE_LEFT);
        vb_player.setMargin(t_score, new Insets(10, 0, 16, 0));
        vb_player.setMargin(t_last_move, new Insets(10, 0, 16, 0));

        // opponent info labels
        t_opponent = new Text("Opponent");
        t_opponent.setFill(Color.WHITE);
        t_opponent.setFont(tf_font);
        t_opponent_score = new Text("" + opponent_score);
        t_opponent_score.setFill(Color.WHITE);
        t_opponent_score.setFont(score_font);
        t_opponent_last_move = new Text("Last Move: ");
        t_opponent_last_move.setFill(Color.WHITE);
        t_opponent_last_move.setFont(tf_font);
        vb_opponent = new VBox();
        vb_opponent.getChildren().addAll(t_opponent, t_opponent_score, t_opponent_last_move);
        vb_opponent.setAlignment(Pos.BASELINE_RIGHT);
        vb_opponent.setMargin(t_opponent_score, new Insets(10, 0, 16, 0));
        vb_opponent.setMargin(t_opponent_last_move, new Insets(10, 0, 16, 0));

        // rounds label
        t_rounds = new Text("Round:\t" + rounds);
        t_rounds.setFill(Color.WHITE);
        t_rounds.setFont(round_font);

        hb_scores = new HBox();
        hb_scores.getChildren().addAll(vb_player, vb_opponent);
        hb_scores.setAlignment(Pos.CENTER);
        hb_scores.setMargin(vb_player, new Insets(0, 375, 0, 0));

        // rock button
        b_rock = new Button("\u270A");
        b_rock.setAlignment(Pos.CENTER);
        b_rock.setMinHeight(64);
        b_rock.setMaxWidth(64);
        b_rock.setPadding(new Insets(16, 16, 16, 16));
        b_rock.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 64;");
        tt_rock = new Tooltip("Rock beats:\n - Scissors\n - Lizard");
        tt_rock.setStyle("-fx-font-size: 18");
        b_rock.setTooltip(tt_rock);
        b_rock.setOnMouseEntered(e -> adjustButton(true, b_rock, 8.0));
        b_rock.setOnMouseExited(e -> adjustButton(false, b_rock, 8.0));
        b_rock.setOnAction(e -> makeMove("rock"));

        // paper button
        //b_paper = new Button("\u270B");
        b_paper = new Button("📜");
        b_paper.setAlignment(Pos.CENTER);
        b_paper.setMinHeight(64);
        b_paper.setMaxWidth(64);
        b_paper.setPadding(new Insets(16, 16, 16, 16));
        b_paper.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 64;");
        tt_paper = new Tooltip("Paper beats:\n - Rock\n - Spock");
        tt_paper.setStyle("-fx-font-size: 18");
        b_paper.setTooltip(tt_paper);
        b_paper.setOnMouseEntered(e -> adjustButton(true, b_paper, 8.0));
        b_paper.setOnMouseExited(e -> adjustButton(false, b_paper, 8.0));
        b_paper.setOnAction(e -> makeMove("paper"));

        // scissors button
        //b_scissors = new Button("\u270C");
        b_scissors = new Button("✂");
        b_scissors.setAlignment(Pos.CENTER);
        b_scissors.setMinHeight(64);
        b_scissors.setMaxWidth(64);
        b_scissors.setPadding(new Insets(16, 16, 16, 16));
        b_scissors.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 64;");
        tt_scissors = new Tooltip("Scissors beats:\n - Paper\n - Lizard");
        tt_scissors.setStyle("-fx-font-size: 18");
        b_scissors.setTooltip(tt_scissors);
        b_scissors.setOnMouseEntered(e -> adjustButton(true, b_scissors, 8.0));
        b_scissors.setOnMouseExited(e -> adjustButton(false, b_scissors, 8.0));
        b_scissors.setOnAction(e -> makeMove("scissors"));

        // lizard button
        b_lizard = new Button("🦎");
        b_lizard.setAlignment(Pos.CENTER);
        b_lizard.setMinHeight(64);
        b_lizard.setMaxWidth(64);
        b_lizard.setPadding(new Insets(16, 16, 16, 16));
        b_lizard.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 64;");
        tt_lizard = new Tooltip("Lizard beats:\n - Paper\n - Spock");
        tt_lizard.setStyle("-fx-font-size: 18");
        b_lizard.setTooltip(tt_lizard);
        b_lizard.setOnMouseEntered(e -> adjustButton(true, b_lizard, 8.0));
        b_lizard.setOnMouseExited(e -> adjustButton(false, b_lizard, 8.0));
        b_lizard.setOnAction(e -> makeMove("lizard"));

        // spock button
        b_spock = new Button("🖖");
        b_spock.setAlignment(Pos.CENTER);
        b_spock.setMinHeight(64);
        b_spock.setMaxWidth(64);
        b_spock.setPadding(new Insets(16, 16, 16, 16));
        b_spock.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);"
                + "-fx-background-insets: 0,1,2,3;"
                + "-fx-background-radius: 3,2,2,2;"
                + "-fx-padding: 12 30 12 30;"
                + "-fx-text-fill: white;"
                + "-fx-font-family: Consolas;"
                + "-fx-font-size: 64;");
        tt_spock = new Tooltip("Spock beats:\n - Scissors\n - Rock");
        tt_spock.setStyle("-fx-font-size: 18");
        b_spock.setTooltip(tt_spock);
        b_spock.setOnMouseEntered(e -> adjustButton(true, b_spock, 8.0));
        b_spock.setOnMouseExited(e -> adjustButton(false, b_spock, 8.0));
        b_spock.setOnAction(e -> makeMove("spock"));

        hb_moves = new HBox();
        hb_moves.getChildren().addAll(b_rock, b_paper, b_scissors, b_lizard, b_spock);
        hb_moves.setAlignment(Pos.BASELINE_LEFT);
        hb_moves.setMargin(b_rock, new Insets(8));
        hb_moves.setMargin(b_paper, new Insets(8));
        hb_moves.setMargin(b_scissors, new Insets(8));
        hb_moves.setMargin(b_lizard, new Insets(8));
        hb_moves.setMargin(b_spock, new Insets(8));

        // layout for game screen
        vb_game = new VBox();
        vb_game.getChildren().addAll(t_rounds, hb_scores, hb_moves);
        vb_game.setAlignment(Pos.CENTER);
        vb_game.setPadding(new Insets(32, 32, 32, 32));
        vb_game.setMargin(hb_scores, new Insets(10, 0, 16, 0));
        vb_game.setMargin(hb_moves, new Insets(16, 0, 0, 0));
        vb_game.setBackground(new Background(bi_wallpaper));

        // set the layout to the scene as 800px by 800px
        s_game = new Scene(vb_game, 750, 550);

        //return vb_game;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        // store refernce to primaryStage
        mainStage = primaryStage;

        // initialize background image
        bi_wallpaper = new BackgroundImage(new Image("background.jpg", 0, 0, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        // initialize UI fonts
        font = Font.font("Arial", FontPosture.REGULAR, 48);
        tf_font = Font.font("Consolas", FontPosture.REGULAR, 24);
        score_font = Font.font("Showcard Gothic", FontPosture.REGULAR, 96);
        round_font = Font.font("Showcard Gothic", FontPosture.REGULAR, 48);

        // load all three scenes and jump to connect scene
        s_connect = new Scene(createConnectScene());
        createSearchScene();
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

            // set to search scene
            setCurrentScene(1);
        } catch (Exception e) {
            System.out.println("> Bad IP [" + tf_ip.getText() + "] or bad port [" + tf_port.getText() + "]");
        }
    }

    /* connects an available player to a game */
    private void play() {

        try {

            // get the selected client to challenge as a string
            String selected_client = extractString();

            // check if selected player is available, if so match them
            client.challengeOpponent(selected_client);

            System.out.println("> Player [" + tf_username.getText() + "] challenged [" + selected_client + "]");

            // updates username and opponent's name to UI of game screen
            t_player.setText(username);
            t_opponent.setText(selected_client);

            // set user as challenger
            isChallenger = true;

            // set to game scene
            setCurrentScene(2);
        } catch (Exception e) {
            System.out.println("> Seleced client [" + extractString() + "] unavailble");
        }
    }

    /* sends the specified move */
    private void makeMove(String move) {

        // try to send the client's move
        try {

            // send the move to the server
            client.sendChoiceToGame(move);

            // prevent player from making another move in same round
            disableGameButtons(true);

            // update last move
            updateLastMove(move, 1);

            System.out.println("> Successfully sent move of [" + move + "]");
        } catch (Exception e) {
            System.out.println("> Failed to send move of [" + move + "]");
        }
    }

    public void setTotalpointsText(String p1_score, String p2_score) {
        //totalpointsText.setText(score);
        t_score.setText(p1_score);
        t_opponent_score.setText(p2_score);
        
        score = Integer.parseInt(p1_score);
        opponent_score = Integer.parseInt(p2_score);
    }

    public void setOpponentplayedText(String selection) {
        //opponentplayedText.setText(selection);
        updateLastMove(selection, 2);
    }

    public void setServerText(String message) {
        //serverText.setText(message);
    }

    /* called when challenged by another player */
    public void setConnectedTo(String opponent) {

        // updates username and opponent's name to UI of game screen
        t_player.setText(username);
        t_opponent.setText(opponent);

        // set to game scene (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setCurrentScene(2);

                // switch score locations if 'player 2'
                /*if (isChallenger == false) {
                    vb_player.getChildren().remove(t_score);
                    vb_player.getChildren().add(1, t_opponent_score);
                    vb_opponent.getChildren().remove(t_opponent_score);
                    vb_opponent.getChildren().add(1, t_score);
                }*/
            }
        });
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
                title += "Search for Clients";
                mainStage.setScene(s_search);
                updateClientList();
                lv_clients.getSelectionModel().clearSelection();
                b_play.setDisable(true);
                break;

            // mode 2 -> set to game screen
            case 2:
                title += "Playing against another Client";
                mainStage.setScene(s_game);
                disableGameButtons(false);
                break;
        }

        // set scene title, prevent resizing of window, and show the scene
        mainStage.setTitle(title);
        mainStage.setResizable(false);
        mainStage.show();
    }

    /* updates the list view with all available clients */
    public void updateClientList() {

        // get data structure containing all clients
        ArrayList<String> connected_clients = client.getOpponentList();

        // update client list (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // clear the list before populating it
                lv_clients.getItems().clear();

                // for each client in data structure
                for (String s : connected_clients) {

                    // add current client as string to the list view
                    lv_clients.getItems().add(new Text(s));

                }
            }
        });

    }

    /* adjust button size on hover */
    private void adjustButton(boolean isHovered, Button btn, double offset) {

        // if hovering over component, increase (font) size by offset
        if (isHovered) {
            btn.setStyle(btn.getStyle() + "-fx-font-size: " + (btn.getFont().getSize() + offset) + "px;");
        } // otherwise decrease (font) size by offset
        else {
            btn.setStyle(btn.getStyle() + "-fx-font-size: " + (btn.getFont().getSize() - offset) + "px;");
        }
    }

    /* adjust text color on hover for main menu title text */
    private void adjustText(boolean IsHovered, Text txt) {

        // if hovering over component
        if (IsHovered) {

            // identify which text object to change
            switch (txt.getText()) {

                case "Rock, ":
                    txt.setFill(Color.BROWN);
                    break;

                case "Paper, ":
                    txt.setFill(Color.BISQUE);
                    break;

                case "Scissors,":
                    txt.setFill(Color.SLATEGRAY);
                    break;

                case "Lizard, ":
                    txt.setFill(Color.GREEN);
                    break;

                case "Spock!":
                    txt.setFill(Color.CORNFLOWERBLUE);
                    break;
            }
        } // otherwise reset to default state
        else {

            // identify which text object to change
            switch (txt.getText()) {

                case "Rock, ":
                    txt.setFill(Color.SADDLEBROWN);
                    break;

                case "Paper, ":
                    txt.setFill(Color.WHITESMOKE);
                    break;

                case "Scissors,":
                    txt.setFill(Color.SILVER);
                    break;

                case "Lizard, ":
                    txt.setFill(Color.DARKOLIVEGREEN);
                    break;

                case "Spock!":
                    txt.setFill(Color.CADETBLUE);
                    break;
            }
        }
    }

    /* extracts string value from text object selected in list view object  */
    private String extractString() {
        String selection = lv_clients.getSelectionModel().getSelectedItem().toString();
        selection = selection.substring(selection.indexOf("\"") + 1);
        selection = selection.substring(0, selection.indexOf("\""));

        return selection;
    }

    /* disable or enable the game buttons */
    private void disableGameButtons(boolean flag) {
        b_rock.setDisable(flag);
        b_paper.setDisable(flag);
        b_scissors.setDisable(flag);
        b_lizard.setDisable(flag);
        b_spock.setDisable(flag);
    }

    /* updates the last move made for the specified player */
    private void updateLastMove(String move, int player) {

        // update UI based on last move played
        switch (move.toLowerCase()) {
            case "rock":
                if (player == 1) {
                    t_last_move.setText("Last Move: \u270A");
                } else {
                    t_opponent_last_move.setText("Last Move: \u270A");
                }
                break;

            case "paper":
                if (player == 1) {
                    t_last_move.setText("Last Move: 📜");
                } else {
                    t_opponent_last_move.setText("Last Move: 📜");
                }
                break;

            case "scissors":
                if (player == 1) {
                    t_last_move.setText("Last Move: ✂");
                } else {
                    t_opponent_last_move.setText("Last Move: ✂");
                }
                break;

            case "lizard":
                if (player == 1) {
                    t_last_move.setText("Last Move: 🦎");
                } else {
                    t_opponent_last_move.setText("Last Move: 🦎");
                }
                break;

            case "spock":
                if (player == 1) {
                    t_last_move.setText("Last Move: 🖖");
                } else {
                    t_opponent_last_move.setText("Last Move: 🖖");
                }
                break;

            default:
                t_last_move.setText("Last Move: ");
                t_opponent_last_move.setText("Last Move: ");
        }
    }

    /* updates the game UI */
    public void updateGameInfo(int winner) {
        t_rounds.setText("Round: " + ++rounds);

        status = winner;

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
                //t_score.setText("" + ++score);
                System.out.println("> Game won [" + score + " - " + opponent_score + "]");
                confirmGameOver("Game won [" + score + " - " + opponent_score + "]");
                break;

            // client lost game
            case 4:
                //t_opponent_score.setText("" + ++opponent_score);
                System.out.println("> Game lost [" + score + " - " + opponent_score + "]");
                confirmGameOver("Game lost [" + score + " - " + opponent_score + "]");
                break;

            // client tied game
            case 5:
                //t_score.setText("" + ++score);
                //t_opponent_score.setText("" + ++opponent_score);
                System.out.println("> Game tied [" + score + " - " + opponent_score + "]");
                confirmGameOver("Game tied [" + score + " - " + opponent_score + "]");
                break;
        }

        disableGameButtons(false);

    }

    /* display dialog box to confirm a game over */
    private void confirmGameOver(String status) {

        // spawn a dialog box (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // spawn the dialog box
                Alert alert = new Alert(AlertType.NONE, status + "\n\nClick OK to search for a new game.", ButtonType.OK);
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

    private void resetGame() {

        // reset game status
        status = -1;

        // reset challenger status
        isChallenger = false;

        // reset move choice (so that server doesn't use move from last game
        //client.sendChoiceToGame("---");
        // reset round info
        rounds = 1;
        t_rounds.setText("Round:" + rounds);

        // reset score info
        score = 0;
        t_score.setText("" + score);
        opponent_score = 0;
        t_opponent_score.setText("" + opponent_score);

        // reset last move info
        t_last_move.setText("Last Move: ");
        t_opponent_last_move.setText("Last Move: ");

        // take player back to search scene (on main JavaFX thread)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setCurrentScene(1);
            }
        });

    }

}

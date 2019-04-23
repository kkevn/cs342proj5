

/******************************************************************************
 * CS 342 - Project 4, Server GUI - Updated R, P, S, L, S in JavaFX
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
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class frontEndServer extends Application {

    /* useful variables */
    private int default_port = 5555;
    private String default_ip = "127.0.0.1";
    private int client_count = 0;
    Server server;

    /* connection object */
    //private NetworkConnection conn = createServer(default_port);

    /* universal elements */
    private Stage mainStage;
    private Scene s_connect, s_search;
    private Font font, tf_font;
    private BackgroundImage bi_wallpaper;

    /* setup screen components */
    private Text t_port;
    private TextField tf_port;
    private Button b_connect;
    private HBox hb_port;
    private VBox vb_connect;

    /* view screen components */
    private Text t_clients;
    private ListView lv_clients;
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
        b_connect.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);" +
                "-fx-background-insets: 0,1,2,3;" +
                "-fx-background-radius: 3,2,2,2;" +
                "-fx-padding: 12 30 12 30;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: Consolas;" +
                "-fx-font-size: 18px;");
        b_connect.setOnMouseEntered(e -> adjustButton(true, b_connect, 6.0));
        b_connect.setOnMouseExited(e -> adjustButton(false, b_connect, 6.0));
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

    /* creates the scene for the searching clients */
    private void createViewScene() {

        // text label
        t_clients = new Text("Online Clients:\t(" + client_count + ")");
        t_clients.setFill(Color.WHITE);
        t_clients.setFont(tf_font);
        hb_title = new HBox();
        hb_title.getChildren().add(t_clients);
        hb_title.setAlignment(Pos.BASELINE_RIGHT);

        // list view for clients
        lv_clients = new ListView();
        lv_clients.getItems().add(new Text("test"));

        // connect button
        b_update = new Button("Update Client List");
        b_update.setFont(font);
        b_update.setAlignment(Pos.CENTER);
        b_update.setMinHeight(50);
        b_update.setMinWidth(180);
        b_update.setPadding(new Insets(16, 16, 16, 16));
        b_update.setStyle("-fx-background-color: #000000, linear-gradient(#7ebcea, #2f4b8f), linear-gradient(#426ab7, #263e75), linear-gradient(#395cab, #223768);" +
                "-fx-background-insets: 0,1,2,3;" +
                "-fx-background-radius: 3,2,2,2;" +
                "-fx-padding: 12 30 12 30;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: Consolas;" +
                "-fx-font-size: 18px;");
        b_update.setOnMouseEntered(e -> adjustButton(true, b_update, 2.0));
        b_update.setOnMouseExited(e -> adjustButton(false, b_update, 2.0));
        b_update.setOnAction(e -> updateClientList());

        // layout for connect screen
        vb_search = new VBox();
        vb_search.getChildren().addAll(hb_title, lv_clients, b_update);
        vb_search.setAlignment(Pos.CENTER);
        vb_search.setPadding(new Insets(16, 32, 16, 32));
        vb_search.setMargin(t_clients, new Insets(10, 0, 16, 0));
        vb_search.setMargin(lv_clients, new Insets(0, 0, 16, 0));
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

        // initialize background image
        bi_wallpaper = new BackgroundImage(new Image("background.jpg", 0, 0, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        // initialize UI fonts
        font = Font.font("Arial", FontPosture.REGULAR, 48);
        tf_font = Font.font("Consolas", FontPosture.REGULAR, 24);

        // load all three scenes and jump to connect scene
        s_connect = new Scene(createSetupScene());
        createViewScene();
        mainStage.setTitle("[Server] - Setup Server");
        mainStage.setScene(s_connect);
        mainStage.setResizable(false);
        mainStage.show();
        b_connect.requestFocus();
    }
/*
    @Override
    public void init() throws Exception{
        conn.startConn();
    }

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }

    private Server createServer(int port) {
        return new Server(port, data -> {
            Platform.runLater(()->{
                // TODO????
                //do something with data.toString()
            });
        });
    }
*/
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
                title += "View Connected Clients";
                mainStage.setScene(s_search);
                updateClientList();
                lv_clients.getSelectionModel().clearSelection();
                break;
        }

        // set scene title, prevent resizing of window, and show the scene
        mainStage.setTitle(title);
        mainStage.setResizable(false);
        mainStage.show();
    }

    /* adjust button size on hover */
    private void adjustButton(boolean isHovered, Button btn, double offset) {

        // if hovering over component, increase (font) size by offset
        if (isHovered) {
            btn.setStyle(btn.getStyle() + "-fx-font-size: " + (btn.getFont().getSize() + offset) + "px;");
        }

        // otherwise decrease (font) size by offset
        else {
            btn.setStyle(btn.getStyle() + "-fx-font-size: " + (btn.getFont().getSize() - offset) + "px;");
        }
    }

    /* sets up a server on valid text field input */
    private void connect() {

        try {

            // get port from text field
            int port = Integer.parseInt(tf_port.getText());

            // setup new server connection
            // TODO
            //conn = createServer(new_port);

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
    private void updateClientList() {

        // get data structure containing all clients
        // TODO
            /*
               data structure found in ClientThread class or similar, where
               each client must have a list of all connected clients
               something like ...
               ArrayList clients = ClientThread.getAllClients();
            */

        // clear the list before populating it
        lv_clients.getItems().clear();
        client_count = 0;

        // TODO
        // below is a fake list of clients for visual purposes
        // this list will not work and should be deleted

        // for each client in data structure

        /*
        for (int i = 1; i < 5; i++) {

            // add current client as string to the list view
            lv_clients.getItems().add(new Text("client" + i));
            client_count++;
        }
*/
        // TODO
        // update client count from data structure of connected clients
        //client_count = clients.size();
        t_clients.setText("Online Clients:\t(" + server.getTotalClients() + ")");


        if(server.getListOfClientConnections().size() >= 1) {
            for(int i = 0; i < server.getListOfClientConnections().size(); i++){
                if(server.getListOfClientConnections().get(i).score != -999)
                    lv_clients.getItems().add(new Text(server.getListOfClientConnections().get(i).userName));
            }
        }

        // TODO
            /*
                below is the real list of clients but cannot be used
                until fetching the client list is implemented (along
                with ClientThread.getNameAsString())
                uncomment the for loop below once the above features
                are implemented
            */

        // for each client in data structure
            /*
            for (ClientThread ct : clients) {

                // add current client as string to the list view
                lv_clients.getItems().add(new Text(ct.getNameAsString()));
            }
            */
    }


}

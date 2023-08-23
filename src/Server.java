import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Server extends Application {

    public static boolean isActive = false;
    public static Stage window;

    private Player[] playersConnected = {new Player(Player.Team.NO_TEAM, false), new Player(Player.Team.NO_TEAM, false)};
    public ExecutorService threadPool = Executors.newCachedThreadPool();

    private ExecutorService sockets = Executors.newFixedThreadPool(2);

    private BorderPane root;
    private GridPane board;
    private TextArea serverText;
    private Label title;
    private Button reset;
    private Pane top;
    private String[][] gameCells = new String[3][3];
    private ScrollPane right;
    private Scene scene;

    @Override
    public void start(Stage stage) {

        root = new BorderPane();
        root.setPadding(new Insets(50, 30, 30, 30));


        board = new GridPane();
        board.setStyle("-fx-background-color: darkgray;");
        board.setHgap(4);
        board.setVgap(4);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                board.add(new ServerCell(row, col), col, row);
            }
        }

        Arrays.fill(gameCells, new String[]{"","",""});


        title = new Label("Tic Tac Toe!");
        title.translateXProperty().bind(root.widthProperty().divide(2).subtract(80));
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 30));

        threadPool.execute(() -> {
            try {
                var obj = new Object() {
                    int i = 0;
                };
                while (true) {
                    obj.i++;
                    Thread.sleep(10);
                    Platform.runLater(() -> {
                        try {
                            title.setTextFill(getColor(obj.i, obj.i % 512 <= 256));
                        } catch (IllegalArgumentException e) {

                        }

                    });
                }
            } catch (InterruptedException ex) {

            }
        });

        // Start of Menus
        MenuBar menuBar = new MenuBar();

        // Start of boardMenu
        Menu boardMenu = new Menu("Board");

        MenuItem resetBoard = new MenuItem("reset board",
                new ImageView(Server.class.getResource("Assets/clearBoard.png").toExternalForm()));
        resetBoard.setOnAction(e -> {

            for (ServerCell[] cellRow : ServerCell.cells) {
                for (ServerCell c : cellRow) {
                    c.setValue("");
                    c.setText("");

                }
            }
        });
        boardMenu.getItems().addAll(resetBoard);
        menuBar.getMenus().add(boardMenu);

        // Start of Server Menu

        Menu server = new Menu("Server");

        MenuItem resetCapacity = new MenuItem("reset capacity",
                new ImageView(Server.class.getResource("Assets/Admin.png").toExternalForm()));

        resetCapacity.setOnAction(e -> {
            playersConnected = new Player[]{new Player(Player.Team.NO_TEAM, false),
                new Player(Player.Team.NO_TEAM, false)};
        });

        MenuItem checkCapacity = new MenuItem("check capacity",
                new ImageView(Server.class.getResource("Assets/QuestionMark.png").toExternalForm()));

        checkCapacity.setOnAction(e -> {
            sendMessage(String.format("Player 1: %s%nPlayer 2: %s", playersConnected[0], playersConnected[1]));
        });
        MenuItem resetGame = new MenuItem("reset game", new ImageView(Server.class.getResource("Assets/explosion.png").toExternalForm()));

        resetGame.setOnAction(e -> {

            Arrays.fill(gameCells, new String[]{"","",""});
        });
        server.getItems().addAll(resetCapacity, checkCapacity, resetGame);

        // End of "Server" Menu
        // Start of "Messages" Menu

        Menu messageMenu = new Menu("Messages");

        MenuItem clearMessage = new MenuItem("clear messages",
                new ImageView(Server.class.getResource("Assets/clearServerMessages.png").toExternalForm()));

        clearMessage.setOnAction(e -> {serverText.setText("Started server on " + new Date());});

        menuBar.getMenus().add(messageMenu);

        messageMenu.getItems().add(clearMessage);

        // End of "Messages" Menu

        menuBar.getMenus().add(server);

        menuBar.setTranslateY(-40);

        top = new Pane();
        top.getChildren().addAll(title, menuBar);

        // right of root

        serverText = new TextArea("Started server on " + new Date());
        serverText.setStyle("-fx-fill: black;-fx-border-color: green;-fx-text-fill: black;-fx-highlight-fill: yellow");
        serverText.setPrefHeight(275);
        serverText.setBackground(Styling.Backgrounds.LIGHT_GRAY.getBackground());
        right = new ScrollPane(serverText);



        root.setTop(top);
        root.setRight(right);
        root.setCenter(board);

        scene = new Scene(root, 800, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        ServerCell.windowHeight.bind(scene.heightProperty());
        ServerCell.windowWidth.bind(scene.widthProperty());

        window = stage;
        window.setScene(scene);
        window.setTitle("Tic Tac Toe(Server)");
        window.show();

        threadPool.execute(() -> {

            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                isActive = true;
                while (true) {

                    Socket client = serverSocket.accept();
                    System.out.println("accepted client");
                    sockets.execute(new ClientHandler(this, client));
                    System.out.println("client handled");
                }

            } catch (IOException e) {

            }
        });


        window.setOnHidden(e -> {
            threadPool.shutdownNow();
            sockets.shutdownNow();
            if (!sockets.isTerminated() || !threadPool.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println(threadPool);
            System.out.println(sockets);
            System.exit(0);
        });

    }
    public Player connectPlayer(Player player) {
        sendMessage("\n\n\n player inside 'connectPlayer': " + player);
        for (int i = 0; i < playersConnected.length; i++) {
            if (playersConnected[i].getTeam() == Player.Team.NO_TEAM) {
                player.setConnection(true);
                playersConnected[i] = player;
                return player;
            }
        }

        throw new RuntimeException("could not add player");
    }
    public Player disconnectPlayer(Player player) {
        System.out.println(Arrays.toString(playersConnected));
        for (int i = 0; i < playersConnected.length; i++) {
            if (player.getTeam().equals(playersConnected[i].getTeam()) && player.isConnected()) {
                playersConnected[i] = new Player(Player.Team.NO_TEAM, false);
                return player;
            }
        }
        throw new RuntimeException("could not disconnect player");
    }
    public String[][] checkCells(String[][] cells) {

        gameCells = cells;
        Stream.of(gameCells).forEach(c -> System.out.println(Arrays.toString(c)));

        return gameCells;
    }

    public Player[] getPlayersConnected() {

        return playersConnected;
    }
    public void sendMessage(String message) {
        Platform.runLater(() -> {

            this.serverText.setText(serverText.getText() + "\n" + message);

        });

    }


    public String[][] getGameCells() {
        return gameCells;
    }

    public void setGameCells(String[][] gameCells) {
        this.gameCells = gameCells;
    }
    public ScrollPane getRight() {
        return right;
    }
    private TextArea getServerText() {

        return serverText;
    }
    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public GridPane getBoard() {
        return board;
    }

    public void setBoard(GridPane board) {
        this.board = board;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Button getReset() {
        return reset;
    }

    public void setReset(Button reset) {
        this.reset = reset;
    }

    public Pane getTop() {
        return top;
    }

    public void setTop(Pane top) {
        this.top = top;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public static Color getColor(int i, boolean rev) {

        int r = 0;
        int g = 0;
        int b = 0;

        if (rev) {
            g = 256 - (i % 256);
            b = 256 - (i % 256);
        } else {
            b = i % 256;
            g = i % 256;
        }

        return Color.rgb(r, g, b);
    }



    public static void main(String[] args) {
        Server.launch(args);
    }



}
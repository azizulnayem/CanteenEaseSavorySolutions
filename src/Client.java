

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;


public class Client extends Application {


    public Stage window;
    private ObjectOutputStream toServer = null;
    private ObjectInputStream fromServer = null;
    private String team = "";

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) {

        window = stage;
        window.setScene(this.new Display(500, 400).setTitleScreen());
        window.setTitle("Tic Tac Toe (Client)");
        window.show();


        window.setOnHidden(e -> {

            Display.serverHandler.shutdownNow();
            double time = 0.0;
            if (!Display.serverHandler.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println(Display.serverHandler);

            System.exit(0);
        });


    }

    private class Display {
        private static ExecutorService serverHandler = Executors.newCachedThreadPool();
        private int width;
        private int height;
        private BlockingQueue<ServerRequest> serverQueue;
        private Player player;
        private Client client;

        public Display(int width, int height) {
            client = Client.this;
            this.width = width;
            this.height = height;
            this.player = null;
            this.serverQueue = new ArrayBlockingQueue<>(1, true);
        }

        public Scene setTitleScreen() {

            System.out.println(client);
            BorderPane root = new BorderPane();


            root.setBackground(Styling.Backgrounds.LIGHT_GRAY.getBackground());
            VBox center = new VBox();
            center.setSpacing(40);
            center.setAlignment(Pos.CENTER);

            Label title = new Label("Welcome to Tic Tac Toe", new ImageView(new Image(Client.class.getResource("Assets/Backgrounds/TicTac.png").toExternalForm())));
            title.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 40));
            Display.serverHandler.execute(animatedTitle(title));

            Button joinServer = new Button("Join a game");
            joinServer.setBackground(Styling.Backgrounds.SILVER.getBackground());
            joinServer.setFont(Styling.FontPresets.REGULAR.getFont());

            joinServer.setPrefHeight(50);
            joinServer.setOnAction(e -> {
                window.setScene(setServerScreen());
            });

            center.getChildren().addAll(title, joinServer);

            root.setCenter(center);

            Scene scene = new Scene(root, width, height);
            return scene;
        }

        public Scene setServerScreen() {


            VBox root = new VBox();
            root.setAlignment(Pos.CENTER);

            root.setBackground(Styling.Backgrounds.LIGHT_GRAY.getBackground());


            ComboBox<String> serverBox = new ComboBox<>();
            serverBox.setStyle("-fx-text-fill: white;");
            serverBox.setBackground(Styling.Backgrounds.SILVER.getBackground());
            serverBox.setOnAction(e -> {

                System.out.println(Thread.currentThread().getName());
                window.setScene(pickTeam());


            });
            serverBox.setValue("please select a Server");
            serverBox.setPrefSize(400, 20);


            root.getChildren().add(serverBox);

            Scene scene = new Scene(root, width, height);


            serverHandler.execute(() -> {
                try {
                    Socket socket = new Socket();
                    int i = 0;
                    while (true) {

                        try {
                            Thread.sleep(1000);
                            if (window.getScene() != scene) {
                                break;
                            }

                            while (!socket.isConnected()) {
                                try {
                                    socket = new Socket("localhost", 8000);
                                    toServer = new ObjectOutputStream(socket.getOutputStream());
                                    fromServer = new ObjectInputStream(socket.getInputStream());
                                } catch (SocketException ex) {

                                }
                            }
                            toServer.writeObject(new ServerRequest(ServerRequest.RequestType.GET_INFO));
                            toServer.flush();

                            System.out.println("info executed");
                            InetAddress socketInfo = (InetAddress) fromServer.readObject();
                            Platform.runLater(() -> {
                                if (!serverBox.getItems().contains(socketInfo.toString()))
                                    serverBox.getItems().add(0, socketInfo.toString());


                            });


                        } catch (SocketException ex) {
                            socket = new Socket();
                            serverBox.getItems().clear();
                        }

                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {

                }

            });

            return scene;
        }

        public Scene setGame() {


            BorderPane root = new BorderPane();
            root.setPadding(new Insets(30, 30, 30, 30));

            root.setBackground(Styling.Backgrounds.LIGHT_GRAY.getBackground());

            GridPane board = new GridPane();
            board.setStyle("-fx-background-color: darkgray;");
            board.setHgap(4);
            board.setVgap(4);

            LabelCell.turn.setValue(player.getTurn());
            System.out.println(player.getTurn());
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    board.add(new LabelCell(row, col, player.getTeam()), col, row);
                }
            }

            serverHandler.execute(() -> {
                try {

                    while (true) {
                        Thread.sleep(100);
                        toServer.writeObject(new ServerRequest(ServerRequest.RequestType.GET_BOARD));
                        toServer.flush();

                        String[][] cellsToSend = LabelCell.matrixToString(LabelCell.cells);
                        System.out.println("object sent: ");
                        Stream.of(cellsToSend).forEach(cArr -> {
                            System.out.println(Arrays.toString(cArr));
                        });

                        String[][] newBoard = (String[][]) fromServer.readObject();

                        System.out.println("after reading");
                        Stream.of(newBoard).forEach(c -> System.out.println(Arrays.toString(c)));
                        System.out.println("here is one of the cells: \"" + newBoard[2][2] + "\"");
                        Platform.runLater(() -> {

                            for (int i = 0; i < newBoard.length * newBoard[1].length; i++) {

                                LabelCell c = (LabelCell) board.getChildren().get(i);
                                if (!c.getValue().equals(newBoard[i/3][i%3]))
                                    LabelCell.turn.setValue(true);
                                c.setValue(newBoard[i / 3][i % 3]);
                            }

                        });
                        Platform.runLater(() -> {
                            Stream.of(LabelCell.cells).forEach(c -> System.out.println(Arrays.toString(c)));
                            switch (LabelCell.checkCells()) {

                                case "X" -> {
                                    window.setScene(LabelCell.endScreen("X"));
                                    serverHandler.shutdownNow();
                                }

                                case "O" -> {
                                    window.setScene(LabelCell.endScreen("O"));
                                    serverHandler.shutdownNow();
                                }
                            }
                        });


                    }

                } catch (IOException f) {
                    f.printStackTrace();
                } catch (ClassNotFoundException g) {
                    g.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            });


            LabelCell.turn.addListener(o -> {
                if (LabelCell.turn.getValue() == false)
                serverHandler.execute(() -> {
                    try {
                        toServer.writeObject(new ServerRequest(ServerRequest.RequestType.SEND_BOARD));
                        toServer.flush();

                        String[][] cellsToSend = LabelCell.matrixToString(LabelCell.cells);
                        System.out.println("object sent: ");
                        Stream.of(cellsToSend).forEach(cArr -> {
                            System.out.println(Arrays.toString(cArr));
                        });
                        toServer.writeObject(cellsToSend);
                        toServer.flush();

                        String[][] newBoard = (String[][]) fromServer.readObject();

                        System.out.println("after reading");
                        Stream.of(board.getChildren()).forEach(System.out::println);
                        Platform.runLater(() -> {

                            for (int i = 0; i < newBoard.length * newBoard[1].length; i++) {

                                LabelCell c = (LabelCell) board.getChildren().get(i);
                                c.setValue(newBoard[i / 3][i % 3]);

                            }
                        });
                    } catch (IOException f) {
                        f.printStackTrace();
                    } catch (ClassNotFoundException g) {
                        g.printStackTrace();
                    }

                });
            });


            Label title = new Label("Tic Tac Toe!");
            title.translateXProperty().bind(root.widthProperty().divide(2).subtract(80));
            title.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 30));

            Platform.runLater(() -> Display.animatedTitle(title));
            Button reset = new Button("reset board");
            reset.setId("reset-btn");

            reset.setOnAction(e -> {

                serverHandler.execute(() -> {

                    try {

                        toServer.writeObject(new ServerRequest(ServerRequest.RequestType.RESET_BOARD));

                        toServer.flush();


                        System.out.println("before reading");

                        //Creating the object that will back the GridPane representing the board
                        String[][] newBoard = (String[][]) fromServer.readObject();
                        //Creating the styling for the Transient fields
                        Stream.of(newBoard).forEach(cArr -> System.out.println(Arrays.toString(cArr)));
                        System.out.println("after reading");
                        Stream.of(board.getChildren()).forEach(System.out::println);
                        Platform.runLater(() -> {

                            for (int row = 0; row < newBoard.length; row++) {
                                for (int col = 0; col < newBoard[row].length; col++) {
                                    LabelCell c = (LabelCell) board.getChildren().get((col + (row * col)));
                                    c.setValue(newBoard[row][col]);


                                }
                            }

                        });

                    } catch (IOException f) {
                        f.printStackTrace();
                    } catch (ClassNotFoundException g) {
                        g.printStackTrace();
                    }
                    try {
                        toServer.writeObject(new ServerRequest(ServerRequest.RequestType.SEND_BOARD));
                        toServer.flush();

                        String[][] cellsToSend = LabelCell.matrixToString(LabelCell.cells);
                        System.out.println("object sent: ");
                        Stream.of(cellsToSend).forEach(cArr -> {
                            System.out.println(Arrays.toString(cArr));
                        });
                        toServer.writeObject(cellsToSend);
                        toServer.flush();

                        String[][] newBoard = (String[][]) fromServer.readObject();

                        System.out.println("after reading");
                        Stream.of(board.getChildren()).forEach(System.out::println);
                        Platform.runLater(() -> {

                            for (int i = 0; i < newBoard.length * newBoard[1].length; i++) {

                                LabelCell c = (LabelCell) board.getChildren().get(i);
                                c.setValue(newBoard[i / 3][i % 3]);

                            }
                        });

                        //receive other client's board


                    } catch (IOException f) {
                        f.printStackTrace();
                    } catch (ClassNotFoundException g) {
                        g.printStackTrace();
                    }

                });


            });

            Pane top = new Pane();
            top.getChildren().addAll(title, reset);

            root.setTop(top);

            root.setCenter(board);

            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(Client.class.getResource("application.css").toExternalForm());
            LabelCell.windowHeight.bind(scene.heightProperty());
            LabelCell.windowWidth.bind(scene.widthProperty());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            }));

            window.setOnHidden(e -> {


                try {
                    System.out.println("writing player...");
                    toServer.writeObject(new ServerRequest(ServerRequest.RequestType.DISCONNECT_PLAYER));
                    toServer.flush();

                    toServer.writeObject(player);
                    toServer.flush();
                    Player removedPlayer = (Player) fromServer.readObject();
                    System.out.println("removed " + removedPlayer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                Display.serverHandler.shutdownNow();
                double time = 0.0;
                if (!Display.serverHandler.isTerminated()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                System.out.println(Display.serverHandler);

                System.exit(0);


            });

            return scene;
        }

        // START OF SETGAME()


        public Scene pickTeam() {


            Label xTeamText = new Label("X");
            Label oTeamText = new Label("O");
            Rectangle oTeam = new Rectangle(0, 0, 250, 500);
            Rectangle xTeam = new Rectangle(250, 0, 250, 500);

            Pane root = new Pane();
            root.setPadding(new Insets(10, 0, 0, 0));

            AtomicBoolean playerTurn = new AtomicBoolean(false);

            root.setOnMouseClicked(e -> {

                Point2D ePoint = new Point2D(e.getX(), e.getY());

                if (oTeam.getBoundsInParent().contains(ePoint) && !oTeamText.getText().equals("FULL")) {


                    serverHandler.execute(() -> {
                        try {

                            boolean capturedTurn = playerTurn.get();

                            System.out.println("player's turn is " + capturedTurn);

                            toServer.writeObject(new ServerRequest(ServerRequest.RequestType.ADD_PLAYER));
                            toServer.flush();
                            Player player = new Player(Player.Team.O_TEAM, false, capturedTurn);

                            toServer.writeObject(player);
                            toServer.flush();

                            Player playerAdded = (Player) fromServer.readObject();
                            this.player = playerAdded;
                            Platform.runLater(() -> {
                                window.setScene(setGame());
                            });

                            System.out.println("Player added: " + playerAdded);

                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });

                } else if (xTeam.getBoundsInParent().contains(ePoint) && !xTeamText.getText().equals("FULL")) {


                    serverHandler.execute(() -> {

                        try {
                            boolean capturedTurn = playerTurn.get();
                            System.out.println("player's turn is " + capturedTurn);

                            toServer.writeObject(new ServerRequest(ServerRequest.RequestType.ADD_PLAYER));
                            toServer.flush();
                            Player player = new Player(Player.Team.X_TEAM, false, capturedTurn);

                            toServer.writeObject(player);
                            toServer.flush();

                            Player playerAdded = (Player) fromServer.readObject();
                            this.player = playerAdded;
                            Platform.runLater(() -> {
                                window.setScene(setGame());
                            });

                            System.out.println("Player added: " + playerAdded);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            });


            root.getChildren().addAll(xTeam, xTeamText, oTeam, oTeamText);

            Scene scene = new Scene(root, 500, 500);


            Runnable checkCapacity = () -> {
                new Thread(() -> {
                    try {
                        while (true) {
                            Thread.sleep(100);
                            serverQueue.put(new ServerRequest(ServerRequest.RequestType.CHECK_PLAYERS));

                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }).start();
                Player[] serverPlayers = new Player[2];
                while (true) {

                    if (window.getScene() != scene)
                        break;
                    String capacity = "";
                    try {
                        toServer.writeObject(serverQueue.take());

                        serverPlayers = (Player[]) fromServer.readObject();

                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                    capacity = "";
                    for (int i = 0; i < serverPlayers.length; i++) {

                        if (serverPlayers[i].getTeam() != Player.Team.NO_TEAM && capacity.matches("[OX]_TAKEN")) {
                            capacity = "FULL";
                        } else if (serverPlayers[i].getTeam() == Player.Team.NO_TEAM && capacity.matches("()|(OPEN)")) {
                            capacity = "OPEN";
                        } else if (serverPlayers[i].getTeam() == Player.Team.X_TEAM) {
                            capacity = "X_TAKEN";
                        } else if (serverPlayers[i].getTeam() == Player.Team.O_TEAM) {
                            capacity = "O_TAKEN";

                        }

                    }
                    //System.out.println(capacity);
                    switch (capacity) {
                        case "OPEN" -> {
                            Platform.runLater(() -> {
                                playerTurn.set(true);
                                oTeam.setFill(Color.BLUE);

                                xTeam.setFill(Color.RED);

                                oTeamText.setText("O");
                                oTeamText.setTranslateX(125 - 40);
                                oTeamText.setTranslateY(250 - 40);
                                oTeamText.setFont(Styling.FontPresets.REGULAR_LARGE.getFont());

                                xTeamText.setText("X");
                                xTeamText.setTranslateX(375 - 40);
                                xTeamText.setTranslateY(250 - 40);
                                xTeamText.setFont(Styling.FontPresets.REGULAR_LARGE.getFont());


                            });

                        }
                        case "FULL" -> {
                            Platform.runLater(() -> {
                                playerTurn.set(false);
                                oTeam.setFill(Color.GRAY);

                                xTeam.setFill(Color.GRAY);

                                oTeamText.setText("FULL");
                                oTeamText.setTranslateX(125 - 80);
                                oTeamText.setTranslateY(250 - 40);
                                oTeamText.setFont(Styling.FontPresets.FULL_FONT.getFont());


                                xTeamText.setText("FULL");
                                xTeamText.setTranslateX(375 - 80);
                                xTeamText.setTranslateY(250 - 40);
                                xTeamText.setFont(Styling.FontPresets.FULL_FONT.getFont());
                            });


                        }
                        case "O_TAKEN" -> {

                            Platform.runLater(() -> {
                                playerTurn.set(false);
                                oTeam.setFill(Color.GRAY);

                                xTeam.setFill(Color.RED);

                                oTeamText.setText("FULL");
                                oTeamText.setTranslateX(125 - 80);
                                oTeamText.setTranslateY(250 - 40);
                                oTeamText.setFont(Styling.FontPresets.FULL_FONT.getFont());


                                xTeamText.setText("X");
                                xTeamText.setTranslateX(375 - 40);
                                xTeamText.setTranslateY(250 - 40);
                                xTeamText.setFont(Styling.FontPresets.REGULAR_LARGE.getFont());
                            });

                        }
                        case "X_TAKEN" -> {

                            Platform.runLater(() -> {
                                playerTurn.set(false);
                                oTeam.setFill(Color.BLUE);

                                xTeam.setFill(Color.GRAY);

                                oTeamText.setText("O");
                                oTeamText.setTranslateX(125 - 40);
                                oTeamText.setTranslateY(250 - 40);
                                oTeamText.setFont(Styling.FontPresets.REGULAR_LARGE.getFont());

                                xTeamText.setText("FULL");
                                xTeamText.setTranslateX(375 - 80);
                                xTeamText.setTranslateY(250 - 40);
                                xTeamText.setFont(Styling.FontPresets.FULL_FONT.getFont());
                            });


                        }
                        default -> {
                            Platform.runLater(() -> {
                                oTeam.setFill(Color.WHITE);
                                oTeamText.setFont(Styling.FontPresets.REGULAR_LARGE.getFont());

                                xTeam.setFill(Color.WHITE);
                                xTeamText.setFont(Styling.FontPresets.REGULAR_LARGE.getFont());
                                System.out.println("incorrect String or could not get player info");
                            });

                        }
                    }


                }


            };
            serverHandler.execute(checkCapacity);


            return scene;
        }


        public static Runnable animatedTitle(Label title) {

            return () -> {
                try {
                    var obj = new Object() {
                        int i = 0;
                    };
                    while (true) {
                        obj.i++;
                        Thread.sleep(10);
                        Platform.runLater(() -> {
                            try {
                                title.setTextFill(Display.getColor(obj.i, obj.i % 512 <= 256));
                            } catch (IllegalArgumentException e) {

                            }

                        });
                    }
                } catch (InterruptedException e) {

                }
            };
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


    }
}
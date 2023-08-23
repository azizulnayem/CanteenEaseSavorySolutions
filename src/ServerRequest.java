

import javafx.event.ActionEvent;

import java.io.Serializable;
import java.util.Arrays;

public class ServerRequest implements Serializable {


    public enum RequestType {
        RESET_BOARD,
        GET_INFO,
        CHECK_PLAYERS,
        ADD_PLAYER,
        DISCONNECT_PLAYER,
        SEND_BOARD,
        GET_BOARD;
    }

    private String request;
    private ActionEvent event;

    public ServerRequest(ActionEvent event, RequestType requestType) {

        this.request = requestType.toString();
        this.event = event;


    }

    public ServerRequest(RequestType requestType) {

        this.request = requestType.toString();

    }

    public ServerRequest(String request) {

        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public ActionEvent getEvent() {
        return event;
    }

    public Object fulfillRequest(Server server) {
        switch (request) {

            case "RESET_BOARD" -> {
                return resetBoard(server);

            }
            case "CHECK_PLAYERS" -> {

                return checkPlayers(server);
            }
            case "GET_BOARD" -> {
                return retrieveBoard(server);
            }

        }
        System.out.println(request + " didn't match");
        return new Object();
    }

    private String[][] retrieveBoard(Server server) {

        return server.getGameCells();
    }

    private Player[] checkPlayers(Server server) {
        Player[] players = Arrays.copyOf(server.getPlayersConnected(),2);

        return players;
    }



    private String[][] resetBoard(Server server) {

        return new String[3][3];

    }
}
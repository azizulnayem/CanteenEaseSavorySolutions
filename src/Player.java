

import java.io.Serializable;

public class Player implements Serializable {




    public enum Team {

        X_TEAM,
        NO_TEAM,
        ERR_TEAM,
        O_TEAM;
    }
    private boolean connected;
    private boolean turn;
    private Team team;

    public Player(Team team, boolean connected, boolean turn) {
        this.team = team;
        this.turn = turn;
        this.connected = connected;

    }
    public Player(Team team, boolean connected) {
        this.team = team;
        this.turn = false;
        this.connected = connected;

    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setConnection(boolean connection) {
        this.connected = true;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return String.format("[(player's team: %s), (player's connection: %b), (player's turn: %b)]", this.getTeam(), this.isConnected(), this.getTurn());
    }


}
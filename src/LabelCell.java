

import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LabelCell extends Cell implements Serializable {

    public static DoubleProperty windowHeight = new SimpleDoubleProperty();
    public static DoubleProperty windowWidth = new SimpleDoubleProperty();
    public static BooleanProperty turn = new SimpleBooleanProperty(false);
    public static LabelCell[][] cells = new LabelCell[3][3];
    private String value;
    private final Player.Team team;


    public LabelCell(int row, int col, Player.Team team) {

        super("");

        cells[row][col] = this;
        this.setValue("");
        this.prefHeightProperty().bind(windowHeight);
        this.prefWidthProperty().bind(windowWidth);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("cell");
        this.team = team;
        this.setOnMouseClicked(e -> {
            System.out.println("cell outer check");
            if (turn.getValue() && this.getValue().equals("")) {
                System.out.println("cell check");
                this.setValue(team.toString().substring(0, 1));
                System.out.println(team.toString().substring(0,1));
                turn.setValue(false);
            }

            System.out.println("cell value is " + "\"" + this.getValue() + "\"");
            System.out.println("cell turn is " + this.turn);

        });
    }
    public static String[][] matrixToString(Cell[][] cells) {

        String[][] strungCells = new String[3][3];

        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells.length; col++) {

                strungCells[row][col] = cells[row][col].getValue();
            }
        }


        return strungCells;
    }


    public static String checkCells() {

        Pattern xWon = Pattern.compile("(XXX......)|(X...X...X)|(..X.X.X..)|(.X..X..X.)|(X..X..X..)|(...XXX...)|(......XXX)|(..X..X..X)");
        Pattern oWon = Pattern.compile("(OOO......)|(O...O...O)|(..O.O.O..)|(.O..O..O.)|(O..O..O..)|(...OOO...)|(......OOO)|(..O..O..O)");
        String won = "";
        for (int i = 0; i < 9; i++) {
            if (cells[i / 3][i % 3].getValue().equals(""))
                won = won + "n";
            else
                won = won + cells[i / 3][i % 3].getValue();

        }
        System.out.println("printing out stream: ");
        Stream.of(cells).forEach(c -> {System.out.println(Arrays.toString(c));});
        System.out.println(won);
        if (won.matches(xWon.pattern())) {

             return "X";


        } else if (won.matches(oWon.pattern())) {


            return "O";
        }
        return "n";
    }

    public static Scene endScreen(String won) {

        Text text = new Text(won + " won in Tic Tac Toe!");

        StackPane endScreen = new StackPane(text);
        endScreen.setBackground(Styling.Backgrounds.LIGHT_GRAY.getBackground());

        text.setFont(Font.font(Font.getFamilies().get(17), FontWeight.BOLD, 30));
        text.setScaleY(1.2);
        text.setStrokeWidth(4);
        text.setFill(Color.BEIGE);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStroke(Color.BLACK);


        Scene scene = new Scene(endScreen, 500, 300);


        return scene;
    }


    public static LabelCell[][] renewFunctionality(LabelCell[][] cells) {
        for (LabelCell[] cArr : cells) {
            for (LabelCell c : cArr) {
                c.setText(" ");
                c.prefHeightProperty().bind(windowHeight);
                c.prefWidthProperty().bind(windowWidth);
                c.setAlignment(Pos.CENTER);
                c.getStyleClass().add("cell");
                c.setOnMouseClicked(e -> {

                    if (turn.getValue() && c.getValue() == "") {
                        c.setValue(c.getTeam().toString().substring(0, 1));
                        LabelCell.turn.setValue(false);
                        checkCells();

                    }

                });

            }

        }


        return cells;
    }

    public Player.Team getTeam() {
        return team;
    }

    @Override
    public String getValue() {

        return super.getValue();
    }
    @Override
    public void setValue(String value) {
        super.setValue(value);
        this.setText(value);
        this.setFont(Font.font(Font.getFamilies().get(17), FontWeight.BOLD, this.getPrefHeight() / 6));
        this.setTextFill(this.getValue().equals("O") ? Color.BLUE : Color.RED);
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;
        }

        if (o instanceof LabelCell) {

            LabelCell other = (LabelCell) o;
            return this.getValue().equals(other.getValue());
        }

        return false;
    }

    @Override
    public String toString() {

        return String.format("%s", super.toString());
    }

}
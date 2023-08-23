

import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;




public class Styling {



    public enum Backgrounds {


        SERVER_BG ("Server.png"),
        SKY_BLUE ("SkyBlue.png"),
        SILVER("Silver.png"),

        LIGHT_GRAY ("LightGray.png");



        private Background background;


        Backgrounds(String image) {

            Background background = new Background(new BackgroundImage(
                    new Image(Styling.class.getResource("Assets/Backgrounds/"+image).toExternalForm(),600,400,false,true),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

            this.background = background;
        }

        public Background getBackground() {
            return background;
        }

    }


    public enum FontPresets {

        REGULAR(Font.font("Times new Roman", FontWeight.NORMAL, FontPosture.REGULAR, 17)),
        REGULAR_LARGE(Font.font("Times new Roman", FontWeight.BOLD, FontPosture.REGULAR, 100)),
        FULL_FONT(Font.font("Times new Roman", FontWeight.BOLD, FontPosture.REGULAR, 60));

        private final Font font;

        FontPresets(Font font) {
            this.font = font;
        }

        public Font getFont() {
            return this.font;
        }
    }
}
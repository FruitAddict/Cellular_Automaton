package MVCCA.View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class NumberPane extends BorderPane {
    /**
     * Widget pane to initially control time between generations
     * Will be reused later. Made to avoid too much code in lambda
     */

    public NumberPane(int min, int max, View view){

        Button incrementButton = new Button("+");
        incrementButton.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 14));

        Button decrementButton = new Button("-");
        decrementButton.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 14));

        Label currentValue = new Label(view.getController().getFps()+" fps");
        currentValue.setContentDisplay(ContentDisplay.CENTER);
        currentValue.setPrefWidth(90);

        Label nameLabel = new Label("Target FPS");
        nameLabel.setFont(Font.font("Helvetica",FontWeight.NORMAL,FontPosture.REGULAR,20));

        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(decrementButton,currentValue, incrementButton);
        this.setCenter(contentBox);
        this.setTop(nameLabel);
        this.setAlignment(nameLabel,Pos.CENTER);
        this.setStyle("-fx-border-color: grey; -fx-border-width: 1");
        incrementButton.setOnAction(e->{
            if(view.getController().getFps()<max) {
                view.getController().setFps(view.getController().getFps() + 1);
                currentValue.setText(view.getController().getFps() + " fps");
            }
        });
        decrementButton.setOnAction(e->{
            if(view.getController().getFps()>min) {
                view.getController().setFps(view.getController().getFps() - 1);
                currentValue.setText(view.getController().getFps() + " fps");
            }
        });


    }
}
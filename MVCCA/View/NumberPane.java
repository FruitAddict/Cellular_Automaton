package MVCCA.View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class NumberPane extends BorderPane {
    /**
     * Widget pane to initially control time between generations
     * Will be reused later. Made to avoid too much code in lambda
     */

    public NumberPane(int min, int max, View view){

        Button incrementButton = new Button("+");
        incrementButton.setStyle("-fx-background-color: WHITE; -fx-fill: WHITE");
        incrementButton.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button decrementButton = new Button("-");
        decrementButton.setStyle("-fx-background-color: WHITE; -fx-fill: WHITE");
        decrementButton.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Label currentValue = new Label(view.getController().getFps()+" fps");
        currentValue.setPrefWidth(100);
        currentValue.setStyle("-fx-background-color: WHITE");

        Label nameLabel = new Label("Target FPS");
        nameLabel.setFont(Font.font("Helvetica",FontWeight.NORMAL,FontPosture.REGULAR,20));
        nameLabel.setStyle("-fx-background-color: LIGHTBLUE");

        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(decrementButton,currentValue,incrementButton);
        this.setCenter(contentBox);
        this.setTop(nameLabel);
        this.setAlignment(nameLabel,Pos.CENTER);

        incrementButton.setOnAction(e->{
            if(view.getController().getFps()<200) {
                view.getController().setFps(view.getController().getFps() + 1);
                currentValue.setText(view.getController().getFps() + " fps");
            }
        });
        decrementButton.setOnAction(e->{
            if(view.getController().getFps()>1) {
                view.getController().setFps(view.getController().getFps() - 1);
                currentValue.setText(view.getController().getFps() + " fps");
            }
        });


    }
}
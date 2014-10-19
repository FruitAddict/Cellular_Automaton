package MVCCA.View;

import MVCCA.Controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class NumberPane extends BorderPane {
    /**
     * Widget pane to initially control time between generations
     * Will be reused later. Made to avoid too much code in lambda
     */
    int minValue;
    int maxValue;

    public NumberPane(String name,int min, int max, String whatIsIt, Controller controller){
        this.minValue=min;
        this.maxValue=max;

        Button incrementButton = new Button("+");
        incrementButton.setStyle("-fx-background-color: PERU; -fx-fill: WHITE");
        incrementButton.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button decrementButton = new Button("-");
        decrementButton.setStyle("-fx-background-color: ORCHID; -fx-fill: WHITE");
        decrementButton.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Label currentValue = new Label(Integer.toString(controller.getSleepTime())+" "+whatIsIt);
        currentValue.setPrefWidth(100);
        currentValue.setStyle("-fx-background-color: WHITE");

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Helvetica",FontWeight.NORMAL,FontPosture.REGULAR,20));
        nameLabel.setStyle("-fx-background-color: LIGHTBLUE");

        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(decrementButton,currentValue,incrementButton);
        this.setCenter(contentBox);
        this.setTop(nameLabel);

        incrementButton.setOnAction(e->{
            if(controller.getSleepTime()<max){
                currentValue.setText(Integer.toString(controller.getSleepTime()+1)+" "+whatIsIt);
                controller.setSleepTime(controller.getSleepTime()+1);
            }
        });

        decrementButton.setOnAction(e -> {
            if (controller.getSleepTime() > min) {
                currentValue.setText(Integer.toString(controller.getSleepTime()-1)+" "+whatIsIt);
                controller.setSleepTime(controller.getSleepTime()-1);
            }
        });
    }
}
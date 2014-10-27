package MVCCA.View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * CameraPane to control the canvas position on the screen
 */
public class CameraPane extends BorderPane {

    public CameraPane(View view){

        BorderPane mainPane = new BorderPane();
        HBox holdingBox = new HBox();
        holdingBox.setAlignment(Pos.CENTER);

        TextField xCoordInputField = new TextField(Double.toString(view.getCanvas().getTranslateX()));
        TextField yCoordInputField = new TextField(Double.toString(view.getCanvas().getTranslateY()));

        Button applyButton = new Button("Apply");
        applyButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14));


        holdingBox.getChildren().addAll(xCoordInputField,yCoordInputField);
        holdingBox.setPrefWidth(100);

        mainPane.setBottom(holdingBox);

        Label name = new Label("Camera Control");
        name.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        mainPane.setCenter(name);
        this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
        this.setTop(name);
        this.setCenter(mainPane);
        this.setBottom(applyButton);
        setAlignment(name, Pos.CENTER);
        setAlignment(applyButton, Pos.CENTER);

        applyButton.setOnAction(e->{
            try{
                double valueX = Double.parseDouble(xCoordInputField.getText());
                double valueY = Double.parseDouble(yCoordInputField.getText());
                view.getCanvas().setTranslateX(valueX);
                view.getCanvas().setTranslateY(valueY);
                applyButton.setText("Apply");
            }catch (NumberFormatException ex){
                applyButton.setText("Error");
            }
        });
    }
}

package MVCCA.View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * CameraPane to control the canvas position on the screen
 */
public class CameraPane extends BorderPane {

    public CameraPane(View view){

        BorderPane mainPane = new BorderPane();

        Button up = new Button("up");
        Button down = new Button("down");
        Button left = new Button("left");
        Button right = new Button("right");
        up.setStyle("-fx-background-color: Wheat");
        down.setStyle("-fx-background-color: Wheat");
        left.setStyle("-fx-background-color: Wheat");
        right.setStyle("-fx-background-color: Wheat");

        Label center = new Label(view.getCanvas().getTranslateX()+" "+view.getCanvas().getTranslateY());
        center.setStyle("-fx-background-color: WHITE");

        Label name = new Label("Camera Control");
        name.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        name.setStyle("-fx-background-color: LIGHTBLUE");

        mainPane.setCenter(center);
        mainPane.setTop(up);
        mainPane.setBottom(down);
        mainPane.setLeft(left);
        mainPane.setRight(right);
        mainPane.setAlignment(up, Pos.CENTER);
        mainPane.setAlignment(down, Pos.CENTER);

        this.setCenter(mainPane);
        this.setTop(name);
        setAlignment(name, Pos.CENTER);


        up.setOnAction(e->{
            view.translateCanvas(view.getCanvas().getTranslateX(),view.getCanvas().getTranslateY()-10);
            center.setText(view.getCanvas().getTranslateX()+" "+view.getCanvas().getTranslateY());
        });
        down.setOnAction(e -> {
            view.translateCanvas(view.getCanvas().getTranslateX(), view.getCanvas().getTranslateY() + 10);
            center.setText(view.getCanvas().getTranslateX() + " " + view.getCanvas().getTranslateY());
        });
        left.setOnAction(e->{
            view.translateCanvas(view.getCanvas().getTranslateX()-10,view.getCanvas().getTranslateY());
            center.setText(view.getCanvas().getTranslateX()+" "+view.getCanvas().getTranslateY());
        });
        right.setOnAction(e->{
            view.translateCanvas(view.getCanvas().getTranslateX()+10,view.getCanvas().getTranslateY());
            center.setText(view.getCanvas().getTranslateX()+" "+view.getCanvas().getTranslateY());
        });
    }
}

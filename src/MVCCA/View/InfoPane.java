package MVCCA.View;

import MVCCA.Logic.Abstract.Logic;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Info pane to display info about logics
 */
public class InfoPane extends BorderPane {
    BorderPane mainPane;
    TextArea txArea;
    Hyperlink link;

    public InfoPane(View v){
        mainPane = new BorderPane();
        txArea = new TextArea();
        txArea.setWrapText(true);
        this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
        txArea.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        update(v.getController().getLogic());
        txArea.setEditable(false);
    }

    public void update(Logic l){
        this.getChildren().clear();
        txArea.clear();
        Label name = new Label("Info ");
        name.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        File f = new File("resources\\"+l.getClass().getSimpleName()+".txt");
        if(f.exists()){
            System.out.println("File found");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line;
                while((line = reader.readLine())!=null){
                    txArea.appendText(line+"\n");
                }
            }catch(IOException ex){
                txArea.appendText("There was an error loading the info file.");
            }
        }

        File g = new File("resources\\"+l.getClass().getSimpleName()+"Link.txt");
        if(g.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(g));
                String linkText = reader.readLine();
                link = new Hyperlink(linkText);
                link.setDisable(false);
            } catch (IOException ex) {
                link = new Hyperlink("Error");
            }
        } else {
            link = new Hyperlink();
            link.setDisable(true);
        }
        this.setTop(name);
        this.setCenter(txArea);
        this.setBottom(link);
        setAlignment(name, Pos.CENTER);
        setAlignment(link, Pos.CENTER);

        link.setOnAction(e->{
            try {
                URI uri = new URI(link.getText());
                java.awt.Desktop.getDesktop().browse(uri);
            }catch(Exception ex){
                txArea.clear();
                txArea.appendText("Error opening link");
            }
        });
    }
}

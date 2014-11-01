package MVCCA.View;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Resources.Resources;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.*;
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
        try (
            InputStream inStream = Resources.class.getResourceAsStream(l.getClass().getSimpleName()+".txt");
            )
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while((line = reader.readLine())!=null){
                txArea.appendText(line+"\n");
                if(line.contains("null")){
                    txArea.clear();
                    txArea.appendText("No information found");
                    break;
                }
            }
        }catch(Exception ex){
            txArea.appendText("There was an error loading the info file.");
        }


        try (
            InputStream inStream = Resources.class.getResourceAsStream(l.getClass().getSimpleName()+"Link.txt");
            )
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String linkText = reader.readLine();
            link = new Hyperlink(linkText);
            link.setDisable(false);
        } catch (Exception ex) {
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

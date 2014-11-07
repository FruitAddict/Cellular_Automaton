package mvcca.view.utilitypanes;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mvcca.logic.abstracted.Logic;
import mvcca.logic.utilities.Grid;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * GridIO class, displays system file chooser to save or load grids. Checks whether the type of logic is right
 * by parsing the name of the file.
 */
public class GridIO {

    public static void loadGrid(Logic logic, Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add ( new FileChooser.ExtensionFilter("GRID (*.grid)", "*.grid") );
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            try (
                    ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))
                    ){
                Grid loadedGrid = (Grid)input.readObject();
                if(loadedGrid.getOwnerName().equals(logic.getCurrentGrid().getOwnerName())){
                    logic.setCurrentGrid(loadedGrid);
                }
            } catch(IOException  | ClassNotFoundException ex){
                System.out.println("There's been an error loading the file: "+ file.getName());
                ex.printStackTrace();
            }
        }
    }

    public static void saveGrid(Logic l , Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add ( new FileChooser.ExtensionFilter("GRID (*.grid)", "*.grid"));
        File file = fileChooser.showSaveDialog(stage);
        if(file!=null) {
            try (
                    ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))
            ) {
                output.writeObject(l.getCurrentGrid());

            } catch (IOException ex) {
                System.out.println("Theres been an error saving to the file: " + file.getName());
                ex.printStackTrace();
            }
        }
    }

    public static void loadFromImage(Logic l, Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"),
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG"),
                new FileChooser.ExtensionFilter("BMP files (*.bmp", "*.BMP"),
                new FileChooser.ExtensionFilter("All files","*.*"));
        File f = fileChooser.showOpenDialog(stage);
        if(f!=null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(f);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                if (image.getWidth() != 198 || image.getHeight() != 198) {
                    System.out.println("Image too large to turn into grid");
                } else {
                    PixelReader pixelReader = image.getPixelReader();
                    HashMap<Color, Integer> colorCount = new HashMap<>();

                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            Color color = pixelReader.getColor(i, j);
                            if (colorCount.containsKey(color)) {
                                colorCount.put(color, colorCount.get(color) + 1);
                            } else {
                                colorCount.put(color, 1);
                            }
                        }
                    }
                    Color dominant = null;
                    int highest = 0;
                    for (Map.Entry<Color, Integer> entry : colorCount.entrySet()) {
                        if (entry.getValue() > highest) {
                            dominant = entry.getKey();
                            highest = entry.getValue();
                        }
                    }

                    Grid newGrid = new Grid(200, 200, 1, 0, l.getName());
                    newGrid.clear();
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            boolean alive;
                            if (pixelReader.getColor(i, j).equals(dominant)) {
                                alive = false;
                                newGrid.set(i + 1, j + 1, 1);
                            } else {
                                alive = true;
                            }
                            if (l.getName().equals("Wireworld") && alive) {
                                newGrid.set(i + 1, j + 1, 4);
                            } else if (alive) {
                                newGrid.set(i + 1, j + 1, 2);
                            }
                        }
                    }
                    l.setCurrentGrid(newGrid);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

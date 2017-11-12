package auto_gui;

import auto_gui.api.LinePlus;
import auto_gui.api.Path;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Controller extends Pane {
    private Stage stage;

    private ArrayList<Path> paths = new ArrayList<>();

    @FXML
    private ImageView imageContainer;
    @FXML
    private Menu savePath, deletePath;
    @FXML
    private MenuItem openPath, about;
    @FXML
    private TextField currentLine, currentPath, numberOfLines;

    private double minLineLength = 100;
    private boolean isCreatingPath = false;

    private void setPathColorOnTextMouseOver(Text text, Path path) {
        text.setOnMouseEntered(event -> {
            path.setColor(Color.RED);
        });
        text.setOnMouseExited(event -> {
            path.setColor(Color.BLACK);
        });
    }

    private Object getLastInList(List list) {
        return list.get(list.size() - 1);
    }

    Controller(Stage stage) {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        openPath.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open path");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    paths.add(Path.fromCoordArray((double[][])new ObjectInputStream(new FileInputStream(file)).readObject()));
                    addPathsToMenus();
                    paths.get(paths.size() - 1).addToPane(this);
                } catch (Exception e) {
                    System.out.println("Error opening file");
                    e.printStackTrace();
                }
            }
        });
        setOnMousePressed(event -> {
            if (event.getX() > imageContainer.getLayoutX() && event.getX() <= imageContainer.getBoundsInLocal().getWidth() && event.getY() > imageContainer.getLayoutY() && event.getY() <= imageContainer.getBoundsInLocal().getHeight()) {
                paths.add(new Path());
                paths.get(paths.size() - 1).add(new LinePlus(LineBuilder.create().stroke(Color.BLACK).strokeWidth(2f).startX(event.getX()).startY(event.getY()).build()));
                endLine(paths.get(paths.size() - 1).get(paths.get(paths.size() - 1).size() - 1), event.getX(), event.getY(), imageContainer.getLayoutX(), imageContainer.getLayoutY(), imageContainer.getBoundsInLocal().getWidth(), imageContainer.getBoundsInLocal().getHeight());
                getChildren().add(paths.get(paths.size() - 1).get(paths.get(paths.size() - 1).size() - 1));
                isCreatingPath = true;
            }
        });
        setOnMouseDragged(event -> {
            if (isCreatingPath) {
                // the latest path
                Path path = paths.get(paths.size() - 1);
                LinePlus line = path.get(path.size() - 1);
                endLine(line, event.getX(), event.getY(), imageContainer.getLayoutX(), imageContainer.getLayoutY(), imageContainer.getBoundsInLocal().getWidth(), imageContainer.getBoundsInLocal().getHeight());
                if (line.getLength() >= minLineLength) {
                    path.add(new LinePlus(LineBuilder.create().stroke(Color.BLACK).strokeWidth(2f).startX(line.getEndX()).startY(line.getEndY()).endX(line.getEndX()).endY(line.getEndY()).build()));
                    getChildren().add(path.get(path.size() - 1));
                }
                currentLine.setText(String.format("%.2f", ((LinePlus)getLastInList(path)).getLength()));
                currentPath.setText(String.format("%.2f", path.getLength()));
                numberOfLines.setText(String.valueOf(path.size()));
            }
        });
        setOnMouseReleased(event -> {
            if (isCreatingPath) {
                // the latest path
                Path path = paths.get(paths.size() - 1);
                Line line = path.get(path.size() - 1);
                endLine(line, event.getX(), event.getY(), imageContainer.getLayoutX(), imageContainer.getLayoutY(), imageContainer.getBoundsInLocal().getWidth(), imageContainer.getBoundsInLocal().getHeight());
                //getChildren().add(line);
                simplifyPath(path);
                numberOfLines.setText(String.valueOf(path.size()));

                isCreatingPath = false;
                //savePath.getItems().add(new MenuItem("Path " + paths.size()));
                addPathsToMenus();
            }
        });
        about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About the SciBorgs Java Autonomous GUI");
            alert.setHeaderText("SciBorgs Java Autonomous GUI");
            alert.setContentText("The SciBorgs Java Autonomous GUI is a GUI for path generation. These paths will be followed by our " +
                    "robot during the Autonomous Period in the FIRST Robotics Competition.\n\n" +
                    "It mas made by Alejandro Ramos (aeramos on GitHub) for the SciBorgs robotics team (Team 1155)");
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("auto_gui/resources/SciBorgs.png"));
            alert.showAndWait();
        });
    }

    private void addPathsToMenus() {
        addPathsToMenu(savePath, true);
        addPathsToMenu(deletePath, false);
    }

    private void addPathsToMenu(Menu menu, boolean isSaveMenu) {
        menu.getItems().clear();
        menu.getItems().add(new MenuItem("", new Text("Close")));
        if (!isSaveMenu) {
            menu.getItems().add(new MenuItem("", new Text("Delete all paths")));
            for (Path path : paths) {
                setPathColorOnTextMouseOver((Text)((MenuItem)getLastInList(menu.getItems())).getGraphic(), path);
            }
            ((MenuItem)getLastInList(menu.getItems())).setOnAction(event -> {
                for (Path path : paths) {
                    path.removeFromPane(this);
                }
                paths.clear();
                addPathsToMenu(savePath, true);
                addPathsToMenu(deletePath, false);
            });
        }
        for (int i = 0; i < paths.size(); i++) {
            Text text = new Text("Path " + (i + 1));
            setPathColorOnTextMouseOver(text, paths.get(i));
            menu.getItems().add(new MenuItem("", text));
            final int j = i;
            ((MenuItem)getLastInList(menu.getItems())).setOnAction(event -> {
                if (isSaveMenu) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Path");
                    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                    File destination = fileChooser.showSaveDialog(stage);
                    if (destination != null) {
                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(destination));
                            outputStream.writeObject(paths.get(j).toCoordArray());
                        } catch (Exception e) {
                            System.out.println("Error saving");
                            e.printStackTrace();
                        }
                    }
                } else {
                    paths.get(j).removeFromPane(this);
                    paths.remove(j);
                    addPathsToMenu(savePath, true);
                    addPathsToMenu(deletePath, false);
                }
            });
        }
    }

    private void endLine(Line line, double endX, double endY, double boundStartX, double boundStartY, double boundEndX, double boundEndY) {
        if (endX < boundStartX) {
            line.setEndX(boundStartX);
        } else if (endX > boundEndX) {
            line.setEndX(boundEndX);
        } else {
            line.setEndX(endX);
        }
        if (endY < boundStartY) {
            line.setEndY(boundStartY);
        } else if (endY > boundEndY) {
            line.setEndY(boundEndY);
        } else {
            line.setEndY(endY);
        }
    }

    private void simplifyPath(Path path) {
        for (int i = path.size() - 1; i > 0; i--) {
            if (path.get(i).getSlope() == path.get(i - 1).getSlope()) {
                path.get(i).setStartX(path.get(i - 1).getStartX());
                path.get(i).setStartY(path.get(i - 1).getStartY());
                getChildren().remove(path.get(i - 1));
                path.remove(i - 1);
            }
        }
    }
}

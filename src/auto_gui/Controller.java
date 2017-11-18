package auto_gui;

import auto_gui.api.Path;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Controller extends Pane {
    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<ArrayList<Line>> lines = new ArrayList<>();

    @FXML
    private ImageView imageContainer;
    @FXML
    private Menu savePath, deletePath;
    @FXML
    private MenuItem openPath, about;
    @FXML
    private TextField pathNumber, currentPath, numberOfPoints, minimumLineLength;
    @FXML
    private Text minimumLineLengthLabel;
    @FXML
    private ChoiceBox drawMode;

    private boolean isCreatingPath = false;

    Controller() {
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
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                try {
                    Path path = new Path(file);
                    paths.add(path);
                    lines.add(getLines(paths.get(paths.size() - 1)));
                    addPathsToMenus();
                    System.out.println("Loaded path from " + file);
                } catch (IOException e) {
                    System.out.println("Error loading path from " + file);
                }
            } else {
                System.out.println("Error loading path: file not found");
            }
        });

        drawMode.getItems().addAll("Click Mode", "Drag Mode");
        drawMode.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(0)) {
                setMode(true);
                minimumLineLength.setVisible(false);
                minimumLineLengthLabel.setVisible(false);
            } else if (newValue.equals(1)) {
                setMode(false);
                minimumLineLength.setVisible(true);
                minimumLineLengthLabel.setVisible(true);
            }
        });
        drawMode.getSelectionModel().select(0);

        minimumLineLength.textProperty().addListener((observable, oldValue, newValue) -> {
            // make sure we only take a numeric value
            if (!newValue.matches("\\d*")) {
                minimumLineLength.setText(newValue.replaceAll("[^\\d]", ""));
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

    private ArrayList<Line> getLines(Path path) {
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            lines.add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(path.get(i)[0]).startY(path.get(i)[1]).endX(path.get(i)[0]).endY(path.get(i)[1]).build());
            lines.add(LineBuilder.create().strokeWidth(2f).stroke(Color.BLACK).startX(path.get(i)[0]).startY(path.get(i)[1]).endX(path.get(i + 1)[0]).endY(path.get(i + 1)[1]).build());
            getChildren().add(lines.get(lines.size() - 2));
            getChildren().add(lines.get(lines.size() - 1));
        }
        lines.add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(path.getLast()[0]).startY(path.getLast()[1]).endX(path.getLast()[0]).endY(path.getLast()[1]).build());
        getChildren().add(lines.get(lines.size() - 1));
        return lines;
    }

    private void addPathsToMenus() {
        addPathsToMenu(savePath, true);
        addPathsToMenu(deletePath, false);
    }

    private void addPathsToMenu(Menu menu, boolean isSaveMenu) {
        menu.getItems().clear();
        menu.getItems().add(new MenuItem("", new Text("Close")));
        if (!isSaveMenu) {
            Text text = new Text("Delete all paths");
            setPathColorOnTextMouseover(text, lines);
            menu.getItems().add(new MenuItem("", text));
            // when "Delete all paths" is selected, delete all paths
            ((MenuItem)getLastInList(menu.getItems())).setOnAction(event -> {
                for (ArrayList<Line> lines : lines) {
                    for (Line line : lines) {
                        getChildren().remove(line);
                    }
                }
                lines.clear();
                paths.clear();
                addPathsToMenu(savePath, true);
                addPathsToMenu(deletePath, false);
            });
        }
        for (int i = 0; i < paths.size(); i++) {
            Text text = new Text("Path " + (i + 1));
            setPathColorOnTextMouseover(text, lines.get(i).toArray(new Line[0]));
            menu.getItems().add(new MenuItem("", text));
            final int j = i;
            ((MenuItem)getLastInList(menu.getItems())).setOnAction(event -> {
                if (isSaveMenu) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Path");
                    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                    File file = fileChooser.showSaveDialog(new Stage());
                    if (file != null) {
                        if (paths.get(j).save(file)) {
                            System.out.println("Saved path to " + file);
                        } else {
                            System.out.println("Error saving path to " + file);
                        }
                    } else {
                        System.out.println("Error saving path: file not found");
                    }
                } else {
                    for (Line line : lines.get(j)) {
                        getChildren().remove(line);
                    }
                    lines.remove(j);
                    paths.remove(j);
                    addPathsToMenu(savePath, true);
                    addPathsToMenu(deletePath, false);
                }
            });
        }
    }

    private Object getLastInList(List list) {
        return list.get(list.size() - 1);
    }

    // when the mouse goes over the text, the path becomes red, when the mouse leaves, the path goes back to black
    private void setPathColorOnTextMouseover(Text text, ArrayList<ArrayList<Line>> listOfLines) {
        for (ArrayList<Line> lines : listOfLines) {
            setPathColorOnTextMouseover(text, lines.toArray(new Line[0]));
        }
    }

    private void setPathColorOnTextMouseover(Text text, Line[] lines) {
        text.setOnMouseEntered(event -> {
            for (Line line : lines) {
                line.toFront();
                line.setStroke(Color.RED);
            }
        });
        text.setOnMouseExited(event -> {
            for (Line line : lines) {
                line.toFront();
                line.setStroke(Color.BLACK);
            }
        });
    }

    private void startLine(MouseEvent event) {
        if (!isCreatingPath) {
            paths.add(new Path());
            lines.add(new ArrayList<>());

            pathNumber.setText(String.valueOf(paths.size()));
        }
        paths.get(paths.size() - 1).add(event.getX(), event.getY());

        // add the dot and the line
        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(2f).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());

        //add them to the pane
        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 2));
        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));

        isCreatingPath = true;
    }

    private void setMode(boolean clickMode) {
        if (clickMode) {
            // reset the other mode
            setOnMousePressed(event -> {
            });
            setOnMouseReleased(event -> {
            });

            setOnMouseClicked((MouseEvent event) -> {
                if (isInNode(event.getX(), event.getY(), imageContainer)) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        startLine(event);
                    } else if (event.getButton() == MouseButton.SECONDARY && isCreatingPath) {
                        paths.get(paths.size() - 1).add(event.getX(), event.getY());
                        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
                        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));
                        isCreatingPath = false;
                        addPathsToMenus();
                    }
                    currentPath.setText(String.valueOf(paths.get(paths.size() - 1).getLength()));
                    numberOfPoints.setText(String.valueOf(paths.get(paths.size() - 1).size()));
                }
            });

            setOnMouseMoved(event -> {
                if (isInNode(event.getX(), event.getY(), imageContainer) && isCreatingPath) {
                    getChildren().remove(getChildren().size() - 1);
                    lines.get(lines.size() - 1).remove(lines.get(lines.size() - 1).size() - 1);
                    lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(2f).startX(paths.get(paths.size() - 1).getLast()[0]).startY(paths.get(paths.size() - 1).getLast()[1]).endX(event.getX()).endY(event.getY()).build());
                    getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));
                }
            });

            setOnMouseDragged(getOnMouseMoved());
        } else {
            // reset the other mode
            setOnMouseClicked(event -> {
            });
            setOnMouseMoved(event -> {
            });

            setOnMousePressed(event -> {
                if (isInNode(event.getX(), event.getY(), imageContainer) && event.getButton() == MouseButton.PRIMARY) {
                    startLine(event);
                }
            });

            setOnMouseDragged(event -> {
                if (isInNode(event.getX(), event.getY(), imageContainer) && event.getButton() == MouseButton.PRIMARY && isCreatingPath) {
                    Line line = lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1);
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());

                    // add a new point. if the point is less than the minimum distance away from the last one, remove it and wait until the distance is long enough
                    paths.get(paths.size() - 1).add(event.getX(), event.getY());
                    if (paths.get(paths.size() - 1).getDistance(paths.get(paths.size() - 1).size() - 2, paths.get(paths.size() - 1).size() - 1) < Integer.parseInt(minimumLineLength.getText())) {
                        paths.get(paths.size() - 1).remove(paths.get(paths.size() - 1).size() - 1);
                    } else {
                        // add the dot and the line
                        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
                        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(2f).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());

                        // add them to the pane
                        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 2));
                        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));

                        numberOfPoints.setText(String.valueOf(paths.get(paths.size() - 1).size()));
                        currentPath.setText(String.valueOf(paths.get(paths.size() - 1).getLength()));
                    }
                }
            });

            setOnMouseReleased(event -> {
                if (isInNode(event.getX(), event.getY(), imageContainer) && event.getButton() == MouseButton.PRIMARY && isCreatingPath) {
                    // the latest path
                    paths.get(paths.size() - 1).add(event.getX(), event.getY());
                    Line line = lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1);
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());

                    lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
                    getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));

                    numberOfPoints.setText(String.valueOf(paths.get(paths.size() - 1).size()));
                    currentPath.setText(String.valueOf(paths.get(paths.size() - 1).getLength()));

                    isCreatingPath = false;
                    addPathsToMenus();
                }
            });
        }
    }

    private boolean isInNode(double x, double y, Node node) {
        return x >= node.getLayoutX() && y >= node.getLayoutY() && x <= node.getBoundsInLocal().getWidth() && y <= node.getBoundsInLocal().getHeight();
    }
}

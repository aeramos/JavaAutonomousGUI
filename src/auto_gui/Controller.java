package auto_gui;

import api.AutonomousRoutine;
import api.Position;
import api.realtime.Server;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Controller extends Pane {
    private ArrayList<AutonomousRoutine> autonomousRoutines = new ArrayList<>();
    private ArrayList<ArrayList<Line>> lines = new ArrayList<>();

    @FXML
    private ImageView imageContainer;
    @FXML
    private Menu saveAutonomousRoutine, deleteAutonomousRoutine;
    @FXML
    private MenuItem openAutonomousRoutine, about;
    @FXML
    private TextField autonomousRoutineNumber, currentAutonomousRoutine, numberOfPoints, minimumLineLength;
    @FXML
    private Text minimumLineLengthLabel;
    @FXML
    private ChoiceBox drawMode;

    private boolean isCreatingAutonomousRoutine = false;

    private Server server;
    private Line robot;

    private ScheduledExecutorService scheduledExecutorService;

    Controller(Stage stage, final String version) {
        try {
            server = new Server();
            robot = LineBuilder.create().strokeWidth(8f).stroke(Color.RED).build();
        } catch (Exception | Error e) {
            System.out.println("Error starting server, shutting down GUI");
            e.printStackTrace();
            System.exit(1);
        }
        if (server.isClosed()) {
            System.out.println("Error starting server, shutting down GUI");
            System.exit(1);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        getChildren().add(robot);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                Position position = server.getPosition();
                robot.setStartX(position.getX());
                robot.setEndX(position.getX());
                robot.setStartY(position.getY());
                robot.setEndY(position.getY());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        openAutonomousRoutine.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open autonomousRoutine");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                try {
                    AutonomousRoutine autonomousRoutine = new AutonomousRoutine(file);
                    autonomousRoutines.add(autonomousRoutine);
                    lines.add(getLines(autonomousRoutines.get(autonomousRoutines.size() - 1)));
                    addAutonomousRoutinesToMenus();
                    System.out.println("Loaded autonomousRoutine from " + file);
                } catch (IOException e) {
                    System.out.println("Error loading autonomousRoutine from " + file);
                }
            } else {
                System.out.println("Error loading autonomousRoutine: file not found");
            }
        });

        drawMode.getItems().addAll("Map Mode", "Click Mode", "Drag Mode");
        drawMode.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(0)) {
                setMode(null);
            } else if (newValue.equals(1)) {
                setMode(true);
            } else if (newValue.equals(2)) {
                setMode(false);
            }
        });
        drawMode.getSelectionModel().select(0);

        minimumLineLength.textProperty().addListener((observable, oldValue, newValue) -> {
            // make sure we only take a numeric value
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }

            // remove leading zeros
            while (newValue.length() > 1 && newValue.charAt(0) == '0') {
                newValue = newValue.substring(1);
            }

            // if the string is empty, add a 0
            if (newValue.length() == 0) {
                newValue = "0";
            }
            minimumLineLength.setText(newValue);
        });

        about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About the SciBorgs Java Autonomous GUI");
            alert.setHeaderText("SciBorgs Java Autonomous GUI\nVersion " + version);
            alert.setContentText("The SciBorgs Java Autonomous GUI is a GUI for autonomousRoutine generation. These autonomousRoutines will be followed by our " +
                    "robot during the Autonomous Period in the FIRST Robotics Competition.\n\n" +
                    "It also has the capability to get realtime coordinates of the robot while it drives and represent these on the map.\n\n" +
                    "It mas made by Alejandro Ramos (aeramos on GitHub) for the SciBorgs robotics team (Team 1155)");
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("api/resources/SciBorgs.png"));
            alert.showAndWait();
        });

        stage.setOnCloseRequest(event -> {
            scheduledExecutorService.shutdownNow();
            server.close();
        });
    }

    private ArrayList<Line> getLines(AutonomousRoutine autonomousRoutine) {
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < autonomousRoutine.size() - 1; i++) {
            lines.add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(autonomousRoutine.getCoordinate(i)[0]).startY(autonomousRoutine.getCoordinate(i)[1]).endX(autonomousRoutine.getCoordinate(i)[0]).endY(autonomousRoutine.getCoordinate(i)[1]).build());
            lines.add(LineBuilder.create().strokeWidth(2f).stroke(Color.BLACK).startX(autonomousRoutine.getCoordinate(i)[0]).startY(autonomousRoutine.getCoordinate(i)[1]).endX(autonomousRoutine.getCoordinate(i + 1)[0]).endY(autonomousRoutine.getCoordinate(i + 1)[1]).build());
            getChildren().add(lines.get(lines.size() - 2));
            getChildren().add(lines.get(lines.size() - 1));
        }
        lines.add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(autonomousRoutine.getLastCoordinate()[0]).startY(autonomousRoutine.getLastCoordinate()[1]).endX(autonomousRoutine.getLastCoordinate()[0]).endY(autonomousRoutine.getLastCoordinate()[1]).build());
        getChildren().add(lines.get(lines.size() - 1));
        return lines;
    }

    private void addAutonomousRoutinesToMenus() {
        addAutonomousRoutinesToMenu(saveAutonomousRoutine, true);
        addAutonomousRoutinesToMenu(deleteAutonomousRoutine, false);
    }

    private void addAutonomousRoutinesToMenu(Menu menu, boolean isSaveMenu) {
        menu.getItems().clear();
        menu.getItems().add(new MenuItem("", new Text("Close")));
        if (!isSaveMenu) {
            Text text = new Text("Delete all autonomousRoutines");
            setAutonomousRoutineColorOnTextMouseover(text, lines);
            menu.getItems().add(new MenuItem("", text));
            // when "Delete all autonomousRoutines" is selected, delete all autonomousRoutines
            ((MenuItem)getLastInList(menu.getItems())).setOnAction(event -> {
                isCreatingAutonomousRoutine = false;
                for (ArrayList<Line> lines : lines) {
                    for (Line line : lines) {
                        getChildren().remove(line);
                    }
                }
                lines.clear();
                autonomousRoutines.clear();
                addAutonomousRoutinesToMenu(saveAutonomousRoutine, true);
                addAutonomousRoutinesToMenu(deleteAutonomousRoutine, false);
            });
        }
        for (int i = 0; i < autonomousRoutines.size(); i++) {
            Text text = new Text("AutonomousRoutine " + (i + 1));
            setAutonomousRoutineColorOnTextMouseover(text, lines.get(i).toArray(new Line[0]));
            menu.getItems().add(new MenuItem("", text));
            final int j = i;
            ((MenuItem)getLastInList(menu.getItems())).setOnAction(event -> {
                if (isSaveMenu) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save AutonomousRoutine");
                    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                    File file = fileChooser.showSaveDialog(new Stage());
                    if (file != null) {
                        if (autonomousRoutines.get(j).save(file)) {
                            System.out.println("Saved autonomousRoutine to " + file);
                        } else {
                            System.out.println("Error saving autonomousRoutine to " + file);
                        }
                    } else {
                        System.out.println("Error saving autonomousRoutine: file not found");
                    }
                } else {
                    for (Line line : lines.get(j)) {
                        getChildren().remove(line);
                    }
                    lines.remove(j);
                    autonomousRoutines.remove(j);
                    addAutonomousRoutinesToMenu(saveAutonomousRoutine, true);
                    addAutonomousRoutinesToMenu(deleteAutonomousRoutine, false);
                }
            });
        }
    }

    private Object getLastInList(List list) {
        return list.get(list.size() - 1);
    }

    // when the mouse goes over the text, the autonomousRoutine becomes red, when the mouse leaves, the autonomousRoutine goes back to black
    private void setAutonomousRoutineColorOnTextMouseover(Text text, ArrayList<ArrayList<Line>> listOfLines) {
        text.setOnMouseEntered(event -> {
            for (ArrayList<Line> lines : listOfLines) {
                for (Line line : lines) {
                    line.toFront();
                    line.setStroke(Color.RED);
                }
            }
        });
        text.setOnMouseExited(event -> {
            for (ArrayList<Line> lines : listOfLines) {
                for (Line line : lines) {
                    line.toFront();
                    line.setStroke(Color.BLACK);
                }
            }
        });
    }

    private void setAutonomousRoutineColorOnTextMouseover(Text text, Line[] lines) {
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
        if (!isCreatingAutonomousRoutine) {
            autonomousRoutines.add(new AutonomousRoutine());
            lines.add(new ArrayList<>());

            autonomousRoutineNumber.setText(String.valueOf(autonomousRoutines.size()));
        }
        autonomousRoutines.get(autonomousRoutines.size() - 1).add((int)event.getX(), (int)event.getY(), null);

        // add the dot and the line
        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(2f).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());

        //add them to the pane
        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 2));
        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));

        isCreatingAutonomousRoutine = true;
    }

    private void setMode(Boolean clickMode) {
        if (clickMode == null) {
            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < lines.get(i).size(); j++) {
                    lines.get(i).get(j).toBack();
                }
            }

            setOnMousePressed(event -> {
            });
            setOnMouseReleased(event -> {
            });
            setOnMouseClicked(event -> {
            });
            setOnMouseMoved(event -> {
            });
            setOnMouseDragged(event -> {
            });

            robot.toFront();
        } else {
            robot.toBack();

            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < lines.get(i).size(); j++) {
                    lines.get(i).get(j).toFront();
                }
            }
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
                        } else if (event.getButton() == MouseButton.SECONDARY && isCreatingAutonomousRoutine) {
                            autonomousRoutines.get(autonomousRoutines.size() - 1).add((int)event.getX(), (int)event.getY(), null);
                            lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
                            getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));
                            isCreatingAutonomousRoutine = false;
                            addAutonomousRoutinesToMenus();
                        }
                        currentAutonomousRoutine.setText(String.valueOf(Math.round(autonomousRoutines.get(autonomousRoutines.size() - 1).getLength() * 100f) / 100f));
                        numberOfPoints.setText(String.valueOf(autonomousRoutines.get(autonomousRoutines.size() - 1).size()));
                    }
                });

                setOnMouseMoved(event -> {
                    if (isInNode(event.getX(), event.getY(), imageContainer) && isCreatingAutonomousRoutine) {
                        getChildren().remove(getChildren().size() - 1);
                        lines.get(lines.size() - 1).remove(lines.get(lines.size() - 1).size() - 1);
                        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(2f).startX(autonomousRoutines.get(autonomousRoutines.size() - 1).getLastCoordinate()[0]).startY(autonomousRoutines.get(autonomousRoutines.size() - 1).getLastCoordinate()[1]).endX(event.getX()).endY(event.getY()).build());
                        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));
                    }
                });

                setOnMouseDragged(getOnMouseMoved());

                minimumLineLength.setVisible(false);
                minimumLineLengthLabel.setVisible(false);
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
                    if (isInNode(event.getX(), event.getY(), imageContainer) && event.getButton() == MouseButton.PRIMARY && isCreatingAutonomousRoutine) {
                        Line line = lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1);
                        line.setEndX(event.getX());
                        line.setEndY(event.getY());

                        // add a new point. if the point is less than the minimum distance away from the last one, remove it and wait until the distance is long enough
                        autonomousRoutines.get(autonomousRoutines.size() - 1).add((int)event.getX(), (int)event.getY(), null);
                        if (autonomousRoutines.get(autonomousRoutines.size() - 1).getDistance(autonomousRoutines.get(autonomousRoutines.size() - 1).size() - 2, autonomousRoutines.get(autonomousRoutines.size() - 1).size() - 1) < Integer.parseInt(minimumLineLength.getText())) {
                            autonomousRoutines.get(autonomousRoutines.size() - 1).remove(autonomousRoutines.get(autonomousRoutines.size() - 1).size() - 1);
                        } else {
                            // add the dot and the line
                            lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
                            lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(2f).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());

                            // add them to the pane
                            getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 2));
                            getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));

                            numberOfPoints.setText(String.valueOf(autonomousRoutines.get(autonomousRoutines.size() - 1).size()));
                            currentAutonomousRoutine.setText(String.valueOf(Math.round(autonomousRoutines.get(autonomousRoutines.size() - 1).getLength() * 100f) / 100f));
                        }
                    }
                });

                setOnMouseReleased(event -> {
                    if (isInNode(event.getX(), event.getY(), imageContainer) && event.getButton() == MouseButton.PRIMARY && isCreatingAutonomousRoutine) {
                        // the latest autonomousRoutine
                        autonomousRoutines.get(autonomousRoutines.size() - 1).add((int)event.getX(), (int)event.getY(), null);
                        Line line = lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1);
                        line.setEndX(event.getX());
                        line.setEndY(event.getY());

                        lines.get(lines.size() - 1).add(LineBuilder.create().strokeWidth(5f).stroke(Color.BLACK).startX(event.getX()).startY(event.getY()).endX(event.getX()).endY(event.getY()).build());
                        getChildren().add(lines.get(lines.size() - 1).get(lines.get(lines.size() - 1).size() - 1));

                        numberOfPoints.setText(String.valueOf(autonomousRoutines.get(autonomousRoutines.size() - 1).size()));
                        currentAutonomousRoutine.setText(String.valueOf(Math.round(autonomousRoutines.get(autonomousRoutines.size() - 1).getLength() * 100f) / 100f));

                        isCreatingAutonomousRoutine = false;
                        addAutonomousRoutinesToMenus();
                    }
                });

                minimumLineLength.setVisible(true);
                minimumLineLengthLabel.setVisible(true);
            }
        }
    }

    private boolean isInNode(double x, double y, Node node) {
        return x >= node.getLayoutX() && y >= node.getLayoutY() && x <= node.getBoundsInLocal().getWidth() && y <= node.getBoundsInLocal().getHeight();
    }
}

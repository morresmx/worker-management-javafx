package org.example.gymbeam4;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main extends Application {

    private static final WorkerManager workerManager = new WorkerManager();
    private final TableView<Worker> workerTableView = new TableView<>();
    private final TableView<Shift> shiftTableView = new TableView<>();
    private final TableView<Shift> assignmentTableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Worker Management Information System");

        BorderPane borderPane = new BorderPane();

        Button createShiftButton = new Button("Create Shift");
        Button addMainWorkerButton = new Button("Add Main Worker");
        Button addRegularWorkerButton = new Button("Add Regular Worker");
        Button scheduleShiftButton = new Button("Schedule Shift");

        createShiftButton.setOnAction(event -> createShift());
        addMainWorkerButton.setOnAction(event -> addWorker(true));
        addRegularWorkerButton.setOnAction(event -> addWorker(false));
        scheduleShiftButton.setOnAction(event -> scheduleShift());

        VBox buttonVBox = new VBox(10);
        buttonVBox.getChildren().addAll(createShiftButton, addMainWorkerButton, addRegularWorkerButton, scheduleShiftButton);
        buttonVBox.setPadding(new Insets(40, 10, 10, 15));

        HBox tableHBox = new HBox(10);
        tableHBox.getChildren().addAll(workerTableView, shiftTableView);
        tableHBox.setPadding(new Insets(0, 0, 20, 0));

        VBox tableVBox = new VBox(10);
        tableVBox.getChildren().addAll(new Label("Workers and Shifts"), tableHBox, new Label("Shift Assignments"), assignmentTableView);
        tableVBox.setPadding(new Insets(10, 20, 20, 10));

        workerTableView.setPrefWidth(350);
        shiftTableView.setPrefWidth(350);


        borderPane.setLeft(buttonVBox);
        borderPane.setCenter(tableVBox);
        Scene scene = new Scene(borderPane, 800, 600);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void createShift() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Shift");
        dialog.setHeaderText(null);

        TextField shiftIdField = new TextField();
        DatePicker datePicker = new DatePicker();
        Spinner<Integer> startHourSpinner = new Spinner<>(0, 23, 0);
        Spinner<Integer> startMinuteSpinner = new Spinner<>(0, 59, 0);
        Spinner<Integer> endHourSpinner = new Spinner<>(0, 23, 0);
        Spinner<Integer> endMinuteSpinner = new Spinner<>(0, 59, 0);

        HBox startHourMinuteBox = new HBox(5); // Расстояние между элементами в HBox
        startHourMinuteBox.getChildren().addAll(startHourSpinner, new Label(":"), startMinuteSpinner);

        HBox endHourMinuteBox = new HBox(5); // Расстояние между элементами в HBox
        endHourMinuteBox.getChildren().addAll(endHourSpinner, new Label(":"), endMinuteSpinner);

        VBox shiftVBox = new VBox(10);
        shiftVBox.getChildren().addAll(
                new Label("Shift ID:"),
                shiftIdField,
                new Label("Start Date:"),
                datePicker,
                new Label("Start Time:"),
                startHourMinuteBox, // Группируем часы и минуты в одном HBox
                new Label("End Time:"),
                endHourMinuteBox // Группируем часы и минуты в одном HBox
        );

        dialog.getDialogPane().setContent(shiftVBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    int id = Integer.parseInt(shiftIdField.getText().trim());

                    LocalDate date = datePicker.getValue();
                    int startHour = startHourSpinner.getValue();
                    int startMinute = startMinuteSpinner.getValue();
                    int endHour = endHourSpinner.getValue();
                    int endMinute = endMinuteSpinner.getValue();

                    LocalDateTime startTime = LocalDateTime.of(date, LocalTime.of(startHour, startMinute));
                    LocalDateTime endTime = LocalDateTime.of(date, LocalTime.of(endHour, endMinute));

                    Shift shift = new Shift(id, date, startTime.toLocalTime(), endTime.toLocalTime(), null, null);
                    workerManager.addShift(shift);

                    showAlert(Alert.AlertType.INFORMATION, "Shift Created", "Shift has been created successfully.");
                    updateShiftTableView();
                    System.out.println("Shift added: " + shift);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid shift ID format.");
                }
            }
        });
    }

    // Измененный метод addWorker
    private void addWorker(boolean isMain) {
        System.out.println("Adding worker...");

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Worker");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter worker ID and name:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(workerDetails -> {
            String[] splitDetails = workerDetails.split(",");
            if (splitDetails.length == 2) {
                try {
                    int workerId = Integer.parseInt(splitDetails[0].trim());
                    String name = splitDetails[1].trim();

                    WorkerFactory factory = isMain ? MainWorkerFactory.getInstance() : new RegularWorkerFactory();
                    Worker worker = factory.createWorker(workerId, name, isMain);

                    workerManager.addWorker(worker);

                    showAlert(Alert.AlertType.INFORMATION, "Worker Added", "Worker has been added successfully.");
                    updateWorkerTableView();
                    System.out.println("Worker added: " + worker);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format.");
            }
        });
    }

    private void scheduleShift() {
        Dialog<ButtonType> shiftDialog = new Dialog<>();
        shiftDialog.setTitle("Schedule Shift");
        shiftDialog.setHeaderText("Select a shift to schedule:");

        ButtonType nextButtonType = new ButtonType("Next", ButtonBar.ButtonData.OK_DONE);
        shiftDialog.getDialogPane().getButtonTypes().addAll(nextButtonType, ButtonType.CANCEL);

        ListView<String> shiftListView = new ListView<>();
        ObservableList<String> shiftItems = FXCollections.observableArrayList();

        for (Shift shift : workerManager.getShifts()) {
            String shiftInfo = "ID: " + shift.getId() + "\n" +
                    "Start Date: " + shift.getDate() + "\n" +
                    "Time: " + shift.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + shift.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            shiftItems.add(shiftInfo);
        }

        shiftListView.setItems(shiftItems);

        shiftListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        shiftDialog.getDialogPane().setContent(shiftListView);


        Optional<ButtonType> shiftResult = shiftDialog.showAndWait();
        shiftResult.ifPresent(buttonType -> {
            if (buttonType == nextButtonType) {
                Optional<Shift> selectedShift = Optional.ofNullable(shiftListView.getSelectionModel().getSelectedItem())
                        .map(selectedShiftInfo -> {
                            // Разбираем информацию о выбранной смене, чтобы получить ее идентификатор
                            String[] shiftInfoParts = selectedShiftInfo.split("\n");
                            String shiftId = shiftInfoParts[0].split(": ")[1];
                            for (Shift shift : workerManager.getShifts()) {
                                if (Integer.toString(shift.getId()).equals(shiftId)) {
                                    return shift;
                                }
                            }
                            return null;
                        });

                selectedShift.ifPresent(shift -> {
                    Dialog<List<Worker>> workerDialog = new Dialog<>();
                    workerDialog.setTitle("Select Workers for Shift");
                    workerDialog.setHeaderText("Select workers for the shift:");

                    ButtonType scheduleButtonType = new ButtonType("Schedule", ButtonBar.ButtonData.OK_DONE);
                    workerDialog.getDialogPane().getButtonTypes().addAll(scheduleButtonType, ButtonType.CANCEL);

                    TableView<Worker> mainTableView = new TableView<>();
                    TableColumn<Worker, Integer> mainIdColumn = new TableColumn<>("ID");
                    mainIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                    TableColumn<Worker, String> mainNameColumn = new TableColumn<>("Name");
                    mainNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    mainTableView.getColumns().addAll(mainIdColumn, mainNameColumn);
                    mainTableView.getItems().addAll(workerManager.getMainWorkersForShift(shift));

                    TableView<Worker> regularTableView = new TableView<>();
                    TableColumn<Worker, Integer> regularIdColumn = new TableColumn<>("ID");
                    regularIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                    TableColumn<Worker, String> regularNameColumn = new TableColumn<>("Name");
                    regularNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    regularTableView.getColumns().addAll(regularIdColumn, regularNameColumn);
                    regularTableView.getItems().addAll(workerManager.getRegularWorkersForShift(shift));

                    TableView<Worker> selectedTableView = new TableView<>();
                    TableColumn<Worker, Integer> selectedIdColumn = new TableColumn<>("ID");
                    selectedIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                    TableColumn<Worker, String> selectedNameColumn = new TableColumn<>("Name");
                    selectedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    selectedTableView.getColumns().addAll(selectedIdColumn, selectedNameColumn);


                    // IF WE ADD 1 MAIN WORKER, WE CANT CHOOSE ANOTHER ONE ( NOT WORKING ... )
                    Button addMainButton = new Button("+");
                    addMainButton.setOnAction(event -> {
                        boolean hasMainWorker = selectedTableView.getItems().stream().anyMatch(Worker::isMain);
                        if (!hasMainWorker) {
                            Worker selectedWorker = mainTableView.getSelectionModel().getSelectedItem();
                            if (selectedWorker != null) {
                                mainTableView.getItems().remove(selectedWorker);
                                regularTableView.getItems().remove(selectedWorker);
                                selectedTableView.getItems().add(selectedWorker);
                            }
                        } else {
                            addMainButton.setDisable(true);
                        }
                    });

                    Button addAllRegularButton = new Button("+");
                    addAllRegularButton.setOnAction(event -> {
                        List<Worker> allRegularWorkers = new ArrayList<>(regularTableView.getItems());
                        selectedTableView.getItems().addAll(allRegularWorkers);
                        regularTableView.getItems().clear();
                    });

                    Button removeAllButton = new Button("-");
                    removeAllButton.setOnAction(event -> {
                        List<Worker> allSelectedWorkers = new ArrayList<>(selectedTableView.getItems());
                        for (Worker worker : allSelectedWorkers) {
                            if (worker.isMain()) {
                                mainTableView.getItems().add(worker);
                            } else {
                                regularTableView.getItems().add(worker);
                            }
                            selectedTableView.getItems().remove(worker);
                        }
                    });

                    // IF EMPTY
                    mainTableView.setPlaceholder(new Label("No main workers available."));
                    regularTableView.setPlaceholder(new Label("No regular workers available."));
                    selectedTableView.setPlaceholder(new Label("No workers selected."));
                    mainTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    regularTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    selectedTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                    TableColumn<Worker, Worker> mainActionsColumn = new TableColumn<>("Actions");
                    mainActionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
                    mainActionsColumn.setCellFactory(param -> new TableCell<Worker, Worker>() {
                        private final Button addButton = new Button("+");

                        @Override
                        protected void updateItem(Worker worker, boolean empty) {
                            super.updateItem(worker, empty);

                            if (worker == null) {
                                setGraphic(null);
                                return;
                            }

                            setGraphic(addButton);
                            addButton.setOnAction(event -> {
                                mainTableView.getItems().remove(worker);
                                regularTableView.getItems().remove(worker);
                                selectedTableView.getItems().add(worker);
                            });
                        }
                    });

                    TableColumn<Worker, Worker> regularActionsColumn = new TableColumn<>("Actions");
                    regularActionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
                    regularActionsColumn.setCellFactory(param -> new TableCell<Worker, Worker>() {
                        private final Button addButton = new Button("+");

                        @Override
                        protected void updateItem(Worker worker, boolean empty) {
                            super.updateItem(worker, empty);

                            if (empty || worker == null) {
                                setGraphic(null);
                                return;
                            }

                            setGraphic(addButton);
                            addButton.setOnAction(event -> {
                                mainTableView.getItems().remove(worker);
                                regularTableView.getItems().remove(worker);
                                selectedTableView.getItems().add(worker);
                            });
                        }
                    });


                    TableColumn<Worker, Worker> selectedActionsColumn = new TableColumn<>("Actions");
                    selectedActionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
                    selectedActionsColumn.setCellFactory(param -> new TableCell<Worker, Worker>() {
                        private final Button removeButton = new Button("-");

                        @Override
                        protected void updateItem(Worker worker, boolean empty) {
                            super.updateItem(worker, empty);

                            if (worker == null) {
                                setGraphic(null);
                                return;
                            }

                            setGraphic(removeButton);
                            removeButton.setOnAction(event -> {
                                selectedTableView.getItems().remove(worker);
                                if (worker.isMain()) {
                                    mainTableView.getItems().add(worker);
                                } else {
                                    regularTableView.getItems().add(worker);
                                }
                            });
                        }
                    });

                    mainTableView.getColumns().add(mainActionsColumn);
                    regularTableView.getColumns().add(regularActionsColumn);
                    selectedTableView.getColumns().add(selectedActionsColumn);

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.addRow(0, new Label("Main Workers"), new Label("Regular Workers"), new Label("Selected Workers"));
                    gridPane.addRow(1, mainTableView, regularTableView, selectedTableView);

                    workerDialog.getDialogPane().setContent(gridPane);

                    Optional<List<Worker>> workerDialogResult = workerDialog.showAndWait();
                    List<Worker> newSelectedWorkers = new ArrayList<>(selectedTableView.getItems());
                    workerManager.updateScheduleShift(shift, newSelectedWorkers);

                    assignmentTableView.getItems().clear();
                    assignmentTableView.getItems().addAll(workerManager.getShifts());

                    System.out.println("Смена успешно переписана");
                    System.out.println("Работяги : " + newSelectedWorkers);
                    updateScheduleTableView();
                });
            }
        });
    }

    //    DELETE AND ADD ALL NEW INFO ABOUT WORKERS
    private void updateWorkerTableView() {
        System.out.println("Updating worker table view...");

        workerTableView.getItems().clear();
        workerTableView.getItems().addAll(workerManager.getWorkers());

        // Add columns to the TableView
        TableColumn<Worker, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().workerIdProperty().asObject());

        TableColumn<Worker, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Worker, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        workerTableView.getColumns().clear();
        workerTableView.getColumns().addAll(idColumn, nameColumn, typeColumn);

        System.out.println("Current workers: " + workerManager.getWorkers());
    }

    //    DELETE AND ADD ALL NEW INFO ABOUT SHIFTS
    private void updateShiftTableView() {
        shiftTableView.getItems().clear();

        TableColumn<Shift, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Shift, LocalDate> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Shift, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> {
            Shift shift = cellData.getValue();
            return new SimpleStringProperty(shift.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + shift.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        });

        shiftTableView.getColumns().clear();
        shiftTableView.getColumns().addAll(idColumn, startDateColumn, timeColumn);

        shiftTableView.getItems().addAll(workerManager.getShifts());

        shiftTableView.refresh();
        System.out.println("Current shifts: " + workerManager.getShifts());
    }


    //    DELETE AND ADD ALL NEW INFO ABOUT SCHEDULE
    private void updateScheduleTableView() {
        assignmentTableView.getItems().clear();

        TableColumn<Shift, LocalDate> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Shift, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> {
            Shift shift = cellData.getValue();
            return new SimpleStringProperty(shift.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + shift.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        });

        TableColumn<Shift, String> mainWorkerColumn = new TableColumn<>("Main Worker");
        mainWorkerColumn.setCellValueFactory(cellData -> {
            Worker mainWorker = cellData.getValue().getMainWorker();
            return new SimpleStringProperty(mainWorker != null ? mainWorker.getName() : "");
        });

        TableColumn<Shift, String> regularWorkersColumn = new TableColumn<>("Regular Workers");
        regularWorkersColumn.setCellValueFactory(cellData -> {
            List<Worker> regularWorkers = cellData.getValue().getRegularWorkers();
            if (regularWorkers != null && !regularWorkers.isEmpty()) {
                StringBuilder names = new StringBuilder();
                for (Worker worker : regularWorkers) {
                    names.append(worker.getName()).append(", ");
                }
                return new SimpleStringProperty(names.substring(0, names.length() - 2));
            } else {
                return new SimpleStringProperty("");
            }
        });

        assignmentTableView.getColumns().clear();
        assignmentTableView.getColumns().addAll(startDateColumn, timeColumn, mainWorkerColumn, regularWorkersColumn);

        assignmentTableView.getItems().addAll(workerManager.getShifts());

        assignmentTableView.refresh();
        System.out.println("Current shifts: " + workerManager.getShifts());
    }


    // WARNING
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // LAUNCH MAIN
    public static void main(String[] args) {
        launch(args);
    }
}

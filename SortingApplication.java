import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.ArrayList;
import java.util.Random;



public class SortingApplication extends Application {
    public static final int screen_width = 1000;
    public static final int screen_height = 800;
    public static final int top_bar_width = screen_width;
    public static final int top_bar_height = 100;
    public static final int bottom_bar_width = screen_width;
    public static final int bottom_bat_height = 100;
    public static final int main_group_height = screen_width - top_bar_height - bottom_bat_height;
    public static final int max_button = 15;
    public static final int main_group_width = screen_width;
    public static final int column_width = 37;
    public static final int gap_X = 3;
    public static final int cell_width = column_width + gap_X;
    public static final int startX = (screen_width - (column_width + gap_X) * max_button) / 2;
    public static final int choiceBoxWidth = 150;
    public final Button addBtn = new Button("ADD");
    public final Button removeBtn = new Button("REMOVE");
    public final Button startBtn = new Button("START");
    public final Button shuffleBtn = new Button("SHUFFLE");
    public final TextField addField = new TextField();
    public final ChoiceBox<String> choiceBox = new ChoiceBox<>();
    public final ButtonBar buttonBar = new ButtonBar();
    public final Button FWBtn = new Button("FW"); //forward button
    public final Button BWBtn = new Button("BW"); //backward button
    public final Button playBtn = new Button("STOP");
    public final ProgressBar progressBar = new ProgressBar();
    private SequentialTransition st;

    Group root0 = new Group();
    Group mainGroup = new Group();
    HBox topBar = new HBox();
    HBox bottomBar = new HBox();
    boolean isRunning;
    public ArrayList<Column> columns = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(root0, screen_width, screen_height);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sorting Visualisation");
        primaryStage.show();

        //set position and size for 3 main parts in application
        set_position_and_size(topBar, 0, 0, top_bar_width, top_bar_height);
        set_position_and_size(mainGroup, 0, top_bar_height, main_group_width, main_group_height);
        set_position_and_size(bottomBar, 0, screen_height - bottom_bat_height, bottom_bar_width, bottom_bat_height);

        //set up some nodes inside main parts
        set_up_choice_box(choiceBox);
        playBtn.setDisable(true);
        FWBtn.setDisable(true);
        BWBtn.setDisable(true);
        buttonAction();
        this.columns.clear();
        this.columns = init_columns();
        mainGroup.getChildren().addAll(columns);

        //add nodes to 3 main parts and then add 3 main parts to the main root.
        buttonBar.getButtons().addAll(BWBtn, playBtn, FWBtn);
        topBar.getChildren().addAll(addBtn, addField, removeBtn, startBtn, shuffleBtn, choiceBox);
        bottomBar.getChildren().addAll(buttonBar, progressBar);
        root0.getChildren().addAll(topBar, mainGroup, bottomBar);

    }

    private void set_up_choice_box(ChoiceBox<String> choiceBox) {
        choiceBox.setPrefWidth(choiceBoxWidth);
        choiceBox.getItems().addAll("Bubble", "Selection", "Insertion");
        choiceBox.setValue("Bubble");
    }

    private void set_position_and_size(Parent p, int x, int y, int width, int height) {
        p.prefWidth(width);
        p.prefHeight(height);
        p.setLayoutX(x);
        p.setLayoutY(y);
    }

    void buttonAction() {
        startBtn.setOnAction(e-> start_button_action());
        removeBtn.setOnAction(e->remove_button_action());
        addBtn.setOnAction(e->add_button_action());
        shuffleBtn.setOnAction(e->shuffle_button_action());
        playBtn.setOnAction(e-> pause_button_action());
        BWBtn.setOnAction(e->back_ward_button_action());
        FWBtn.setOnAction(e->forward_button_action());
    }

    void pause_button_action() {
        if (isRunning) {
            st.pause();
            isRunning = false;
            playBtn.setText("CONTINUE");
        } else {
            st.play();
            isRunning = true;
            playBtn.setText("STOP");
        }
    }

    void start_button_action() {
        st = new SequentialTransition();
        addBtn.setDisable(true);
        removeBtn.setDisable(true);
        startBtn.setDisable(true);
        choiceBox.setDisable(true);
        shuffleBtn.setDisable(true);
        playBtn.setDisable(false);
        FWBtn.setDisable(false);
        BWBtn.setDisable(false);
        st.getChildren().addAll(get_sort_type().run(columns));
        isRunning = true;
        st.play();
        st.setOnFinished(e->{
            addBtn.setDisable(false);
            removeBtn.setDisable(false);
            startBtn.setDisable(false);
            choiceBox.setDisable(false);
            shuffleBtn.setDisable(false);
            playBtn.setDisable(true);
            FWBtn.setDisable(true);
            BWBtn.setDisable(true);
            isRunning = false;
        });
    }

    private void remove_button_action() {
        if (!columns.isEmpty()) {
            int t = columns.size() - 1;
            mainGroup.getChildren().removeAll(columns.get(t));
            columns.remove(t);
        }
    }

    private void add_button_action() {
        String text = addField.getText().replaceAll(" ", "");
        boolean hasAnother = false;
        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) hasAnother = true;
        }
        if (!hasAnother && columns.size() <= max_button) {
            int value = Integer.parseInt(text);
            if (value >= 1 && value <= Column.maxValue) {
                Column col = new Column(value, columns.size());
                mainGroup.getChildren().add(col);
                columns.add(col);
            } else {
                JOptionPane.showMessageDialog(null, "Giá trị nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void shuffle_button_action() {
        mainGroup.getChildren().clear();
        this.columns = init_columns();
        mainGroup.getChildren().addAll(columns);
    }

    private void back_ward_button_action() {
        double rate = st.getRate();
        st.setRate(-10);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        st.setRate(rate);
                    }
                }, 500
        );
    }

    private void forward_button_action() {
        double rate = st.getRate();
        st.setRate(10);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        st.setRate(rate);
                    }
                },
                500
        );
    }

    private Sorting get_sort_type() {
        switch (choiceBox.getValue()) {
            case "Bubble":
                return new Bubble();
            case "Selection":
                return new Selection();
            case "Insertion":
                return new Insertion();
        }
        return null;
    }

    private ArrayList<Column> init_columns() {
        ArrayList<Column> columns = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int value = 1 + r.nextInt(50);
            Column col = new Column(value, i);
            columns.add(col);
        }
        return columns;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SortingApplication extends Application {
    public static final int screen_width = 1200;
    public static final int screen_height = 800;
    public static final int description_width = 200;
    public static final int top_bar_width = screen_width - description_width;
    public static final int top_bar_height = 100;
    public static final int bottom_bar_width = screen_width;
    public static final int bottom_bat_height = 100;
    public static final int main_group_height = screen_width - top_bar_height - bottom_bat_height;
    public static final int main_group_width = screen_width - description_width;
    public static final int max_button = 15;
    public static final int column_width = 37;
    public static final int height_per_value = 6;
    public static final int gap_X = 3;
    public static final int cell_width = column_width + gap_X;
    public static final int startX = (screen_width - (column_width + gap_X) * max_button) / 2;
    public static final int choiceBoxWidth = 150;
    private final Button addBtn = new Button("ADD");
    private final Button removeBtn = new Button("REMOVE");
    private final Button startBtn = new Button("START");
    private final Button shuffleBtn = new Button("SHUFFLE");
    private final TextField addField = new TextField();
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private final ButtonBar buttonBar = new ButtonBar();
    private final Button FWBtn = new Button("FW"); //forward button
    private final Button BWBtn = new Button("BW"); //backward button
    private final Button playBtn = new Button("STOP");
    private final ProgressBar progressBar = new ProgressBar();
    private SequentialTransition st;

    private Group root0 = new Group();
    private Group mainGroup = new Group();
    private HBox topBar = new HBox();
    private HBox bottomBar = new HBox();
    private VBox descriptionArea;
    private boolean isRunning;
    public ArrayList<Column> columns = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root0, screen_width, screen_height);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sorting Visualisation");
        primaryStage.show();

        //set position and size for 3 main parts in application
        descriptionArea = set_up_sorting_description();
        set_position_and_size(topBar, 0, 0, top_bar_width, top_bar_height);
        set_position_and_size(mainGroup, 0, top_bar_height, main_group_width, main_group_height);
        set_position_and_size(bottomBar, 0, screen_height - bottom_bat_height, bottom_bar_width, bottom_bat_height);

        //set up some nodes inside main parts
        add_tool_tip(addField, "1. Mỗi lần chỉ có thể thêm 1 số\n" +
                                    "2. Mỗi số chỉ có thể nhận giá trị từ 1 đến 50 và không chứa ký tự khác số\n" +
                                    "3. Những cột có giá trị nhỏ hơn 4 sẽ không hiển thị số");
        set_up_choice_box(choiceBox);
        playBtn.setDisable(true);
        FWBtn.setDisable(true);
        BWBtn.setDisable(true);
        button_action();
        this.columns.clear();
        this.columns = init_columns();
        mainGroup.getChildren().addAll(columns);

        //add nodes to 3 main parts and then add 3 main parts to the main root.
        buttonBar.getButtons().addAll(BWBtn, playBtn, FWBtn);
        topBar.getChildren().addAll(addBtn, addField, removeBtn, startBtn, shuffleBtn, choiceBox);
        bottomBar.getChildren().addAll(buttonBar, progressBar);
        root0.getChildren().addAll(topBar, mainGroup, bottomBar, descriptionArea);
    }

    private void add_tool_tip(TextField node, String text) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText(text);
        node.setTooltip(tooltip);
    }

    void button_action() {
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
            int value = 1 + r.nextInt(Column.maxValue);
            Column col = new Column(value, i);
            columns.add(col);
        }
        return columns;
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

    private VBox set_up_sorting_description() {
        HBox[] line = new HBox[4];
        line[0] = set_up_description_line(Sorting.default_color, "Màu mặc định");
        line[1] = set_up_description_line(Sorting.compare, "So sánh 2 cột");
        line[2] = set_up_description_line(Sorting.swap, "Đổi chỗ 2 cột");
        line[3] = set_up_description_line(Sorting.in_right_position, "Cột đã ở đúng vị trí");
        VBox vbox = new VBox();
        vbox.setPrefWidth(description_width);
        vbox.getChildren().addAll(line);
        vbox.setLayoutX(screen_width - description_width);
        vbox.setLayoutY(0);
        return vbox;
    }

    private HBox set_up_description_line(Color color, String text) {
        int height = height_per_value * 4;
        Rectangle rect = new Rectangle();
        rect.setWidth(column_width);
        rect.setHeight(height);
        rect.setFill(color);
        Text t = new Text(" " + text);
        HBox result = new HBox();
        result.getChildren().addAll(rect, t);
        return result;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

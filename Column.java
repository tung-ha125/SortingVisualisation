import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Column extends StackPane {
    public static int maxValue = 50;
    private int value;
    public Rectangle rect = new Rectangle();

    public int getValue() {
        return this.value;
    }

    public void set_value(int value) {
        this.value = value;
    }

    /**
     * constructor.
     * @param value value the column has
     * @param i use to support for setting position
     */
    public Column(int value, int i) {
        this.value = value;
        this.setLayoutX(SortingApplication.startX + i * SortingApplication.cell_width);
        this.setLayoutY(SortingApplication.main_group_height / 2 - value * 6);
        this.setWidth(SortingApplication.column_width);
        this.setHeight(value * 6);
        rect.setFill(Color.LIGHTBLUE);
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(this.getWidth());
        rect.setHeight(this.getHeight());

        if (value >= 4) {
            Text text = new Text("" + value);
            this.getChildren().addAll(rect, text);
        } else {
            this.getChildren().add(rect);
        }
    }

    /**
     * move horizontally animation.
     * @param x move length
     * @return move animation
     */
    public TranslateTransition moveX(int x) {
        TranslateTransition trans = new TranslateTransition();
        trans.setNode(this);
        trans.setDuration(Duration.millis(1000));
        trans.setByX(x);
        return trans;
    }

    /**
     * move vertically animation.
     * @param y move length
     * @return move animation
     */
    public TranslateTransition moveY(int y) {
        TranslateTransition trans = new TranslateTransition();
        trans.setNode(this);
        trans.setDuration(Duration.millis(1000));
        trans.setByY(y);
        return trans;
    }
}

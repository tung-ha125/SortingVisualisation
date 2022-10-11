import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;


public abstract class Sorting {
    public static final Color default_color = Color.LIGHTBLUE;
    public static final Color compare = Color.YELLOW;
    public static final Color swap = Color.LIGHTGREEN;
    public static final Color in_right_position = Color.ORANGE;
    public Sorting() {

    }

    public abstract ArrayList<Transition> run(ArrayList<Column> columns);

    /**
     * swap 2 columns.
     * @param i first column
     * @param j second column
     */
    public static ParallelTransition swap(ArrayList<Column> a, int i, int j) {
        ParallelTransition paral = new ParallelTransition();
        int dis = j - i;
        paral.getChildren().addAll(a.get(i).moveX(dis * SortingApplication.cell_width),
                a.get(j).moveX(-dis * SortingApplication.cell_width));
        Column temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
        return paral;
    }

    /**
     * coloring a list of columns.
     * @param list list of columns
     * @param color color you want to add
     * @return a parallel animation.
     */
    public static ParallelTransition colorColumns(List<Column> list, Color color) {
        ParallelTransition pal = new ParallelTransition();
        for (int i = 0; i < list.size(); i++) {
            pal.getChildren().add(fillTransition(list.get(i), color));
        }
        return pal;
    }

    /**
     * coloring a single column.
     * @param col column
     * @param color color you want to add
     * @return a fill animation
     */
    public static FillTransition fillTransition(Column col, Color color) {
        FillTransition trans = new FillTransition();
        trans.setShape(col.rect);
        trans.setToValue(color);
        trans.setDuration(Duration.millis(500));
        return trans;
    }
}

import javafx.animation.Transition;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Bubble extends Sorting {
    Bubble() {

    }

    public ArrayList<Transition> run(ArrayList<Column> columns) {
        return bubble_sort(columns);
    }

    /**
     * set up for buble sort animation.
     * @param columns columns you want to sort
     * @return a list of animations.
     */
    public ArrayList<Transition> bubble_sort(ArrayList<Column> columns) {
        ArrayList<Transition> result = new ArrayList<>();
        int n = columns.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                result.add(Sorting.colorColumns(columns.subList(j, j + 2), compare));
                if (columns.get(j).getValue() > columns.get(j + 1).getValue()) {
                    result.add(Sorting.colorColumns(columns.subList(j, j + 2), swap));
                    result.add(Sorting.swap(columns, j, j + 1));
                    result.add(Sorting.colorColumns(columns.subList(j, j + 2), default_color));
                } else {
                    result.add(Sorting.colorColumns(columns.subList(j, j + 2), default_color));
                }
            }
            result.add(Sorting.fillTransition(columns.get(n - i - 1), in_right_position));
        }
        result.add(Sorting.fillTransition(columns.get(0), in_right_position));
        return result;
    }
}

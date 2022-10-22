import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import java.util.ArrayList;

public class Insertion extends Sorting {
    Insertion() {

    }

    public ArrayList<Transition> run(ArrayList<Column> columns) {
        return insertion_sort(columns);
    }

    /**
     * set up for insertion sort animation.
     * @param columns columns you want to sort
     * @return a list of animations.
     */
    public ArrayList<Transition> insertion_sort(ArrayList<Column> columns) {
        ArrayList<Transition> result = new ArrayList<>();
        int n = columns.size();
        result.add(Sorting.fillTransition(columns.get(0), in_right_position));
        for (int i = 1; i < n; i++) {
            Column key = columns.get(i);
            result.add(key.moveY(300));
            result.add(Sorting.fillTransition(key, compare));
            int j = i - 1;
            while (j >= 0 && columns.get(j).getValue() > key.getValue()) {
                ParallelTransition pal = new ParallelTransition();
                pal.getChildren().add(columns.get(j).moveX(SortingApplication.cell_width));
                pal.getChildren().add(key.moveX(-SortingApplication.cell_width));
                result.add(pal);
                columns.set(j+1, columns.get(j));
                j = j - 1;
            }
            result.add(key.moveY(-300));
            result.add(Sorting.fillTransition(key, in_right_position));
            columns.set(j+1, key);
        }
        result.add(Sorting.fillTransition(columns.get(n-1), in_right_position));
        return result;
    }
}

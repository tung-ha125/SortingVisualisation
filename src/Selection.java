import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Selection extends Sorting {
    Selection() {

    }

    public ArrayList<Transition> run(ArrayList<Column> columns) {
        return selectionSort(columns);
    }

    /**
     * set up for selection sort animation.
     * @param columns columns you want to sort
     * @return a list of animations.
     */
    public ArrayList<Transition> selectionSort(ArrayList<Column> columns)
    {
        ArrayList<Transition> result = new ArrayList<>();
        int n = columns.size();
        int min_idx;

        // One by one move boundary of
        // unsorted subarray
        for (int i = 0; i < n-1; i++)
        {

            // Find the minimum element in
            // unsorted array
            min_idx = i;
            result.add(Sorting.fillTransition(columns.get(min_idx), compare));
            for (int j = i+1; j < n; j++) {
                result.add(Sorting.fillTransition(columns.get(j), compare));
                if (columns.get(j).getValue() < columns.get(min_idx).getValue()) {
                    ParallelTransition pal = new ParallelTransition();
                    pal.getChildren().add(Sorting.fillTransition(columns.get(min_idx), default_color));
                    pal.getChildren().add(Sorting.fillTransition(columns.get(j), compare));
                    result.add(pal);
                    min_idx = j;
                } else {
                    result.add(Sorting.fillTransition(columns.get(j), default_color));
                }
            }

            // Swap the found minimum element
            // with the first element
            if(min_idx!=i) {
                result.add(Sorting.swap(columns, i, min_idx));
            }
            result.add(Sorting.fillTransition(columns.get(i), in_right_position));
        }
        result.add(Sorting.fillTransition(columns.get(n-1), in_right_position));
        return result;
    }
}

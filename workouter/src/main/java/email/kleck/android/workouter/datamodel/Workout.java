package email.kleck.android.workouter.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Workout extends BaseItem implements Serializable {

    public final static String TAG_ELEMENT = "workout";

    public final static String TAG_MAX_WEIGHT_PERCENTAGE = "max_weight_percentage";
    public final static String TAG_WEEKDAY = "weekday";
    public final static String TAG_ENDURANCE = "endurance";
    public final static String TAG_ORDER = "order";
    public final static String TAG_NAME = "name";

    public List<Exercise> exercises = new ArrayList<>();
    // the percentage which will calculate the weights
    public String name;
    public String maxWeightPercentage;
    public transient float[] maxWeightPercentages;
    public String weekday;
    public transient int[] weekdays;
    public boolean endurance;
    public int order;

    public transient boolean isNew = false;

    @Override
    public String toString() {
        return "{" +
                "\"exercise\":" + Arrays.toString(exercises.stream().map(e -> e.id).toArray()) +
                ",\"" + TAG_NAME + "\":\"" + name + '\"' +
                ",\"" + TAG_MAX_WEIGHT_PERCENTAGE + "\":\"" + maxWeightPercentage + '\"' +
                ",\"" + TAG_WEEKDAY + "\":\"" + weekday + '\"' +
                ",\"" + TAG_ENDURANCE + "\":" + endurance +
                ",\"" + TAG_ORDER + "\":" + order +
                ",\"" + TAG_ID + "\":" + id +
                '}';
    }
}

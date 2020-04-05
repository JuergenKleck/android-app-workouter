package email.kleck.android.workouter.datamodel;

import java.io.Serializable;

public class Device extends BaseItem implements Serializable {

    public final static String TAG_ELEMENT = "device";

    public final static String TAG_NAME = "name";
    public final static String TAG_WEIGHT_STEPS = "weight_steps";
    public final static String TAG_START_WEIGHT = "start_weight";
    public final static String TAG_MAX_WEIGHT = "max_weight";
    public final static String TAG_PERSONAL_MAX_WEIGHT = "personal_max_weight";
    public final static String TAG_DIFFICULTY = "difficulty";
    public final static String TAG_IGNORE_PERCENT = "ignore_percent";
    public final static String TAG_ENDURANCE_WEIGHT = "endurance_weight";

    public String name;
    // for weight lifting
    public float startWeight = 0f;
    public float weightSteps = 0f;
    public float maxWeight = 0f;
    public float personalMaxWeight = 0f;
    // for running, stepping or similar
    public float difficulty = 0;
    public boolean ignorePercent = false;
    public float enduranceWeight = 0f;

    public transient boolean isNew = false;

    @Override
    public String toString() {
        return "{" +
                "\"" + TAG_NAME + "\":\"" + name + '\"' +
                ",\"" + TAG_START_WEIGHT + "\":" + startWeight +
                ",\"" + TAG_WEIGHT_STEPS + "\":" + weightSteps +
                ",\"" + TAG_MAX_WEIGHT + "\":" + maxWeight +
                ",\"" + TAG_PERSONAL_MAX_WEIGHT + "\":" + personalMaxWeight +
                ",\"" + TAG_DIFFICULTY + "\":" + difficulty +
                ",\"" + TAG_IGNORE_PERCENT + "\":" + ignorePercent +
                ",\"" + TAG_ENDURANCE_WEIGHT + "\":" + enduranceWeight +
                ",\"" + TAG_ID + "\":" + id +
                '}';
    }
}

package email.kleck.android.workouter.datamodel;

import java.io.Serializable;

public class Exercise extends BaseItem implements Serializable {

    public final static String TAG_ELEMENT = "exercise";

    public final static String TAG_REPETITIONS = "repetitions";
    public final static String TAG_ROUNDS = "rounds";
    public final static String TAG_ENDURANCE_REPETITIONS = "endurance_repetitions";
    public final static String TAG_ENDURANCE_ROUNDS = "endurance_rounds";
    public final static String TAG_TIME = "time";

    public Device device;
    // mostly for weight lifting
    public int repetitions;
    public int rounds;
    public int enduranceRepetitions;
    public int enduranceRounds;
    // for other device types
    public int time;

    public transient boolean isNew = false;

    @Override
    public String toString() {
        return "{" +
                "\"device\":" + device.id +
                ",\"" + TAG_REPETITIONS + "\":" + repetitions +
                ",\"" + TAG_ROUNDS + "\":" + rounds +
                ",\"" + TAG_ENDURANCE_REPETITIONS + "\":" + enduranceRepetitions +
                ",\"" + TAG_ENDURANCE_ROUNDS + "\":" + enduranceRounds +
                ",\"" + TAG_TIME + "\":" + time +
                ",\"" + TAG_ID + "\":" + id +
                '}';
    }
}

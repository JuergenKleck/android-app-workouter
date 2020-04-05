package email.kleck.android.workouter.datamodel;

import java.io.Serializable;

public class Settings implements Serializable {

    public final static String TAG_ELEMENT = "settings";

    public final static String TAG_TEXT_R = "text_r";
    public final static String TAG_TEXT_G = "text_g";
    public final static String TAG_TEXT_B = "text_b";
    public final static String TAG_BACK_R = "back_r";
    public final static String TAG_BACK_G = "back_g";
    public final static String TAG_BACK_B = "back_b";
    public final static String TAG_TEXT_SIZE = "text_size";
    public final static String TAG_KG = "kg";

    public int cTextR;
    public int cTextG;
    public int cTextB;
    public int cBackR;
    public int cBackG;
    public int cBackB;

    public int textSize;

    public boolean kg;

    @Override
    public String toString() {
        return "{" +
                "\"" + TAG_TEXT_R + "\":" + cTextR +
                ",\"" + TAG_TEXT_G + "\":" + cTextG +
                ",\"" + TAG_TEXT_B + "\":" + cTextB +
                ",\"" + TAG_BACK_R + "\":" + cBackR +
                ",\"" + TAG_BACK_G + "\":" + cBackG +
                ",\"" + TAG_BACK_B + "\":" + cBackB +
                ",\"" + TAG_TEXT_SIZE + "\":" + textSize +
                ",\"" + TAG_KG + "\":" + kg +
                '}';
    }
}

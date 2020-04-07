package email.kleck.android.workouter.business;

/**
 * Utility class
 */
public final class Utility {

    public static boolean isNotEmpty(String data) {
        return data != null && !data.isEmpty();
    }

    public static String intToString(int[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(Integer.valueOf(data[i]).toString());
            if (i + 1 < data.length) sb.append(",");
        }
        return sb.toString();
    }

    public static int[] stringToInt(String data) {
        String[] values = data.length() > 0 ? data.split(",") : new String[0];
        int[] ints = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            ints[i] = Integer.parseInt(values[i]);
        }
        return ints;
    }

    public static String floatToString(float[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(Float.valueOf(data[i]).toString());
            if (i + 1 < data.length) sb.append(",");
        }
        return sb.toString();
    }

    public static float[] stringToFloat(String data) {
        String[] values = data.length() > 0 ? data.split(",") : new String[0];
        float[] floats = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            floats[i] = Float.parseFloat(values[i]);
        }
        return floats;
    }
}

package email.kleck.android.workouter.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test the utility class
 */
class UtilityTest {

    @Test
    void isNotEmpty_Providing_Null() {
        Assertions.assertFalse(Utility.isNotEmpty(null));
    }

    @Test
    void isNotEmpty_Providing_Empty() {
        Assertions.assertFalse(Utility.isNotEmpty(""));
    }

    @Test
    void isNotEmpty_Providing_NonEmpty() {
        Assertions.assertTrue(Utility.isNotEmpty("not empty"));
    }

    @Test
    void intToString_Providing_Empty() {
        Assertions.assertEquals("", Utility.intToString(new int[0]));
    }

    @Test
    void intToString_Providing_Array() {
        Assertions.assertEquals("1,5,25", Utility.intToString(new int[]{1, 5, 25}));
    }

    @Test
    void stringToInt_Providing_Text() {
        Assertions.assertThrows(NumberFormatException.class, () -> Utility.stringToInt("text"));
    }

    @Test
    void stringToInt_Providing_Floats() {
        Assertions.assertArrayEquals(new int[]{5, 3, 1}, Utility.stringToInt("5,3,1"));
    }

    @Test
    void floatToString_Providing_Empty() {
        Assertions.assertEquals("", Utility.floatToString(new float[0]));
    }

    @Test
    void floatToString_Providing_Array() {
        Assertions.assertEquals("1.0,0.5,0.25", Utility.floatToString(new float[]{1.0f, 0.5f, 0.25f}));
    }

    @Test
    void stringToFloat_Providing_Text() {
        Assertions.assertThrows(NumberFormatException.class, () -> Utility.stringToFloat("text"));
    }

    @Test
    void stringToFloat_Providing_Floats() {
        Assertions.assertArrayEquals(new float[]{5.0f, 2.5f, 1.25f}, Utility.stringToFloat("5, 2.5, 1.25"));
    }
}
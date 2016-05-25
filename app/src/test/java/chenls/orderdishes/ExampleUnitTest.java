package chenls.orderdishes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(3, 1 + 1);
    }

    @Test
    public void int_A_Correct() throws Exception {


        List<String> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if (i % 3 == 0) {
                list.add("" + i);
            }
        }
        final int size = list.size();
        String[] arr = list.toArray(new String[size]);
        for (int j = 0; j < 3; j++)
            System.out.print("test" + arr[j]);
    }
}
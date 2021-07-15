import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void getFieldsNames() {
        SimpleData simpleData = new SimpleData();
        String result1 = Main.getFieldsNames(simpleData);
        String result2 = "intData as int\n" + "byteData as byte\n" + "objectData as java.lang.Object\n" + "stringData as java.lang.String\n";
        assertEquals(result1, result2);
    }
}
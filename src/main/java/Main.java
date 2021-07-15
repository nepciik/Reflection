import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        SimpleData simpleData = new SimpleData();
        WithCollectionData withCollectionData = new WithCollectionData();
        //1
        printFieldsNames(simpleData);
        printFieldsNames(withCollectionData);
        //2
        printMethodsNames(simpleData);
        printMethodsNames(withCollectionData);

        simpleData = new SimpleData(7, (byte) 0x07, "7", "7");
        String[] stringArray = {"6", "6"};
        List<String> listString = new LinkedList<>();
        listString.add("1");
        listString.add("2");
        listString.add("3");
        Map<String, SimpleData> mapStringSimpleData = new TreeMap<>();
        mapStringSimpleData.put("a", new SimpleData());
        mapStringSimpleData.put("b", simpleData);
        withCollectionData = new WithCollectionData(6, (byte) 0x06, "6", "6", stringArray, listString, mapStringSimpleData);
        //3
        //printFieldsValues(simpleData);
        //printFieldsValues(withCollectionData);

        //4
        callSetterWhith8(simpleData);
        //printFieldsValues(simpleData);

        //5
        SimpleData newSimpleData = new SimpleData();
        //printFieldsValues(simpleData);
        //printFieldsValues(newSimpleData);
        copy(simpleData, newSimpleData);
        //printFieldsValues(newSimpleData);

        //6
        //printFieldsValues(copyOf(newSimpleData));

        //7
        //benchmark();

    }

    private static void printLine() {
        System.out.println("--------------------------------------------");
    }

    protected static String getFieldsNames(Object object) {
        String result = "";
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            result += f.getName() + " as " + f.getType().getCanonicalName() + "\n";
        }
        Class cl = object.getClass();
        while (cl.getSuperclass() != null) {
            cl = cl.getSuperclass();
            Field[] fields2 = cl.getDeclaredFields();
            for (Field f : fields2) {
                result += f.getName() + " as " + f.getType().getCanonicalName() + "\n";
            }
        }
        return result;
    }

    private static void printFieldsNames(Object object) {
        printLine();
        System.out.println("Поля класса " + object + " и родительских классов:");
        System.out.println(getFieldsNames(object));
    }

    protected static String getMethodsNames(Object object) {
        String result = "";
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method m : methods) {
            result += m.getName() + " return " + m.getReturnType().getCanonicalName() + "\n";
        }
        Class cl = object.getClass();
        while (cl.getSuperclass().getSuperclass() != null) {
            cl = cl.getSuperclass();
            Method[] methods2 = cl.getDeclaredMethods();
            for (Method m : methods2) {
                result += m.getName() + " return " + m.getReturnType().getCanonicalName() + "\n";
            }
        }
        return result;
    }

    private static void printMethodsNames(Object object) {
        printLine();
        System.out.println("Методы класса " + object + " и родительских классов:");
        System.out.println(getMethodsNames(object));
    }

    private static void printFieldsValues(Object object) {
        printLine();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object value = f.get(object);
                System.out.println(f.getName() + " as " + f.getType().getCanonicalName() + " = " + value);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void callSetterWhith8(Object object) {
        printLine();
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method m : methods) {
            String name = m.getName();
            if (!name.substring(0, 3).equals("set"))
                continue;
            Class<?>[] argTypes = m.getParameterTypes();
            try {
                switch (argTypes[0].getTypeName()) {
                    case "int":
                        m.invoke(object, 8);
                        break;
                    case "byte":
                        m.invoke(object, (byte) 8);
                        break;
                    default:
                        m.invoke(object, "8");
                        break;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void copy(Object source, Object dist) {
        Class<?> sourceClass = source.getClass();
        Class<?> distClass = dist.getClass();
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            try {
                Field distField = distClass.getDeclaredField(sourceField.getName());
                sourceField.setAccessible(true);
                distField.setAccessible(true);
                distField.set(dist, sourceField.get(source));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static Object copyOf(Object source) {
        try {
            Constructor<?> constructor = source.getClass().getConstructor();
            Object dist = constructor.newInstance();
            copy(source, dist);
            return dist;
        } catch (Exception e) {
            return null;
        }
    }

    public static void benchmark() {
        try {
            Date start = new Date();
            SimpleData simpleData = new SimpleData();
            for (int i = 0; i < 1000000000; ++i)
                simpleData.setIntData(500);
            System.out.println(new Date().getTime() - start.getTime());

            Method method = SimpleData.class.getDeclaredMethod("setIntData", Integer.TYPE);
            start = new Date();
            for (int i = 0; i < 1000000000; ++i) {
                method.invoke(simpleData, 500);
            }
            System.out.println(new Date().getTime() - start.getTime());

        }catch (Exception e){
            System.out.println(e);
        }
    }
}

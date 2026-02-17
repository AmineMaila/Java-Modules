package fr.forty_two.reflection.app;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.StringJoiner;

import fr.forty_two.reflection.exceptions.ClassNotFoundException;
import fr.forty_two.reflection.exceptions.PackageNotFoundException;

public class Program {
    private static final String CLASSES_PACKAGE_PATH = "fr/forty_two/reflection/classes";
    private static final String CLASS_SUFFIX = ".class";
    private static final List<Class<?>> classes = new ArrayList<>();

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            scanForClasses();
            printClasses();
            System.out.println("---------------------");
            System.out.println("Enter class name:");
            System.out.print("-> ");
            String line  = sc.nextLine();
            Class<?> chosenClass = getClass(line);
            printClass(chosenClass);
            System.out.println("---------------------");
            System.out.println("Let's create an object.");
            Object obj = createObject(chosenClass, sc);
            System.out.println("Object created: " + obj);
            System.out.println("---------------------");
            updateField(obj, sc);
            System.out.println("Object updated: " + obj);
            System.out.println("---------------------");
            var returnValue = callMethod(obj, sc);
            if (returnValue.isPresent()) {
                System.out.println("Method returned:");
                System.out.println(returnValue.get());
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

    static private void scanForClasses() {
        URL path = Program.class.getClassLoader().getResource(CLASSES_PACKAGE_PATH);

        if (path == null) {
            System.out.println("test");
            throw new PackageNotFoundException("unable to locate 'classes' package, try creating package: " + CLASSES_PACKAGE_PATH);
        }
        try {
            File directory = new File(path.toURI());
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(CLASS_SUFFIX)) {
                    String fileName = file.getName();
                    String className =
                        CLASSES_PACKAGE_PATH.replace('/', '.')
                        + "."
                        + fileName.substring(0, fileName.length() - CLASS_SUFFIX.length());
                    classes.add(Class.forName(className));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PackageNotFoundException("unable to locate 'classes' package, try creating package: " + CLASSES_PACKAGE_PATH);
        }
    }

    static private void printClasses() {
        System.out.println("Classes:");
        for (var clazz : classes) {
            System.out.println(clazz.getSimpleName());
        }
    }

    static private void printClass(Class<?> clazz) {
        System.out.println("fields:");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.printf("\t%s%n", field.getName());
        }

        System.out.println("methods:");

        Method[] methods = clazz.getDeclaredMethods();
        Class<?> superClazz = clazz.getSuperclass();
        for (Method method : methods) {
            try {
                superClazz.getMethod(method.getName(), method.getParameterTypes());
                continue;
            } catch (NoSuchMethodException e) {
                // ignore
            }
            StringJoiner sj = new StringJoiner(",", "\t%s(".formatted(method.getName()), ")");
            Class<?>[] parameters =  method.getParameterTypes();
            for (Class<?> param : parameters) {
                sj.add(param.getSimpleName());
            }
            System.out.println(sj);
        }
    }

    static private Class<?> getClass(String className) {
        for (var clazz : classes) {
            if (clazz.getSimpleName().equals(className)) {
                return clazz;
            }
        }
        throw new ClassNotFoundException("Failed to locate '" + className + "' class");
    }

    static private Object createObject(Class<?> clazz, Scanner sc)
        throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Constructor<?> best = null;

        for (var constructor : constructors) {
            if (best == null || constructor.getParameterCount() > best.getParameterCount()) {
                best = constructor;
            }
        }

        if (!best.canAccess(null)) {
            best.setAccessible(true);
        }
        Parameter[] params = best.getParameters();
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            System.out.printf("%s:%n", params[i].getName());
            System.out.print("-> ");
            args[i] = objectOfType(sc.nextLine(), params[i].getType().getSimpleName());
        }
        return best.newInstance(args);
    }

    static private void updateField(Object obj, Scanner sc) throws IllegalAccessException, NoSuchFieldException {
        System.out.println("Enter name of the field for changing:");
        System.out.print("-> ");
        String fieldname = sc.nextLine();
        Field field = obj.getClass().getDeclaredField(fieldname);
        String fieldTypeName = field.getType().getSimpleName();
        System.out.printf("Enter %s value:%n", fieldTypeName);
        System.out.print("-> ");
        String newValueInput = sc.nextLine();
        Object newValueObj = objectOfType(newValueInput, fieldTypeName);
        field.setAccessible(true);
        field.set(obj, newValueObj);
    }

    static private Optional<Object> callMethod(Object obj, Scanner sc) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("Enter name of the method for call:");
        String methodName = sc.nextLine();
        Method[] methods = obj.getClass().getDeclaredMethods();
        String[] inputParms = validateMethodInput(methodName);
        Method  method = null;
        for (Method m: methods) {
            if (methodName.startsWith(m.getName())) {
                Class<?>[] methodParams = m.getParameterTypes();
                if (methodParams.length != inputParms.length) {
                    System.out.println("test1");
                    continue;
                }

                boolean match = true;
                for (int i = 0; i < methodParams.length; i++) {
                    if (!methodParams[i].getSimpleName().equals(inputParms[i])) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    method = m;
                    break;
                }
            }
        }
        if (method == null) {
            throw new NoSuchMethodException("method not found");
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            String parameterTypeName = parameterTypes[i].getSimpleName();
            System.out.printf("Enter %s value:%n", parameterTypeName);
            System.out.print("-> ");
            String value = sc.nextLine();
            args[i] = objectOfType(value, parameterTypeName);
        }

        return Optional.ofNullable(method.invoke(obj, args));
    }

    static private String[] validateMethodInput(String methodSignature) {
        methodSignature.trim();
        int openParan = methodSignature.indexOf('(');
        int closeParan = methodSignature.lastIndexOf(')');

        if (openParan == -1 || closeParan == -1 || openParan > closeParan) {
            throw new IllegalArgumentException("Invalid method signature");
        }

        String paramsPart = methodSignature.substring(openParan + 1, closeParan).trim();
        String[] paramTypeNames;

        if (paramsPart.isEmpty()) {
            paramTypeNames = new String[0];
        } else {
            paramTypeNames = paramsPart.split("\\s*,\\s*");
        }
        return paramTypeNames;
    }

    static private Object objectOfType(String input, String typeName) {
        return switch(typeName) {
            case "Integer" -> Integer.valueOf(input);
            case "int" -> Integer.parseInt(input);
            case "Double" -> Double.valueOf(input);
            case "double" -> Double.parseDouble(input);
            case "Boolean" -> Boolean.valueOf(input);
            case "boolean" -> Boolean.parseBoolean(input);
            case "Long" -> Long.valueOf(input);
            case "long" -> Long.parseLong(input);
            case "String" -> input;
            default -> throw new IllegalArgumentException("Unsupported type");
        };
    }
}
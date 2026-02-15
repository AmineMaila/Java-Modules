package fr.forty_two.reflection.app;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.forty_two.reflection.exceptions.PackageNotFoundException;

public class Program {
    private static final String CLASSES_PACKAGE_PATH = "fr/forty_two/reflection/classes";
    private static final String CLASS_SUFFIX = ".class";
    private static final List<Class<?>> classes = new ArrayList<>();

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            scanForClasses();
            printClasses();
            System.out.println("Enter class name:");
            String line  = sc.nextLine();
            printClass(line);

        } catch (Throwable e) {
            System.err.println(e.getMessage());
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
        System.out.println("---------------------");
    }

    static private void printClass(String className) {
        for (var clazz : classes) {
            if (clazz.getSimpleName().equals(className)) {
                System.out.println("fields:");
                
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    System.out.printf("%-4s%n", field.getName());
                }

                System.out.println("methods:");

                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    System.out.printf("%-4s%n", method.getName());
                }
                return;
            }
        }
        throw new IllegalArgumentException("");

    }
}
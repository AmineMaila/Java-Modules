package fr.forty_two.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.forty_two.spring.printer.Printer;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        Printer printerPrefix = context.getBean("printerWithPrefix", Printer.class);
        Printer printerDate = context.getBean("printerWithDateTime", Printer.class);
        printerPrefix.print("Hello!");
        printerDate.print("Goodbye!");
    }
}

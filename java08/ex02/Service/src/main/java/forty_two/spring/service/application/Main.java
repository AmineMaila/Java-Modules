package forty_two.spring.service.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import forty_two.spring.service.repositories.UsersRepository;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        UsersRepository usersRepository = context.getBean("usersRepositoryJdbc", UsersRepository.class);
        // usersRepository.save(new User(null, "test3@mail.com"));
        // System.out.println(usersRepository.findById(4L));
        usersRepository.delete(null);
        usersRepository = context.getBean("usersRepositoryJdbcTemplate", UsersRepository.class);
        // usersRepository.save(new User(null, "test6@email.com"));
        usersRepository.delete(null);
        System.out.println(usersRepository.findAll());
    }
}


public class Program {
    public static void main(String[] args) {
        UsersArrayList users = new UsersArrayList();

        users.add(new User("Mohamed", 0));
        users.add(new User("Amine", 15.0));
        users.add(new User("Maila", 30.5));

        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }

        try {
            System.out.println(users.findById(15));
        } catch (UserNotFoundException e) {
            System.out.println("User Not Found");
        }
        System.out.println(users.size());
        System.out.println(users.findById(2));
    }
}

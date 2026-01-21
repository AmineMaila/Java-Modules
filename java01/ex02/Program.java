
public class Program {
    public static void main(String[] args) {
        UsersArrayList users = new UsersArrayList();

        users.add(new User("Mohamed", 0));
        users.add(new User("Amine", 15.0));
        users.add(new User("Maila", 30.5));

        System.out.println(users.get(0));

        System.out.println(users.get(1));
        System.out.println(users.get(2));
        System.out.println(users.get(3));
        System.out.println(users.size());
        System.out.println(users.findById(2));

    }
}

import java.util.Scanner;

public class Program {
    private static final int INPUT_LIMIT = 10;
    private static final int DAYS_IN_MONTH = 30;
    private static final int DAYS_IN_WEEK = 7;
    private static final int HOURS_IN_DAY = 6;
    private static final String[] WEEK = new String[]{"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
    private static final String[] students = new String[INPUT_LIMIT];
    private static int studentSize = 0;
    private static final boolean[][] schedule = new boolean[DAYS_IN_WEEK][HOURS_IN_DAY];

    // the first array represent the students and the second is month days and third is hour of attendence
    private static final int[][][] attendence = new int[INPUT_LIMIT][DAYS_IN_MONTH][HOURS_IN_DAY];

    public static boolean isClassOnDay(boolean[][] schedule, int dayOfMonth) {
        for (int hour = 0; hour < schedule[dayOfMonth % WEEK.length].length; hour++) {
            if (schedule[dayOfMonth % WEEK.length][hour]) {
                return true;
            }
        }
        return false;
    }

    public static int parseDay(char[] day) {
        for (int i = 0; i < WEEK.length; i++) {
            char[] weekDayChars = WEEK[i].toCharArray();
            if (weekDayChars[0] == day[0] && weekDayChars[1] == weekDayChars[1]) {
                return i;
            }
        }
        return -1;
    }

    public static int matches(char[] s1, char[] s2, int s2start, int s2end) {
        if (s1.length != s2end - s2start) {
            return -1;
        }
        for (int i = 0; i < s1.length; i++) {
            if (s1[i] != s2[s2start + i]) {
                return -1;
            }
        }
        return 1;
    }

    public static void printHeader() {
        System.out.printf("%10s", "");
        for (int dayOfMonth = 1; dayOfMonth <= DAYS_IN_MONTH; dayOfMonth++) {
            for (int hour = 0; hour < HOURS_IN_DAY; hour++) {
                int dayOfWeek = dayOfMonth % WEEK.length;
                if (schedule[dayOfWeek][hour]) {
                    String date = hour + ":00 " + WEEK[dayOfMonth % WEEK.length] + " " + dayOfMonth;
                    System.out.printf("%10s|", date);
                }
            }
        }
        System.out.println();
    }

    public static void parseStudent(Scanner reader) {
        while (reader.hasNextLine()) {
            String student = reader.nextLine();
            if (student.equals(".")) {
                return;
            }
            students[studentSize++] = student;
        }
    }
    public static void parseSchedule(Scanner reader) {
        while (reader.hasNextLine()) {
            String clazz = reader.nextLine();
            if (clazz.equals("."))  {
                return;
            }
            char[] chars = clazz.toCharArray();
            int hour = chars[0] - '0';
            int day = parseDay(new char[]{chars[2], chars[3]});

            schedule[day][hour] = true;
        }
    }

    public static void parseAttendence(Scanner reader) {
        while (reader.hasNextLine()) {
            String input = reader.nextLine();
            if (input.equals(".")) {
                return;
            }

            char[] inputCharArr = input.toCharArray();
            int idx = 0;
            while(inputCharArr[idx] != ' ') {
                idx++;
            }

            int studentIndex = -1;
            for (int i = 0; i < studentSize; i++) {
                if (matches(students[i].toCharArray(), inputCharArr, 0, idx) == 1) {
                    studentIndex = i;
                }
            }
            idx++;

            int hour = inputCharArr[idx] - '0';
            idx+=2;
            int monthDay = 0;
            while (inputCharArr[idx] != ' ') {
                monthDay = monthDay * 10 + (inputCharArr[idx] - '0');
                idx++;
            }
            idx++;
            attendence[studentIndex][monthDay - 1][hour] = matches(new char[]{'H', 'E', 'R', 'E'}, inputCharArr, idx, inputCharArr.length);
        }
    }

    public static void printTimeTable() {
        for (int studentIndex = 0; studentIndex < studentSize; studentIndex++) {
            System.out.printf("%10s", students[studentIndex]);
            for (int dayOfMonth = 1; dayOfMonth <= DAYS_IN_MONTH; dayOfMonth++) {
                if (!isClassOnDay(schedule, dayOfMonth)) {
                    continue;
                }
                for (int hour = 0; hour < HOURS_IN_DAY; hour++) {
                    if (!schedule[dayOfMonth % WEEK.length][hour]) {
                        continue;
                    }
                    if (attendence[studentIndex][dayOfMonth - 1][hour] != 0) {
                        System.out.printf("%10d|", attendence[studentIndex][dayOfMonth - 1][hour]);
                    } else {
                        System.out.printf("%10s|", "");
                    }
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        parseStudent(sc);
        parseSchedule(sc);
        parseAttendence(sc);

        printHeader();
        printTimeTable();
        
        sc.close();
    }
}

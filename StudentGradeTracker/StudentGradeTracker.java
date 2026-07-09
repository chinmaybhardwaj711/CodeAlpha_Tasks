import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeTracker {
    private static class Student {
        private final String name;
        private final double score;

        Student(String name, double score) {
            this.name = name;
            this.score = score;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.println("Student Grade Tracker");
        System.out.println("---------------------");

        int totalStudents = readInt(scanner, "How many students do you want to enter? ", 1);

        for (int i = 1; i <= totalStudents; i++) {
            System.out.println();
            String name = readName(scanner, "Enter name for student " + i + ": ");
            double score = readScore(scanner, "Enter score for " + name + " (0-100): ");
            students.add(new Student(name, score));
        }

        printSummary(students);
        scanner.close();
    }

    private static int readInt(Scanner scanner, String prompt, int minValue) {
        while (true) {
            System.out.print(prompt);

            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();

                if (value >= minValue) {
                    return value;
                }
            } else {
                scanner.nextLine();
            }

            System.out.println("Please enter a whole number of at least " + minValue + ".");
        }
    }

    private static String readName(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println("Name cannot be blank.");
        }
    }

    private static double readScore(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);

            if (scanner.hasNextDouble()) {
                double score = scanner.nextDouble();
                scanner.nextLine();

                if (score >= 0 && score <= 100) {
                    return score;
                }
            } else {
                scanner.nextLine();
            }

            System.out.println("Please enter a score between 0 and 100.");
        }
    }

    private static void printSummary(ArrayList<Student> students) {
        double total = 0;
        Student highest = students.get(0);
        Student lowest = students.get(0);

        for (Student student : students) {
            total += student.score;

            if (student.score > highest.score) {
                highest = student;
            }

            if (student.score < lowest.score) {
                lowest = student;
            }
        }

        double average = total / students.size();

        System.out.println();
        System.out.println("Summary Report");
        System.out.println("--------------");
        System.out.printf("%-5s %-25s %10s%n", "No.", "Student Name", "Score");
        System.out.println("------------------------------------------");

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.printf("%-5d %-25s %10.2f%n", i + 1, student.name, student.score);
        }

        System.out.println("------------------------------------------");
        System.out.printf("Average Score: %.2f%n", average);
        System.out.printf("Highest Score: %.2f (%s)%n", highest.score, highest.name);
        System.out.printf("Lowest Score:  %.2f (%s)%n", lowest.score, lowest.name);
    }
}

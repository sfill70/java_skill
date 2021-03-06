import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.matches;

public class ToDoList {
    //  (?i) делает выражение или часть не чуствительным к регистру (?-i) снова включает чувствительность к регистру
    public static final String REG_CHECK_DATA_ADD = "(?i)add\\s+.+";
    public static final String REG_CHECK_DATA_FULL = "(?i)(add)(\\s+)(\\d+)(\\s+)(.+)";
    public static final String REG_CHECK_DATA_EDIT = "(?i)edit\\s+\\d+\\s+.+";
    public static final String REG_CHECK_DATA_DEL = "(?i)delete\\s+\\d+";

    public static final Pattern dataAdd = Pattern.compile(REG_CHECK_DATA_ADD, Pattern.CASE_INSENSITIVE);
    public static final Pattern dataAddFull = Pattern.compile(REG_CHECK_DATA_FULL, Pattern.CASE_INSENSITIVE);
    public static final Pattern dataEdit = Pattern.compile(REG_CHECK_DATA_EDIT, Pattern.CASE_INSENSITIVE);
    public static final Pattern dataDelete = Pattern.compile(REG_CHECK_DATA_DEL, Pattern.CASE_INSENSITIVE);


    static ArrayList<String> toDoList = new ArrayList<>();

    public static void main(String[] args) {
        runToDoList();

    }

    static void viewToDoList() {
        if (toDoList.size() == 0) {
            System.out.println("Список дел пуст");
            return;
        }
        for (int i = 0; i < toDoList.size(); i++) {
            System.out.print("Дело N" + (i + 1) + " - ");
            System.out.println(toDoList.get(i));
        }
    }

    static void addToDoList(String[] data) {
        if (data.length == 2) {
            toDoList.add(data[1]);
            System.out.println("Добавлена запись - " + toDoList.size());
        } else if (data.length == 3) {
            int index = Integer.parseInt(data[1]);
            if (index > 0 && index - 1 <= toDoList.size()) {
                toDoList.add(index - 1, data[2]);
                System.out.println("Добавлена запись - " + (index));
            } else {
                System.out.println("Заданный номер выходит за границы списка, допустимый номер от 1 до " + (toDoList.size() + 1));
            }
        }
    }

    static void editToDoList(String[] data) {
        int index = Integer.parseInt(data[1]);
        if (index > 0 && index <= toDoList.size()) {
            toDoList.set(index - 1, data[2]);
            System.out.println("Обновлена запись - " + (index));
        } else {
            System.out.println("Заданный номер выходит за границы списка, допустимый номер от 1 до " + (toDoList.size()));
        }
    }

    static void deleteToDoList(String[] data) {
        int index = Integer.parseInt(data[1]);
        if (index > 0 && index <= toDoList.size()) {
            toDoList.remove(index - 1);
            System.out.println("Удалена запись " + (index));
        } else {
            System.out.println("Заданный номер выходит за границы списка, допустимый номер от 1 до " + (toDoList.size()));
        }
    }

    static String[] splitData(String data, int num) {
        return data.trim().replaceAll("\\s+", " ").split("\\s", num);
    }

    static void processingCommands(String data) {
        data = data.trim().replaceAll("\\s+", " ");
        String command = data;
        if (data.contains(" ")) {
            command = data.substring(0, data.indexOf(" "));
        }
        if (command.equalsIgnoreCase("exit")) {
            return;
        } else if (command.equalsIgnoreCase("list")) {
            viewToDoList();
        } else if (data.matches(REG_CHECK_DATA_ADD)) {
            if (data.matches(REG_CHECK_DATA_FULL)) {
                addToDoList(splitData(data, 3));
            } else {
                addToDoList(splitData(data, 2));
            }
        } else if (data.matches(REG_CHECK_DATA_EDIT)) {
            editToDoList(splitData(data, 3));
        } else if (dataDelete.matcher(data).matches()) {
            deleteToDoList(splitData(data, 2));
        }
        else {
            System.out.println("Неверная команда");
        }
    }

    static void runToDoList() {
        String command;
        try (Scanner scanner = new Scanner(System.in)) {
            do {
                System.out.println("Введите команду " +
                        "Для выхода введите - exit");
                command = scanner.nextLine();
                processingCommands(command);
            } while (!command.equalsIgnoreCase("exit"));
        }
    }
}

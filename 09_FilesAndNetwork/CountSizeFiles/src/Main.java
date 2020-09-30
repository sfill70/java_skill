import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {


    public static void main(String[] args) {

        try {
            runCountSizeFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void runCountSizeFiles() throws IOException {
        String path;
        CountSizeFiles countSizeFilesRecursion = new CountSizeFilesRecursion();
        CountSizeFiles countSizeFilesQueue = new CountSizeFilesQueue();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                try {
                    System.out.println("Введите путь к директории " +
                            "Для выхода введите - exit");
                    path = reader.readLine();
                    if (path.equalsIgnoreCase("exit")) {
                        break;
                    }
                    printDataDir(countSizeFilesRecursion, path);
                    printDataDir(countSizeFilesQueue, path);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void printDataDir(CountSizeFiles countSizeFiles, String path) throws NullPointerException {
        System.out.println("Метод - " + countSizeFiles.getClass().getCanonicalName());
        countSizeFiles.printFiles(path);
    }
}

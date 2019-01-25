import java.io.*;
import java.util.*;

public class formatPokemonText {
    public formatPokemonText(String source, String destination) throws IOException {
        File file1 = new File(source);
        Scanner scanner = new Scanner(file1);
        File file2 = new File(destination);
        FileWriter fw = new FileWriter(file2);
        PrintWriter pw = new PrintWriter(fw);
        while (scanner.hasNextLine()) {
            String[] nameAndTypes = scanner.nextLine().split("(?=[A-Z])");
            for (int i = 0; i < nameAndTypes.length; i++) {
                nameAndTypes[i] = nameAndTypes[i].trim();
            }
            System.out.println(nameAndTypes.length);
            pw.printf("%s - %s", nameAndTypes[0], nameAndTypes[1]);
            if (nameAndTypes.length == 3) {
                pw.printf("/%s\n", nameAndTypes[2]);
            } else {
                pw.println();
            }
        }
        pw.close();
    }
}

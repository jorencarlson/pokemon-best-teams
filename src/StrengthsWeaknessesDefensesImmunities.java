import java.util.HashSet;
import java.io.*;
import java.util.*;


/*
   This class creates four HashMaps when it is instantiated: strengths, weaknesses, defenses, and immunities.
   Each HashMap stores every type and its corresponding attributes (strengths, weaknesses, defenses, or immunities).
*/

public class StrengthsWeaknessesDefensesImmunities {

    public HashMap<String, HashSet<String>> strengths;
    public HashMap<String, HashSet<String>> weaknesses;
    public HashMap<String, HashSet<String>> defenses;

    // Immunities are currently considered to be defenses.
    public HashMap<String, HashSet<String>> immunities;

    public StrengthsWeaknessesDefensesImmunities() throws FileNotFoundException {
        String fileName;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                fileName = "strengths.txt";
            }
            else if (i == 1) {
                fileName = "weaknesses.txt";
            }
            else if (i == 2) {
                fileName = "defenses.txt";
            }
            else {
                fileName = "immunities.txt";
            }
            HashMap<String, HashSet<String>> typesAttributes = new HashMap<>();
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String type = scanner.nextLine();
                String attribute = scanner.nextLine();
                HashSet<String> attributeSet = new HashSet<>();
                while (!(attribute.equals(""))) {
                    attributeSet.add(attribute);
                    attribute = (scanner.hasNextLine()) ? scanner.nextLine() : "";
                }
                typesAttributes.put(type, attributeSet);
            }
            if (fileName.equals("strengths.txt")) {
                strengths = typesAttributes;
            }
            else if (fileName.equals("weaknesses.txt")) {
                weaknesses = typesAttributes;
            }
            else if (fileName.equals("defenses.txt")) {
                defenses = typesAttributes;
            }
            else {
                immunities = typesAttributes;
            }
        }
    }
}

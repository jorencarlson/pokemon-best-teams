import java.util.*;
import java.io.*;

public class Game {

    // The best teams
    private LinkedList<String> teams = new LinkedList<String>();

    // The current team
    private String[] team = new String[6];

    // The highest team score found
    private double highestScore = Integer.MIN_VALUE;

    // Contains the number of occurrences of each type
    private HashMap<String, Integer> typeStats = new HashMap<String, Integer>();

    // All of the types present in the game
    private ArrayList<String> types = new ArrayList<>();

    // All the double types present in this game
    private HashSet<String> doubleTypes = new HashSet<String>();

    // The score for all types present in the game
    private HashMap<String, Double> scores = new HashMap<String, Double>();

    // Used to determine whether a team has STABs for all types
    private HashMap<String, Boolean> allTypesSuperEffectiveSTABs = new HashMap<String, Boolean>();

    // Used to determine if team has a type that can learn fly
    private boolean flyPass = false;

    // Name of the Pokemon game (eg. Gen1, Gen2, etc.)
    private String game;

    // An object that has the strengths, weaknesses, defenses, and immunities for every type
    private StrengthsWeaknessesDefensesImmunities swd;

    // All the types that can learn fly
    public HashSet<String> flyPokemon = new HashSet<>(Arrays.asList("Normal/Fly", "Psychic/Fly",
            "Rock/Fly", "Water/Fly", "Dark/Fly", "Dark/Dragon", "Bug/Fire", "Ground/Ghost", "Ghost/Fly", "Dragon/Fly",
            "Grass/Fly", "Ground/Dragon", "Steel/Fly", "Ice/Fly", "Poison/Fly"));

    public Game(String game) throws FileNotFoundException {
        this.game = game;
        swd = new StrengthsWeaknessesDefensesImmunities();
        countAllTypes();

        // addDoubleTypeStrengths() must come before addDoubleTypeWeaknesses()
        addDoubleTypeStrengths();
        addDoubleTypeDefenses();
        addDoubleTypeWeaknesses();

        for (String type : typeStats.keySet()) {
            scores.put(type, score(type));
        }
        for (String type : typeStats.keySet()) {
            if (!type.equals("Ghost/Dark")) // Ghost/Dark type excluded because it has no weaknesses
            allTypesSuperEffectiveSTABs.put(type, false);
        }

        int[] chosen = new int[7];
        findTeams(chosen, 0, 6, 0, types.size() - 1);
    }


    public double getScore(String type) {
        return scores.get(type);
    }

    public LinkedList<String> getTeams() {
        return teams;
    }

    /*
       Goes through the list of Pokemon in the given text file and counts the number of occurrences for each type.
       If a double type is found and not yet present in "swd", a Pokemon object is created to determine the
       strengths, weaknesses, defenses, and immunities for the type. Those attributes are then added to "swd".
    */

    private void countAllTypes() throws FileNotFoundException {
        String pathname = "Pokemon_" + game + ".txt";
        File myFile = new File(pathname);
        Scanner scanner = new Scanner(myFile);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            String[] nameAndTypes = nextLine.split("-");
            String name = nameAndTypes[0].trim();
            nameAndTypes[1] = nameAndTypes[1].trim();
            String[] types = nameAndTypes[1].split("/");
            if (types.length == 1) {
                Integer typeCount = typeStats.get(types[0]);
                if (typeCount == null) {
                    typeStats.put(types[0], 1);
                    this.types.add(types[0]);
                }
                else {
                    typeStats.put(types[0], typeCount + 1);
                }
            }
            else {
                String doubleType = types[0] + "/" + types[1];
                Integer typeCount = typeStats.get(doubleType);
                if (typeCount == null) {
                    this.types.add(doubleType);
                    Pokemon pokemon = new Pokemon(name, types[0], types[1]);
                    typeStats.put(doubleType, 1);
                    doubleTypes.add(doubleType);
                    swd.weaknesses.put(doubleType, pokemon.weaknesses);
                    swd.defenses.put(doubleType, pokemon.defenses);
                    swd.strengths.put(doubleType, pokemon.strengths);
                    swd.immunities.put(doubleType, pokemon.immunities);
                }
                else {
                    typeStats.put(doubleType, typeCount + 1);
                }
            }
        }
    }

    /*
       For every type or double type in the given game all the double types are iterated through. If a double type's
       strength is the given type, then the double type is added to the weaknesses of the given type in "swd".
    */

    private void addDoubleTypeWeaknesses() {
        for (String oneOrTwoTypes : typeStats.keySet()) {
            for (String doubleType : doubleTypes) {
                if (swd.strengths.get(doubleType).contains(oneOrTwoTypes)) {
                    HashSet<String> newWeaknesses = swd.weaknesses.get(oneOrTwoTypes);
                    newWeaknesses.add(doubleType);
                    swd.weaknesses.put(oneOrTwoTypes, newWeaknesses);
                }
            }
        }
    }

    /*
       For every type or double type in the given game all the double types are iterated through. If the type is found
       to be a weakness of a given double type then that double type is added to the type's strengths in "swd". If
       strengths are being added to a double type, this process happens for each type of the double type.
     */

    private void addDoubleTypeStrengths() {
        for (String oneOrTwoTypes : typeStats.keySet()) {
            String[] types = oneOrTwoTypes.split("/");
            for (String type : types) {
                for (String doubleType : doubleTypes) {
                    HashSet<String> doubleTypeWeaknesses = swd.weaknesses.get(doubleType);
                    for (String weakness : doubleTypeWeaknesses) {
                        if (type.equals(weakness)) {
                            HashSet<String> newStrengths = swd.strengths.get(oneOrTwoTypes);
                            newStrengths.add(doubleType);
                            swd.strengths.put(oneOrTwoTypes, newStrengths);
                        }
                    }
                }
            }
        }
    }

     /*
       For every type or double type in the given game all the double types are iterated through. If both of the types
       in a double type are found in the given type or double type's defenses then the double type is added to the
       type or double type's defenses in "swd".
     */

    private void addDoubleTypeDefenses() {
        for (String oneOrTwoTypes : typeStats.keySet()) {
            for (String doubleType : doubleTypes) {
                String[] currentDoubleType = doubleType.split("/");
                Boolean pass = true;
                for (String type : currentDoubleType) {
                    if (!(swd.defenses.get(oneOrTwoTypes).contains(type))) {
                        pass = false;
                        break;
                    }
                }
                if (pass) {
                    HashSet<String> newDefenses = swd.defenses.get(oneOrTwoTypes);
                    newDefenses.add(doubleType);
                    swd.defenses.put(oneOrTwoTypes, newDefenses);
                }
            }
        }
    }

    /* Scores a team based on the total quantity of strength and defense attributes divided by the total quantity of
       weakness attributes
     */

    private double score(String type) {
        double strScore = 0;
        for (String strength : swd.strengths.get(type)) {
            Integer typeCount = typeStats.get(strength);
            if (typeCount != null) {
                strScore += (double)typeCount;
            }
        }
        double weakScore = 1;
        for (String weakness : swd.weaknesses.get(type)) {
            Integer typeCount = typeStats.get(weakness);
            if (typeCount != null) {
                weakScore += (double)typeCount;
            }
        }
        double defScore = 0;
        for (String defense : swd.defenses.get(type)) {
            Integer typeCount = typeStats.get(defense);
            if (typeCount != null) {
                defScore += (double)typeCount;
            }
        }
        return (strScore + defScore) / weakScore;
    }

    // Mark the span of STABs for team

    private void gatherTeamInfo() {
        for (int i = 0; i < team.length; i++) {
            for (String strength: swd.strengths.get(team[i])) {
                this.allTypesSuperEffectiveSTABs.put(strength, true);
            }
        }
    }

    private boolean firstAnalysis() {
        for (String type : allTypesSuperEffectiveSTABs.keySet()) {    // if STAB do not span every type, stop here
            if (!allTypesSuperEffectiveSTABs.get(type)) {
                return false;
            }
        }
        return true;
    }

    // True if the team contains a type that can learn fly

    private boolean flyAnalysis() {
        for (String type : team) {
            if (flyPokemon.contains(type)) {
                return true;
            }
        }
        return false;
    }

    // True if the team has the highest score found yet

    private boolean secondAnalysis() {
        double teamScore = 0;
        for (String type : team) {
            teamScore += scores.get(type);
        }
        if (teamScore >= highestScore) {
            highestScore = teamScore;
            return true;
        }
        else {
            return false;
        }
    }

    // Clear the STABs span of the last team

    public void clear() {
        for (String type : this.allTypesSuperEffectiveSTABs.keySet()) {
            this.allTypesSuperEffectiveSTABs.put(type, false);
        }
    }

    // Created using combination with repetition code

    public void findTeams(int chosen[], int index, int r,
                          int start, int end) {
        if (index == r) {
            for (int i = 0; i < r; i++) {
                team[i] = types.get(chosen[i]);
            }
            clear();
            gatherTeamInfo();
            // replace "true" with "flyPass" if pokemon with fly is necessary
            if (flyAnalysis()) {
                if (firstAnalysis()) {
                    if (secondAnalysis()) {
                        for (int i = 0; i < team.length; i++) {
                            teams.add(team[i]);
                        }
                    }
                }
            }
            return;
        }
        for (int i = start; i <= end; i++) {
            chosen[index] = i;
            findTeams(chosen, index + 1, r, i, end);
        }
    }
}

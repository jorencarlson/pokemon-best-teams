import java.io.FileNotFoundException;
import java.util.*;

// currently used to determine the attributes of double types

public class Pokemon {
        public String[] types;
        public HashSet<String> strengths;
        public HashSet<String> weaknesses;
        public HashSet<String> defenses;
        public HashSet<String> immunities;
        public String name;
        private StrengthsWeaknessesDefensesImmunities swd;

        public Pokemon(String name, String type1, String type2) throws FileNotFoundException {
            swd = new StrengthsWeaknessesDefensesImmunities();
            types = new String[2];
            types[0] = type1;
            types[1] = type2;
            this.name = name;
            findStrengths();
            findWeaknesses();
            findDefenses();
            findImmunities();
            for (Iterator<String> iterator = weaknesses.iterator(); iterator.hasNext();) {
                String weakness = iterator.next();
                if (defenses.contains(weakness)) {
                    defenses.remove(weakness);
                    iterator.remove();
                }
            }
        }

        public Pokemon(String name, String type1) throws FileNotFoundException {
            swd = new StrengthsWeaknessesDefensesImmunities();
            types = new String[1];
            types[0] = type1;
            this.name = name;
            findStrengths();
            findWeaknesses();
            findDefenses();
            //canLearnFly();
        }

        private void findImmunities() {
            if (types.length == 1) {
                this.immunities = swd.immunities.get(types[0]);
            }
            else {
                HashSet<String> immunities = new HashSet<>();
                for (String type : types) {
                    if (swd.immunities.containsKey(type)) {
                        for (String immunity : swd.immunities.get(type)) {
                            immunities.add(immunity);
                        }
                    }
                }
                this.immunities = immunities;
            }
        }

        private void findDefenses() {
            if (types.length == 1) {
                this.defenses = swd.defenses.get(types[0]);
            }
            else {
                HashSet<String> defenses = new HashSet<>();
                for (String type : types) {
                    for (String defense : swd.defenses.get(type)) {
                        defenses.add(defense);
                    }
                }
                this.defenses = defenses;
            }
        }

        private void findWeaknesses() {
            if (types.length == 1) {
                this.weaknesses = swd.weaknesses.get(types[0]);
            }
            else {
                HashSet<String> weaknesses = new HashSet<>();
                for (String type : types) {
                    for (String weakness : swd.weaknesses.get(type)) {
                        weaknesses.add(weakness);
                    }
                }
                this.weaknesses = weaknesses;
            }
        }

        private void findStrengths() {
            if (types.length == 1) {
                this.strengths = swd.strengths.get(types[0]);
            }
            else {
                HashSet<String> strengths = new HashSet<>();
                for (String type : types) {
                    for (String strength : swd.strengths.get(type)) {
                        strengths.add(strength);
                    }
                }
                this.strengths = strengths;
            }
        }
    }

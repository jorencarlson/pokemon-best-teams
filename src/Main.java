import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;


public class Main {

    public static void main(String[] args) throws IOException {

        Game game = new Game("Gen3"); // select the pokemon generation here

        LinkedList<String> teams = game.getTeams();
        Iterator<String> iterator = teams.iterator();
        for (int i = 0; i < (teams.size()/6); i++) {
            System.out.printf("Team %d: ", i+1);
            double totalScore = 0;
            for (int j = 0; j < 6 && iterator.hasNext(); j++) {
                String current = iterator.next();
                System.out.printf("%s ", current);
                totalScore += game.getScore(current);

            }
            System.out.printf("\nTotal score: %f\n\n", totalScore);
        }
    }
}

# pokemon-best-teams

To choose the game for which best teams will be selected: 

1. Open Main.java
2. Edit the value passed to the instantiation of "Game" to select one of the text files with Pokémon (eg. "Gen1", "Gen2", etc.)

Installation: javac -d .. Main.java

Execution: java Main



Notes on program:

To reduce program runtime, Pokémon are represented only by their type(s) (eg. Grass, Water/Grass). Thus, the set of type(s) is examined, as opposed to the set of Pokémon. Repeated combinations of the set of type(s) are examined, where the total number of items is the number of unique types in the game and the number of chosen items is six, which represents a full team.

For any chosen team, it is determined whether it should be added to the set of best teams by two characteristics:

1. The team must have STABs that are super effective for all of the types present in the text file of Pokémon.

2. The team score is the highest thus far.

All type(s) are assigned a score. This score is calculated using a data structure that has every type in the text file of Pokémon as its keys and the number of Pokémon for each type(s) as its values. 
The score is determined by three subscores: strength, weakness, and defense. The strength score is determined by the number of Pokémon that the type(s) have super effective STABs for. The weakness score is determined by the number of Pokémon that have super effective STABs for the type(s). The defense score is determined by the number of Pokémon that have ineffective STABs for the type(s). The final score is calculated by the sum of the strength score and the defense score divided by the weakness score.

Future updates: accounting for the Levitate ability



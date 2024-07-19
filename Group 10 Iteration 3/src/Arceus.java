import java.util.*;

// This is a test class for testing Game functionality without needing to implement GUI elements first.
// It runs the game using user input from commandline


public class Arceus {
    public static void main(String[] args){
        Kingdomino test = new Kingdomino();
        Scanner input = new Scanner(System.in);

        //Create Game
        System.out.println("Enter Number of Players");
        int NumberOfPlayers = input.nextInt();
        //input.close();
        //test.StartGame(new Game(test, NumberOfPlayers));
        Game testGame = new Game(test, NumberOfPlayers);

        
        

    }
}

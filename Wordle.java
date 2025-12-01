import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        try {
        return Files.readAllLines(Paths.get(filename)).toArray(new String[0]);
    } catch (IOException e) {
        return new String[0];
    }	
    }

    // Choose a random secret word from the dictionary. 
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {

        String randomWord = dict[(int)(Math.random() * dict.length)];

        return randomWord;
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) { 
    for (int i = 0; i < secret.length(); i++) {
        if (secret.charAt(i) == c) {
            return true; // Found the character
        }
    }
    return false; // Character not found in the secret word
}
    

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
    for (int i = 0; i < secret.length(); i++) {
        // 1. Check Green (Exact Match)
        if (guess.charAt(i) == secret.charAt(i)) {
            resultRow[i] = 'G';
        }
        // 2. Check Yellow (Exists, but not here)
        else if (containsChar(secret, guess.charAt(i))) { // <-- Now checks for TRUE/FALSE
            resultRow[i] = 'Y';
        }
        // 3. Check Gray (Doesn't exist)
        else {
            resultRow[i] = '_';
        }
    }
}

    // Store guess string (chars) into the given row of guesses 2D array.
    // For example, of guess is HELLO, and row is 2, then after this function 
    // guesses should look like:
    // guesses[2][0] // 'H'
	// guesses[2][1] // 'E'
	// guesses[2][2] // 'L'
	// guesses[2][3] // 'L'
	// guesses[2][4] // 'O'
    public static void storeGuess(String guess, char[][] guesses, int row) {
		for (int i = 0; i < guess.length(); i++) {
                   guesses[row][i]= guess.charAt(i);
    }
}

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
		 int counter = 0;
        for (int col = 0; col < resultRow.length; col++) {
                if(resultRow[col]!='G'){
                    return false;
                }
            }
            return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);
        System.out.println(secret);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[6][WORD_LENGTH];
        char[][] results = new char[6][WORD_LENGTH];
        // Prepare to read from the standart input 
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readString();
                
                if (guess.length()!=5) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }
            guess = guess.toUpperCase();
            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
            if (!won) {
            System.out.println("You ran out of attempts!");
            System.out.println("The secret word was: " + secret);
        }
        }

        inp.close();
    }
}

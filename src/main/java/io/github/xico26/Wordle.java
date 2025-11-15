package io.github.xico26;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Wordle {
    private List<String> wordList;

    private String word;

    public Wordle() {
        wordList = new ArrayList<String>();
        loadWords();
    }

    public void loadWords () {
        try {
            this.wordList = Files.readAllLines(Paths.get("src/main/java/io/github/xico26/word_list.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
    }

    public String selectWord() {
        int random =  (int) (Math.random() * this.wordList.size());

        return this.wordList.get(random);
    }

    public void printWords () {
        System.out.println("Words:");
        for (String word : this.wordList) {
            System.out.println(word);
        }
    }

    public void startGame () {
        this.word = selectWord();

        System.out.println("[DEBUG] THE WORD IS: " + this.word);

        boolean hasGuessed = false;
        int numGuesses = 0;
        Scanner scanner = new Scanner(System.in);

        List<Character> rightPlace = new ArrayList<>();
        List<Character> wrongPlace = new ArrayList<>();

        while (!hasGuessed && numGuesses < 5) {
            System.out.print("Guess #" + (numGuesses + 1) + ": ");
            String input = scanner.nextLine();

            if (!this.wordList.contains(input)) {
                System.out.println("Invalid word, try again!");
                continue;
            }

            if (input.equals(this.word)) {
                hasGuessed = true;
                for (int i = 0; i < input.length(); i++) {
                    rightPlace.add(input.charAt(i));
                }
                printResult(input, rightPlace, wrongPlace);
                System.out.println("Congrats!! You guessed the word in " + (numGuesses + 1) + " tries.");
            } else {
                for (int i = 0; i < 5; i++) {
                    if (this.word.charAt(i) == input.charAt(i)) {
                        rightPlace.add(input.charAt(i));
                    } else if (hasChar(input.charAt(i)) && !rightPlace.contains(input.charAt(i))) {
                        wrongPlace.add(input.charAt(i));
                    }
                }

                numGuesses++;
                printResult(input, rightPlace, wrongPlace);
                rightPlace.clear();
                wrongPlace.clear();
            }
        }

        System.out.println("The word was " + this.word);
    }

    public boolean hasChar (char ch) {
        for (int i = 0; i < this.word.length(); i++) {
            if (this.word.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    public void printResult (String input, List<Character> rightPlace, List<Character> wrongPlace) {
        StringBuilder sb = new StringBuilder();
        sb.append("| ");
        for (int i = 0; i < input.length(); i++) {
            if (rightPlace.contains(input.charAt(i))) {
                sb.append("\033[0;32m").append(input.charAt(i));
            } else if (wrongPlace.contains(input.charAt(i))) {
                sb.append("\033[1;33m").append(input.charAt(i));
            } else {
                sb.append("\033[0m").append(input.charAt(i));
            }
            sb.append("\033[0m | ");
        }

        sb.append("\033[0m\n");

        System.out.print(sb);
    }
}

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Hangman {
    private static final Output NO_GAME = Output.empty();

    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        var wordEvents = words.<Event>map(NewWord::new);

        var letterEvents = letters.<Event>map(Guess::new).concatWith(Observable.just(new LettersFinished()));

        return Observable
            .merge(wordEvents, letterEvents)
            .takeUntil(event -> event instanceof LettersFinished)
            .scan(NO_GAME, Hangman::handle)
            .skip(1);
    }

    private static Output handle(Output game, Event event) {
        return switch (event) {
            case NewWord newWord -> startGame(newWord.word());
            case Guess guess -> isFinished(game) ? game : guess(game, guess.letter());
            case LettersFinished ignored -> game;
        };
    }

    private static Output startGame(String secret) {
        return new Output(secret, discovered(secret, Set.of()), Set.of(), Set.of(), List.of(), Status.PLAYING);
    }

    private static Output guess(Output game, String letter) {
        if (itWasPreviouslyPlayed(game, letter)) {
            throw new IllegalArgumentException("Letter " + letter + " was already played");
        }

        return isItGoodGuess(game, letter) ? guessedLetter(game, letter) : missedLetter(game, letter);
    }


    private static boolean isItGoodGuess(Output game, String letter) {
        return game.secret.contains(letter);
    }

    private static boolean itWasPreviouslyPlayed(Output game, String letter) {
        return game.guess.contains(letter) || game.misses.contains(letter);
    }

    private static Output missedLetter(Output game, String letter) {
        var misses = new LinkedHashSet<>(game.misses);
        misses.add(letter);
        return new Output(game.secret,
                          game.discovered,
                          game.guess,
                          Collections.unmodifiableSet(misses),
                          parts(misses),
                          misses.size() == PART_VALUES.length ? Status.LOSS : Status.PLAYING);
    }

    private static Output guessedLetter(Output game, String letter) {
        var guess = new LinkedHashSet<>(game.guess);
        guess.add(letter);
        var disc = discovered(game.secret, guess);
        return new Output(game.secret,
                          disc,
                          Collections.unmodifiableSet(guess),
                          game.misses,
                          game.parts,
                          game.secret.equals(disc) ? Status.WIN : Status.PLAYING);
    }

    private static boolean isFinished(Output game) {
        return game == NO_GAME || game.status != Status.PLAYING;
    }

    private static String discovered(String secret, Set<String> guesses) {
        return secret.chars()
            .mapToObj(c -> String.valueOf((char) c))
            .map(letter -> guesses.contains(letter) ? letter : "_")
            .collect(Collectors.joining());
    }

    private static final Part[] PART_VALUES = Part.values();

    private static List<Part> parts(Set<String> misses) {
        return Arrays.stream(PART_VALUES).limit(misses.size()).toList();
    }

    private sealed interface Event permits NewWord, Guess, LettersFinished {}

    private record NewWord(String word) implements Event {}

    private record Guess(String letter) implements Event {}

    private record LettersFinished() implements Event {}
}

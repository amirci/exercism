import io.reactivex.Observable;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;

class Hangman {
    private static final Game NO_GAME = new Game("", Set.of(), Set.of(), Status.PLAYING);

    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        var wordEvents = words.<Event>map(NewWord::new);

        var letterEvents = letters.<Event>map(Guess::new).concatWith(Observable.just(new LettersFinished()));

        return Observable
            .merge(wordEvents, letterEvents)
            .takeUntil(event -> event instanceof LettersFinished)
            .scan(NO_GAME, Hangman::handle)
            .skip(1)
            .filter(game -> game != null)
            .map(Hangman::output);
    }

    private static Game handle(Game game, Event event) {
        return switch (event) {
            case NewWord newWord -> new Game(newWord.word(), Set.of(), Set.of(), Status.PLAYING);
            case Guess guess -> isFinished(game) ? game : guess(game, guess.letter());
            case LettersFinished ignored -> game;
        };
    }

    private static Game guess(Game game, String letter) {
        if (game.guesses().contains(letter) || game.misses().contains(letter)) {
            throw new IllegalArgumentException("Letter " + letter + " was already played");
        }

        var guesses = new LinkedHashSet<>(game.guesses());
        var misses = new LinkedHashSet<>(game.misses());

        if (game.secret().contains(letter)) {
            guesses.add(letter);
        } else {
            misses.add(letter);
        }

        var status = status(game.secret(), guesses, misses);

        return new Game(game.secret(), Collections.unmodifiableSet(guesses), Collections.unmodifiableSet(misses), status);
    }

    private static boolean isFinished(Game game) {
        return game == NO_GAME || game.status() != Status.PLAYING;
    }

    private static Output output(Game game) {
        return new Output(
                game.secret(),
                discovered(game.secret(), game.guesses()),
                game.guesses(),
                game.misses(),
                parts(game),
                game.status()
        );
    }

    private static String discovered(String secret, Set<String> guesses) {
        return secret.chars()
            .mapToObj(c -> String.valueOf((char) c))
            .map(letter -> guesses.contains(letter) ? letter : "_")
            .collect(Collectors.joining());
    }

    private static final Part[] PART_VALUES = Part.values();

    private static List<Part> parts(Game game) {
        return Arrays.stream(PART_VALUES)
            .limit(game.misses().size())
            .toList();
    }

    private static Status status(String secret, Set<String> guesses, Set<String> misses) {
        if (discovered(secret, guesses).equals(secret)) {
            return Status.WIN;
        }

        return misses.size() == PART_VALUES.length ? Status.LOSS : Status.PLAYING;
    }

    private sealed interface Event permits NewWord, Guess, LettersFinished {}

    private record NewWord(String word) implements Event {}

    private record Guess(String letter) implements Event {}

    private record LettersFinished() implements Event {}

    private record State(Game game, Output output) {}

    private record Game(String secret, Set<String> guesses, Set<String> misses, Status status) {}
}

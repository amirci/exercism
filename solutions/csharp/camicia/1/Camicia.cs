using System.Collections;
using Card = string;
using InitialDecksUsed = System.Collections.Generic.HashSet<string>;

public static class Camicia
{
    public enum GameStatus
    {
        Finished,
        Loop
    }

    public record GameResult(GameStatus Status, int Tricks, int Cards);

    public static GameResult SimulateGame(Card[] playerA, Card[] playerB)
    {
        var state = InitialState(playerA, playerB);
        ITurn turn = new RegularTurn(state.PlayerA, state.PlayerB);
        var used = new InitialDecksUsed { DecksAsKey(state) };

        while(!GameEnded(state))
        {
            var previousTrickCount = state.TrickCount;
            (state, turn) = turn.Play(state);

            if (state.TrickCount > previousTrickCount)
            {
                if (LoopDetected(used, state))
                {
                    return new GameResult(GameStatus.Loop, state.TrickCount, state.PlayedCardsCount);
                }

                used.Add(DecksAsKey(state));
            }
        }

        return new GameResult(GameStatus.Finished, state.TrickCount, state.PlayedCardsCount);
    }

    private static GameState InitialState(Card[] playerA, Card[] playerB) =>
        new(
            new Deck(playerA.Select(NormalizeCard)),
            new Deck(playerB.Select(NormalizeCard)),
            []
        );

    private static Card NormalizeCard(Card card) =>
        card is "J" or "Q" or "K" or "A" ? card : "N";

    private record GameState(
        Deck PlayerA,
        Deck PlayerB,
        Pile Pile,
        int TrickCount = 0,
        int PlayedCardsCount = 0
    );

    private class Deck(IEnumerable<Card> cards): IEnumerable<Card>
    {
        private readonly Queue<Card> _cards = new(cards);
        
        public bool IsEmpty => _cards.Count == 0;
        public Card Draw() => _cards.Dequeue();
        public void AddToBottom(Pile pile)
        {
            foreach (var card in pile)
            {
                _cards.Enqueue(card);
            }

            pile.Clear();
        }

        public IEnumerator<Card> GetEnumerator() => _cards.GetEnumerator();
        IEnumerator IEnumerable.GetEnumerator() => GetEnumerator();
    }

    private class Pile: IEnumerable<Card>
    {
        private readonly List<Card> _cards = [];

        public bool IsEmpty => _cards.Count == 0;
        public void Add(Card card) => _cards.Add(card);
        public void Clear() => _cards.Clear();
        public IEnumerator<Card> GetEnumerator() => _cards.GetEnumerator();
        IEnumerator IEnumerable.GetEnumerator() => GetEnumerator();
    }

    private static bool LoopDetected(InitialDecksUsed seen, GameState state) =>
        seen.Contains(DecksAsKey(state));

    private static string DecksAsKey(GameState state) =>
        $"{string.Join(",", state.PlayerA)}|{string.Join(",", state.PlayerB)}";

    private static bool GameEnded(GameState state) =>
        state.Pile.IsEmpty && (state.PlayerA.IsEmpty || state.PlayerB.IsEmpty);

    private interface ITurn
    {
        public (GameState, ITurn) Play(GameState state);
    }

    private record RegularTurn(Deck Current, Deck Other) : ITurn
    {
        public (GameState, ITurn) Play(GameState state)
        {
            if (Current.IsEmpty)
            {
                return CollectTrick(state, Other, Current);
            }

            var card = Current.Draw();
            state.Pile.Add(card);

            return (
                state with { PlayedCardsCount = state.PlayedCardsCount + 1 },
                IsPenaltyCard(card)
                    ? new PenaltyTurn(Other, Current, PenaltyFor(card))
                    : new RegularTurn(Other, Current)
            );
        }
    }

    private record PenaltyTurn(Deck Payer, Deck Opponent, int Penalty) : ITurn
    {
        public (GameState, ITurn) Play(GameState state)
        {
            var totalPlayed = 0;
            Card? card = null;

            while(!Payer.IsEmpty && (card == null || !IsPenaltyCard(card)) && totalPlayed < Penalty)
            {
                card = Payer.Draw();
                state.Pile.Add(card);
                totalPlayed++;
            }

            var nextState = state with { PlayedCardsCount = state.PlayedCardsCount + totalPlayed };

            return card is not null && IsPenaltyCard(card)
                ? (nextState, new PenaltyTurn(Opponent, Payer, PenaltyFor(card)))
                : CollectTrick(nextState, Opponent, Payer);
        }
    }

    private static (GameState, ITurn) CollectTrick(GameState state, Deck collector, Deck opponent)
    {
        collector.AddToBottom(state.Pile);
        return (
            state with { TrickCount = state.TrickCount + 1 },
            new RegularTurn(collector, opponent));
    }

    private static bool IsPenaltyCard(Card card) =>
        card is "J" or "Q" or "K" or "A";

    private static int PenaltyFor(Card card) =>
        card switch
        {
            "J" => 1,
            "Q" => 2,
            "K" => 3,
            "A" => 4,
            _ => 0
        };
}

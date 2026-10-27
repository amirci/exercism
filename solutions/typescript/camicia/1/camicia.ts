type GameResult = {
  status: "finished" | "loop";
  cards: number;
  tricks: number;
};

const PAYMENTS = new Map([
  ["J", 1],
  ["Q", 2],
  ["K", 3],
  ["A", 4],
]);

export function simulateGame(playerA: readonly string[], playerB: readonly string[]): GameResult {
  // Number cards are equivalent; payment values preserve all relevant state.
  const decks = [playerA, playerB].map(deck =>
    deck.map(card => PAYMENTS.get(card) ?? 0)
  );
  const seen = new Set<string>();
  let current = 0;
  let cards = 0;
  let tricks = 0;

  while (decks.every(deck => deck.length > 0)) {
    const state = `${current}:${decks[0].join("")}:${decks[1].join("")}`;
    if (seen.has(state)) return { status: "loop", cards, tricks };
    seen.add(state);

    const pile: number[] = [];
    let paymentDue = 0;

    while (true) {
      const card = decks[current].shift();
      if (card === undefined) {
        current = 1 - current;
        break;
      }

      pile.push(card);
      cards++;

      if (card > 0) {
        paymentDue = card;
        current = 1 - current;
      } else if (paymentDue > 0) {
        paymentDue--;
        if (paymentDue === 0) {
          current = 1 - current;
          break;
        }
      } else {
        current = 1 - current;
      }
    }

    decks[current].push(...pile);
    tricks++;
  }

  return { status: "finished", cards, tricks };
}

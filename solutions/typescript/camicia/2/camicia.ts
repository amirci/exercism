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

type Game = {
  decks: number[][];
  current: number;
  cards: number;
  tricks: number;
};

type Trick = {
  winner: number;
  pile: number[];
};

function createGame(playerA: readonly string[], playerB: readonly string[]): Game {
  // Number cards are equivalent; payment values preserve all relevant state.
  const decks = [playerA, playerB].map(deck =>
    deck.map(card => PAYMENTS.get(card) ?? 0)
  );
  return { decks, current: 0, cards: 0, tricks: 0 };
}

function isFinished(game: Game): boolean {
  return game.decks.some(deck => deck.length === 0);
}

function stateKey(game: Game): string {
  return `${game.current}:${game.decks[0].join("")}:${game.decks[1].join("")}`;
}

function checkAndRememberState(game: Game, seen: Set<string>): boolean {
  const key = stateKey(game);
  if (seen.has(key)) return true;
  seen.add(key);
  return false;
}

function playTrick(game: Game): Trick {
  const pile: number[] = [];
  let current = game.current;
  let paymentDue = 0;

  while (true) {
    const card = game.decks[current].shift();
    if (card === undefined) return { winner: 1 - current, pile };

    pile.push(card);

    if (card > 0) {
      paymentDue = card;
      current = 1 - current;
    } else if (paymentDue > 0) {
      paymentDue--;
      if (paymentDue === 0) return { winner: 1 - current, pile };
    } else {
      current = 1 - current;
    }
  }
}

function collectPile(game: Game, { winner, pile }: Trick): void {
  game.decks[winner].push(...pile);
  game.current = winner;
  game.cards += pile.length;
  game.tricks++;
}

function result(game: Game, status: GameResult["status"]): GameResult {
  return { status, cards: game.cards, tricks: game.tricks };
}

const loopFound = (game: Game) => result(game, "loop");

const gameFinished = (game: Game) => result(game, "finished");

export function simulateGame(playerA: readonly string[], playerB: readonly string[]): GameResult {
  const game = createGame(playerA, playerB);
  const seen = new Set<string>();

  while (!isFinished(game)) {
    if (checkAndRememberState(game, seen)) return loopFound(game);

    const trick = playTrick(game);
    collectPile(game, trick);
  }

  return gameFinished(game);
}

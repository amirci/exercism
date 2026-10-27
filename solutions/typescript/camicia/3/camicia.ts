type CardLabel = string;
// Payment value of a card: 0 for number cards, 1-4 for J, Q, K, A.
type CardValue = number;
type Deck = CardValue[];
type NonEmptyDeck = [CardValue, ...CardValue[]];
type PlayerIndex = 0 | 1;

// A non-negative integer, maintained by the game logic.
type Count = number;

type GameResult = {
  status: "finished" | "loop";
  cards: Count;
  tricks: Count;
};

const PAYMENTS = new Map([
  ["J", 1],
  ["Q", 2],
  ["K", 3],
  ["A", 4],
]);

type Game = {
  decks: Deck[];
  current: PlayerIndex;
  cards: Count;
  tricks: Count;
};

type Trick = {
  winner: PlayerIndex;
  playedCards: Deck;
};

type TrickState = Pick<Trick, "playedCards"> & {
  current: PlayerIndex;
  paymentDue: Count;
  winner: PlayerIndex | undefined;
};

function cardValue(card: CardLabel): CardValue {
  return PAYMENTS.get(card) ?? 0;
}

function createGame(playerA: readonly CardLabel[], playerB: readonly CardLabel[]): Game {
  // Number cards are equivalent; payment values preserve all relevant state.
  const decks: Deck[] = [playerA, playerB].map(deck =>
    deck.map(cardValue)
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

function otherPlayer(player: PlayerIndex): PlayerIndex {
  return player === 0 ? 1 : 0;
}

function hasCards(deck: Deck): deck is NonEmptyDeck {
  return deck.length > 0;
}

function drawCard(deck: NonEmptyDeck): CardValue {
  const card = deck[0];
  deck.shift();
  return card;
}

function isPaymentCard(card: CardValue): boolean {
  return card > 0;
}

function playCard(deck: NonEmptyDeck, state: TrickState): PlayerIndex | undefined {
  const card = drawCard(deck);
  state.playedCards.push(card);

  if (isPaymentCard(card)) {
    state.paymentDue = card;
    state.current = otherPlayer(state.current);
  } else if (state.paymentDue > 0) {
    state.paymentDue--;
    if (state.paymentDue === 0) return otherPlayer(state.current);
  } else {
    state.current = otherPlayer(state.current);
  }
  return undefined;
}

function playTrick(game: Game): Trick {
  const state: TrickState = {
    current: game.current,
    paymentDue: 0,
    winner: undefined,
    playedCards: [],
  };

  do {
    const deck = game.decks[state.current];

    state.winner = hasCards(deck) ? playCard(deck, state) : otherPlayer(state.current);

  } while (state.winner === undefined);

  return { winner: state.winner, playedCards: state.playedCards };
}

function collectPile(game: Game, { winner, playedCards }: Trick): void {
  game.decks[winner].push(...playedCards);
  game.current = winner;
  game.cards += playedCards.length;
  game.tricks++;
}

function completeTrick(game: Game): void {
  const trick = playTrick(game);
  collectPile(game, trick);
}

function result(game: Game, status: GameResult["status"]): GameResult {
  return { status, cards: game.cards, tricks: game.tricks };
}

const loopFound = (game: Game) => result(game, "loop");

const gameFinished = (game: Game) => result(game, "finished");

export function simulateGame(playerA: readonly CardLabel[], playerB: readonly CardLabel[]): GameResult {
  const game = createGame(playerA, playerB);
  const seen = new Set<string>();

  while (!isFinished(game)) {
    if (checkAndRememberState(game, seen)) return loopFound(game);

    completeTrick(game);
  }

  return gameFinished(game);
}

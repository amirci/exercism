const RANKS = ["2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"] as const;
const SUITS = ["C", "D", "H", "S"] as const;
type Suit = typeof SUITS[number];
type Rank = number;
type Card = { rank: Rank; suit: Suit };
type Hand = readonly [Card, Card, Card, Card, Card];
type RankCount = { rank: Rank; count: number };

type HighCardRank = {category: "highCard"; strength: 0; tieBreak: readonly [Rank, Rank, Rank, Rank, Rank];};
type OnePairRank = {category: "onePair"; strength: 1; tieBreak: readonly [Rank, Rank, Rank, Rank];};
type TwoPairRank = {category: "twoPair"; strength: 2; tieBreak: readonly [Rank, Rank, Rank];};
type ThreeOfAKindRank = {category: "threeOfAKind"; strength: 3; tieBreak: readonly [Rank, Rank, Rank];};
type StraightRank = { category: "straight"; strength: 4; tieBreak: readonly [Rank] };
type FlushRank = {category: "flush"; strength: 5; tieBreak: readonly [Rank, Rank, Rank, Rank, Rank];};
type FullHouseRank = { category: "fullHouse"; strength: 6; tieBreak: readonly [Rank, Rank] };
type FourOfAKindRank = { category: "fourOfAKind"; strength: 7; tieBreak: readonly [Rank, Rank] };
type StraightFlushRank = { category: "straightFlush"; strength: 8; tieBreak: readonly [Rank] };

type HandRank = HighCardRank | OnePairRank | TwoPairRank | ThreeOfAKindRank
  | StraightRank | FlushRank | FullHouseRank | FourOfAKindRank | StraightFlushRank;

function parseCardOrFail(label: string): Card {
  const [rankStr, suitStr] = [label.slice(0, -1), label.slice(-1)];
  const rank = RANKS.findIndex(candidate => candidate === rankStr);
  const suit = SUITS.find(candidate => candidate === suitStr);
  if (rank === -1 || suit === undefined) throw new Error("Invalid card " + label);
  return { rank: rank + 2, suit };
}

function parseHandOrFail(input: string): Hand {
  const cards = input.trim().split(/\s+/).map(parseCardOrFail);
  if (cards.length !== 5) throw new Error("A hand must contain five cards");
  return [cards[0], cards[1], cards[2], cards[3], cards[4]];
}

type Frequencies = RankCount[];

function frequencies(hand: Hand): Frequencies {
  const counts = new Map<Rank, number>();
  for (const { rank } of hand) counts.set(rank, (counts.get(rank) ?? 0) + 1);
  return [...counts].map(([rank, count]) => ({ rank, count }))
    .sort((left, right) => right.count - left.count || right.rank - left.rank);
}

function straightHighCard(ranks: readonly Rank[]): Rank | undefined {
  if (ranks.every((rank, index) => rank === ranks[0] - index)) return ranks[0];
  if (ranks.join(",") === "14,5,4,3,2") return 5;
  return undefined;
}

type HandStats = {
    freqs: Frequencies,
    ranks: readonly Rank[],
    flush: boolean,
    straightHighCard: Rank | undefined
};

function calculateStats(hand: Hand): HandStats {
  const ranks = hand.map(card => card.rank).sort((a, b) => b - a);

    return {
        ranks,
        flush: hand.every(card => card.suit === hand[0].suit),
        freqs: frequencies(hand),
        straightHighCard: straightHighCard(ranks),
    }
}

function straightFlush(handStats: HandStats): HandRank | undefined {
  return handStats.flush && handStats.straightHighCard !== undefined
    ? { category: "straightFlush", strength: 8, tieBreak: [handStats.straightHighCard] }
    : undefined;
}

function fourOfAKind(handStats: HandStats): HandRank | undefined {
  const [quad, kicker] = handStats.freqs;
  return quad.count === 4
    ? { category: "fourOfAKind", strength: 7, tieBreak: [quad.rank, kicker.rank] }
    : undefined;
}

function fullHouse(handStats: HandStats): HandRank | undefined {
  const [triple, pair] = handStats.freqs;
  return triple.count === 3 && pair.count === 2
    ? { category: "fullHouse", strength: 6, tieBreak: [triple.rank, pair.rank] }
    : undefined;
}

function flush(handStats: HandStats): HandRank | undefined {
  if (!handStats.flush) return undefined;
  const [first, second, third, fourth, fifth] = handStats.ranks;
  return { category: "flush", strength: 5, tieBreak: [first, second, third, fourth, fifth] };
}

function straight(handStats: HandStats): HandRank | undefined {
  return handStats.straightHighCard === undefined
    ? undefined
    : { category: "straight", strength: 4, tieBreak: [handStats.straightHighCard] };
}

function threeOfAKind(handStats: HandStats): HandRank | undefined {
  if (handStats.freqs[0].count !== 3) return undefined;
  return {
    category: "threeOfAKind",
    strength: 3,
    tieBreak: [handStats.freqs[0].rank, handStats.freqs[1].rank, handStats.freqs[2].rank],
  };
}

function twoPair(handStats: HandStats): HandRank | undefined {
  if (handStats.freqs[0].count !== 2 || handStats.freqs[1].count !== 2) return undefined;
  return {
    category: "twoPair",
    strength: 2,
    tieBreak: [handStats.freqs[0].rank, handStats.freqs[1].rank, handStats.freqs[2].rank],
  };
}

function onePair(handStats: HandStats): HandRank | undefined {
  if (handStats.freqs[0].count !== 2) return undefined;
  return {
    category: "onePair",
    strength: 1,
    tieBreak: [handStats.freqs[0].rank, handStats.freqs[1].rank, handStats.freqs[2].rank, handStats.freqs[3].rank],
  };
}

function highCard(handStats: HandStats): HandRank {
  const [first, second, third, fourth, fifth] = handStats.ranks;
  return { category: "highCard", strength: 0, tieBreak: [first, second, third, fourth, fifth] };
}

const rankers: ((stats: HandStats) => HandRank | undefined)[] = [
  straightFlush, fourOfAKind, fullHouse, flush, straight,
  threeOfAKind, twoPair, onePair,
];

function rankHand(hand: Hand): HandRank {
  const handStats = calculateStats(hand);
  return rankers.reduce<HandRank | undefined>(
    (best, ranker) => best ?? ranker(handStats),
    undefined
  ) ?? highCard(handStats);
}

function compareTieBreaks(left: HandRank, right: HandRank): number {
  const [rank1, rank2] = [left.tieBreak, right.tieBreak];
  for (let index = 0; index < rank1.length; index++) {
    const difference = rank1[index] - rank2[index];
    if (difference !== 0) return difference;
  }
  return 0;
}

function compareHands(left: HandRank, right: HandRank): number {
  const diff = left.strength - right.strength;
  return diff || compareTieBreaks(left, right);
}

export function bestHands(hands: string[]): string[] {
  if (hands.length === 0) return [];

  const ranked = hands
    .map(original => {
      const hand = parseHandOrFail(original);
      return { original, rank: rankHand(hand) };
    })
    .sort((left, right) => compareHands(right.rank, left.rank));

  const best = ranked[0].rank;
  return ranked
    .filter(({ rank }) => compareHands(rank, best) === 0)
    .map(({ original }) => original);
}

const RANKS = ["2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"] as const;
const SUITS = ["C", "D", "H", "S"] as const;
type Suit = typeof SUITS[number];
type Rank = number;
type Card = { rank: Rank; suit: Suit };
type Hand = readonly [Card, Card, Card, Card, Card];
type RankCount = { rank: Rank; count: number };

type HandRank =
  | { category: "highCard" | "flush"; tieBreak: readonly [Rank, Rank, Rank, Rank, Rank] }
  | { category: "onePair"; tieBreak: readonly [Rank, Rank, Rank, Rank] }
  | { category: "twoPair" | "threeOfAKind"; tieBreak: readonly [Rank, Rank, Rank] }
  | { category: "fullHouse" | "fourOfAKind"; tieBreak: readonly [Rank, Rank] }
  | { category: "straight" | "straightFlush"; tieBreak: readonly [Rank] };

const CATEGORY_STRENGTH: Record<HandRank["category"], number> = {
  highCard: 0,
  onePair: 1,
  twoPair: 2,
  threeOfAKind: 3,
  straight: 4,
  flush: 5,
  fullHouse: 6,
  fourOfAKind: 7,
  straightFlush: 8,
};

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
    ? { category: "straightFlush", tieBreak: [handStats.straightHighCard] }
    : undefined;
}

function fourOfAKind(handStats: HandStats): HandRank | undefined {
  const [quad, kicker] = handStats.freqs;
  return quad.count === 4
    ? { category: "fourOfAKind", tieBreak: [quad.rank, kicker.rank] }
    : undefined;
}

function fullHouse(handStats: HandStats): HandRank | undefined {
  const [triple, pair] = handStats.freqs;
  return triple.count === 3 && pair.count === 2
    ? { category: "fullHouse", tieBreak: [triple.rank, pair.rank] }
    : undefined;
}

function flush(handStats: HandStats): HandRank | undefined {
  if (!handStats.flush) return undefined;
  const [first, second, third, fourth, fifth] = handStats.ranks;
  return { category: "flush", tieBreak: [first, second, third, fourth, fifth] };
}

function straight(handStats: HandStats): HandRank | undefined {
  return handStats.straightHighCard === undefined
    ? undefined
    : { category: "straight", tieBreak: [handStats.straightHighCard] };
}

function threeOfAKind(handStats: HandStats): HandRank | undefined {
  if (handStats.freqs[0].count !== 3) return undefined;
  return {
    category: "threeOfAKind",
    tieBreak: [handStats.freqs[0].rank, handStats.freqs[1].rank, handStats.freqs[2].rank],
  };
}

function twoPair(handStats: HandStats): HandRank | undefined {
  if (handStats.freqs[0].count !== 2 || handStats.freqs[1].count !== 2) return undefined;
  return {
    category: "twoPair",
    tieBreak: [handStats.freqs[0].rank, handStats.freqs[1].rank, handStats.freqs[2].rank],
  };
}

function onePair(handStats: HandStats): HandRank | undefined {
  if (handStats.freqs[0].count !== 2) return undefined;
  return {
    category: "onePair",
    tieBreak: [handStats.freqs[0].rank, handStats.freqs[1].rank, handStats.freqs[2].rank, handStats.freqs[3].rank],
  };
}

function highCard(handStats: HandStats): HandRank {
  const [first, second, third, fourth, fifth] = handStats.ranks;
  return { category: "highCard", tieBreak: [first, second, third, fourth, fifth] };
}

const rankers = [straightFlush, fourOfAKind, fullHouse, flush, straight, threeOfAKind, twoPair, onePair] as const;

function rankHand(hand: Hand): HandRank {
  const handStats = calculateStats(hand);
  return rankers.reduce<HandRank | undefined>(
    (best, ranker) => best ?? ranker(handStats),
    undefined
  ) ?? highCard(handStats);
}

function compareHands(left: HandRank, right: HandRank): number {
  const categoryDifference = CATEGORY_STRENGTH[left.category] - CATEGORY_STRENGTH[right.category];
  if (categoryDifference !== 0) return categoryDifference;
  for (let index = 0; index < left.tieBreak.length; index++) {
    const difference = left.tieBreak[index] - right.tieBreak[index];
    if (difference !== 0) return difference;
  }
  return 0;
}

export function bestHands(hands: string[]): string[] {
  let best: HandRank | undefined;
  let winners: string[] = [];
  for (const hand of hands) {
    const rank = rankHand(parseHandOrFail(hand));
    const comparison = best === undefined ? 1 : compareHands(rank, best);
    if (comparison > 0) {
      best = rank;
      winners = [hand];
    } else if (comparison === 0) {
      winners.push(hand);
    }
  }
  return winners;
}

const RANKS = ["2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"] as const;
const SUITS = ["C", "D", "H", "S"] as const;
type Suit = typeof SUITS[number];
type Rank = number;
type Card = { rank: Rank; suit: Suit };
type Hand = readonly [Card, Card, Card, Card, Card];
type RankGroup = { rank: Rank; count: number };

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

function parseCard(label: string): Card {
  const rank = RANKS.findIndex(candidate => candidate === label.slice(0, -1));
  const suit = SUITS.find(candidate => candidate === label.slice(-1));
  if (rank === -1 || suit === undefined) throw new Error("Invalid card");
  return { rank: rank + 2, suit };
}

function parseHand(input: string): Hand {
  const cards = input.trim().split(/\s+/).map(parseCard);
  if (cards.length !== 5) throw new Error("A hand must contain five cards");
  return [cards[0], cards[1], cards[2], cards[3], cards[4]];
}

function groupRanks(hand: Hand): RankGroup[] {
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

function rankHand(hand: Hand): HandRank {
  const ranks = hand.map(card => card.rank).sort((a, b) => b - a);
  const [first, second, third, fourth, fifth] = ranks;
  const flush = hand.every(card => card.suit === hand[0].suit);
  const straight = straightHighCard(ranks);
  const groups = groupRanks(hand);
  const [largest, next] = groups;

  if (flush && straight !== undefined) return { category: "straightFlush", tieBreak: [straight] };
  if (largest.count === 4) return { category: "fourOfAKind", tieBreak: [largest.rank, next.rank] };
  if (largest.count === 3 && next.count === 2) {
    return { category: "fullHouse", tieBreak: [largest.rank, next.rank] };
  }
  if (flush) return { category: "flush", tieBreak: [first, second, third, fourth, fifth] };
  if (straight !== undefined) return { category: "straight", tieBreak: [straight] };
  if (largest.count === 3) {
    return { category: "threeOfAKind", tieBreak: [largest.rank, next.rank, groups[2].rank] };
  }
  if (largest.count === 2 && next.count === 2) {
    return { category: "twoPair", tieBreak: [largest.rank, next.rank, groups[2].rank] };
  }
  if (largest.count === 2) {
    return { category: "onePair", tieBreak: [largest.rank, next.rank, groups[2].rank, groups[3].rank] };
  }
  return { category: "highCard", tieBreak: [first, second, third, fourth, fifth] };
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
    const rank = rankHand(parseHand(hand));
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

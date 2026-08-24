//
// This is only a SKELETON file for the 'BookStore' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

const BOOK_PRICE = 800;
const DISCOUNTS = { 1: 1, 2: 0.95, 3: 0.9, 4: 0.8, 5: 0.75 };
const BOOK_IDS = [1, 2, 3, 4, 5];

export const cost = (books) => {
  const booksMatchingId = (id) => books.filter((book) => book === id);
  const counts = BOOK_IDS.map((id) => booksMatchingId(id).length);

  return lowestCost(counts);
};

function lowestCost(counts, memo = {}) {
  const key = counts.join(',');

  if (key in memo) {
    return memo[key];
  }

  if (noBooksLeft(counts)) {
    return 0;
  }

  let bestCost = Infinity;

  for (const group of possibleGroups(counts)) {
    const nextCounts = removeGroup(counts, group);
    const groupCost = group.length * BOOK_PRICE * DISCOUNTS[group.length];

    bestCost = Math.min(bestCost, groupCost + lowestCost(nextCounts, memo));
  }

  memo[key] = bestCost;
  return bestCost;
}

function noBooksLeft(counts) {
  return counts.every((count) => count === 0);
}

function removeGroup(counts, group) {
  return counts.map((count, index) => (group.includes(index) ? count - 1 : count));
}

function possibleGroups(counts) {
  const availableBooks = availableBookIndexes(counts);

  return availableBooks
    .reduce(addBookToGroupCombinations, [[]])
    .filter((group) => group.length > 0);
}

function availableBookIndexes(counts) {
  return counts.map((_, index) => index).filter((index) => counts[index] > 0);
}

function addBookToGroupCombinations(groups, bookIndex) {
  return groups.concat(groups.map((group) => [...group, bookIndex]));
}

//
// This is only a SKELETON file for the 'BookStore' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

const GROUP_PRICES = [0, 800, 1520, 2160, 2560, 3000];
const BOOK_TYPE_COUNT = 5;

export const cost = (books) => lowestCost(bookCounts(books));


function lowestCost(counts, memo = {}) {
  const key = counts.join(',');

  if (key in memo) {
    return memo[key];
  }

  if (noBooksLeft(counts)) {
    return 0;
  }

  let bestCost = Infinity;

  for (const group of possibleBookGroups(counts)) {
    const nextCounts = removeGroup(counts, group);

    bestCost = Math.min(bestCost, groupCost(group) + lowestCost(nextCounts, memo));
  }

  memo[key] = bestCost;
  return bestCost;
}

function bookCounts(books) {
  const counts = Array(BOOK_TYPE_COUNT).fill(0);

  for (const book of books) {
    counts[book - 1] += 1;
  }

  return counts;
}

function groupCost(group) {
  return GROUP_PRICES[group.length];
}

function noBooksLeft(counts) {
  return counts.every((count) => count === 0);
}

function removeGroup(counts, group) {
  return counts.map((count, index) => (group.includes(index) ? count - 1 : count));
}

function possibleBookGroups(counts) {
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

const startsWithVowelSound = (word) =>
  /^(?:[aeiou]|xr|yt)/.test(word);

const prefixLength = (word) => {
  const quCluster = word.match(/^[^aeiouy]*qu/)?.[0];
  if (quCluster) return quCluster.length;

  const yPrefix = word.match(/^[^aeiouy]+(?=y)/)?.[0];
  if (yPrefix) return yPrefix.length;

  return word.search(/[aeiou]/);
};

const splitWordAtPrefix = (word) => {
  const length = prefixLength(word);
  return [word.slice(0, length), word.slice(length)];
};

const translateWord = (word) => {
  if (startsWithVowelSound(word)) {
    return `${word}ay`;
  }

  const [start, end] = splitWordAtPrefix(word);
  return `${end}${start}ay`;
};

export const translate = (phrase) =>
  phrase.split(" ").map(translateWord).join(" ");

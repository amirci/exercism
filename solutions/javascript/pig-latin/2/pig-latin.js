const translateWord = (word) => {
  if (/^(?:[aeiou]|xr|yt)/.test(word)) {
    return `${word}ay`;
  }

  const quCluster = word.match(/^[^aeiouy]*qu/)?.[0];
  const yPrefix = word.match(/^[^aeiouy]+(?=y)/)?.[0];
  const clusterLength = quCluster?.length
    ?? yPrefix?.length
    ?? word.search(/[aeiou]/);

  return `${word.slice(clusterLength)}${word.slice(0, clusterLength)}ay`;
};

export const translate = (phrase) =>
  phrase.split(" ").map(translateWord).join(" ");

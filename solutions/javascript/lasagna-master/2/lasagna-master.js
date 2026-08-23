/// <reference path="./global.d.ts" />
// @ts-check

/**
 * Implement the functions needed to solve the exercise here.
 * Do not forget to export them so they are available for the
 * tests. Here an example of the syntax as reminder:
 *
 * export function yourFunction(...) {
 *   ...
 * }
 */

export function cookingStatus(remainingTime) {
  if (remainingTime === undefined) {
    return 'You forgot to set the timer.';
  }

  if (remainingTime === 0) {
    return 'Lasagna is done.';
  }

  return 'Not done, please wait.';
}

export function preparationTime(layers, averagePreparationTime = 2) {
  return layers.length * averagePreparationTime;
}

export function quantities(layers) {
  const amounts = {
    noodles: 0,
    sauce: 0,
  };

  for (const layer of layers) {
    if (layer === 'noodles') {
      amounts.noodles += 50;
    }

    if (layer === 'sauce') {
      amounts.sauce += 0.2;
    }
  }

  return amounts;
}

export function addSecretIngredient(friendsList, myList) {
  myList.push(friendsList[friendsList.length - 1]);
}

export function scaleRecipe(recipe, portions) {
  const factor = portions / 2;

  return Object.entries(recipe).reduce((scaledRecipe, [ingredient, amount]) => {
    scaledRecipe[ingredient] = amount * factor;
    return scaledRecipe;
  }, {});
}

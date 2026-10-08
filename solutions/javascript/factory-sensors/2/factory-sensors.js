// @ts-check

/** Error raised when the temperature sensor argument is invalid. */
export class ArgumentError extends Error {
  constructor(message) {
    super(message);
    this.name = "ArgumentError";
  }
}

/** Error raised when a machine temperature is too high. */
export class OverheatingError extends Error {
  constructor(temperature) {
    super(`The temperature is ${temperature}! Overheating!`);
    this.name = "OverheatingError";
    this.temperature = temperature;
  }
}

/**
 * Check whether the room humidity is within the allowed range.
 *
 * @param {number} humidityPercentage
 * @throws {Error}
 */
export function checkHumidityLevel(humidityPercentage) {
  if (humidityPercentage > 70) {
    throw new Error("The humidity percentage is too high");
  }
}

/**
 * Report a broken temperature sensor or an overheating machine.
 *
 * @param {number|null} temperature
 * @throws {ArgumentError|OverheatingError}
 */
export function reportOverheating(temperature) {
  if (temperature === null) {
    throw new ArgumentError("The temperature sensor is broken");
  }

  if (temperature > 500) {
    throw new OverheatingError(temperature);
  }
}

/**
 * Run the machine check and dispatch the appropriate response.
 *
 * @param {{
 *   check: function,
 *   alertDeadSensor: function,
 *   alertOverheating: function,
 *   shutdown: function
 * }} actions
 * @throws {ArgumentError|OverheatingError|Error}
 */
export function monitorTheMachine(actions) {
  try {
    actions.check();
  } catch (error) {
    if (error instanceof ArgumentError) {
      actions.alertDeadSensor();
    } else if (error instanceof OverheatingError) {
      if (error.temperature < 600) {
        actions.alertOverheating();
      } else {
        actions.shutdown();
      }
    } else {
      throw error;
    }
  }
}

// Non-negative elapsed seconds, maintained by the stopwatch.
type Seconds = number;
type Command = "start" | "stop" | "lap" | "reset";

type LapTimes = {
  current: Seconds;
  previous: readonly Seconds[];
};

type StopwatchState =
  | { kind: "ready"; current: 0; previous: readonly [] }
  | { kind: "running" | "stopped" } & LapTimes
  ;

type StateName = StopwatchState["kind"];

type CommandRule = {
  allowedFrom: readonly StateName[];
  error: string;
};

const COMMAND_RULES: Record<Command, CommandRule> = {
  start: {
    allowedFrom: ["ready", "stopped"],
    error: "cannot start an already running stopwatch",
  },
  stop: {
    allowedFrom: ["running"],
    error: "cannot stop a stopwatch that is not running",
  },
  lap: {
    allowedFrom: ["running"],
    error: "cannot lap a stopwatch that is not running",
  },
  reset: {
    allowedFrom: ["stopped"],
    error: "cannot reset a stopwatch that is not stopped",
  },
};

const READY_STATE = { kind: "ready", current: 0, previous: [] } as const;

function parseDuration(duration: string): Seconds {
  const [hours, minutes, seconds] = duration.split(":").map(Number);
  return hours * 3600 + minutes * 60 + seconds;
}

function formatDuration(seconds: Seconds): string {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor(seconds / 60) % 60;
  return [hours, minutes, seconds % 60]
    .map(part => String(part).padStart(2, "0"))
    .join(":");
}

function updateState(state: StopwatchState, kind: "running" | "stopped"): StopwatchState {
  return { ...state, kind };
}

function addLapToHistory(state: StopwatchState): StopwatchState {
  return { kind: "running", current: 0, previous: [...state.previous, state.current] };
}

function transition(state: StopwatchState, command: Command): StopwatchState {
  const rule = COMMAND_RULES[command];
  if (!rule.allowedFrom.includes(state.kind)) {
    throw new Error(rule.error);
  }

  switch (command) {
    case "start":
      return updateState(state, "running");
    case "stop":
      return updateState(state, "stopped");
    case "lap":
      return addLapToHistory(state);
    case "reset":
      return READY_STATE;
  }
}

export class SplitSecondStopwatch {
  private stopwatch: StopwatchState = READY_STATE;

  public get state(): StateName {
    return this.stopwatch.kind;
  }

  public get currentLap(): string {
    return formatDuration(this.stopwatch.current);
  }

  public get total(): string {
    const seconds = this.stopwatch.previous.reduce(
      (total, lap) => total + lap,
      this.stopwatch.current
    );
    return formatDuration(seconds);
  }

  public get previousLaps(): string[] {
    return this.stopwatch.previous.map(formatDuration);
  }

  public start(): void { this.updateStopWatch("start"); }

  public stop(): void { this.updateStopWatch("stop"); }

  public lap(): void { this.updateStopWatch("lap"); }

  public reset(): void { this.updateStopWatch("reset"); }

  private updateStopWatch(cmd: Command) {
    this.stopwatch = transition(this.stopwatch, cmd);
  }

  public advanceTime(duration: string): void {
    if (this.stopwatch.kind === "running") {
      this.stopwatch = {
        ...this.stopwatch,
        current: this.stopwatch.current + parseDuration(duration),
      };
    }
  }
}

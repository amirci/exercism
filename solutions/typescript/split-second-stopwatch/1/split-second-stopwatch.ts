// Non-negative elapsed seconds, maintained by the stopwatch.
type Seconds = number;
type Command = "start" | "stop" | "lap" | "reset";

type LapTimes = {
  current: Seconds;
  previous: readonly Seconds[];
};

type StopwatchState =
  | { kind: "ready" }
  | { kind: "running" } & LapTimes
  | { kind: "stopped" } & LapTimes;

type StateName = StopwatchState["kind"];

function parseDuration(duration: string): Seconds {
  return duration.split(":").reduce((seconds, part) => seconds * 60 + Number(part), 0);
}

function formatDuration(seconds: Seconds): string {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor(seconds / 60) % 60;
  return [hours, minutes, seconds % 60]
    .map(part => String(part).padStart(2, "0"))
    .join(":");
}

function transition(state: StopwatchState, command: Command): StopwatchState {
  switch (command) {
    case "start":
      if (state.kind === "running") {
        throw new Error("cannot start an already running stopwatch");
      }
      return state.kind === "ready"
        ? { kind: "running", current: 0, previous: [] }
        : { ...state, kind: "running" };
    case "stop":
      if (state.kind !== "running") {
        throw new Error("cannot stop a stopwatch that is not running");
      }
      return { ...state, kind: "stopped" };
    case "lap":
      if (state.kind !== "running") {
        throw new Error("cannot lap a stopwatch that is not running");
      }
      return { ...state, current: 0, previous: [...state.previous, state.current] };
    case "reset":
      if (state.kind !== "stopped") {
        throw new Error("cannot reset a stopwatch that is not stopped");
      }
      return { kind: "ready" };
  }
}

export class SplitSecondStopwatch {
  private stopwatch: StopwatchState = { kind: "ready" };

  public get state(): StateName {
    return this.stopwatch.kind;
  }

  public get currentLap(): string {
    return formatDuration(this.stopwatch.kind === "ready" ? 0 : this.stopwatch.current);
  }

  public get total(): string {
    if (this.stopwatch.kind === "ready") return formatDuration(0);
    const seconds = this.stopwatch.previous.reduce(
      (total, lap) => total + lap,
      this.stopwatch.current
    );
    return formatDuration(seconds);
  }

  public get previousLaps(): string[] {
    return this.stopwatch.kind === "ready"
      ? []
      : this.stopwatch.previous.map(formatDuration);
  }

  public start(): void {
    this.stopwatch = transition(this.stopwatch, "start");
  }

  public stop(): void {
    this.stopwatch = transition(this.stopwatch, "stop");
  }

  public lap(): void {
    this.stopwatch = transition(this.stopwatch, "lap");
  }

  public reset(): void {
    this.stopwatch = transition(this.stopwatch, "reset");
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

type Roll = number & { readonly __brand: "Roll" }
type RollHistory = Roll[]
type Frame = {
    start: number;
    end: number;
}

function assertValidRoll(pins: number): asserts pins is Roll {
    if (pins < 0) {
        throw new Error("Negative roll is invalid")
    }
    if (pins > 10) {
        throw new Error("Pin count exceeds pins on the lane")
    }
}

function ensureThat(condition: boolean, message: string): void {
    if (!condition) {
        throw new Error(message)
    }
}

type RollStateFn = (pins: Roll) => RollStateFn;
type PendingRolls = 0 | 1 | 2;

function isStrike(pins: Roll) {
    return pins === 10;
}

const newFrame = (start: number): Frame => ({
    start,
    end: start,
});

function isSpare(lastRoll: Roll, pins: Roll) {
    return lastRoll + pins === 10;
}

const gameEnded: RollStateFn = (_: Roll) => {
    throw new Error("Cannot roll after game is over")
};

export class Bowling {
    private rolls: RollHistory = []
    private frames: Frame[] = [newFrame(0)]
    private registerRoll: RollStateFn;

    constructor() {
        this.registerRoll = this.openFrameFirstRoll.bind(this)
    }

    public roll(pins: number): void {
        assertValidRoll(pins)

        ensureThat(this.registerRoll !== gameEnded, "Cannot roll after game is over")

        this.registerRoll = this.registerRoll(pins)
        this.recordRoll(pins)
    }

    private openFrameFirstRoll(pins: Roll): RollStateFn {
        const lastFrame = this.frames[this.frames.length - 1];
        if (isStrike(pins)) {
            // if strike then end is in the next two rolls, add new frame
            lastFrame.end = lastFrame.start + 2;
            return this.addNewFrame(lastFrame.start + 1, 2)
        }

        // otherise switch increment index
        lastFrame.end = lastFrame.start + 1;
        return this.openFrameSecondRoll;
    }

    private openFrameSecondRoll(pins: Roll): RollStateFn {
        const lastFrame = this.frames[this.frames.length - 1];
        const lastRoll = this.rolls[this.rolls.length - 1];

        ensureThat(lastRoll + pins <= 10, "Pin count exceeds pins on the lane")

        // if spare then end is in the next roll, add new frame
        // otherise increment index and add new frame
        const [increment, pending] = isSpare(lastRoll, pins)
            ? [2, 1] as const
            : [1, 0] as const
        lastFrame.end = lastFrame.start + increment;
        return this.addNewFrame(lastFrame.start + 2, pending)
    }

    public score(): number {
        ensureThat(this.registerRoll === gameEnded, "Score cannot be taken until the end of the game")

        return this.frames
            .flatMap(frame => this.frameToRolls(frame))
            .reduce((a, b) => a + b, 0)
    }

    private frameToRolls(frame: Frame): RollHistory {
        return this.rolls.slice(frame.start, frame.end + 1)
    }

    private recordRoll(pins: Roll): void {
        this.rolls.push(pins)
    }

    private addNewFrame(
        nextStart: number,
        pending: PendingRolls = 0,
    ): RollStateFn {
        if (this.frames.length === 10) {
            return this.lastFrameRoll(pending)
        }

        this.frames.push(newFrame(nextStart));
        return this.openFrameFirstRoll;
    }

    private lastFrameRoll(pending: PendingRolls): RollStateFn {
        if (pending === 0) {
            return gameEnded;
        }

        const goToEnd = (_: Roll) => gameEnded;

        if (pending === 1) {
            return goToEnd;
        }

        return (firstBonus: Roll) => isStrike(firstBonus)
            ? goToEnd
            : (secondBonus: Roll) => {
                ensureThat(firstBonus + secondBonus <= 10, "Pin count exceeds pins on the lane")
                return gameEnded;
        };
    }
}

export class Tournament {
  public tally(input: string): string {
    const rows = [...buildTeamStats(input)]
      .sort(compareTeamRanking)
      .map(formatTeamRow)

    return [HEADER, ...rows].join("\n")
  }
}

type MatchOutcome = "win" | "draw" | "loss"

type Match = {
  firstTeamName: string
  secondTeamName: string
  outcome: MatchOutcome
}

type TeamStats = {
  name: string
  matchesPlayed: number
  wins: number
  draws: number
  losses: number
  points: number
}

const HEADER = "Team                           | MP |  W |  D |  L |  P"
const WIN_POINTS = 3
const DRAW_POINTS = 1

function buildTeamStats(input: string): Iterable<TeamStats> {
    return input
      .split(/\r?\n/)
      .filter((line) => line.length > 0)
      .reduce(updateStats, new Map<string, TeamStats>())
      .values();
}

function getOrCreateTeam(teams: Map<string, TeamStats>, name: string): TeamStats {
  let team = teams.get(name)

  if (team === undefined) {
    team = { name, matchesPlayed: 0, wins: 0, draws: 0, losses: 0, points: 0 }
    teams.set(name, team)
  }

  return team
}

function parseMatch(line: string): Match {
  const [firstTeamName, secondTeamName, outcome] = line.split(";") as [string, string, MatchOutcome]
  return { firstTeamName, secondTeamName, outcome }
}

function registerWin(winner: TeamStats, loser: TeamStats): void {
  winner.wins++
  winner.points += WIN_POINTS
  loser.losses++
}

function registerDraw(first: TeamStats, second: TeamStats): void {
  first.draws++
  first.points += DRAW_POINTS
  second.draws++
  second.points += DRAW_POINTS
}

type Roster =Map<string, TeamStats>

function updateStats(teams: Roster, line: string): Roster {
  const { firstTeamName, secondTeamName, outcome } = parseMatch(line)
  const first = getOrCreateTeam(teams, firstTeamName)
  const second = getOrCreateTeam(teams, secondTeamName)

  first.matchesPlayed++
  second.matchesPlayed++

  switch (outcome) {
    case "win":
      registerWin(first, second)
      break
    case "draw":
      registerDraw(first, second)
      break
    case "loss":
      registerWin(second, first)
      break
  }

  return teams
}

function compareTeamRanking(first: TeamStats, second: TeamStats): number {
  return second.points - first.points || first.name.localeCompare(second.name)
}

function formatTeamRow(team: TeamStats): string {
  return `${team.name.padEnd(30)} | ${team.matchesPlayed.toString().padStart(2)} | ${team.wins.toString().padStart(2)} | ${team.draws.toString().padStart(2)} | ${team.losses.toString().padStart(2)} | ${team.points.toString().padStart(2)}`
}

type Person = string
type FamilyTree = Record<Person, readonly Person[]>
type FamilyGraph = Map<Person, Set<Person>>

type SearchState = {
  person: Person
  separation: number
}

export function degreesOfSeparation(
  familyTree: FamilyTree,
  personA: Person,
  personB: Person
): number {
  if (personA === personB) return 0

  const graph = buildFamilyGraph(familyTree)
  const visited = new Set<Person>([personA])
  const queue: SearchState[] = [{ person: personA, separation: 0 }]

  for (let index = 0; index < queue.length; index++) {
    const { person, separation } = queue[index]

    for (const relative of graph.get(person) ?? []) {
      if (visited.has(relative)) continue
      if (relative === personB) return separation + 1

      visited.add(relative)
      queue.push({ person: relative, separation: separation + 1 })
    }
  }

  return -1
}

function buildFamilyGraph(familyTree: FamilyTree): FamilyGraph {
  const graph: FamilyGraph = new Map()

  for (const [parent, children] of Object.entries(familyTree)) {
    children.forEach((child) => addConnection(graph, parent, child))

    for (let firstIndex = 0; firstIndex < children.length; firstIndex++) {
      for (let secondIndex = firstIndex + 1; secondIndex < children.length; secondIndex++) {
        addConnection(graph, children[firstIndex], children[secondIndex])
      }
    }
  }

  return graph
}

function addConnection(graph: FamilyGraph, first: Person, second: Person): void {
  const firstConnections = graph.get(first) ?? new Set<Person>()
  const secondConnections = graph.get(second) ?? new Set<Person>()

  firstConnections.add(second)
  secondConnections.add(first)
  graph.set(first, firstConnections)
  graph.set(second, secondConnections)
}

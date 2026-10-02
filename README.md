# Sightseeing Problem Hyper-Heuristic

A hyper-heuristic optimiser for the sightseeing problem, written in Java on the [HyFlex](https://people.cs.nott.ac.uk/pszwj1/chesc2011/) framework.

**The problem:** a tourist leaves their hotel, visits every point of interest once, and finishes at the airport. Find the visiting order with the shortest total distance. It is a variant of the travelling salesman problem with fixed, distinct start and end points.

**The approach:** rather than one fixed search algorithm, a hyper-heuristic chooses between several low-level heuristics as it runs, learning which ones are paying off on the instance at hand.

## Low-level heuristics

| Heuristic | Type | What it does |
|---|---|---|
| Adjacent swap | Mutation | Swaps neighbouring locations in the tour |
| Reinsertion | Mutation | Removes a location and reinserts it elsewhere |
| Segment reversal | Mutation | Reverses a section of the tour |
| Davis's hill climbing | Local search | Tries swaps in a random order, keeping improvements |
| Next descent | Local search | Accepts the first improving move found |
| Steepest descent | Local search | Evaluates all moves and takes the best |
| Order crossover (OX) | Crossover | Combines two tours while preserving relative order |

Mutations scale with HyFlex's *intensity of mutation* setting and local searches with *depth of search*. Solutions can be initialised randomly or constructively.

## The learning hyper-heuristic

`LearningHH` keeps a score for every heuristic and for each candidate parameter value (0.1 to 0.9) of depth of search and intensity of mutation.

- **Selection:** with probability 0.1 it explores by picking a heuristic at random. Otherwise it samples one in proportion to the exponential of its score (softmax), so better heuristics are chosen more often without the others being shut out. Parameter values are sampled the same way.
- **Application:** local searches are applied up to five times in a row while they keep improving.
- **Learning:** a heuristic that does not worsen the solution has its score raised; one that does has its score multiplied by 0.95. The parameter values used are rewarded or decayed alongside.
- **Acceptance:** only improving candidates replace the current solution.

A simpler baseline, `SR_IE_HH` (random selection, accepting improving or equal moves), is included for comparison.

## Running it

Requires Java 17 or later. From the repository root:

```bash
javac -cp HyFlex_1.0.0_ThreadTimer.jar -d build $(find src -name "*.java")
java -cp build:HyFlex_1.0.0_ThreadTimer.jar com.aim.project.ssp.runners.LearningSelectionVisualRunner
```

This runs the learning hyper-heuristic for ten seconds on one of the instances in `instances/ssp/`, prints the initial and best tours with their costs, and opens a window drawing the best route found. Swap in `SR_IE_VisualRunner` to run the baseline.

## Project layout

```
src/com/aim/project/ssp/
  SightseeingProblemDomain.java   problem domain: solution memory, heuristic dispatch, instance loading
  SSPObjectiveFunction.java       tour cost, hotel -> locations -> airport
  heuristics/                     the seven low-level heuristics
  hyperheuristics/                LearningHH and the SR_IE baseline
  instance/, solution/            instance data, file reader and solution representation
  runners/, visualiser/           entry points and the route viewer
instances/ssp/                    problem instances
```

## What I wrote and what was provided

The project started from a template by Warren G. Jackson at the University of Nottingham. The template supplied the interfaces, class skeletons with method stubs, the runners, the visualiser, the instance files and the HyFlex library.

I implemented the seven low-level heuristics, the objective function, the problem domain methods, instance reading, the solution representation and its initialisation, and designed and wrote the learning hyper-heuristic.

## Author

Ron Prekopuca

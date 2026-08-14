object SpaceAge {
  private type PlanetAgeFn = Double => Double

  private val EarthYearInSeconds = 31557600.0

  val onEarth: PlanetAgeFn = ageOn(1.0)

  val onVenus: PlanetAgeFn = ageOn(0.61519726)

  val onMercury: PlanetAgeFn = ageOn(0.2408467)

  val onMars: PlanetAgeFn = ageOn(1.8808158)

  val onJupiter: PlanetAgeFn = ageOn(11.862615)

  val onSaturn: PlanetAgeFn = ageOn(29.447498)

  val onUranus: PlanetAgeFn = ageOn(84.016846)

  val onNeptune: PlanetAgeFn = ageOn(164.79132)

  private def ageOn(orbitalPeriod: Double): PlanetAgeFn =
    seconds => seconds / EarthYearInSeconds / orbitalPeriod
}

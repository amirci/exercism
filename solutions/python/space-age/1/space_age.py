"""Calculate ages on each planet in the solar system."""

EARTH_YEAR_IN_SECONDS = 31_557_600
ORBITAL_PERIODS = {
    "mercury": 0.2408467,
    "venus": 0.61519726,
    "earth": 1.0,
    "mars": 1.8808158,
    "jupiter": 11.862615,
    "saturn": 29.447498,
    "uranus": 84.016846,
    "neptune": 164.79132,
}


class SpaceAge:
    """Age converter for planet-specific years."""

    def __init__(self, seconds):
        self.seconds = seconds

    def on_mercury(self):
        """Return the age in Mercury years."""
        return self._on_planet("mercury")

    def on_venus(self):
        """Return the age in Venus years."""
        return self._on_planet("venus")

    def on_earth(self):
        """Return the age in Earth years."""
        return self._on_planet("earth")

    def on_mars(self):
        """Return the age in Mars years."""
        return self._on_planet("mars")

    def on_jupiter(self):
        """Return the age in Jupiter years."""
        return self._on_planet("jupiter")

    def on_saturn(self):
        """Return the age in Saturn years."""
        return self._on_planet("saturn")

    def on_uranus(self):
        """Return the age in Uranus years."""
        return self._on_planet("uranus")

    def on_neptune(self):
        """Return the age in Neptune years."""
        return self._on_planet("neptune")

    def _on_planet(self, planet):
        return round(
            self.seconds / EARTH_YEAR_IN_SECONDS / ORBITAL_PERIODS[planet],
            2,
        )

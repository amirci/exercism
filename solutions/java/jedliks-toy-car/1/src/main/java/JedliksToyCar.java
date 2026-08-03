public class JedliksToyCar {
    private PowerState state = new Charged();

    public static JedliksToyCar buy() {
        return new JedliksToyCar();
    }

    public String distanceDisplay() {
        return "Driven " + state.meters() + " meters";
    }

    public String batteryDisplay() {
        return state.batteryDisplay();
    }

    public void drive() {
        state = state.advance();
    }

    private interface PowerState {
        int meters();

        PowerState advance();

        String batteryDisplay();
    }

    private record Empty(int meters) implements PowerState {
        public PowerState advance() {
            return this;
        }

        public String batteryDisplay() {
            return "Battery empty";
        }
    }

    private static class Charged implements PowerState {
        private int charge = 100;
        private int meters;

        public int meters() {
            return meters;
        }

        public PowerState advance() {
            meters += 20;
            charge--;

            return charge == 0 ? new Empty(meters) : this;
        }

        public String batteryDisplay() {
            return "Battery at " + charge + "%";
        }
    }
}

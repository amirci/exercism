import java.util.Map;
import java.util.HashMap;
import java.util.function.Predicate;

public class DialingCodes {
    private final Map<Integer, String> codes = new HashMap<>();

    public Map<Integer, String> getCodes() {
        return Map.copyOf(codes);
    }

    public void setDialingCode(Integer code, String country) {
        codes.put(code, country);
    }

    public String getCountry(Integer code) {
        return codes.get(code);
    }

    public void addNewDialingCode(Integer code, String country) {
        if (isCodeAndCountryAvailable(code, country)) {
            codes.put(code, country);
        }
    }

    public Integer findDialingCode(String country) {
        return codes.entrySet()
                .stream()
                .filter(sameCountryAs(country))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void updateCountryDialingCode(Integer code, String country) {
        if (!removeCountryDialingCode(country)) {
            return;
        }

        codes.put(code, country);
    }

    private boolean removeCountryDialingCode(String country) {
        Integer currentCode = findDialingCode(country);
        if (currentCode == null) {
            return false;
        }

        codes.remove(currentCode);
        return true;
    }

    private Predicate<Map.Entry<Integer, String>> sameCountryAs(String country) {
        return entry -> entry.getValue().equals(country);
    }

    private boolean isCodeAndCountryAvailable(Integer code, String country) {
        return !codes.containsKey(code) && !codes.containsValue(country);
    }
}

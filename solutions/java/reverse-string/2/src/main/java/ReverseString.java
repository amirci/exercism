class ReverseString {

    String reverse(String inputString) {
        var reversed = new StringBuilder(inputString.length());
        for (var index = inputString.length() - 1; index >= 0; index--) {
            reversed.append(inputString.charAt(index));
        }
        return reversed.toString();
    }

}

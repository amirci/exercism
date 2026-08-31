class MicroBlog {
    private static final int MAX_LENGTH = 5;

    public String truncate(String input) {
        int endIndex = input.offsetByCodePoints(0, Math.min(MAX_LENGTH, input.codePointCount(0, input.length())));

        return input.substring(0, endIndex);
    }
}

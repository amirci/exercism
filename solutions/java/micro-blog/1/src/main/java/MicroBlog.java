class MicroBlog {
    private static final int MAX_LENGTH = 5;

    public String truncate(String input) {
        return input.codePoints()
                .limit(MAX_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

class Bob {

    String hey(String input) {
        var message = input.strip();
        if (message.isEmpty()) {
            return "Fine. Be that way!";
        }

        var shouting = message.equals(message.toUpperCase())
                && !message.equals(message.toLowerCase());
        var question = message.endsWith("?");

        if (shouting && question) {
            return "Calm down, I know what I'm doing!";
        }
        if (shouting) {
            return "Whoa, chill out!";
        }
        if (question) {
            return "Sure.";
        }
        return "Whatever.";
    }

}

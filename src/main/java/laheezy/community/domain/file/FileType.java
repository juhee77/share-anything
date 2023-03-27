package laheezy.community.domain.file;

public enum FileType {
    PROFILE(Purposes.PRO_FILE),
    POST(Purposes.POST_FILE),
    CHAT(Purposes.CHAT_FILE),
    COMMENT(Purposes.COMMENT_FILE);

    private String name;

    FileType(String name) {
        this.name = name;
    }

    public static class Purposes {
        public static final String PRO_FILE = "PROFILE";
        public static final String POST_FILE = "POST";
        public static final String CHAT_FILE = "CHAR";
        public static final String COMMENT_FILE = "COMMENT";
    }
}

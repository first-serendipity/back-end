package firstserendipity.server.domain.role;

public enum Role {
    MEMBER(Authority.MEMBER), //사용자 권한

    NAYOUNG(Authority.NAYOUNG); //관리자 권한

    private final String authority;

    Role(String authority) { //생성자
        this.authority = authority;
    }
    public String getAuthority() { //Getter
        return this.authority;
    }

    public static class Authority {
        public static final String MEMBER = "MEMBER";
        public static final String NAYOUNG = "NAYOUNG";
    }
}

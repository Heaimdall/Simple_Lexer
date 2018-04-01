public class StringToken extends Token {
    public final String value;

    protected StringToken(String value, Position starting, Position following) {
        super(DomainTag.STRING, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "STRING " + super.toString() + ": " + value;
    }
}

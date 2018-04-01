/**
 * Created by heimdall on 27.03.18.
 */
public class RegularToken extends Token {
    public final String value;

    protected RegularToken(String value, Position starting, Position following) {
        super(DomainTag.REGULAR_EXP, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "REGULAR_EXP " + super.toString() + ": " + value;
    }
}

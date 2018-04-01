public class Scanner {
    public final String program;

    private Compiler compiler;
    private Position cur;

    public Scanner(String program, Compiler compiler) {
        this.compiler = compiler;
        this.program = program;
        cur = new Position(program);
    }

    public Token nextToken() {
        while (!cur.isEOF()) {
            while (cur.isWhitespace())
                cur = cur.next();
            Token token = null;
            switch (cur.getCode()) {
                case '/':
                    token = readReg(cur);
                    break;
                case '\"':
                    if ((cur.next().getCode() == '\"') && (cur.next().next().getCode() == '\"')) {
                        token = readString(cur);
                        break;
                    }
            }
            if (token == null || token.tag == DomainTag.UNKNOWN) {
                compiler.addMessage(true,cur,"Token: unrecognized token");
                cur = cur.next();
            }
            else {
                cur = token.coords.following;
                return token;
            }
        }
        return new UnknownToken(DomainTag.END_OF_PROGRAM,cur,cur);
    }

    private Token readReg(Position cur) {
        Position p = cur.next();
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (p.isNewLine())
                break;
            else if (p.getCode() == '\\'){
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
                if (p.getCode() == '/'){
                    sb.append(Character.toChars(p.getCode()));
                    p = p.next();
                }
            }
            else if (p.getCode() == '/'){
                return new RegularToken(sb.toString(), cur, p.next());
            }
            else {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
        }
        compiler.addMessage(false,cur,"Regular expression cannot cross the border");
        return new RegularToken(sb.toString(), cur, p.next());
    }

    private Token readString(Position cur) {
        Position p = cur.next().next().next();
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (p.isEOF())
                break;
            else if ((p.getCode() == '\"') && (p.next().getCode() == '\"') && (p.next().next().getCode() == '\"')){
                p = p.next().next();
                return new StringToken(sb.toString(),cur,p.next());
            }
            else {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
        }
        compiler.addMessage(false,cur,"String literal is not closed");
        return new StringToken(sb.toString(),cur,p.next());
    }

}
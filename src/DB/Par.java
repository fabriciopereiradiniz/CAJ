package DB;

public class Par<A, B> {
    private final A first;
    private final B second;

    public Par(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
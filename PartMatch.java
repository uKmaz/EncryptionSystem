public class PartMatch {
    int index;
    int length;

    public PartMatch(int index, int length) {
        this.index = index;
        this.length = length;
    }

    @Override
    public String toString() {
        return "Index: " + index + ", Length: " + length;
    }
}

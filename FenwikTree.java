
class FenwikTree {
    int[] bit;
    int n;

    public FenwikTree(int k) {
        n = k;
        bit = new int[n];
    }

    public void add(int idx, int val) {
        while (idx < n) {
            bit[idx] = Math.max(bit[idx], val);
            idx += (idx & -idx);
        }
    }

    public int get(int idx) {
        int max = 0;
        while (idx > 0) {
            max = Math.max(bit[idx], max);
            idx -= (idx & -idx);
        }
        return max;
    }
}
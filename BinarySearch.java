
public class BinarySearch {

    public int upperBoundBS(int[] arr, int t) {
        int l = 0;
        int r = arr.length;

        while (l < r) {
            int m = l + (r - l) / 2;

            if (arr[m] <= t)
                l = m + 1;
            else
                r = m;
        }
        return r;
    }

    public int lowerBoundBS(int[] arr, int t) {
        int l = 0;
        int r = arr.length;

        while (l < r) {
            int m = l + (r - l) / 2;

            if (arr[m] >= t)
                r = m - 1;
            else
                l = m;
        }
        return l;
    }
}
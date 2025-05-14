    public class SegmetTree {
        class Node {
            Node left, right;
            int val;

            public Node(int v) {
                val = v;
            }
        }

        private Node root = null;
        private int inf = (int) 1e9 + 7;
        private int n;

        public SegmetTree(int[] arr) {
            n = arr.length;
            root = buildTree(0, n - 1, arr);
        }

        private int solve(int left, int right) {
            return Math.min(left, right); // update here
        }

        private Node buildTree(int l, int r, int[] arr) {
            if (l == r)
                return new Node(arr[l]);

            int m = l + (r - l) / 2;
            Node left = buildTree(l, m, arr);
            Node right = buildTree(m + 1, r, arr);

            Node cur = new Node(solve(left.val, right.val));
            cur.left = left;
            cur.right = right;

            return cur;
        }

        int search(int x, int y) {
            return search(x, y, 0, n - 1, root);
        }

        private int search(int x, int y, int l, int r, Node cur) {
            if (r < x || y < l)
                return inf;
            if (x <= l && r <= y)
                return cur.val;

            int m = l + (r - l) / 2;
            int left = search(x, y, l, m, cur.left);
            int right = search(x, y, m + 1, r, cur.right);

            return solve(left, right);
        }
    }

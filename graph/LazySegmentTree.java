
    public class SegmentTree {
        class Node {
            Node left, right;
            int val;
            Integer lazy;

            public Node(int v) {
                val = v;
                lazy = null;
            }
        }

        private Node root = null;
        private int n;

        public SegmetTree(int[] arr) {
            n = arr.length;
            root = buildTree(0, n - 1, arr);
        }

        private int inf = (int) 1e9 + 7;
        private int DEFAULT = 0;

        private int solve(int left, int right) {
            return left + right; // update here
        }

        private int applyLazy(Node cur, int l, int r) {
            return (r - l + 1) * cur.lazy; // update here
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
            propogate(cur, l, r);
            if (r < x || y < l)
                return DEFAULT;
            if (x <= l && r <= y)
                return cur.val;

            int m = l + (r - l) / 2;
            int left = search(x, y, l, m, cur.left);
            int right = search(x, y, m + 1, r, cur.right);

            return solve(left, right);
        }

        void update(int x, int y, int val) {
            update(x, y, 0, n - 1, root, val);
        }

        private void update(int x, int y, int l, int r, Node cur, int val) {
            propogate(cur, l, r);

            if (r < x || y < l)
                return;
            if (x <= l && r <= y) {
                cur.lazy = val;
                propogate(cur, l, r);
                return;
            }

            int m = l + (r - l) / 2;
            update(x, y, l, m, cur.left, val);
            update(x, y, m + 1, r, cur.right, val);

            cur.val = solve(cur.left.val, cur.right.val);
        }

        private void propogate(Node cur, int l, int r) {
            if (cur.lazy != null) {
                cur.val = applyLazy(cur, l, r);

                if (l != r) {
                    cur.left.lazy = cur.lazy;
                    cur.right.lazy = cur.lazy;
                }
                cur.lazy = null;
            }
        }
    }

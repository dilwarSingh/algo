class Solution {
    ArrayList<Integer>[] tree;
    FastLCA lca;
    SegmentTree sg;
    HeavyLight hl;

    // Code start here LC:"Palindromic Path Queries in a Tree"
    public List<Boolean> palindromePath(int n, int[][] edges, String s, String[] queries) {
        tree = new ArrayList[n];
        for (int i = 0; i < n; i++)
            tree[i] = new ArrayList<>();

        for (int[] e : edges) {
            tree[e[0]].add(e[1]);
            tree[e[1]].add(e[0]);
        }

        lca = new FastLCA(n, tree);
        hl = new HeavyLight(n, tree);
        sg = new SegmentTree(n, s);

        for (int i = 0; i < s.length(); i++) {
            sg.add(hl.pos[i], s.charAt(i) - 'a');
        }

        List<Boolean> ans = new ArrayList<>();
        for (String q : queries) {
            String[] arr = q.split(" ");

            if (arr[0].equals("query")) {
                ans.add(find(Integer.parseInt(arr[1]), Integer.parseInt(arr[2])));
            } else {
                int loc = Integer.parseInt(arr[1]);
                char c = arr[2].charAt(0);
                sg.add(hl.pos[loc], c - 'a');
            }
        }

        return ans;
    }

    boolean find(int u, int v) {
        int pt = lca.findLca(u, v);
        int ans = findAns(u, pt) ^ findAns(v, pt) ^ findAns(pt, pt);

        int cnt = 0;
        while (ans != 0) {
            cnt += (ans & 1);
            ans >>= 1;
        }

        return cnt <= 1;
    }

    int findAns(int a, int b) {
        int ans = 0;

        while (hl.head[a] != hl.head[b]) {

            if (lca.depth[hl.head[a]] < lca.depth[hl.head[b]]) {
                int tmp = a;
                a = b;
                b = tmp;
            }

            ans ^= sg.search(hl.pos[hl.head[a]], hl.pos[a]);
            a = hl.parents[hl.head[a]];
        }

        if (lca.depth[a] < lca.depth[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        ans ^= sg.search(hl.pos[b], hl.pos[a]);

        return ans;
    }
    
    class HeavyLight {
        int n;
        int[] parents;
        int[] heavy, head, pos;
        ArrayList<Integer>[] tree;
        int cur_pos = 0;

        public HeavyLight(int n, ArrayList<Integer>[] tree) {
            this.tree = tree;
            this.n = n;
            parents = new int[n];
            heavy = new int[n];
            head = new int[n];
            pos = new int[n];
            Arrays.fill(heavy, -1);

            dfs(0, 0);
            decompose(0, 0);
        }

        void decompose(int cur, int h) {
            head[cur] = h;
            pos[cur] = cur_pos++;

            if (heavy[cur] != -1) {
                decompose(heavy[cur], h);
            }

            for (int nx : tree[cur]) {
                if (nx != parents[cur] && nx != heavy[cur])
                    decompose(nx, nx);
            }
        }

        int dfs(int cur, int pt) {
            int sz = 1;
            int max_sz = 0;
            for (int nx : tree[cur]) {
                if (nx == pt)
                    continue;
                parents[nx] = cur;
                int c_sz = dfs(nx, cur);
                sz += c_sz;
                if (c_sz > max_sz) {
                    max_sz = c_sz;
                    heavy[cur] = nx;
                }
            }
            return sz;
        }
    }

    class SegmentTree {
        Node head;
        int n;
        String str;

        public SegmentTree(int n, String s) {
            this.n = n;
            this.str = s;
            head = buildTree(0, n - 1);
        }

        Node buildTree(int s, int e) {
            if (s == e)
                return new Node(1 << (char) (str.charAt(s) - 'a'));

            int m = s + (e - s) / 2;
            Node node = new Node(0);

            node.left = buildTree(s, m);
            node.right = buildTree(m + 1, e);

            node.val = node.left.val ^ node.right.val;
            return node;
        }

        void add(int k, int v) {
            add(0, n - 1, head, k, v);
        }

        Node add(int s, int e, Node cur, int key, int val) {
            if (e < key || key < s)
                return cur;
            if (s == e && s == key) {
                cur.val = (1 << val);
                return cur;
            }

            int m = s + (e - s) / 2;
            add(s, m, cur.left, key, val);
            add(m + 1, e, cur.right, key, val);

            cur.val = cur.left.val ^ cur.right.val;
            return cur;
        }

        int search(int l, int r) {
            return search(0, n - 1, head, l, r);
        }

        int search(int s, int e, Node cur, int l, int r) {
            if (e < l || r < s)
                return 0;
            if (l <= s && e <= r)
                return cur.val;

            int m = s + (e - s) / 2;

            int a = search(s, m, cur.left, l, r);
            int b = search(m + 1, e, cur.right, l, r);

            return a ^ b;
        }

        class Node {
            Node left, right;
            int val;

            public Node(int v) {
                val = v;
            }
        }
    }

    class FastLCA {
        int LOG = 20;
        int[] depth;
        int[][] parents;
        ArrayList<Integer>[] tree;

        public FastLCA(int n, ArrayList<Integer>[] tree) {
            depth = new int[n];
            parents = new int[n][LOG];
            this.tree = tree;

            for (int[] p : parents)
                Arrays.fill(p, -1);

            findDepth(0, -1);
        }

        void findDepth(int cur, int pt) {
            if (pt != -1)
                depth[cur] = depth[pt] + 1;
            parents[cur][0] = pt;

            for (int l = 1; l < LOG; l++) {
                if (parents[cur][l - 1] != -1) {
                    parents[cur][l] = parents[parents[cur][l - 1]][l - 1];
                }
            }

            for (int nx : tree[cur])
                if (nx != pt)
                    findDepth(nx, cur);
        }

        int findLca(int u, int v) {
            int du = depth[u];
            int dv = depth[v];

            if (dv > du)
                return findLca(v, u);

            for (int l = LOG - 1; l >= 0; l--) {
                if (parents[u][l] != -1 && depth[parents[u][l]] >= depth[v])
                    u = parents[u][l];
            }

            if (u == v)
                return u;

            for (int l = LOG - 1; l >= 0; l--) {
                if (parents[u][l] != -1 && parents[u][l] != parents[v][l]) {
                    u = parents[u][l];
                    v = parents[v][l];
                }
            }
            return parents[u][0];
        }
    }
}

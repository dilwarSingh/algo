class FastLCA {
    ArrayList<Integer>[] tree;
    int LOG = 20;

    public void master(int[][] edges) {
        int n = edges.length + 1;
        tree = new ArrayList[n];
        for (int i = 0; i < n; i++)
            tree[i] = new ArrayList<>();

        for (int[] e : edges) {
            tree[e[0] - 1].add(e[1] - 1);
            tree[e[1] - 1].add(e[0] - 1);
        }

        int[] depths = new int[n];
        int[][] parents = new int[n][LOG];
        for (int[] p : parents)
            Arrays.fill(p, -1);

        findDepth(0, -1, depths, parents);

        // findLCA like
        int lcaNode = findLca(3, 8, depths, parents);
    }

    void findDepth(int cur, int pt, int[] depth, int[][] parents) {
        if (pt != -1) depth[cur] = depth[pt] + 1;
        parents[cur][0] = pt;

        for (int l = 1; l < LOG; l++) {
            if (parents[cur][l - 1] != -1) {
                parents[cur][l] = parents[parents[cur][l - 1]][l - 1];
            }
        }

        for (int nx : tree[cur])
            if (nx != pt)
                findDepth(nx, cur, depth, parents);
    }

    int findLca(int u, int v, int[] depth, int[][] parents) {
        int du = depth[u];
        int dv = depth[v];

        if (dv > du)
            return findLca(v, u, depth, parents);

        for (int l = LOG - 1; l >= 0; l--) {
            if (parents[u][l] != -1 && depth[parents[u][l]] >= depth[v])
                u = parents[u][l];
        }

        if (u == v) return u;

        for (int l = LOG - 1; l >= 0; l--) {
            if (parents[u][l] != -1 && parents[u][l] != parents[v][l]) {
                u = parents[u][l];
                v = parents[v][l];
            }
        }
        return parents[u][0];
    }
}

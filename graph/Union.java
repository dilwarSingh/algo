package graph;

/**
 * Union
 */
public class Union {

    private int[] p, s;
    int comps;

    public Union(int n) {
        comps = n;
        p = new int[n];
        s = new int[n];
        
        for(int i=0; i<n; i++) {
            p[i] = i;
            s[i] = 1;
        }
    }

    public int find(int u) {
        if(u == p[u]) return u;
        return p[u] = find(p[u]);
    }

    public boolean unite(int u, int v) {
        int pu = find(u);
        int pv = find(v);
    
        if(pu == pv) return false;
        
        if(s[pu] > s[pv]) {
            s[pu] += s[pv];
            p[pv] = pu;
        } else {
            s[pv] += pu;
            p[pu] = pv; 
        }
        comps--;
        return true;    
    }
}
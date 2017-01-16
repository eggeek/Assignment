#include <stdexcept>
#include <iostream>
#include <sstream>
#include <fstream>
#include <cassert>
#include <cstring>
#include <cstdarg>
#include <cstdio>
#include <random>
#include <cmath>
#include <ctime>
#include <functional>
#include <algorithm>
#include <complex>
#include <numeric>
#include <limits>
#include <bitset>
#include <vector>
#include <string>
#include <queue>
#include <deque>
#include <array>
#include <list>
#include <map>
#include <set>
using namespace std;
#define ALL(a) (a).begin(), (a).end()
#define SZ(a) int((a).size())
#define MP(x, y) make_pair((x),(y))
#define FI first
#define SE second
#define LOWB(x) (x & (-x))
#define UNIQUE(a) sort(ALL(a)), (a).erase(unique(ALL(a)), (a).end())
#define HEIGHT(n) (sizeof(int) * 8 - __builtin_clz(n)) //height of range n segment tree
typedef long long llong;
typedef pair<int, int> pii;
typedef vector<int> vi;
template<class T> inline T min(T a, T b, T c) {return min(min(a,b),c);}
template<class T> inline T min(T a, T b, T c, T d) {return min(min(a,b),min(c,d));}
template<class T> inline T max(T a, T b, T c) {return max(max(a,b),c);}
template<class T> inline T max(T a, T b, T c, T d) {return max(max(a,b),max(c,d));}
const int INF = 1e9;
const llong INF_LL = 4e18;
const double pi = acos(-1.0);
int dx[] = {-1, 1, 0, 0};
int dy[] = {0, 0, -1, 1};
/*-----------------------------------*/
#define MAXN 10001
struct item {
    int id;
    llong v, w;
};
vector<item> its;
vector<llong> dp[2];

llong solve(int n, int k) {
    for (int i=0; i<2; i++) dp[i].assign(k+1, 0);
    for (int i=0; i<n; i++) {
        vector<llong>& cur = dp[i % 2];
        vector<llong>& nxt = dp[(i + 1) % 2];
        llong v = its[i].v;
        llong w = its[i].w;
        for (int j=0; j<=k; j++) nxt[j] = cur[j];
        for (int j=0; j<k; j++) if (j + w <= k) {
            nxt[j + w] = max(nxt[j + w], cur[j] + v);
        }
    }
    return dp[n%2][k];
}

int main() {
    int n, k;
    cin >> n >> k;
    for (int i=0; i<n; i++) {
        llong x, y;
        cin >> x >> y;
        its.push_back(item{i, x, y});
    }
    cout << solve(n, k) << endl;
    return 0;
}

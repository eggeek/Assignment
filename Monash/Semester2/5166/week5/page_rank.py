#!/usr/bin/env python
# -*- encoding:utf-8 -*-


def page_rank(g=[], p=1.0, I=[], cnt=3):
    n = len(g)
    out = [0 for i in range(n)]
    for i in range(n):
        for j in range(n):
            out[i] += g[i][j]
    for _ in range(cnt):
        nxt = [0 for i in I]
        for i in range(n):
            for j in range(n):
                if not g[j][i]:
                    continue
                nxt[i] += (1.0 - p) * I[j] / out[j]
            nxt[i] += p
        I = nxt
        print I
    return I


def work():
    g = [[0, 1, 1, 0],
         [0, 0, 1, 0],
         [1, 0, 0, 0],
         [0, 0, 1, 0]]
    n = len(g)
    I = [1 for i in range(n)]
    p = 1 - 0.85
    ans = page_rank(g, p, I, cnt=3)
    print ans

if __name__ == "__main__":
    work()

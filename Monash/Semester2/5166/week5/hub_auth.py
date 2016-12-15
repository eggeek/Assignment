#!/usr/bin/env python
# -*-encoding: utf-8 -*-


def hub_auth(g=[], x=[], y=[], cnt=3):
    n = len(g)
    for _ in range(cnt):
        nxt_x = [0 for i in range(n)]
        nxt_y = [0 for i in range(n)]
        for i in range(n):
            for j in range(n):
                if not g[j][i]:
                    continue
                nxt_x[i] += y[j]
        for j in range(n):
            for i in range(n):
                if not g[j][i]:
                    continue
                nxt_y[j] += x[i]
        x = nxt_x
        y = nxt_y
        print "auth:", x
        print "hub:", y
    return x, y


def work():
    g = [[0, 1, 1, 0, 1],
         [0, 0, 0, 0, 0],
         [0, 0, 0, 0, 0],
         [0, 1, 1, 0, 0],
         [0, 0, 0, 0, 0]]
    n = len(g)
    ans = hub_auth(g, x=[1 for i in range(n)], y=[1 for i in range(n)], cnt=3)
    print ans


if __name__ == "__main__":
    work()

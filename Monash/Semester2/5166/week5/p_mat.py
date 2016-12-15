#!/usr/bin/env python
# -*- encoding: utf-8 -*-


def mult_mat(a=[], b=[]):
    n = len(a)
    ans = [[0 for i in range(n)] for j in range(n)]
    for i in range(n):
        for j in range(n):
            for k in range(n):
                ans[i][j] += a[i][k] * b[k][j]
    return ans


def p_mat(g=[], cnt=5):
    n = len(g)
    I = [[0.0 for i in range(n)] for j in range(n)]
    for i in range(n):
        I[i][i] = 1.0
    while cnt:
        if cnt % 2:
            I = mult_mat(a=I, b=g)
            for i in I:
                print ','.join(str(j) for j in i)
                print 'offset: ', 1.0 - sum(i)
        cnt >>= 1
        g = mult_mat(a=g, b=g)
        print cnt
    return I


def work():
    g = [[0, 0, 0, 1.0],
         [0.5, 0, 0, 0.5],
         [0.5, 0.5, 0, 0],
         [0, 0, 1.0, 0]]
    it = input()
    n = len(g)
    p = p_mat(g, cnt=it)
    for i in p:
        print i
    ans = [sum([1.0 / float(n) * p[j][i] for j in range(n)]) for i in range(n)]
    print ans


if __name__ == "__main__":
    work()

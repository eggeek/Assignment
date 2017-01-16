#!/usr/bin/python
# -*- coding: utf-8 -*-
import time
from collections import namedtuple
Item = namedtuple("Item", ['index', 'value', 'weight'])


class KnapsackSolver:

    best = -1
    taken = set()
    start = 0

    def __init__(self, n=0, k=0, items=[]):
        self.n = n
        self.k = k
        self.items = items
        self.record = [False] * n
        self.it_counter = 0
        self.limit = 50000000
        self.sumw = []
        self.sumv = []

    def preprocess(self):
        self.sumw = [0] * self.n
        self.sumv = [0] * self.n
        self.items.sort(key=lambda it: float(it.value) / float(it.weight), reverse=True)
        self.sumw[0] = self.items[0].weight
        self.sumv[0] = self.items[0].value
        for i in range(1, self.n):
            self.sumw[i] = self.sumw[i-1] + self.items[i].weight
            self.sumv[i] = self.sumv[i-1] + self.items[i].value

    def solve(self):
        if sys.getrecursionlimit() < self.n:
            sys.setrecursionlimit(self.n+1)
        self.preprocess()
        return self.dfs()
        if (self.n * self.k <= 50000000):
            return self.dp(self.n, self.k, self.items)
        else:
            # return self.greedy(self.n, self.k, self.items)
            return self.dfs()

    def update_ans(self, curv=0):
        if curv <= self.best:
            return False
        self.best = curv
        self.taken = set([i for i in xrange(len(self.record)) if self.record[i] is True])
        # print 'iterate times: %d, best: %d, ts: %fs' % (self.it_counter, self.best, time.time() - self.start)
        return True

    def dfs(self):
        self.it_counter = 0
        self.start = time.time()
        best, taken = self.greedy(self.n, self.k, self.items)
        self.best = best
        self.taken = taken
        # print 'best from greedy: %d' % best
        self._dfs(curn=0, curk=0, curv=0)
        return self.best, self.taken

    def bsearch(self, curn, restk):
        l = curn
        r = self.n-1
        best = curn-1
        while l <= r:
            mid = (l + r) >> 1
            range_w = self.sumw[mid]
            if curn:
                range_w -= self.sumw[curn-1]
            if range_w <= restk:
                best = mid
                l = mid + 1
            else:
                r = mid - 1
        return best

    def estimate(self, curn, restk):
        best_idx = self.bsearch(curn, restk)
        range_w = self.sumw[best_idx]
        range_v = self.sumv[best_idx]
        if curn:
            range_w -= self.sumw[curn-1]
            range_v -= self.sumv[curn-1]
        if best_idx + 1 < self.n:
            it = self.items[best_idx + 1]
            range_v += (restk - range_w) * float(it.value) / float(it.weight)
        return range_v

    def _dfs(self, curn=0, curk=0, curv=0):
        self.update_ans(curv)
        if curn == self.n:
            return
        if self.it_counter > self.limit:
            return
        it = self.items[curn]
        restn = self.n - curn
        restk = self.k - curk
        rating = self.estimate(curn, restk)
        if rating + curv < self.best:
            # print 'cut by estimating, it_counter: %d, ts: %fs' % (self.it_counter, time.time() - self.start)
            return
        if restn * restk <= self.limit / 10:
            best, taken = self.dp(restn, restk, self.items[curn:])
            curv += best
            for i in taken:
                self.record[i] = True
            if self.update_ans(curv):
                pass
                # print 'calc sub db, cur best: %d, new best: %d, ts: %fs' % (self.best, curv, time.time() - self.start)
            self.update_ans(curv)
            for i in taken:
                self.record[i] = False
            return
        self.it_counter += 1
        if curk + it.weight <= self.k:
            self.record[it.index] = True
            self._dfs(curn=curn+1, curk=curk+it.weight, curv=curv+it.value)
            self.record[it.index] = False
        self._dfs(curn=curn+1, curk=curk, curv=curv)

    def dp(self, n, k, items):

        d = [[0 for i in range(k + 1)] for i in range(n + 1)]

        counter = 0
        for i in range(n):
            w = items[i].weight
            v = items[i].value
            for j in range(0, k + 1):
                d[i + 1][j] = d[i][j]
                counter += 1
            for j in range(0, k - w + 1):
                d[i + 1][j + w] = max(d[i + 1][j + w], d[i][j] + v)
            for j in range(1, k + 1):
                d[i + 1][j] = max(d[i + 1][j], d[i + 1][j - 1])

        taken = set()
        ans = 0
        for i in reversed(range(n)):
            w = items[i].weight
            v = items[i].value
            if k - w >= 0 and d[i + 1][k] == d[i][k - w] + v:
                k -= w
                ans += v
                taken.add(items[i].index)
        self.it_counter += counter / 2
        return ans, taken

    def greedy(self, n=0, k=0, items=None):
        items.sort(key=lambda it: float(it.value) / float(it.weight), reverse=True)
        C = 0
        V = 0
        taken = set()
        for it in items:
            if C + it.weight <= k:
                V += it.value
                C += it.weight
                taken.add(it.index)
        return V, taken


def solve_it(input_data):
    # Modify this code to run your optimization algorithm

    # parse the input
    lines = input_data.split('\n')

    firstLine = lines[0].split()
    item_count = int(firstLine[0])
    capacity = int(firstLine[1])

    items = []

    for i in range(1, item_count + 1):
        line = lines[i]
        parts = line.split()
        items.append(Item(i - 1, int(parts[0]), int(parts[1])))

    solver = KnapsackSolver(n=item_count, k=capacity, items=items)
    value, taken_set = solver.solve()
    taken = [0] * item_count
    for i in taken_set:
        taken[i] = 1
    # prepare the solution in the specified output format
    output_data = str(value) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, taken))
    return output_data


import sys

if __name__ == '__main__':
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        input_data_file = open(file_location, 'r')
        input_data = ''.join(input_data_file.readlines())
        input_data_file.close()
        print solve_it(input_data)
    else:
        print 'This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/ks_4_0)'

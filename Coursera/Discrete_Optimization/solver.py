#!/usr/bin/python
# -*- coding: utf-8 -*-

from collections import namedtuple
Item = namedtuple("Item", ['index', 'value', 'weight'])


def dp(n, k, items):

    d = [[0 for i in range(k + 1)] for i in range(n + 1)]
    for i in range(n):
        w = items[i].weight
        v = items[i].value
        for j in range(0, k + 1):
            d[i + 1][j] = d[i][j]
        for j in range(0, k - w + 1):
            d[i + 1][j + w] = max(d[i + 1][j + w], d[i][j] + v)
        for j in range(1, k + 1):
            d[i + 1][j] = max(d[i + 1][j], d[i + 1][j - 1])

    taken = [0 for i in range(n)]
    ans = 0
    for i in reversed(range(n)):
        w = items[i].weight
        v = items[i].value
        if k - w >= 0 and d[i + 1][k] == d[i][k - w] + v:
            k -= w
            ans += v
            taken[items[i].index] = 1
    return ans, taken


def greedy(n=0, k=0, items=None):
    items.sort(key=lambda it: float(it.value / float(it.weight)), reverse=True)
    C = 0
    V = 0
    taken = [0 for i in range(n)]
    for it in items:
        if C + it.weight <= k:
            V += it.value
            C += it.weight
            taken[it.index] = 1
        else:
            break
    return V, taken


def knapsack(n=0, k=0, items=None):
    if (n * k <= 50000000):
        return dp(n, k, items)
    else:
        return greedy(n, k, items)


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

    value, taken = knapsack(n=item_count, k=capacity, items=items)
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

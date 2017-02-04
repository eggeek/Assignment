#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
from collections import defaultdict

class Graph:

  dgr = dict()
  eset = set()
  edges = []
  root = 0

  def __init__(self, node_count):
    self.n = node_count
    self.edges = [[] for i in range(self.n)]
    self.dgr = {i: 0 for i in range(self.n)}

  def add_edge(self, u, v):
    self.eset.add((u, v))
    self.edges[u].append(v)
    self.edges[v].append(u)
    self.dgr[u] += 1
    self.dgr[v] += 1

  def initialize(self):
    self.sort()
    for i in range(self.n):
      if (self.dgr[i] > self.dgr[self.root]):
        self.root = i

  def sort(self):
    for i in range(self.n):
      self.edges[i].sort(key=lambda x: self.dgr[x], reverse=True)

class Solver:

  time_limit = 5e6
  counter = 0
  best = None
  vis_counter = 0
  cur = dict()
  constrain = dict()
  edges = None

  def __init__(self, node_count, edges):
    self.edges = edges
    self.g = Graph(node_count)
    for u, v in edges:
      self.g.add_edge(u, v)
    self.g.initialize()
    self.counter = 0
    if sys.getrecursionlimit() < self.time_limit:
      sys.setrecursionlimit(int(self.time_limit)+1)

  def run(self):
    ans = range(0, self.g.n)
    self.best = self.greedy()
    # self.best = self.greedy_priority_queue()
    for a in [-1, 1]:
      for b in [-1, 1]:
        for c in [-1, 1]:
          tmp = self.greedy_priority_queue((a, b, c))
          if len(set(tmp)) < len(set(ans)):
            # print 'update priority_queue'
            ans = tmp
    if (len(set(ans)) <= len(set(self.best))):
      # print 'priority_queue better'
      self.best = ans
    return self.best

  def greedy(self):
    self.cur = dict()
    idxes = [i for i in range(self.g.n)]
    idxes.sort(key=lambda x: self.g.dgr[i], reverse=True)
    self.constrain = {i: set() for i in range(self.g.n)}
    for i in idxes:
      if self.cur.get(i) is None:
        self.dfs_greedy(i)
    ans = [self.cur[i] for i in range(self.g.n)]
    return ans

  def greedy_priority_queue(self, config):
    u, v, w = config
    self.constrain = {i: set() for i in range(self.g.n)}
    self.cur = dict()
    import heapq
    q = []
    for i in range(self.g.n)[::-1]:
      heapq.heappush(q, (u * len(self.constrain[i]), v * self.g.dgr[i], w * i))
    while q:
      l, dgr, item = heapq.heappop(q)
      l /= u
      dgr /= v
      item /= w
      # print 'item: %d, l: %d, dgr: %d' % (item, l, self.g.dgr[item])
      if self.cur.get(item) is not None:
        continue
      if len(self.constrain[item]) != l:
        continue
      c = 0
      for i in self.constrain[item]:
        if c == i:
          c += 1
      self.cur[item] = c
      # print 'cur[%d]=%d' % (item, c)
      for i in self.g.edges[item]:
        self.constrain[i].add(c)
        if self.cur.get(i, None) is None:
          heapq.heappush(q, (u * len(self.constrain[i]), v * self.g.dgr[i], w * i))
    ans = [self.cur[i] for i in range(self.g.n)]
    return ans

  def dfs_greedy(self, node):
    self.counter += 1
    if self.counter > self.time_limit:
      return
    c = 0
    s = self.constrain[node]
    for i in s:
      if c == i:
        c += 1
    self.cur[node] = c
    for i in self.g.edges[node]:
      self.constrain[i].add(c)

    self.g.edges[node].sort(key=lambda x: len(self.constrain[x]), reverse=True)
    for i in self.g.edges[node]:
      if self.cur.get(i) is None:
        self.dfs_greedy(i)

  def verify(self, solution):
    for i in range(self.g.n):
      for j in self.g.edges[i]:
        if solution[i] == solution[j]:
          return False
    return True


def solve_it(input_data):
    # Modify this code to run your optimization algorithm

    # parse the input
    lines = input_data.split('\n')

    first_line = lines[0].split()
    node_count = int(first_line[0])
    edge_count = int(first_line[1])

    edges = []
    for i in range(1, edge_count + 1):
        line = lines[i]
        parts = line.split()
        edges.append((int(parts[0]), int(parts[1])))

    # build a trivial solution
    # every node has its own color
    solver = Solver(node_count, edges)
    solution = solver.run()
    # print solution[1], solution[5]
    # print solution[1], solution[24]
    # print solution[2], solution[15]
    for u, v in edges:
      assert(solution[u] != solution[v])
    assert(solver.verify(solution))

    # prepare the solution in the specified output format
    output_data = str(len(set(solution))) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, solution))

    return output_data

if __name__ == '__main__':
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/gc_4_1)')

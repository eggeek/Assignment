#!/usr/bin/env python
import math
import random


class point:

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def dist(self, p):
        dx = self.x - p.x
        dy = self.y - p.y
        return math.sqrt(dx ** 2 + dy ** 2)

    @classmethod
    def means(cls, ps=[]):
        x = sum([i.x for i in ps]) / float(len(ps))
        y = sum([i.y for i in ps]) / float(len(ps))
        return point(x, y)


class kmean:

    ps = {}
    classify = {}
    cs = {}
    flag = False

    def __init__(self, ps=[], cs=[]):
        self.ps = {i: ps[i] for i in range(len(ps))}
        self.classify = {i: 0 for i in range(len(ps))}
        self.cs = {i: cs[i] for i in range(len(cs))}
        self.reassign()

    def reassign(self, report=True):
        self.flag = False
        tmp = self.classify.items()
        for pid, pc in tmp:
            a = self.ps[pid].dist(self.cs[pc])
            for cid, c in self.cs.items():
                b = self.ps[pid].dist(c)
                if cid != pc and a > b:
                    print "p%d class change from %d(%lf) to %d(%lf)" % (pid, pc, a, cid, b)
                    self.classify[pid] = cid
                    self.flag = True
        if report:
            self.report_classify()

    def recalc(self, report=True):
        for cid in self.cs.keys():
            tmp = [self.ps[pid] for pid, pc in self.classify.items() if pc == cid]
            self.cs[cid] = point.means(tmp) if (len(tmp)) else ""
        if report:
            self.report_cs()

    def report_classify(self):
        for pid in self.classify.keys():
            for cid in self.cs.keys():
                print "dist(p%d, c%d): %.4lf" % (pid, cid, self.ps[pid].dist(self.cs[cid]))

        for pid, cid in self.classify.items():
            print "%d belong to class: %d" % (pid, cid)

    def report_cs(self):
        for cid, c in self.cs.items():
            print "cid:%d at (%.3lf, %.3lf)" % (cid, c.x, c.y)


def gen(is_random=False):
    ps = [point(1, 1.5),
          point(1, 4.5),
          point(2, 1.5),
          point(2, 3.5),
          point(3, 2.5),
          point(5, 6)
          ]
    if not is_random:
        cs = [point(1.25, 1.25), point(1.75, 4.75)]
    else:
        cs = [point(random.random() * 5, random.random() * 5) for i in range(2)]
    k = kmean(ps, cs)
    return k

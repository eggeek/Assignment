#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import argparse
from collections import defaultdict

stops = ['a', 'to', 'of', 'are', 'the', 'an', 'be', 'will', 'their',
         'and', 'it', 'in', 'for', 'on', 'as', 'with', 'this']
db = defaultdict(list)
docs = []


def tokenize(filename):
    lines = []
    with open(filename, 'r') as f:
        for i in f.readlines():
            lines.append(i)
    raw = ' '.join(lines)
    ws = [i.strip('.\n, ').lower() for i in raw.split(' ')]
    return [i for i in ws if i not in stops]


def normalize(documents=[]):
    # no normalize rule
    words = []
    for i in documents:
        for j in i:
            words.append(j)
    return dict({w: w for w in words})


def query(ts=[]):
    res = set([1, 2, 3])
    for t in ts:
        tmp = set(db[t])
        res.intersection_update(tmp)
    return res


def init_db(files):
    for f in files:
        docs.append(tokenize(f))
    word_mapping = normalize(docs)
    for w in word_mapping.keys():
        for i in range(len(docs)):
            db[w].append(sum([1 for x in docs[i] if x == w]))
    for k, v in db.items():
        print '|%s|' % k, v

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("documents", nargs='+')
    args = parser.parse_args()
    files = args.documents
    init_db(files)

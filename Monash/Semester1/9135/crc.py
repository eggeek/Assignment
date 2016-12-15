#!/usr/bin/env python
# -*- encoding: utf-8 -*-


def crc(s, g):
    s = s.lstrip('0')
    if len(s) < len(g):
        return s
    s = list(s)
    for i in range(len(g)):
        s[i] = str(int(s[i]) ^ int(g[i]))
    s = ''.join(s).lstrip('0')
    return crc(s, g)


if __name__ == "__main__":
    s = raw_input("input msg: ")
    g = raw_input("poly: ")
    for i in range(len(g) - 1):
        s += "0"
    print crc(s, g)

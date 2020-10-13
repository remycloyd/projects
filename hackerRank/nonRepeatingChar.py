def nonRepeat(string1):
    singles = []
    overflow = []
    for i in string1:
        if i not in singles:
            singles.append(i)
        else:
            overflow.append(i)
            singles.remove(i)
        for i in overflow:
            if i in singles:
                singles.remove(i)
    print(singles[0])
    if len(singles) == 0:
        return None
    return singles[0]


testString = "abcab"
nonRepeat(testString)

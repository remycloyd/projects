def oneAwayStrings(s1, s2):
    editCount = 0
    a = len(s1)              # length of passed strings, for comparisons
    b = len(s2)
    if s1 == s2:              # if they're identical strings, stop
        print("TRUE")
        return True
    if abs(a-b) > 1:        # if one is two chars larger, then edit distance is at least 2, stop
        print("FALSE")
        return False
    x = 0
    y = 0                 # iterators for while loop, each bound to corresponding str length
    while x < a and y < b:   # while in bounds of lists iterating through
        if s1[x] != s2[y]:   # if mismatch, increase count
            editCount += 1
            if editCount > 1:  # If count becomes more than 1, Stop
                print("FALSE")
                return False
            if a > b:           # if a larger then mismatch cure is delete from a
                x += 1          # skip current index to simulate deletion
            elif b > a:         # ^^^^^^^^
                y += 1          # ^^^^^^^^
            elif b == a:        # if same length, simulate a character change
                x += 1
                y += 1
        if s1[x] == s2[y]:      # if no mismatch move to next char in both strings
            x += 1
            y += 1
    print(editCount)
    return True


# driver
st2 = "jeremy"
st1 = "jerome"
oneAwayStrings(st1, st2)

def rotate(given_array, n):
    flipped = list(zip(*given_array[::-1]))
    rotated = list(zip(*given_array))[::-1]
    print(rotated)
    print(flipped)
    return flipped

arr = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
rotate(arr, 3)


    # [::-1] - makes a shallow copy of the original list in reverse order. Could also use reversed()
    # which would produce a reverse iterator over the list rather than actually copying the list (more memory efficient).
    # * - makes each sublist in the original list a separate argument to zip() (i.e., unpacks the list)
    # zip() - takes one item from each argument and makes a list (well, a tuple) from those, and repeats until all the sublists are exhausted. This is where the transposition actually happens.
    # list() converts the output of zip() to a list.

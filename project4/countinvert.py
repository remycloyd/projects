# Team 21: Jeremy Cloyd and Sam locicero
# using Starter Code for Counting Inversions
# CS 2123 The University of Tulsa
# Oct 24, 2018


def mergeandcount(left, right):
    """
    Glue procedure to count inversions between left and rgt.
    Input: two ordered sequences left and rgt
    Output: tuple (number inversions, sorted combined sequence)
    """
    inver = 0                     # counter of inversions initialized to zero
    sequence = []                 # list to hold ordered sequence
    left.reverse()                # reverse method for left list
    right.reverse()               # reverse method for right list
    while left and right:         # while both lists are nonempty
        nLt = left.pop()          # elements pointed to by current pointer
        nRt = right.pop()         # elements pointed to by current pointer
        if nLt <= nRt:            # if left value is not larger
            sequence.append(nLt)  # append to output list sequence
            right.append(nRt)     # append nRt to the right list
        else:                     # otherwise
            sequence.append(nRt)  # append to output sequence
            left.append(nLt)      # append nLt to the left list
            inver += len(left)    # increment inversion counter by num of elements in left list
            print(str(nRt) +
                  " conflicts with " + str(left))  # print conflicts between left value and all larger values
    if left:                                       # in case of elements only in left
        left.reverse()
        sequence = sequence + left                 # ordered sequence from sequence plus corrected left list
    else:                                          # in case of elements only in right
        right.reverse()
        sequence = sequence + right                # ordered sequence from sequence plus corrected left list
    return inver, sequence                         # spits out number of inversions and sequence


def sortandcount(sequence):
    """
    Divide-conquer-glue method for counting inversions.
    Function should invoke mergeandcount() to complete glue step.
    Input: ordered sequence
    Output: tuple (number inversions, sequence)
    """
    if len(sequence) == 1:
        return 0, sequence                  # if the list has only 1 element then there are no inversions
    mid = int(len(sequence) / 2)            # split list in half with mid point as the halfway mark
    (I1, A) = sortandcount(sequence[:mid])  # sort and count left half from midpoint contained in A
    (I2, B) = sortandcount(sequence[mid:])  # sort and count right half from midpoint contained in B
    (I, sequence) = mergeandcount(A, B)     # merge and count both halves to glue
    I = I1 + I2 + I                         # Inputs together
    return I, sequence                      # return them and sorted list sequence


if __name__ == "__main__":
    sequence1 = [7, 10, 18, 3, 14, 17, 23, 2, 11, 16]
    sequence2 = [2, 1, 3, 6, 7, 8, 5, 4, 9, 10]
    sequence3 = [1, 3, 2, 6, 4, 5, 7, 10, 8, 9]
    songs1 = [(1, "Stevie Ray Vaughan: Couldn't Stand the Weather"),
              (2, "Jimi Hendrix: Voodoo Chile"),
              (3, "The Lumineers: Ho Hey"),
              (4, "Adele: Chasing Pavements"),
              (5, "Cake: I Will Survive"),
              (6, "Aretha Franklin: I Will Survive"),
              (7, "Beyonce: All the Single Ladies"),
              (8, "Coldplay: Clocks"),
              (9, "Nickelback: Gotta be Somebody"),
              (10, "Garth Brooks: Friends in Low Places")]

    songs2 = [(3, "The Lumineers: Ho Hey"),
               (4, "Adele: Chasing Pavements"),
               (2, "Jimi Hendrix: Voodoo Chile"),
               (1, "Stevie Ray Vaughan: Couldn't Stand the Weather"),
               (8, "Coldplay: Clocks"),
               (6, "Aretha Franklin: I Will Survive"),
               (5, "Cake: I Will Survive"),
               (7, "Beyonce: All the Single Ladies"),
               (9, "Nickelback: Gotta be Somebody"),
               (10, "Garth Brooks: Friends in Low Places") ]
    songs3 = [(1, "Stevie Ray Vaughan: Couldn't Stand the Weather"),
               (2, "Jimi Hendrix: Voodoo Chile"),
               (3, "The Lumineers: Ho Hey"),
               (4, "Adele: Chasing Pavements"),
               (6, "Aretha Franklin: I Will Survive"),
               (5, "Cake: I Will Survive"),
               (7, "Beyonce: All the Single Ladies"),
               (8, "Coldplay: Clocks"),
               (10, "Garth Brooks: Friends in Low Places"),
               (9, "Nickelback: Gotta be Somebody")]
    print (sequence1)
    print ("# Inversions: %i\n" % sortandcount(sequence1)[0])
    print (sequence2)
    print ("# Inversions: %i\n" % sortandcount(sequence2)[0])
    print (sequence3)
    print ("# Inversions: %i\n" % sortandcount(sequence3)[0])
    print (songs1)
    print ("# Inversions: %i\n" % sortandcount(songs1)[0])
    print (songs2)
    print ("# Inversions: %i\n" % sortandcount(songs2)[0])
    print (songs3)
    print ("# Inversions: %i\n" % sortandcount(songs3)[0])

# Jeremy Cloyd and Macklin Hitz
# Python implementation of stable matching problem
# Homework 1 Starter Code
# CS 2123 last modified 8/31/16


def gs(men, women, pref):
    """
    Gale-shapley algorithm, modified to exclude unacceptable matches
    Inputs: men (list of men's names)
            women (list of women's names)
            pref (dictionary of preferences mapping names to list of preferred names in sorted order)
            blocked (list of (man,woman) tuples that are unacceptable matches)
    Output: dictionary of stable matches
    """
    rank = {}
    for w in women:
        rank[w] = {}
        i = 1
        for m in pref[w]:
            rank[w][m]=i
            i += 1
    # create a "pointer" to the next woman to propose
    prefptr = {}
    for m in men:
        prefptr[m] = 0

    freemen = set(men)  # initially all men and women are free
    numpartners = len(men)
    S = {}  # build dictionary to store engagements

    # run the algorithm
    while freemen:
        m = freemen.pop()
        # get the highest ranked woman that has not yet been proposed to
        w = pref[m][prefptr[m]]
        prefptr[m] += 1
        if w not in S: S[w] = m
        else:
            mprime = S[w]
            if rank[w][m] < rank[w][mprime]:
                S[w] = m
                freemen.add(mprime)
            else:
                freemen.add(m)
    return S


def gs_block(men, women, pref, blocked):
    # Gale-shapley algorithm, modified to exclude unacceptable matches
    men = ('xavier', 'yancey', 'zeus')

    women = ('amy', 'bertha', 'clare')

    pref = {'xavier': ['amy', 'bertha', 'clare'], 'yancey': ['bertha', 'amy', 'clare'],
            'zeus': ['amy', 'bertha', 'clare'], 'amy': ['yancey', 'xavier', 'zeus'],
            'bertha': ['xavier', 'yancey', 'zeus'], 'clare': ['xavier', 'yancey', 'zeus']}

    rank = {}
    for w in women:
        rank[w] = {}
        i = 1
        for m in pref[w]:
            rank[w][m] = i
            i += 1

    # create a "pointer" to the next woman to propose
    prefptr = {}
    for m in men:
        prefptr[m] = 0

    freemen = set(men)  # initially all men and women are free
    numpartners = len(men)
    S = {}  # build dictionary to store engagements
    # run the algorithm
    while freemen:
        m = freemen.pop()
        w = pref[m][prefptr[m]]
        prefptr[m] += 1

        check_block = (m, w)
        if check_block not in blocked:

            if w not in S:
                S[w] = m
            else:
                mprime = S[w]
                if rank[w][m] < rank[w][mprime]:
                    S[w] = m
                    freemen.add(mprime)
                else:
                    freemen.add(m)
        elif prefptr[m] < (numpartners-1):
            freemen.add(m)

    return S


def gs_tie(men, women, preftie):

    rank = {}
    for w in women:
        rank[w] = {}
        i = 1
        for x in preftie[w]:
            for y in x:
                rank[w][y] = i
            i += 1
    for m in men:
        rank[m] = {}
        i = 1
        for x in preftie[m]:
            for y in x:
                rank[m][y] = i
            i += 1

    # create a "pointer" to the next woman to propose
    prefptr = {}
    for m in men:
        prefptr[m] = 0

    freemen = set(men)  # initially all men and women are free
    numpartners = len(men)
    S = {}  # build dictionary to store engagements

    # run the algorithm
    while freemen:
        m = freemen.pop()
        # get the highest ranked woman that has not yet been proposed to
        w = preftie[m][prefptr[m]].pop()
        if len(preftie[m][prefptr[m]]) == 0:
            prefptr[m] += 1
        if w not in S:
            S[w] = m
        else:
            mprime = S[w]
            if rank[w][m] < rank[w][mprime]:
                S[w] = m
                freemen.add(mprime)
            else:
                freemen.add(m)
    return S


if __name__ == "__main__":
    # input data
    themen = ['xavier', 'yancey', 'zeus']
    thewomen = ['amy', 'bertha', 'clare']

    thepref = {'xavier': ['amy', 'bertha', 'clare'],
               'yancey': ['bertha', 'amy', 'clare'],
               'zeus': ['amy', 'bertha', 'clare'],
               'amy': ['yancey', 'xavier', 'zeus'],
               'bertha': ['xavier', 'yancey','zeus'],
               'clare': ['xavier', 'yancey', 'zeus']}

    thepreftie = {'xavier': [{'bertha'}, {'amy'}, {'clare'}],
                  'yancey': [{'bertha', 'amy'}, {'clare'}],
                  'zeus': [{'amy'}, {'bertha', 'clare'}],
                  'amy': [{'yancey', 'xavier', 'zeus'}],
                  'bertha': [{'zeus'}, {'xavier'}, {'yancey'}],
                  'clare': [{'xavier', 'yancey'}, {'zeus'}]}

    blocked = {('xavier', 'clare'), ('zeus', 'amy'), ('zeus', 'clare')}

    match = gs(themen, thewomen, thepref)
    print(match)

    match_block = gs_block(themen, thewomen, thepref, blocked)
    print(match_block)

    match_tie = gs_tie(themen, thewomen, thepreftie)
    print(match_tie)

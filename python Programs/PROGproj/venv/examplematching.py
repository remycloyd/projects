import collections
import itertools

# The women that the men prefer
preferred_rankings_men = \
    {'ryan': [ 'lizzy', 'sarah', 'zoey', 'daniella' ],
     'josh': [ 'sarah', 'lizzy', 'daniella', 'zoey' ],
     'blake': [ 'sarah', 'daniella', 'zoey', 'lizzy' ],
     'connor': [ 'lizzy', 'sarah', 'zoey', 'daniella' ]
                          }

# The men that the women prefer
preferred_rankings_women = {
    'lizzy': ['ryan', 'blake', 'josh', 'connor'],
    'sarah': ['ryan', 'blake', 'connor', 'josh'],
    'zoey': ['connor', 'josh', 'ryan', 'blake'],
    'daniella': ['ryan', 'josh', 'connor', 'blake']
}

tentative_engagements = []  # Keep track of the people that "may" end up together

free_men = []  # Men who still need to propose and get accepted successfully


def init_free_men():
    # Initialize the arrays of women and men to represent that they're all initially free and not engaged
    for man in preferred_rankings_men.keys():
        free_men.append(man)


def begin_matching(man):
    # Find the first free woman available to a man at any given time

    print("DEALING WITH %s" % man)
    for woman in preferred_rankings_men[man]:

        # Boolean for whether woman is taken or not
        taken_match = [couple for couple in tentative_engagements if woman in couple]

        if len(taken_match) is 0:
            tentative_engagements.append([man, woman])
            free_men.remove(man)
            print('%s is no longer a free man and is now tentatively engaged to %s' % (man, woman))

        elif len(taken_match) > 0:
            print('%s is taken already..' % woman)
            current_guy = preferred_rankings_women[woman].index(taken_match[ 0 ][ 0 ])  # measure current dude vs prospect
            potential_guy = preferred_rankings_women[woman].index(man)

            if current_guy < potential_guy:
                print('She is satisfied with %s..' % (taken_match[ 0 ][ 0 ]))
            else:
                print('%s is better than %s' % (man, taken_match[ 0 ][ 0 ]))
                print('Making %s free again.. and tentatively engaging %s and %s' % (taken_match[ 0 ][ 0 ], man, woman))

                # The new guy is engaged
                free_men.remove(man)

                # The old guy is now single
                free_men.append(taken_match[ 0 ][ 0 ])

                # Update the fiance of the woman (tentatively)
                taken_match[0][0] = man
                break


def stable_matching():
    while len(free_men) > 0:  # Matching algorithm until stable match terminates
        for man in free_men:
            begin_matching(man)


def main():

    init_free_men()

    print(free_men)

    stable_matching()

    print(tentative_engagements)


main()


























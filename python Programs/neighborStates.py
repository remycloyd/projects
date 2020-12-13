# def toggler(current_State, days):
#     nbrs = len(current_State)
#     next_state = [0] * nbrs
#     for x in range(days):
#         for i in range(nbrs):
#             if i < 1:
#                 if current_State[1] == 0:  # if first entry surrounded by 0's
#                     next_state[0] = 0   # deactivate; become 0
#                 if current_State[1] == 1:
#                     next_state[0] = 1   # activate;   become 1
#             if 0 < i < nbrs-1:                                # if not an edge case
#                 if current_State[i-1] == current_State[i+1]:  # if surrounded by same digit
#                     next_state[i] = 0                         # deactivate; become 0
#                 else:
#                     next_state[i] = 1                         # activate; become 1
#             if i == nbrs-1:                  # if last entry
#                 if current_State[i-1] == 0:  # if surrounded by 0's
#                     next_state[i] = 0   # deactivate; become 0
#                 else:
#                     next_state[i] = 1   # activate;   become 1
#                     # print("Next state:   %s" %next_state)
#         current_State = next_state
#         next_state = [None] * nbrs
#         print(current_State)
#     return current_State
# states = [1, 1, 1, 0, 1, 1, 1, 1]
# print("Initial State: %s\n"%states)
# toggler(states, 2)
def toggler(current_state, days):
    nbrs = len(current_state)
    for day in range(days):
        cells = [0] + current_state + [0]  # make new representation of previous state with sentinel 0's
        print("After adding Sentinel 0's:   %s" %cells)
        current_state = [cells[i - 1] ^ cells[i + 1]  # current cell as bitwise Xor between previous and next cell
                         for i in range(1, nbrs + 1)]  # for each cell starting with second and ending at original last cell
    return current_state
initial_state = [1, 1, 1, 0, 1, 1, 1, 1]
print("initial state:                  %s" %initial_state)
print("after 1 day(s), final state:    %s" %toggler(initial_state, 1))

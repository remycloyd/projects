import math

def round_half_up(n):
    multiplier = 10 ** -1
    return math.floor(n * multiplier + 0.5) / multiplier

print(round_half_up(85))

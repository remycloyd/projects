import math
# def round_half_up(n):
#     multiplier = 10 ** -1
#     return math.floor(n * multiplier + 0.5) / multiplier
# print(round_half_up(85))

def bilateral(x):
    biDis = 0
    abl = 100 -biDis
    for i in range(len(x)):
        biDis = biDis + ((abl-biDis) * x[i]*.01)
    biDis = biDis *1.1
    print(biDis)

bilateral(x =[50, 60])
# # program which finds the smallest radius circumscribing a set of given points
import math
# Input = [(1, 1), (-1, -1), (1, -1)]
# We need a circle of radius at least 2 to include the points.
# Input = [(1, 1), (0, 1), (1, -1)]
dis = 50
singleRatings = {100: 3106.04, 90: 1862.96, 80: 1657.8, 70: 1426.17, 60: 1131.68,
                   50: 893.43, 40: 627.61, 30:435.69,20:281.27,10:142.29}
for i in singleRatings:
    while dis == i:
        print(i)
        i+=1

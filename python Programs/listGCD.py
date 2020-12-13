#write an algorithm that determines the gcd of N positive integers, int output
# from functools import reduce
# from math import gcd
# def generalizedGCD(num, arr):
#     # ans = reduce(gcd, arr)
#     x = gcd(arr[0],arr[1])
#     if len(arr)<3:
#         return x
#     ans = 1
#     for item in range(2, num):
#         ans = gcd(x, arr[item])
#         x = gcd(ans,arr[item])
#     return ans
def gcf(num, arr):
    factors = dict()
    cmax = 1
    for item in reversed(arr):  # for each number being considered *reversed
        for fac in range(item + 1, 1, -1): # for all possible factors of that number *reversed
            if item % fac == 0:  # if the potential factor divides that number evenly
                print("for %d, %d is a factor"%(item,fac))
                factors[fac] = factors.get(fac, 1) + 1  # increment that factor in the list of discovered factors
    for (value, total) in factors.items():
        if value > cmax and total == len(arr)+1:        # if factor is a gcd candidate and greater than current candidate
            cmax = value          # replace current candidate
    return cmax

print("\n%d is the greatest common factor. " %gcf(4, [24, 60, 48, 12]))

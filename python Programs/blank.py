# string1 = "Jeremy"
# for i in range(len(string1)):
#     string1 = string1[i:] + string1[:i]
#     print(string1)
import math
# for i in range(len(arr)-1):  # linear search.
#     if arr[i] > 0:
#         if arr[i-1] == 0:
#             print("Found 0 at index: "+ str(i))
#         print("NEG/POS Transition occurs between indices %d and %d"%(i-1, i))
#         break
def binarySearch(array):
    if len(array) <= 1:
        if array[-1] < 0:
            print("Homogeneously negative array detected, terminating")
            exit()
        try:
            return array[0], 0
        except IndexError:
            print("Oops! Empty array")
            exit()
    if array[-1] < 0:
            print("Homogeneously negative array detected, terminating")
            exit()
    L = 0
    R = len(array) - 1
    print(array)
    while L <= R:
        M = math.floor((L + R)/2)
        if array[M] == 0:
            return array[M], M
        elif array[M] < 0:  # if midpoint is negative, no positive #'s exist to left
            if array[M+1] > 0: # DOES NOT WORK FOR ALL NEGATIVE ARRAY, sanity check added
                return array[M],M
            L = M + 1  # adjust leftmost bound to be right after previous midpoint
        elif array[M] > 0:  # if midpoint is positive, first positive # not to right
            if array[M-1] < 0:
                return array[M],M
            R = M - 1  # adjust rightmost bound to be right before previous midpoint
        else:
            return array[M], M
    print("Homogeneously positive array detected")
    return array[0], 0
# arr = [-80, -4, -3, -2, -1, 0, 2, 4, 9, 12, 13, 14, 15, 21, 41, 51, 56]
# arr = [1, 2, 3, 4, 5, 6, 7, 8, 9]
# arr = [-9, -8, -7, -5 , -3]
# arr = [-10, - 3]
# arr = [3]
# arr = [-50]
arr =[]
print("First non-negative value %d occurs at index %d" %(binarySearch(arr)))

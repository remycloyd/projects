# Q: Given an array that repeats, "40, 40, 40, 50, 50, 60, 75, 75, 75, 75"
# you have to count the number of repeats and output something like this: "3, 40, 2, 50, 1, 60, 4, 75"
def itemCounter(arr):
    xdic = {}
    for i in arr:
        if i not in xdic:
            xdic[i] = 1
        else:
            xdic[i] += 1
    print(xdic)
xArr = [40, 40, 40, 50, 50, 60, 75, 75, 75, 75]
itemCounter(xArr)

def recBreak(listy):
    lowCount = highCount = 0
    min = max = listy[0]
    for i in listy:
        if i < min:
            min = i
            lowCount+=1
        elif i > max:
            max = i
            highCount +=1
    return(highCount, lowCount)

points = [10, 5, 20, 20,4,5,2,25,1]
recBreak(points)

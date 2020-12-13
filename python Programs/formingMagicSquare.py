def formingMagicSquare(s):
    missingNos = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    dupes = []
    sum = 0
    for j in s:
        print(j)
        for i in j:
            if i in missingNos:
                # print("removing %d" %i)
                missingNos.remove(i)
                # print("#'s still missing: " + str(missingNos))
            else:
                dupes.append(i)
    print("Replace each of these duplicate items: %s with one of these missing items: %s \n"%(dupes, missingNos))
    for x in sorted(dupes): # for each duplicate number in passed array
        for y in missingNos:  # for each missing number in passed array
            cost = (abs(x - y))  # find the cost of that replacement
            sum += cost
            print("Cost of replacing %d with %d: %d"% (x, y , cost))
            missingNos.remove(y)
            break
        print("adding minimum cost of %d to sum to get total cost %d" %(cost, sum))
        print("Still missing numbers: %s\n"%missingNos)
    return sum
# array = [[4, 8, 2],[4, 5, 7], [6, 1, 6]]
array = [[2, 9, 8], [4, 2, 7], [5, 6, 7]]
# array = [ [ 1, 2, 3 ], [ 1, 2, 3 ], [ 1, 2, 3] ]
print(formingMagicSquare(array))

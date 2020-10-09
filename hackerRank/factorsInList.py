def factorFinder(arr, number):
    newList = []
    for i in sorted(arr):
        if i <= 20:
            newList.append(i)
    for i in newList:
        x = int(number/i)
        if x in newList:
            print(str(x)+" and the number "+str(i)+" are factors of " + str(number))


searchMe = [3, 5, 2, 19, 55, 43, 23, 78, 4, 67, 10, 20, 1]
hunted = int(input("Supply an integer whose factors to hunt for in the array"+"\n"))
factorFinder(searchMe, hunted)

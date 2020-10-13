def isRotation(list1, list2):
    startOfB = 0
    if len(list1) != len(list2):
        print("failed")
        return False
    if list1[0] in list2:
        startOfB += list2.index(list1[0])
    else:
        print("failed")
        return False
    for i in list1:
        if i == list2[startOfB % len(list2)]:
            startOfB += 1
            continue
        else:
            print("failed")
            return False
    print("EUREKA")
    return True


a = [1, 2, 3, 4, 5, 6, 7]
b = [4, 5, 6, 7, 1, 2, 3, 10]
isRotation(a, b)

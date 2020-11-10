def commonFactors(arr1, arr2):
    c = []
    for i in arr1:
        if i in arr2:
            c.append(i)
    print(c)
    return c


a = [1, 3, 4, 6, 7, 9]
b = [1, 2, 4, 5, 9, 10]
commonFactors(a, b)


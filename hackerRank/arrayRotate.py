def rotate(given_array, n):
    flipped = [[0 for x in range(n)] for y in range(n)]
    for x in range(n):  # for each row
        for y in range(n):  # for each column in that row, so each entry in that row
            flipped[x][n-1] = given_array[x][y]
            print(given_array[x][y])
    print(flipped)
    return flipped


arr = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
rotate(arr, 3)

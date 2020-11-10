def mine_sweeper(bombs, num_rows, num_columns):
    w, h = num_rows, num_columns
    field = [[0 for x in range(w)] for y in range(h)]
    for bomb in bombs:
        row_i = bomb[0]
        col_i = bomb[1]
        field[row_i][col_i] = -1
    for i in range((row_i - 1), (row_i + 1)):
        for j in range(col_i - 1, col_i + 1):
            if 0 <= i < num_rows and 0 <= j < num_columns and field[i][j] != -1:
                field[i][j] += 1
    print(field)
    return field


mine_sweeper([[0, 0], [0, 1]], 3, 3)

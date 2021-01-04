from typing import List


def checkPairSumExists(rows: int, cols: int, arry: List[List[int]], sumy: int) -> bool:
    localSet = set()
    for i in range(0, rows):
        for j in range(0, cols):
            localSet.add(arr[i][j])
        if sum - arr[i][j] in localSet:
            return True
        else:
            localSet.add(sum)
    return False

arr =[[1,2,3],[1,2,3]]
sum = 2
# print(checkPairSumExists(rows = 2,cols = 3, arry = arr, sumy = sum))

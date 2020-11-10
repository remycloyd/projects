from collections import Counter
arr = [5, 5, 5, 2, 1, 3, 1, 1, 1, 2, 2, 3, 1, 4, 1, 5]
ary = Counter(arr)
print("the most common entry in the array is " +
      str(Counter.most_common(ary)[0][0]) + ", with an occurrence count of " + str(Counter.most_common(ary)[0][1]))
arry = {}

# def mostFreq(given_array):
#     count = {}
#     max_count = -1
#     max_item = null
#     for i in given_array:
#         if i not in count:
#             count[i] = 1
#         else:
#             count[i] += 1
#         if count[i] > max_count:
#             max_count = count[i]
#             max_item = i
#     print(max_item)
#     return max_item
#
#
# arr = [5, 5, 5, 2, 1, 3, 1, 1, 1, 2, 2, 3, 1, 4, 1, 5]
# mostFreq(arr)

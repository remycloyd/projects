class Solution:
    arr = ["f", "intc", "tsla", "uber", "lyft", "msft", "amzn", "ge", "amd", "vix", "wkhs"]

    def maxLength(self, arr):
        result = [float('-inf')]
        self.unique_char("", arr, 0, result)
        if not result[0] == float('-inf'):
            return result[0]
        return 0

    def unique_char(self, cur, arr, index, result):
        if index == len(arr):  # End of the array
            return
        for index in range(index, len(arr)):  # Iterating from the current word to the end of the array
            if len(set(cur + arr[index])) == len(list(cur + arr[index])):  # If word + next word have unique characters
                result[0] = max(result[0], len(cur + arr[index]))  # Compare the actual length with the previous max
                self.unique_char(cur + arr[index], arr, index + 1, result)  # Make a new call with concatenate words

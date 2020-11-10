from collections import Counter
from typing import List
class Solution:
    
    maxLength = 0

    def maxLen(self, array: List[str]):
        def doDupesExist(CountMe):
            print("entering doDupesExist function")
            count = Counter(CountMe)
            for i in count.values():
                if i > 1:
                    print("dupe detected")
                    return True
                else:
                    print("no dupe detected")
                    return False
        def checkback(index, x):  # index = current position in array being checked, x = concat we have so far.
            self.maxLength = max(len(x), self.maxLength)

            if index == len(array):
                return
            for i in range(index, len(array)):  # for every entry in array
                if not doDupesExist(x + array[i]):  # check for dupes between current concat and current letter. if not:
                    checkback(i + 1, x + array[i])  # run checkback passing next index and next array entry+ next letter

        stocks = ["f", "intc", "tsla", "uber", "lyft", "msft", "amzn", "ge", "amd", "vix", "wkhs"]
        self.maxLength = 0
        checkback(0, "")
        print(self.maxLength)
        return self.maxLength

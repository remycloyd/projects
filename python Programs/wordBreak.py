class Solution(object):
    def wordBreak(self, string, wordDict):
        matrix = [[False for stringItem in range(len(string))] for x in range(len(string))] # create an nxn matrix where n = len(string) pre-populate with Falses
        for stringItem in range(1, len(string)+1):                                  # for every character in the given string:
            for substringItem in range(len(string) - stringItem + 1):               # and for every character in that substring
                print(string[substringItem:substringItem+stringItem])               #
                if string[substringItem:substringItem+stringItem] in wordDict:      # if substring is present in dict,
                    matrix[substringItem][substringItem+stringItem-1] = True        # annotate in matrix at corresp. index
                else:
                    for subSubItem in range(substringItem+1,substringItem+stringItem):
                        if matrix[substringItem][subSubItem-1] and matrix[subSubItem][substringItem+stringItem-1]:
                            matrix[substringItem][substringItem+stringItem-1] = True
        if matrix[0][len(string) - 1]:
            return "string " + str(string)+ " can be divided into dictionary entries"
        else:
            return "String " + str(string)+ " cannot be represented as a combination of dictionary items"

ob1 = Solution()
print(ob1.wordBreak("abc123def456", ["123", "abc","def","456"]))

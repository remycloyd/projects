# this program will determine if any words in a given list are contained within a phone number
# a,b,c = 2
# d,e,f = 3
# g,h,i = 4
# j,k,l = 5
# m,n,o = 6
# p,q,r,s = 7
# t,u,v = 8
# w,x,y,z = 9
def word_finder(number, wordList):
    for i in range(len(wordList)):         # list level
        numlist = ""
        for x in range(len(wordList[i])):  # word level
            if wordList[i][x] == "a" or wordList[i][x] == "b" or wordList[i][x] == "c":
                numlist += "2"

            elif wordList[i][x] == "d" or wordList[i][x] == "e" or wordList[i][x] == "f":
                numlist += "3"

            elif wordList[i][x] == "g" or wordList[i][x] == "h" or wordList[i][x] == "i":
                numlist += "4"

            elif wordList[i][x] == "j" or wordList[i][x] == "k" or wordList[i][x] == "l":
                numlist += '5'

            elif wordList[i][x] == "m" or wordList[i][x] == "n" or wordList[i][x] == "o":
                numlist += "6"

            elif wordList[i][x] == "p" or wordList[i][x] == "q" or wordList[i][x] == "r" or wordList[i][x] == "s":
                numlist += '7'

            elif wordList[i][x] == "t" or wordList[i][x] == "u" or wordList[i][x] == "v":
                numlist += '8'

            elif wordList[i][x] == "w" or wordList[i][x] == "x" or wordList[i][x] == "y" or wordList[i][x] == "z":
                numlist += '9'

        if numlist in p_number:
            print(wordList[i] + " is equivalent to " + numlist + " and appears in the phone number " + number + "\n")
        else:
            print(wordList[i] + " does not appear in phone number " + number + "\n")


print("Please give a phone number to search for words in: \n")
p_number = input()
input_string = input("Enter a list of words separated by space \n ")
words = input_string.split()
print("Hunting for words:", words)
word_finder(p_number, words)

# Split the Number
# CHALLENGE DESCRIPTION:
 # You are given a number N and a pattern. The pattern consists of lowercase latin letters and one operation "+" or "-". 
 # The challenge is to split the number and evaluate it according to this pattern e.g. 
 # 1232 ab+cd -> a:1, b:2, c:3, d:2 -> 12+32 -> 44
 # Each line contains a number and the pattern separated by a single whitespace.
 # INPUT SAMPLE: 3413289830 a-bcdefghij # -413289827 or 776 a+bc # 83 or 12345 a+bcde # 2346 or 1232 ab+cd # 44 or 90602 a+bcde # 611
 # OUTPUT SAMPLE: For each test case print out the result of pattern evaluation. N is in range [100, 1000000000]
stringy = "90602 a+bcde"
spaceIdx = stringy.find(" ")
opIdx = max(stringy.find("+"),stringy.find("-"))
num1 = int(stringy[0:opIdx - spaceIdx - 1])
num2 = int(stringy[opIdx - spaceIdx - 1 : spaceIdx])
if stringy[opIdx] == "-":
    num2 *= -1
print(num1 + num2)

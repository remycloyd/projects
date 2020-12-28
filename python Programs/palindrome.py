s = ""
for i in input():
    if i != " ":            # logic to disregard spaces.
        if i != "'":
            s = s+str.lower(i)  # logic to disregard case sensitivity
a = 0
z = -1
print(s)
for x in range(int(len(s)/2)):  # compare x times; where x is half the length of the input.
    if s[a] == s[z]:
        print("comparing left " + s[a] + " to right " + s[z])
        print("so far so good \n")
        a += 1                  # move inward
        z -= 1                  # move inward
    else:
        print("comparing left " + s[a] + " to right " + s[z])
        print("no palindrome here")
        exit()                  # skip the following success statement and end the program.
print("that was a palindrome")
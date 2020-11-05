s = input()
a = 0
z = -1
for x in range(int(len(s)/2 + 1)):
    if s[a] == s[z]:
        print("comparing " + s[a] + " to " + s[z])
        print("so far so good \n")
        a += 1
        z -= 1
    else:
        print("no palindrome here")
        break
    print("that was a palindrome")

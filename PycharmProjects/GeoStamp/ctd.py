# ask for number of As, and store
#     ask for total credits at grade A, and store
oneCreds = int(input("Please enter the total number of 1 credit courses: "))
oneCredAs = int(input("Please enter the total number one Credit A's: "))
aCreds = 0#dunno

threeCreds = int(input("Please enter the total number of 3 credit Courses: "))
aWeight = 4*aCreds

# ask for number of Bs, and store
#     ask for total credits at grade B and store
bNums = int(input("Please enter the total number of B's: "))
bCreds = int(input("Please enter the total Credits that received c's: "))
bWeight = 3*bCreds

cNums = int(input("Please enter the total number of C's: "))
cCreds = int(input("Please enter the total Credits that received c's: "))
cWeight = 3*cCreds

# sum all individual total credits
totalWeight = aWeight+bWeight+cWeight

totalCreds = aCreds+bCreds+cCreds
print("total major req Credits: ")
print(totalCreds)

GPA = totalWeight/totalCreds
print("Your GPA is : ")
print(GPA)




# # NumDis = int(input("How many disabilities are rated? "))
# percentAble = 100.00
# percentages = list(map(int, input(" Please enter each percentage rating in decreasing order (separate with spaces)")))
# x = 1
# i = 0
# for item in percentages:
#     print("Disability " + str(x) + " = " + str(item) + "%")
#     percentages[i] = (item * 0.01)
#     x += 1
# #
# #
# # for i in percentages:
# #     print("Disability " + str(x) +" = " + str(i*100)+"%")
#
#
# while i < len(percentages):
#     print("current index = " + i)
#     print("percentage at that index = " + percentages[0])
#     percentAble = percentAble - (percentages[i] * percentAble)
#     i += 1
# print(percentAble)

# numbers = [1,2,3,3,4,5,3]
# ourSet = set()
# noCopies =[]
# for i in numbers:
#     if i not in ourSet:
#         ourSet.add(i)
#         noCopies.append(i)
# print (noCopies)



# x = 0
# y = -1
# temp = 0
# for i in range(int(len(numbers)/2)):
#     temp = numbers[x]
#     numbers[x] = numbers[y]
#     numbers[y] = temp
#     y -= 1
#     x +=1

def Fibonacci(n):
    global count
    count += 1
    # if n < 1:
    #     print("bad number, try again")
    # elif n == 1:
    #     return 0
    # elif n == 2:
    #     return 1
    # else:
    #
    #     return (Fibonacci(n-1)+Fibonacci(n-2))

    if n not in fibNums:
        print(str(n) + " not in dictionary, running function on priors...")
        x = Fibonacci(n-1)+Fibonacci(n-2)
        fibNums[n] = x
    return fibNums[n]
fibNums = {1:0, 2:1}
count = 0
# print("Your answer is: "+ str(Fibonacci(int(input("give a value for n \n")))))
# print("the updated count is: " + str(count))
# print(Fibonacci(int(input("which Fibonacci number? \n"))))
print(Fibonacci(int(input("give a value for n \n"))))
print("Fibonacci fcn has been accessed " + str(count) + " times")

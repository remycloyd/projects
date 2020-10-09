import cProfile



N = int(input("how many commands"+"\n"))
def myFunc():
    # lines = []
    if __name__ == '__main__':
        # for i in range(0, N):
        #     tokens = input().split()
        #     if tokens[0] == 'insert':
        #         lines.insert(int(tokens[1]), int(tokens[2]))
        #     elif tokens[0] == 'print':
        #         print(lines)
        #     elif tokens[0] == 'remove':
        #         lines.remove(int(tokens[1]))
        #     elif tokens[0] == 'append':
        #         lines.append(int(tokens[1]))
        #     elif tokens[0] == 'sort':
        #         lines.sort()
        #     elif tokens[0] == 'pop':
        #         lines.pop()
        #     elif tokens[0] == 'reverse':
        #         lines.reverse()
        array = []
        for i in range(0, N):
            command = input().split()

            if str(command[0][2]) == "v":
                array.reverse()

            elif str(command[0][0]) == "i":
                array.insert(int(command[1]), int(command[2]))

            elif str(command[0][1]) == "r":
                print(array)

            elif str(command[0][0]) == "a":
                array.append(int(command[1]))

            elif str(command[0][2]) == "m":
                array.remove(int(command[1]))

            elif str(command[0][0]) == "s":
                array.sort()

            elif str(command[0]) == "pop":
                array.pop()
cProfile.run('myFunc()')

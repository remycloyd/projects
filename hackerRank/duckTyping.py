# Duck Typing and Easier to ask forgiveness than permission (EAFP)
import os
myFile = "/tmp/test.txt"

#race condition
# if os.access(myFile, os.R_OK):
#     with open(myFile) as f:
#         print(f.read())
# else:
#     print('File can not be accessed')

# no race con
try:
    f = open(myFile)
except IOError as e:
    print(" file can't be accessed")
else:
    with f:
        print(f.read())



# myList = [1, 2, 3, 4, 5, 6]
#non pyth
# if len(myList):
#     print(myList[5])
# else:
#     print("Index out of bounds")
# try:
#     print(myList[8])
# except IndexError :
#     print("index seems to not exist")

# person = {'name': 'Jeremy', 'age': 31, 'job': 'Programmer'}
# person = {'name': 'Jeremy', 'age': 31}

# LBYL (Non-Pythonic)
# if 'name' in person and 'age' in person and 'job' in person:
#   print("I'm {name}. I'm {age} years old and I am a {job}".format(**person))
# else:
#   print('there are missing keys')

# EAFP (pythonic)
# try:
#     print("I'm {name}. I'm {age} years old and I am a {job}".format(**person))
# except KeyError as e:
#     print('there are missing keys')


# class Duck:
#     def quack(self):
#         print('Quack, quack')
#
#     def fly(self):
#         print('Flap, Flap!')
#
#
# class Person:
#
#     def quack(self):
#         print("I'm Quacking Like a Duck!")
#
#     def fly(self):
#         print("I'm Flapping my Arms!")
#
#
# def quack_and_fly(thing):
    # pass
    # Not Duck-Typed (Non-Pythonic)
    # if isinstance(thing, Duck):
    # thing.quack()
    # thing.fly()
    # print("")
    # else:
    #     print('This has to be a Duck!')
  # LBYL (Non-Pythonic)
  #   if hasattr(thing, 'quack'):
  #       if callable(thing.quack):
  #           thing.quack()
  #
  #   if hasattr(thing, 'fly'):
  #       if callable(thing.fly):
  #           thing.fly()



# d = Duck()
# quack_and_fly(d)
#
# p = Person()
# quack_and_fly(p)

def catAndMouse(x, y, z):
    if abs(x-z) < abs(y-z):
        print("Cat A")
    elif abs(x-z) > abs(y-z):
        print("Cat B")
    elif abs(x-z) == abs(y-z):
        print("Mouse C")

catAndMouse(2, 5, 4)

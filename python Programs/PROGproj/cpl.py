def main():
    w = 0
    x =0
    z=0

    def A():

        def B():
            x=2
            z=0
            print(x)

        def C ():
            w=4
            a=0
            z=7
            x=4
            D()
            print(w, x)
        x = 3
        z = 5
        B()
        print(x)

    def D():
        x,b = 0

        def E():
            x = 0
            x = 13
            F()
            print(x)

        def F():

            def G():
                print(z)
                x= 7
            print("6")
            G()
            x=8
        x = 6
        w = 19
        E()
        print(x)
    w= 12
    x= 1
    z = 9
    A()
    print(w, x, z)

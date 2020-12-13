def bonAppetit(billy, k, b):
    billy.remove(billy[k])
    sum = 0
    for i in range(len(billy)):
        sum = sum + billy[i]
    if sum/2 == b:
        print("Bon Appetit")
    else:
        print(int(abs(b-sum/2)))



bill = [3, 10 , 2, 9]
n = len(bill)
bb = 7
bonAppetit(bill, 0, bb)

# Function that returns true if the product 
# of even positioned digits is equal to 
# the product of odd positioned digits in num 
def compareProduct(num):
    if num < 10: 
        return False
    oddProdValue = 1; evenProdValue = 1
  
    # Take two consecutive digits at a time
    while num > 0: # First digit
        digit = num % 10
        print("digit = %s"%digit)
        oddProdValue *= digit
        print("odd Product = %s"%oddProdValue)
        num = num//10
        print("num = %s\n"%num)

        if num == 0:  # If num becomes 0 then there's no more digit
            break 
        digit = num % 10
        print("digit = %s"%digit)
        evenProdValue *= digit
        print("Even Product = %s"%evenProdValue)
        num = num//10
        print("num = %s"%num)

    if oddProdValue == evenProdValue: # If the products are equal
        return True
  
    # If the products are not equal 
    return False
print(compareProduct(98))

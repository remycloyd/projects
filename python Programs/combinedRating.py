# Jeremy Cloyd | Veteran's disability Calculator
import math

able = 100
married = input("Are you married?\n").lower()
if married == "no":
    married = False
else:
    married = True
ratings = list(map(int, input("Please enter each rating percentage (separate with spaces) \n").split()))
x = 1
i = 0
for item in ratings:
    print("Disability #" + str(x) + " has reduced your able-ness of "
          + str(able)
          + "% by " + str(ratings[ i ]) +
          "% to " + str(round(able - (ratings[ i ] * 0.01 * able)))
          + "% able, for a overall impact of "
          + str(round(item * 0.01 * able)) + "%\n")
    ratings[ i ] = (ratings[ i ] * 0.01)
    x += 1
    able = able - (ratings[ i ] * able)
    i += 1

disabled = 100 - able
# print("Total ability remaining: " + str(able) + "\n")
print("Disability Percentage: " + str(disabled) + "\n")
print("Disability Rating: " + str(round(100 - able, -1)))
if disabled >= 95:
    print("You have the highest possible rating! 100%")
    if not married:
        print("Monthly rate: $3,106.04 \nYearly rate: $37,272.48")
    else:
        print("Monthly rate: $3,279.22 \nYearly rate: $39,350.64")
    exit()
if 95 > disabled >= 85: # 90%
    if not married:
        print("Monthly rate: $1,862.96 \nYearly rate: $22,355.52")
    else:
        print("Monthly rate: $2,017.96 \nYearly rate: $24,215.52")
if 85 > disabled >= 75: # 80%
    if not married:
        print("Monthly rate: $1,657.80 \nYearly rate: $19,893.60")
    else:
        print("Monthly rate: $1,795.80 \nYearly rate: $21,549.60")
if 75 > disabled >= 65: # 70%
    if not married:
        print("Monthly rate: $1,426.17 \nYearly rate:  $17,114.04")
    else:
        print("Monthly rate: $1,547.17 \nYearly rate: $18,566.04")
if 65 > disabled >= 55: # 60%
    if not married:
        print("Monthly rate: $1,131.68 \nYearly rate: $13,580.16")
    else:
        print("Monthly rate: $1,234.68 \nYearly rate: $14,816.16")
if 55 > disabled >= 45: # 50%
    if not married:
        print("Monthly rate: $893.43 \nYearly rate: $10,721.16")
    else:
        print("Monthly rate: $979.43 \nYearly rate: $11,753.16")
if 45 > disabled >= 35: # 40%
    if not married:
        print("Monthly rate: $627.61 \nYearly rate: $7,531.32")
    else:
        print("Monthly rate: $696.61 \nYearly rate: $8,359.32")
if 35 > disabled >= 25: # 30%
    if not married:
        print("Monthly rate: $435.69 \nYearly rate: $5,228.28")
    else:
        print("Monthly rate: $486.69 \nYearly rate: $5,840.28")
if 25 > disabled >= 15: # 20%
    print("Monthly rate: $281.27 \nYearly rate: $3,375.24")
if 15 > disabled: # 10%
    print("Monthly rate: $142.29 \nYearly rate: $1,707.48")

question = input("Do you: \n A. Want to know how a new rating would impact your percentage? \n "
                 "B. Want to see what rating you need to get to a higher rating? \n C. No, I'm good, thank you. \n").lower()
if question == "a":
    newDis = int(input("What is your projected rating? \n"))
    newRating = int(round(100 - (newDis * 0.01 * able ), -1 ))
    print("With an additional rating of "+ str(newDis) + "%, your disability percentage would be "+ str(newRating)+"%")
    if newRating >= 95:
        print("You'd have the highest possible rating! 100%")
        if not married:
            print("Monthly rate: $3,106.04 \nYearly rate: $37,272.48")
        else:
            print("Monthly rate: $3,279.22 \nYearly rate: $39,350.64")
        exit()
    if 95 > newRating >= 85: # 90%
        if not married:
            print("Monthly rate: $1,862.96 \nYearly rate: $22,355.52")
        else:
            print("Monthly rate: $2,017.96 \nYearly rate: $24,215.52")
    if 85 > newRating >= 75: # 80%
        if not married:
            print("Monthly rate: $1,657.80 \nYearly rate: $19,893.60")
        else:
            print("Monthly rate: $1,795.80 \nYearly rate: $21,549.60")
    if 75 > newRating >= 65: # 70%
        if not married:
            print("Monthly rate: $1,426.17 \nYearly rate:  $17,114.04")
        else:
            print("Monthly rate: $1,547.17 \nYearly rate: $18,566.04")
    if 65 > newRating >= 55: # 60%
        if not married:
            print("Monthly rate: $1,131.68 \nYearly rate: $13,580.16")
        else:
            print("Monthly rate: $1,234.68 \nYearly rate: $14,816.16")
    if 55 > newRating >= 45: # 50%
        if not married:
            print("Monthly rate: $893.43 \nYearly rate: $10,721.16")
        else:
            print("Monthly rate: $979.43 \nYearly rate: $11,753.16")
    if 45 > newRating >= 35: # 40%
        if not married:
            print("Monthly rate: $627.61 \nYearly rate: $7,531.32")
        else:
            print("Monthly rate: $696.61 \nYearly rate: $8,359.32")
    if 35 > newRating >= 25: # 30%
        if not married:
            print("Monthly rate: $435.69 \nYearly rate: $5,228.28")
        else:
            print("Monthly rate: $486.69 \nYearly rate: $5,840.28")
    if 25 > newRating >= 15: # 20%
        print("Monthly rate: $281.27 \nYearly rate: $3,375.24")
    if 15 > newRating: # 10%
        print("Monthly rate: $142.29 \nYearly rate: $1,707.48")

elif question == "b":
    up_rating = (int(input("Please enter a higher rating. \n"))) - 5  # this is the rating we will calculate how to arrive at
    Needed = up_rating - disabled  # the increase in rating needed to achieve the new rating
    ImpactNeeded = round((Needed * 100) / disabled, 3)
    PercNeeded = (up_rating - able)

    print("To obtain a " + str(up_rating + 5) + "% rating you must increase your true rating by " + str(
        round(Needed, 3)) + "%")
    print("You would need another disability rating of at least " +
          str(math.ceil(Needed / (able / 100) / 10) * 10) + "% to achieve that impact")
else:
    exit()

# Jeremy Cloyd | Veteran's disability Calculator
import math
singleRatings = {100: 3106.04, 90: 1862.96, 80: 1657.8, 70: 1426.17, 60: 1131.68,
                   50: 893.43, 40: 627.61, 30:435.69,20:281.27,10:142.29}

marriedRatings = {100:3279.22, 90:2017.96, 80:1795.80, 70:1547.17, 60:1234.68,
                   50:979.43, 40:696.61, 30:486.69, 20:281.27, 10:142.29}
able = 100
ratings = list(map(int, input("Please enter each rating percentage (separate with spaces) \n").split()))
x = 1
i = 0
for item in ratings:
    print("Disability #" + str(x) + " has reduced your able-ness of "
          + str(able)
          + "% by " + str(ratings[ i ]) +
          "% to " + str(round(able - (ratings[ i ] * 0.01 * able)))
          + "% able, for a ability impact of "
          + str(round(item * 0.01 * able)) + "%\n")
    ratings[ i ] = (ratings[ i ] * 0.01)
    x += 1
    able = able - (ratings[ i ] * able)
    i += 1
rawDisabled = 100 - able
calcDis = round(100 - able, -1)
print("Raw Disability Percentage: " + str(rawDisabled))
print("calculated Disability Rating " + str(calcDis)+ "\n")

sYearly = round(singleRatings[calcDis]*12, 2)
mYearly = round(marriedRatings[calcDis]*12,2)
for i in singleRatings:
    while calcDis == i:
        print("Single Monthly rate: "+ '${:,.2f}'.format(singleRatings[i]) + "\nYearly Married Rate: $"+ '${:,.2f}'.format(sYearly) +"\n")
        print("Married Monthly rate: "+ '${:,.2f}'.format(marriedRatings[i]) + "\nYearly Married Rate: $"+ '${:,.2f}'.format(mYearly))
        i+=1
question = input("Do you: \n A. Want to know how a new rating would impact your percentage? \n "
                 "B. Want to see what rating you need to get to a higher rating? \n C. No, I'm good, thank you. \n").lower()
if question == "a":
    newDis = int(input("What is your projected rating? \n"))
    newRating = int(round(rawDisabled + (newDis * 0.01 * able ), -1 ))
    print("With an additional rating of "+ str(newDis) + "%, your disability percentage would be "+ str(newRating)+"%")
    sYearly = round(singleRatings[newRating]*12, 2)
    mYearly = round(marriedRatings[newRating]*12,2)
    for i in singleRatings:
        while newRating == i:
            print("Single Monthly rate: "+ '${:,.2f}'.format(singleRatings[i]) + "\nYearly Married Rate: $"+ '${:,.2f}'.format(sYearly) +"\n")
            print("Married Monthly rate: "+ '${:,.2f}'.format(marriedRatings[i]) + "\nYearly Married Rate: $"+ '${:,.2f}'.format(mYearly))
            i+=1
elif question == "b":
    up_rating = (int(input("Please enter a higher rating. \n"))) - 5  # this is the rating we will calculate how to arrive at
    Needed = up_rating - rawDisabled  # the increase in rating needed to achieve the new rating
    ImpactNeeded = round((Needed * 100) / rawDisabled, 3)
    PercNeeded = (up_rating - able)

    print("To obtain a " + str(up_rating + 5) + "% rating you must increase your raw disability rating by " + str(
        round(Needed, 3)) + "%")
    print("You would need another single disability rating of at least " +
          str(math.ceil(Needed / (able / 100) / 10) * 10) + "% or multiple ratings with an equivalent impact to achieve this new rating")
exit()

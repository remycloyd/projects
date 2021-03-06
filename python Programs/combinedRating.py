# Jeremy Cloyd | Veteran's disability Calculator
import math
singleRatings = {100: 3106.04, 90: 1862.96, 80: 1657.8, 70: 1426.17, 60: 1131.68,
                   50: 893.43, 40: 627.61, 30:435.69,20:281.27,10:142.29}

marriedRatings = {100:3279.22, 90:2017.96, 80:1795.80, 70:1547.17, 60:1234.68,
                   50:979.43, 40:696.61, 30:486.69, 20:281.27, 10:142.29}
rawDisabled = 0
able = 100 - rawDisabled
def bilateral(x):
    biDis = 0
    abl = 100 -biDis
    for i in range(len(x)):
        biDis = biDis + ((abl-biDis) * x[i]*.01)
    biDis = biDis *1.1
    global able 
    able -= biDis
    global rawDisabled
    rawDisabled += biDis

if input("Do you have any bilateral disabilities: y/n?\n").lower() == "y":
    biCon = list(map(int,input("please enter the two ratings seperated by a space \n").split()))
    bilateral(biCon)

ratings = list(map(int, input("Please enter each non biliateral rating percentage (separate with spaces) \n").split()))
j = 1
i = 0
for item in ratings:
    print("Disability # %d has reduced your able-ness of %d percent by %d percent to %d percent able, for a ability impact of %d percent \n" %(j, able, ratings[ i ], round(able - (ratings[ i ] * 0.01 * able)), round(item * 0.01 * able)))
    j += 1
    ratings[i] = (ratings[i] * 0.01) # convert ratings into decimal multipliers
    impact = ratings[i] * able
    rawDisabled = round(rawDisabled + impact)
    able = int(able - round(impact, 0))
    i += 1

calcDis = int(math.floor((rawDisabled*(10 ** -1) + 0.5)) / (10** -1)) # needs to be a multiple of 10 # ---------------problem here, 85 rounding down
# print("Able: %f\nRaw disabled: %f\nCalculated Disability Rating: %s\n" %(able, rawDisabled, calcDis) )
print("Calculated Disability Rate: %d"%calcDis)
sYearly = round(singleRatings[calcDis]*12, 2)
mYearly = round(marriedRatings[calcDis]*12, 2)
for i in singleRatings:
    while calcDis == i:
        print("Single Monthly rate: "+ '${:,.2f}'.format(singleRatings[i]) + "\nYearly Married Rate: "+ '${:,.2f}'.format(sYearly) +"\n")
        print("Married Monthly rate: "+ '${:,.2f}'.format(marriedRatings[i]) + "\nYearly Married Rate: "+ '${:,.2f}'.format(mYearly))
        i+=1
question = input("Do you: \n A. Want to know how a new rating would impact your percentage? \n "
                 "B. Want to see what rating you need to get to a higher rating? \n C. No, I'm good, thank you. \n").lower()
if question == "a":
    newDis = int(input("What is your projected rating? \n"))
    calcDis = int(round(calcDis + (newDis * 0.01 * able ), -1 ))
    print("With an additional rating of "+ str(newDis) + "%, your disability percentage would be "+ str(calcDis)+"%")
    sYearly = round(singleRatings[calcDis]*12, 2)
    mYearly = round(marriedRatings[calcDis]*12,2)
    for i in singleRatings:
        while calcDis == i:
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
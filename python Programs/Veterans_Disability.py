# Jeremy Cloyd | Veteran's disability Calculator
import math

able = 100
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
print("Total ability remaining: " + str(able) + "\n")
print("Disability Percentage: " + str(disabled) + "\n")
print("Disability Rating: " + str(round(100 - able, -1)))

question = input("Do you: \n A. Want to know how a new rating would impact your percentage? \n B. Want to see what rating you need to get to a higher rating? \n C. No, I'm good, thank you. \n").lower()
if question == "a":
    newDis = int(input("What is your projected rating? \n"))
    newRating = int(round(100 - (newDis * 0.01 * able ), -1 ))
    print("With an additional rating of "+ str(newDis) + "%, your disability percentage would be "+ str(newRating)+"%")

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

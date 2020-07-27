percentAble = 100.00
percentages = list(map(int, input("Please enter each percentage rating in decreasing order (separate with spaces) ").split()))
x = 1
i = 0
for item in percentages:
    print("Disability " + str(x) + " has reduced your ability rating of "
          + str(percentAble)
          + " by " + str(percentages[i]) +
          "% to " + str(percentAble - (percentages[i] * 0.01 * percentAble))
          + "% able, for a rating impact of "
          + str(item * 0.01 * percentAble) + "%\n")
    percentages[i] = (percentages[i] * 0.01)
    x += 1
    percentAble = percentAble - (percentages[i] * percentAble)
    i += 1


Rating = 100 - percentAble
print("Total percentage of ability remaining: " + str(percentAble) + "\n")
print("Actual Disability Percentage: " + str(100-percentAble) + "\n")
print("Calculated Rating: " + str(round(Rating, -1)))

Desired = int(input("What Rating do you desire? \n"))
Desired = Desired - 5
Needed = Desired - Rating
ImpactNeeded = (Needed*100)/Rating
print("impact needed: " + str(ImpactNeeded))
PercNeeded = round((ImpactNeeded/(percentAble * 0.01)), -1)
print("percent needed: " + str(PercNeeded))
print("You would need another disability rating of at least " +
      str(PercNeeded) + " to achieve the needed rating impact of " +
      str(ImpactNeeded) + "% and attain a calculated rating of " +
      str(Desired+5))


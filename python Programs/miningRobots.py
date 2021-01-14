# You are starting an asteroid mining mission with a single harvester robot. That robot is capable of mining one gram of mineral per day.
# It also has the ability to clone itself by constructing another harvester robot.
# That new robot becomes available for use the next day and can be involved in the mining process or can be used to construct yet another robot.
# Each day you will decide what you want each robot in your fleet to do.
# They can either mine one gram of mineral or spend the day constructing another robot.
# Write a program to compute a minimum number of days required to mine n grams of mineral.
# Note that you can mine more mineral than required. Just ensure that you spent the minimum possible number of days to have the necessary amount of mineral mined.
# Input: A single integer number n, which is the number of grams of mineral to be mined.
# The value of n will be between 1 and 1000000 (inclusive).
# Output: A single integer, the minimum number of days required to mine n grams of mineral.
def miningOp(n):
    x = 0
    if n == 0:
        return 1
    elif n == 1:
        return 2
    return min(miningOp(n - 1) + 1, miningOp(n - 1) + miningOp(n - 2))

print(miningOp(5))
# print([miningOp(n) for n in range(8)])

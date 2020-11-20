# # Jeremy Cloyd | Veteran's disability Calculator
# import math
# singleRatings = {100: 3106.04, 90: 1862.96, 80: 1657.8, 70: 1426.17, 60: 1131.68,
#                    50: 893.43, 40: 627.61, 30:435.69,20:281.27,10:142.29}
#
# marriedRatings = {100:3279.22, 90:2017.96, 80:1795.80, 70:1547.17, 60:1234.68,
#                    50:979.43, 40:696.61, 30:486.69, 20:281.27, 10:142.29}
# ratings = list(map(int, input("Please enter each rating percentage (separate with spaces) \n").split()))
# disabled = 0
# able = 100-disabled
# i = 0
# for item in ratings:
#     print("After applying rating of : " + str(ratings[i]))
#     ratings[i] = (ratings[i] * 0.01) # convert ratings into decimal multipliers
#     impact = ratings[i]*able
#     disabled =  round(disabled + impact)
#     able = int(able - round(impact, 0))
#     print("able = %s\ndisabled = %d\nImpact = %f" %(able, disabled, impact) )
#     i += 1
#
# # for item in range(len(ratings)):
# #     disabled += ratings[item]*able
# #     able -= disabled
# print(disabled)
#
# # rawDisabled = 100 - able
# # calcDis = round(100 - able, -1)
# # print("Raw Disability Percentage: " + str(rawDisabled))
# # print("calculated Disability Rating " + str(calcDis)+ "\n")
import math
# rounded = int(math.ceil((91) / 10.0)) * 10
rounded = round(91.4)
print(rounded)

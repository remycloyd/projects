def tumbler(item, sety):
    for x in range(len(item)):
        sety.add(item[x:]+item[:x])

genes = ["aag", "gaa", "agc", "cag","aga","gca"]
permutations = set()
output = []
for i in genes:
    if i not in permutations:
        tumbler(i, permutations)
        output.append(i)
print(output,"Unique Sequence Count: %d "%len(output))
# for i in range(len(genes)):
#     for j in range(len(genes)):
#         if i!=j and genes[i] and genes[j] and isRotation(genes[i], genes[j]):
#             genes[j] = None
# Given strings representing genes, determine how many unique
# sequences there are, accounting for rotations.
# For example, given AAG, GAA, and CAA, there are two unique sequences,
# because GAA can be rotated to AAG
# def isRotation(string1, string2):
#     if string2 in string1+string1:
#         return True

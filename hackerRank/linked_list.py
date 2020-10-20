# Use this class to create linked lists.
class Node:
    def __init__(self, value, child=None):
        self.value = value
        self.child = child


# Implement your function below.
def nth_from_last(head, n):
    left = head
    right = head
    for i in range(n):
        if right is None:
            return None
        right = right.child
    while right is not None:
        right = right.child
        left = left.child
    return left


current = Node(1)
for i in range(2, 8):
    current = Node(i, current)
head = current
# head = 7 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1 -> (None)

current2 = Node(4)
for i in range(3, 0, -1):
    current2 = Node(i, current2)
head2uuu = current2
# head2 = 1 -> 2 -> 3 -> 4 -> (None)

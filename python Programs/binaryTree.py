# Data structure to store a Binary Tree node
class Node:
    def __init__(self, key=None, left=None, right=None):
        self.key = key
        self.left = left
        self.right = right


# Iterative function to perform in-order traversal of the tree
# def inorderIterative(root):

    # create an empty stack
	# stack = deque()
    #
	# # start from root node (set current node to root node)
	# curr = root
    #
	# # if current node is None and stack is also empty, we're done
	# while stack or curr:
    #
	# 	# if current node is not None, push it to the stack (defer it)
	# 	# and move to its left child
	# 	if curr:
	# 		stack.append(curr)
	# 		curr = curr.left
	# 	else:
	# 		# else if current node is None, we pop an element from the stack,
	# 		# print it and finally set current node to its right child
	# 		curr = stack.pop()
	# 		print(curr.data, end=' ')
    #
	# 		curr = curr.right

# Recursive function to calculate height of given binary tree
def height(root):
    # Base case: empty tree has height 0
    if root is None:
        return 0

    # recur for left and right subtree and consider maximum depth
    return 1 + max(height(root.left), height(root.right))


# Recursive function to perform in-order traversal of the tree
def inorder(root):
    # return if the current node is empty
    if root is None:
        return
    # Traverse the left subtree
    inorder(root.left)
    # Display the data part of the root (or current node)
    print(root.key, end=' ')
    # Traverse the right subtree
    inorder(root.right)

if __name__ == '__main__':
    root = Node('1')

    root.left = Node('2')
    root.right = Node('3')

    root.left.left = Node('4')
    root.left.right = Node('5')

    root.right.left = Node('6')
    root.right.right = Node('7')

    root.left.left.left = Node('8')
    root.left.left.right = Node('9')

    root.left.right.left = Node('10')
    root.left.right.right = Node('11')

    root.right.left.left = Node('12')
    root.right.left.right = Node('13')

    root.right.right.left = Node('14')
    root.right.right.right = Node('15')

print("The height of the binary tree is", height(root))
print("The In order/ DFS traversal of this tree is:")
inorder(root)

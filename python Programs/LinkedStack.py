from queue import Empty
class LinkedStack:  # lifo stack implementation using a singly linked list for storage.

    class _Node:  # lightweight, nonpublic class for storing a singly linked node.
        __slots__ = '_element', '_next'  # streamline memory usage

        def __init__(self, element, next):  # initialize node's fields.
            self._element = element         # ref to user's element
            self._next = next               # ref to next node

# --------------------------stack methods-----------------------------------
    def __init__(self):     # create an empty stack
        self.head = None    # set head ref
        self.size = 0       # number of elements in stack

    def __len__(self):      # return num of elements in stack
        return self._size

    def is_empty(self):     # return true if stack is empty
        self._size == 0

    def push(self, e):  # add element to top of the stack
        self._head = self._Node(e, self._head)  # create link to newest node
        self._size += 1

    def top(self):  # return without removal, the element at the top of the stack.
        if self.is_empty():
            raise Empty('stack is empty, son')
        return self._head._element  # head element is the top of stack

    def pop(self):
        if self._head is None:
            raise Empty('stack is empty, son')
        x = self._head._element
        self._head = self._head._next  # sets head ref to next node, making previous node un-findable and nonexistent
        self._size -= 1
        return x

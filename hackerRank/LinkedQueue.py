from queue import Empty


class LinkedQueue:  # fifo queue implementation using a singly linked list for storage.
    class _Node:    # lightweight, nonpublic class for storing a singly linked node.
        __slots__ = '_element', '_next'  # streamline memory usage

        def __init__(self, element, next):  # initialize node's fields.
            self._element = element         # ref to user's element
            self._next = next               # ref to next node

    def __init__(self):    # create empty queue
        self._head = None
        self._tail = None
        self._size = 0

    def __len__(self):
        return self._size  # Report back number of elements in list

    def is_empty(self):
        return self._size == 0  # return true if list is empty

    def first(self):  # return but do not remove first element in queue
        if self.is_empty():
            raise Empty('your shit is empty my G')
        return self._head._element      # front is aligned with head of list

    def dequeue(self):  # remove and return the first element of the list.
        if self.is_empty():
            raise Empty('your shit is empty my G')
        x = self._head._element
        self._head = self._head._next
        self._size -= 1
        if self.is_empty():          # special case: queue is now empty
            self._tail._next = None  # previous head was also tail
        return x

    def enqueue(self, e):
        newest = self._Node(e, None)   # create link to newest node
        if self.is_empty():
            self._head = newest        # special case: previously empty, so new node is now head
        else:
            self._tail._next = newest  # if queue not empty then new node is new tail
        self._tail = newest            # update instance ref to tail node
        self._size += 1

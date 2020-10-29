from queue import Empty


class LinkedQueue:  # fifo queue implementation using a singly linked list for storage.
    class _Node:    # lightweight, nonpublic class for storing a singly linked node.
        __slots__ = '_element', '_next'  # streamline memory usage

        def __init__(self, element, next):  # initialize node's fields.
            self._element = element         # ref to user's element
            self._next = next               # ref to next node

    def __init__(self):    # create empty queue
        self.head = None
        self._tail = None
        self._size = 0

    def __len__(self):
        return self._size  # Report back number of elements in list

    def is_empty(self):
        return self._size == 0  # return true if list is empty

    def first(self):  # return but do not remove first element in queue
        if self.is_empty():
            raise Empty('your shit is empty my G')
        return self.head.element      # front is aligned with head of list

    def dequeue(self):  # remove and return the first element of the list.
        if self.is_empty():
            raise Empty('your shit is empty my G')
        x = self.head.element
        self.head = self.head.next
        self._size -= 1
        if self.is_empty():          # special case: queue is now empty
            self._tail._next = None  # previous head was also tail
        return x

    def enqueue(self, e):
        newest = self._Node(e, None)   # create link to newest node
        if self.is_empty():
            self.head = newest        # special case: previously empty, so new node is now head
        else:
            self._tail._next = newest  # if queue not empty then new node is new tail
        self._tail = newest            # update instance ref to tail node
        self._size += 1

    def insertAtPos(self, position, data):
        if position > self._size:
            raise IndexError
        newHead = self.head
        if self.head is None:                # if list is empty
            # print("inserting node into empty List" + "\n")
            newest = self._Node(data, None)  # create node
            self.head = newest               # set head to point to this new node
            self._tail = newest
            self._size += 1
        elif position == 0:                  # if inserting before head of nonEmpty list
            newest = self._Node(data, None)  # create node
            newest._next = self.head
            self.head = newest
            self._size += 1
        else:
            for i in range(position, 0, -1):
                if i == 1:
                    newest = self._Node(data, None)
                    newest._next = newHead._next
                    newHead._next = newest
                    self._size += 1
                    break
                newHead = newHead._next
        return newHead

    @staticmethod
    def _list_print(self, node):  # Print the Doubly Linked list
        while node is not None:
            if node._element is None:
                node = node._next
            else:
                print(node._element)
                node = node._next


slList = LinkedQueue()
slList.enqueue("1")
slList.enqueue("2")
slList.enqueue("4")
slList.insertAtPos(2, "3")
slList._list_print(slList, slList.head)

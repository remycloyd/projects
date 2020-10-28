class _DoublyLinkedBase:
    # base class providing a doubly linked list representation

    class _Node:
        # Lightweight, nonpublic class for storing a doubly linked node.
        __slots__ = '_element', '_prev', '_next'

        def __init__(self, element, prev, next):  # initialize node's element
            self._element = element
            self._prev = prev
            self._next = next

    def __init__(self):  # create an empty list
        self._header = self._Node(None, None, None)
        self._trailer = self._Node(None, None, None)
        self._header._next = self._trailer  # trailer follows header
        self._trailer._prev = self._header  # header precedes trailer
        self._size = 0

    def is_empty(self):
        return self._size == 0

    def __len__(self):
        return self._size

    def _insert_between(self, e, before, after):
        newest = self._Node(e, before, after)  # link new node to neighbors on creation
        before._next = newest
        after._prev = newest
        self._size += 1
        return newest

    def _delete_Node(self, node):
        before = node._prev  # temp variables to move pointers away from trash node
        after = node._next
        after._prev = before
        before._next = after
        self._size -= 1

        element = node._element  # record deleted element
        node.prev = node.next = node._element = None  # deprecate Node to help in garbage collection
        return element  # return deleted element

    @staticmethod
    def _list_print(self, node):  # Print the Doubly Linked list
        while node is not None:
            if node._element is None:
                node = node._next
            else:
                print(node._element)
                node = node._next


dllist = _DoublyLinkedBase()
dllist._insert_between("Play Swamp", dllist._header, dllist._trailer)
dllist._insert_between("Thoughtseize you", dllist._trailer._prev, dllist._trailer)
dllist._insert_between("pass turn", dllist._trailer._prev, dllist._trailer)
dllist._insert_between("Snap Keep", dllist._header, dllist._header._next)
dllist._list_print(dllist, dllist._header)

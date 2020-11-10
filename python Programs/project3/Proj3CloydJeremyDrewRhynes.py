# Jeremy Cloyd and Drew Rhynes
# Data Structures Group Project 3
# Group 3
# October 14, 2018
import datetime
from heapq import heappop, heappush


def scheduleRooms(rooms, cls):
    """
    Input: 
    rooms - list of available rooms
    cls - dictionary mapping class names to pair of (start, ndtym) times
    Output: Return a dictionary mapping  room name to a list of non-conflicting scheduled classes.
    If there are not enough rooms to hold  classes,  return 'Not enough rooms'."""

    rooms.reverse()                                 # reverse for pop to retrieve first room
    room = rooms.pop()                              # retrieve first room
    roomheap = []                                   # initialize room heap
    roomassign = {room: []}                         # dictionary gets first room
    heappush(roomheap, (datetime.time(0), room))    # populate room heap with first room
    ClassHeap = []                                  # initialize class heap
    for key in cls:                                 # for each key in list, do:
        startym, ndtym = cls[key]                   # assign tuple start/ndtym times for each class
        heappush(ClassHeap, (startym, key, ndtym))  # puts classes in heap according by start times
    while ClassHeap:
        startym, cl, ndtym = heappop(ClassHeap)  # use next earliest class
        atyme, room = heappop(roomheap)          # uses first available room
        if startym >= atyme:                     # if  class follows current earliest time
            roomassign[room].append(cl)          # assign this class to a room
            heappush(roomheap, (ndtym, room))    # replace room in heap with updated time
        elif rooms:                              # else more rooms
            heappush(roomheap, (atyme, room))    # replace current room
            room = rooms.pop()                   # get new room
            heappush(roomheap, (ndtym, room))    # put on heap with earliest available time
            roomassign.update({room: [cl]})      # assign  class to  new room
        else:                                    # if no room available for class and no more classes, report it.
            return "Not Enough Rooms."
    return roomassign                            # return  room assignment


if __name__ == "__main__":
    cl1 = {"a": (datetime.time(9), datetime.time(10, 30)),
           "b": (datetime.time(9), datetime.time(12, 30)),
           "c": (datetime.time(9), datetime.time(10, 30)),
           "d": (datetime.time(11), datetime.time(12, 30)),
           "e": (datetime.time(11), datetime.time(14)),
           "f": (datetime.time(13), datetime.time(14, 30)),
           "g": (datetime.time(13), datetime.time(14, 30)),
           "h": (datetime.time(14), datetime.time(16, 30)),
           "i": (datetime.time(15), datetime.time(16, 30)),
           "j": (datetime.time(15), datetime.time(16, 30))}

    rm1 = [1, 2, 3]
    print(cl1)
    print("\n", "Classes in each room using enough rooms" + "\n", scheduleRooms(rm1, cl1))
    print("\n Classes in each room using only two rooms" "\n", scheduleRooms([ 1, 2 ], cl1))
    ensrooms = ['KEH U1', 'KEH M1', 'KEH M2', 'KEH M3', 'KEH U2', 'KEH U3', 'KEH U4', 'KEH M4', 'KEH U8', 'KEH U9']
    csclasses = {'CS 1043': (datetime.time(9, 30), datetime.time(11)),
                 'CS 2003': (datetime.time(10, 30), datetime.time(12)),
                 'CS 2123': (datetime.time(11, 15), datetime.time(12, 45)),
                 'CS 3003': (datetime.time(8, 15), datetime.time(11, 30)),
                 'CS 3353': (datetime.time(11), datetime.time(12)),
                 'CS 4013': (datetime.time(13), datetime.time(14, 45)),
                 'CS 4063': (datetime.time(12, 30), datetime.time(14, 30)),
                 'CS 4123': (datetime.time(14), datetime.time(15)),
                 'CS 4163': (datetime.time(14), datetime.time(16, 30)),
                 'CS 4253': (datetime.time(12), datetime.time(16))}

    print("\n", "Classes" + "\n", csclasses)
    print("\n", "Classes in each room using up to ten rooms" + "\n", scheduleRooms(ensrooms, csclasses))
    print("\n Classes in each room using only first 4 rooms" "\n", scheduleRooms(ensrooms[ :4 ], csclasses))

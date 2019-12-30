

from _sqlite3 import Row
from collections import deque

def read_playlist(filename):
    """
    Input: filename of CSV file listing (song,artist,genre) triples
    Output: List of (song,artist,genre)
    """
    playlist = []
    for line in open(filename):
        bits = [b.strip() for b in line.split(',')]
        playlist.append(bits)
    return playlist

def playlist_transform(s,t,compareType="Song"):
    """
    Computes the edit distance for two playlists s and t, and prints the minimal edits 
      required to transform playlist s into playlist t.
    Inputs:
    s: 1st playlist (format: list of (track name, artist, genre) triples) row
    t: 2nd playlist (format: list of (track name, artist, genre) triples) col
    compareType: String indicating the type of comparison to make.
       "Song" (default): songs in a playlist are considered equivalent if the 
         (song name, artist, genre) triples match.
       "Genre": songs in a playlist are considered equivalent if the same genre is used.
       "Artist": songs in a playlist are considered equivalent if the same artist is used.
    Output: The minimum edit distance and the minimal edits required to transform playlist
      s into playlist t.
    """
    row = [[[j] for j in range(len(t)+1)] for i in range(len(s)+1)] #holy list comprehension
    # go through table row by row from top down 
    # Each edit will keep track of edit score, what the edit is, and the previous edit location [score, "the edit", row, column]
    i=-1
    for k in row:
        j=-1
        i+=1
        for m in k:
            j+=1
            delt = -1
            ksub = -1
            ins = -1
            if i > 0:
                delt = row[i-1][j][0] + 1 #calc delete
                if j > 0:
                    ksub = row[i-1][j-1][0]#calc keep/sub
                    if compareType == "Song": 
                        if s[i-1] != t[j-1]: #comparison for song--exact match
                            ksub += 1
                    elif compareType == "Genre":
                        if s[i-1][2] != t[j-1][2]: #comparison for genre match
                            ksub += 1
                    elif compareType == "Artist":
                        if s[i-1][1] != t[j-1][1]: #comparison for genre match
                            ksub += 1
            if j > 0:
                ins = row[i][j-1][0] + 1 #calc insert
            a = -1 #used in existence table below
            if ksub == -1: #if ksub doesn't exist, one of the others must not exist
                if delt == -1: #if ksub and delt don't exist
                    if ins == -1: #if none exist
                        a = 4 #top left corner, edit distance zero
                    else: #if only ins exists
                        a = 3 #top row, insert
                elif ins == -1: #if ksub and ins don't exist
                    a = 1 #first column, delete
            
            if delt < ksub and delt <= ins and a == -1 or a==1: #1
                row[i][j] = [delt, "Delete " + s[i-1][0] + " by " + s[i-1][1], i-1, j]#store delt score, what's being deleted (s[i-1]), [i-1], and [j]
            elif ksub <= delt and ksub <= ins and a == -1: #2
                if ksub > row[i-1][j-1][0]: 
                    row[i][j] = [ksub, "Sub " + t[j-1][0] + " by " + t[j-1][1] + " for " + s[i-1][0] + " by " + s[i-1][1], i-1, j-1]#store sub score, what's being subbed for what (t[j-1] for s[i-1]), and row[i-1][j-1]
                else:
                    row[i][j] = [ksub, "Keep " + s[i-1][0] + " by " + s[i-1][1], i-1, j-1] #store keep score, what's being kept (s[i-1]), and [i-1], [j-1]
            elif ins < delt and ins < ksub and a == -1 or a == 3: #3
                row[i][j] = [ins, "Insert " + t[j-1][0] + " by " + t[j-1][1], i, j-1]#store ins score, what's being inserted t[j-1], i, and j
            elif a == 4: #4
                row[i][j] = [0,"",-1,-1]
    r = len(row)-1
    c = len(row[0])-1
    ed = row[r][c][0]
    q = deque()
    while r != 0 and c != 0:
        [a,s,r,c] = row[r][c]
        q.append(s)
    print ed
    while q:
        print q.pop()
    # place the bottom right edit in a stack and follow the previous edit to the top left
if __name__=="__main__":
    #obtain local copy from http://secon.utulsa.edu/cs2123/blues1.csv
    b1 = read_playlist("Book1.csv")
    #obtain local copy from http://secon.utulsa.edu/cs2123/blues2.csv
    b2 = read_playlist("Book2.csv")
    print "Playlist 1"
    for song in b1:
        print song
    print "Playlist 2"
    for song in b2:
        print song
    print "Comparing playlist similarity by song"
    playlist_transform(b1,b2)
    print "Comparing playlist similarity by genre"
    playlist_transform(b1,b2,"Genre")
    print "Comparing playlist similarity by artist"
    playlist_transform(b1,b2,"Artist")
    #include your own playlists below

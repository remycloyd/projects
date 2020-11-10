#  jeremy Cloyd
#  project 2
#  20180922

from collections import deque


def byte2str(b):
    """
    Input: byte sequence b of a string
    Output: string form of the byte sequence
    Required for python 3 functionality
    """
    return "".join(chr(a) for a in b)


def getlinks(url, baseurl="http://secon.utulsa.edu/cs2123/webtraverse/"):  # input url to visit
    # import the HTML parser package
    try:
        from bs4 import BeautifulSoup
        import requests
    except:
        print('You must first install the BeautifulSoup or requests package for this code to work')
        raise  # fetch the URL and load it into the HTML parser
    soup = BeautifulSoup(requests.get(url).content, features="html.parser")  # pull out the links from the HTML and return
    return [baseurl+byte2str(a["href"].encode('ascii', 'ignore')) for a in soup.findAll('a')]


def print_dfs(url):
    G = getDict(url)
    dfslist = rec_dfs(G, url)  # list to house depth first links 
    for ulink in dfslist:
        print(ulink)


def rec_dfs(G, url, S=[]):
    S.append(url)
    for ulink in G[url]:
        if ulink in S:
            continue
        rec_dfs(G, ulink, S)
    return S


def print_bfs(url):
    for links in iter_bfs(url):
        print(links)


def iter_bfs(url, S=[]):
    visited, Qq = set(), deque([url])  # set of visited urls and queue of urls to be visited
    G = getDict(url)   # dequeue a url
    while Qq:  # trips are planned
        ulink = Qq.popleft()
        if ulink in visited:
            continue  # it's been visited now
        visited.add(ulink)
        Qq.extend(G[ulink])
        yield ulink
    #    print(link)


def get_parent_bfs(url1, url2):
    G = getDict(url1)
    P, Qqq = {url1: None}, deque([url1])
    while Qqq:
        ulink = Qqq.popleft()
        for v in G[ulink]:
            if v in P:
                continue
            P[v] = ulink = Qqq.append(v)
    return P


def find_shortest_path(url1, url2):
    path = [url2]  # Init path, will end with url2
    linkdict = {}  # similar to bfs imp
    q = deque({url1})  # create bfs tree with url1 as root
    while q:
        sourcelink = q.popleft()
        allbranchlist = getlinks(sourcelink)
        newbranchlist = []
        for branch in allbranchlist:
            if branch not in linkdict and branch not in q and branch != sourcelink:
                q.append(branch)
                newbranchlist.append(branch)
        linkdict[sourcelink] = newbranchlist  # using a dictionary, seek url1 from url2 by crawling from parent urls until found.
    curr_url = url2
    while True:
        curr_locale = curr_url  # begin check if to break loop
        for branch in linkdict:
            if curr_url in linkdict[branch]:  # searching for current url's parent
                path.insert(0, branch)  # add current url's parent to path
                if branch != url1:  # if parent not target url:
                    curr_url = branch    # set that parent as current url,
                    continue  # search for parents of current url
                else:  # if parent of current url is target url->
                    print("Shortest path to such a link found:")
                    print(path)  # print steps from url1 to url2
                    return
        if curr_locale == curr_url:
            break  # breaks if branch not in tree
    print("Path not found")  # If no path exists.


def find_max_depth(start_url):
    linkdict = {}  # like bfs and shortest path
    maxQ = deque({start_url})  # construct bfs tree with start_url as root
    while maxQ:
        maxlink = maxQ.popleft()
        branchlist = getlinks(maxlink)
        newbranchlist = []
        for b in branchlist:
            if b not in linkdict and b not in maxQ and b != maxlink:
                maxQ.append(b)
                newbranchlist.append(b)
        linkdict[maxlink] = newbranchlist
    nestlist = [[start_url]]  # nestlist is nested list with items in each list being children of the item in the following list
    tlist = []  # holds new elements
    while True:
        tlist = nestlist[0]
        childlist = []  # holds the newest children
        for a in tlist:  # for each of the parents in tlist
            for b in linkdict[a]:  # find their children according to the generated dictionary
                childlist.append(b)  # add to the child list
        if childlist:  # if the child list isn't empty, add it to nestlist, break otherwise
            nestlist.insert(0, childlist)
        else:
            break
    print("Deepest Link Found:")
    print(nestlist[0][0])
    find_shortest_path(start_url, nestlist[0][0])  # Find and return URL whose minimum sequence of links is greatest from start_url.


def getDict (start, G = {}):
    G[start] = getlinks(start)
    for link in G[start]:
        if link not in G:
            getDict(link, G)
    return G


if __name__ == "__main__":
    starturl = "http://secon.utulsa.edu/cs2123/webtraverse/index.html"
    print("***********(a) Depth-first search   **********")
    print_dfs(starturl)
    print("***********(b) Breadth-first search **********")
    print_bfs(starturl)
    print("***********(c) Find shortest path between two URLs ********")
    find_shortest_path("http://secon.utulsa.edu/cs2123/webtraverse/index.html", "http://secon.utulsa.edu/cs2123/webtraverse/wainwright.html")
    find_shortest_path("http://secon.utulsa.edu/cs2123/webtraverse/turing.html", "http://secon.utulsa.edu/cs2123/webtraverse/dijkstra.html")
    print("***********(d) Find the Deepest Link from a starting URL *****")
    find_max_depth(starturl)




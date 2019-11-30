import http.client
import urllib.request
import json
import time
import timeit
import sys
import collections
from pygexf.gexf import *

#
# implement your data retrieval code here
#\

api_key1=sys.argv[1]

headers = {}
headers['User-Agent'] = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:48.0) Gecko/20100101 Firefox/48.0"



req = urllib.request.Request('https://rebrickable.com/api/v3/lego/sets/?page_size=350&min_parts=1111&ordering=-num_parts&key='+api_key1, headers = headers)
data=json.load(urllib.request.urlopen(req))
print(urllib.request.urlopen(req).getcode())




# complete auto grader functions for Q1.1.b,d
def min_parts():
    """
    Returns an integer value
    """
    # you must replace this with your own value
    
    return 1111

def lego_sets():
    """
    return a list of lego sets.
    this may be a list of any type of values
    but each value should represent one set

    e.g.,
    biggest_lego_sets = lego_sets()
    print(len(biggest_lego_sets))
    > 280
    e.g., len(my_sets)
    """
    # you must replace this line and return your own list

    result_list=[]

    print(type(data))
    for x in data['results']:
        result_list.append(x)

    print(len(result_list))


    return result_list

def gexf_graph():
    """
    return the completed Gexf graph object
    """
    # you must replace these lines and supply your own graph
    


    import sys,pprint

    #sys.path.append('../gexf')

# test helloworld.gexf
    gexf = Gexf("Q1","A file to visualize sets and parts")
    graph=gexf.addGraph("undirected","static","a hello world graph")
    graph.addNodeAttribute("type",type="string",force_id="type")

    for i in z:
        Node = graph.addNode(id=i['set_num'],label=i['name'],r="0",g="0",b="0")
        Node.addAttribute("type","set")

    
    for j in part_list:
        rgbhex=j[4]['rgb']
        r=str(int(rgbhex[0:2],16))
        g=str(int(rgbhex[2:4],16))
        b=str(int(rgbhex[4:],16))
        Node=graph.addNode(id=j[0],label=j[3],r=r,g=g,b=b)
        Node.addAttribute("type","part")
        

    for k in part_list:
        graph.addEdge(id=k[1]+k[2],source=k[1],target=k[0],weight=k[5])
    


    output_file=open("bricks_graph.gexf","wb")
    gexf.write(output_file)


    return gexf.graphs[0]

# complete auto-grader functions for Q1.2.d

def avg_node_degree():
    """
    hardcode and return the average node degree
    (run the function called “Average Degree”) within Gephi
    """
    # you must replace this value with the avg node degree
    return 4.44

def graph_diameter():
    """
    hardcode and return the diameter of the graph
    (run the function called “Network Diameter”) within Gephi
    """
    # you must replace this value with the graph diameter
    return 10

def avg_path_length():
    """
    hardcode and return the average path length
    (run the function called “Avg. Path Length”) within Gephi
    :return:
    """
    # you must replace this value with the avg path length
    return 4.593





z=lego_sets();

print("Got the sets!")
part_ctr=0
part_list=[]
import operator
for x in z[:2]:
    set_num=x['set_num']
    req1 = urllib.request.Request('https://rebrickable.com/api/v3/lego/sets/'+set_num+'/parts/?page_size=1000&key='+api_key1, headers = headers)
    data1=json.load(urllib.request.urlopen(req1))
    part_ctr+=1
    print(part_ctr)

    k=sorted(data1['results'],key= lambda m: m['quantity'],reverse=True)

    for x in k[:20]:
        node_id=x['part']['part_num']+'_'+x['color']['rgb']
        part_list.append([node_id,set_num,x['part']['part_num'],x['part']['name'],x['color'],x['quantity']])
    
    
print("Completed getting the parts")



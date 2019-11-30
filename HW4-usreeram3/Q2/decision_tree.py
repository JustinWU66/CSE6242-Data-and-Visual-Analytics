from util import entropy, information_gain, partition_classes
import numpy as np 
import ast
import csv
import scipy

class DecisionTree(object):
    def __init__(self):
        # Initializing the tree as an empty dictionary or list, as preferred
        #self.tree = []
        self.tree = {}
        pass

    def learn(self, X, y):
        # TODO: Train the decision tree (self.tree) using the the sample X and labels y
        # You will have to make use of the functions in utils.py to train the tree
        
        # One possible way of implementing the tree:
        #    Each node in self.tree could be in the form of a dictionary:
        #       https://docs.python.org/2/library/stdtypes.html#mapping-types-dict
        #    For example, a non-leaf node with two children can have a 'left' key and  a 
        #    'right' key. You can add more keys which might help in classification
        #    (eg. split attribute and split value)
        nrows = len(X)
        try:
            ncols=len(X[0])
        except:
            print(X)
            

        ent = entropy(y)

        self.tree['entropy'] = ent

        if ent < 0.1 or len(y)<=100:
            if len(y)==0:
                return self

            self.tree['class'] = scipy.stats.mode(y).mode[0]
            self.tree['split_attr'] = 'null'
            self.tree['split_val'] = 'null'
            self.tree['left_child']= None
            self.tree['right_child']= None
            return self

        info_gain = []
        split_val = []
        
        
        
        

        for idx in range(ncols-1):
            best_val_for_column = 0
            best_gain_for_column = 0
            
            series = [row[idx] for row in X] 
        
            
            steps = np.linspace(start=np.min(series),stop=np.max(series),num=5)[1:4]
            for val in steps: 
                X_left, X_right, y_left, y_right = partition_classes(X,y,idx,val)
                gain=information_gain(y,[y_left,y_right])
                if gain > best_gain_for_column: 
                    best_gain_for_column = gain
                    best_val_for_column = val
                        
            info_gain.append(best_gain_for_column)
            split_val.append(best_val_for_column)
            
                
        best_split_col = np.argmax(info_gain) 
        
        best_split_value = split_val[best_split_col]  
        
     
        X_left, X_right, y_left, y_right = partition_classes(X,y,best_split_col,best_split_value) 
        

        self.tree['class']='parent' 
        self.tree['split_attr']=best_split_col
        self.tree['split_val']=best_split_value
        
        self.tree['left_child'] = DecisionTree()
        self.tree['left_child'].learn(X_left,y_left)
        
        self.tree['right_child'] = DecisionTree()
        self.tree['right_child'].learn(X_right,y_right) 


    def classify(self, record):
        if self.tree['split_val']=='null':
            return self.tree['class']
        
        else: #at parent node 
            attr=self.tree['split_attr']
            val = self.tree['split_val']
            
            if record[attr] <= val:
                return self.tree['left_child'].classify(record)
                    
            else:
                return self.tree['right_child'].classify(record)
        
            
    
    def print_tree(self):
        print("class",self.tree['class'])
        print("split_attr",self.tree['split_attr'])
        print("split_val",self.tree['split_val'])
        print("entropy",self.tree['entropy'])
        
        #pre-order traversal +ab
        
        if (self.tree['left_child']!=None):
            self.tree['left_child'].print_tree()
        
        if (self.tree['left_child']!=None):
            self.tree['right_child'].print_tree()
        
    
    
    

# def main():   # testing decision tree first
#     X = list()
#     y = list()
#     XX = list()  # Contains data features and data labels
#     numerical_cols = set([i for i in range(0, 9)])  # indices of numeric attributes (columns)

#     # Loading data set
#     print("reading pulsar_stars")
#     with open("pulsar_stars.csv") as f:
#         next(f, None)
#         for line in csv.reader(f, delimiter=","):
#             xline = []
#             for i in range(len(line)):
#                 if i in numerical_cols:
#                     xline.append(ast.literal_eval(line[i]))
#                 else:
#                     xline.append(line[i])

#             X.append(xline[:-1])
#             y.append(xline[-1])
#             XX.append(xline[:])
        

#     # TODO: Initialize according to your implementation
#     # VERY IMPORTANT: Minimum forest_size should be 10
#     forest_size = 10
    
# #     print(XX[0])
# #     print(y[0])
    
#     obj1=DecisionTree()
    
#     obj1.learn(XX,y)
    
#     #obj1.print_tree()
#     #print(len(XX))
    
#     training_set=XX[:15000]
#     training_y=y[:15000]
  
#     testing_set=XX[15000:]
#     testing_y=y[15000:]
    
#     training_answer=[]
#     testing_answer=[]
#     ctr=0;
#     ctr1=0;
    
#     for i in range(len(training_set)):
#         a=obj1.classify(training_set[i])
#         training_answer.append(a)
#         #print([training_y[i],a])
#         if (a==training_y[i]):
#             ctr+=1
            
#     for i in range(len(testing_set)):
#         b=obj1.classify(testing_set[i])
#         testing_answer.append(b)
#         #print([testing_y[i],b])
#         if (b==testing_y[i]):
#             ctr1+=1
        
        
#     print("Train Accuracy:",(ctr/len(training_y))*100)
    
#     print("Test Accuracy:",(ctr1/len(testing_y))*100)

    

# if __name__ == "__main__":
#     main()
# 2-Dimensional K-D Tree 

##How to compile and run the program:
```
javac KDQuery.java
java KDQuery points.txt directives.txt
```
##Input Files:
###Points Input File
Example file, first column is x coordinate and second column is y coordinate:
```
1 0
2 3
4 5
6 7
```
###Directives Input File
Example file, first column is directive, other columns are parameters of directive.
```
findMaxY
display-tree
display-points
range 0 0 10 10
insert 0 0.5
quit
```
####Directives
#####insert x y | Insert point (x,y) into the tree
#####remove x y | Remove point (x,y) from the tree
#####search x y | Search for point (x,y) in the tree
#####findMinX | Print the point with the smallest x coordinate
#####findMinY | Print the point with the smallest y coordinate
#####findMaxX | Print the point with the largest x coordinate
#####findMaxY | Print the point with the largest y coordinate
#####display-tree | Display the tree
#####display-points | Print a list of points from left to right
#####range llx lly urx ury | Print the list of points within the rectangle specified by lowerleft corner (llx, lly) and upperright corner (urx, ury)
#####quit | End program

##Known Bugs and Limitations:
List any known bugs, deﬁciencies, or limitations with respect to the project speciﬁcations. File directory: If you have multiple source or data ﬁles, please explain the purpose of each.

##Source Files: 
All the source ﬁles.


##========================================================##
##                                                        ##
##   Network Visualization with R                         ##
##   Polnet 2018 Workshop, Washington, DC                 ##
##   www.kateto.net/polnet2018                            ##
##                                                        ##
##   Katherine (Katya) Ognyanova                          ##
##   Web: kateto.net | Email: katya@ognyanova.net         ##
##   GitHub: kateto  | Twitter: @Ognyanova                ##
##                                                        ##
##========================================================##



# ================ Introduction ================


# Download handouts and example data: bit.ly/polnet2018
# Online tutorial: kateto.net/polnet2018


# CONTENTS
#
#   1. Working with colors in R plots
#   2. Reading in the network data
#   3. Network plots in 'igraph'
#   4. Plotting two-mode networks
#   5. Plotting multiplex networks
#   6. Quick example using 'network'
#   7. Simple plot animations in R
#   8. Interactive JavaScript networks
#   9. Interactive and dynamic networks with ndtv-d3
#  10. Plotting networks on a geographic map


# KEY PACKAGES
# Install those now if you do not have the latest versions.
# (please do NOT load them yet!)






# ================ 2. Reading network data into 'igraph' ================


# Download an archive with the data files from http://bit.ly/polnet2018

# Clear your workspace by removing all objects returned by ls():


# Set the working directory to the folder containing the workshop files:
setwd("/")

# If you don't know the path to the folder and you're in RStudio, go to the
# "Session" menu -> "Set Working Directory" -> "To Source File Location"


library("igraph")

args = commandArgs(trailingOnly=TRUE)

# -------~~ DATASET 1: edgelist  --------

# Read in the data:
nodes <- read.csv(fileNode, header=T, as.is=T)
links <- read.csv(fileEdge, header=T, as.is=T)




# ================ 8. Interactive JavaScript networks ================


# There are a number of libraries like 'rcharts' and 'htmlwidgets' that can help you
# export interactive web charts from R. We'll take a quick look at three packages that
# can export networks from R to JavaScript: : 'visNetwork' and 'threejs', and 'networkD3'


# -------~~  Interactive networks with visNetwork --------

# install.packages("visNetwork")

library("visNetwork")

head(nodes)
head(links)


# We'll start by adding new node and edge attributes to our dataframes.
vis.nodes <- nodes
vis.links <- links

# The options for node shape include 'ellipse', 'circle',
# 'database', 'box', 'text', 'image', 'circularImage', 'diamond',
# 'dot', 'star', 'triangle', 'triangleDown', 'square', and 'icon'

vis.nodes$shape  <- vis.nodes$shape
vis.nodes$shadow <- TRUE # Nodes will drop shadow
vis.nodes$title  <- vis.nodes$type.label # Text on click
vis.nodes$label  <- vis.nodes$id # Node label
vis.nodes$size   <- vis.nodes$size # Node size
vis.nodes$borderWidth <- 2 # Node border width

# We can set the color for several elements of the nodes:
# "background" changes the node color, "border" changes the frame color;
# "highlight" sets the color on click, "hover" sets the color on mouseover.

vis.nodes$color.background <- vis.nodes$node.color
vis.nodes$color.border <- "black"
vis.nodes$color.highlight.background <- "orange"
vis.nodes$color.highlight.border <- "darkred"

# Below we change some of the visual properties of the edges:

vis.links$width <- 1+links$weight/8 # line width
vis.links$label <- vis.links$type# line width
vis.links$color <- "gray"    # line color
vis.links$arrows <- "to" # arrows: 'from', 'to', or 'middle'
vis.links$smooth <- TRUE    # should the edges be curved?
vis.links$shadow <- FALSE    # edge shadow

visNetwork(vis.nodes, vis.links)

visnet <- visNetwork(vis.nodes, vis.links, width="900px", height="500px") %>%
    visOptions(selectedBy = list(variable = "filter", multiple = T),highlightNearest = TRUE) %>%
    visEvents(selectNode = "function(properties) {
       window.parent.comp.component.nodeClick(this.body.data.nodes.get(properties.nodes[0]));}") %>%
    visEvents(selectEdge = "function(properties) {
       window.parent.comp.component.nodeClick(this.body.data.edges.get(properties.edges[0]));}")

setwd("/graphs/")
visSave(visnet, file = "./network.html", background = "white")
visnet




detach("package:visNetwork")




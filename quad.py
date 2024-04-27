import random
import matplotlib.pyplot as plt
import matplotlib.patches as patches

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

class Node:
    def __init__(self, x0, y0, w, h, points):
        self.x0 = x0
        self.y0 = y0
        self.width = w
        self.height = h
        self.points = points
        self.children = []

    def get_width(self):
        return self.width
    
    def get_height(self):
        return self.height
    
    def get_points(self):
        return self.points

class QTree:
    def __init__(self, k, n):
        self.threshold = k
        self.points = [Point(random.uniform(0, 100), random.uniform(0, 100)) for _ in range(n)]
        self.root = Node(0, 0, 100, 100, self.points)
        self.splitting_levels = 0  # Counter for splitting levels

    def add_point(self, x, y):
        self.points.append(Point(x, y))
    
    def get_points(self):
        return self.points
    
    def subdivide(self):
        self._recursive_subdivide(self.root, self.threshold)
    
    def visualize(self):
        self._visualize_recursive(self.root, 0)

    def _recursive_subdivide(self, node, k):
        if node.points is None or len(node.points) <= k:
            return
        
        self.splitting_levels += 1  # Increment splitting levels counter

        w_ = float(node.width / 2)
        h_ = float(node.height / 2)

        p1 = self._contains(node.x0, node.y0, w_, h_, node.points)
        p2 = self._contains(node.x0, node.y0 + h_, w_, h_, node.points)
        p3 = self._contains(node.x0 + w_, node.y0, w_, h_, node.points)
        p4 = self._contains(node.x0 + w_, node.y0 + h_, w_, h_, node.points)

        # If all points are in the same partition, stop subdividing
        if all(p is not None and len(p) == len(node.points) for p in [p1, p2, p3, p4]):
            return

        x1 = Node(node.x0, node.y0, w_, h_, p1)
        self._recursive_subdivide(x1, k)

        x2 = Node(node.x0, node.y0 + h_, w_, h_, p2)
        self._recursive_subdivide(x2, k)

        x3 = Node(node.x0 + w_, node.y0, w_, h_, p3)
        self._recursive_subdivide(x3, k)

        x4 = Node(node.x0 + w_, node.y0 + h_, w_, h_, p4)
        self._recursive_subdivide(x4, k)

        node.children = [x1, x2, x3, x4]

    def _contains(self, x, y, w, h, points):
        pts = []
        for point in points:
            if x <= point.x <= x + w and y <= point.y <= y + h:
                pts.append(point)
        return pts if pts else None

    def _visualize_recursive(self, node, depth):
        if not node.children:
            plt.gca().add_patch(patches.Rectangle((node.x0, node.y0), node.width, node.height, fill=False, edgecolor='black'))
            return
        for child in node.children:
            self._visualize_recursive(child, depth + 1)

# Example usage:
qtree = QTree(1, 20)
qtree.subdivide()
qtree.visualize()

# Plot points with same color for all points
plt.scatter([p.x for p in qtree.get_points()], [p.y for p in qtree.get_points()], color='blue')

plt.gca().set_aspect('equal', adjustable='box')
plt.title("Quadtree Visualization")
plt.xlabel("X")
plt.ylabel("Y")
plt.show()

print("Number of splitting levels:", qtree.splitting_levels)

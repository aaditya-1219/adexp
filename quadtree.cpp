#include <iostream>
#include <vector>

struct Point {
    int x, y;
};

struct QuadTreeNode {
    Point topLeft, bottomRight;
    std::vector<Point> points;
    QuadTreeNode* children[4];

    QuadTreeNode(Point topLeft, Point bottomRight) : topLeft(topLeft), bottomRight(bottomRight) {
        for (int i = 0; i < 4; ++i) {
            children[i] = nullptr;
        }
    }
};

class QuadTree {
private:
    QuadTreeNode* root;
    int maxPointsPerNode;

    void insert(QuadTreeNode* node, Point point) {
        if (node->points.size() < maxPointsPerNode) {
            node->points.push_back(point);
            return;
        }

        if (node->children[0] == nullptr) {
            splitNode(node);
        }

        for (int i = 0; i < 4; ++i) {
            if (isPointInNode(node->children[i], point)) {
                insert(node->children[i], point);
                break;
            }
        }
    }

    bool isPointInNode(QuadTreeNode* node, Point point) {
        return (point.x >= node->topLeft.x && point.x <= node->bottomRight.x &&
                point.y >= node->topLeft.y && point.y <= node->bottomRight.y);
    }

    void splitNode(QuadTreeNode* node) {
        int xMid = (node->topLeft.x + node->bottomRight.x) / 2;
        int yMid = (node->topLeft.y + node->bottomRight.y) / 2;

        node->children[0] = new QuadTreeNode(node->topLeft, {xMid, yMid});
        node->children[1] = new QuadTreeNode({xMid, node->topLeft.y}, {node->bottomRight.x, yMid});
        node->children[2] = new QuadTreeNode({node->topLeft.x, yMid}, {xMid, node->bottomRight.y});
        node->children[3] = new QuadTreeNode({xMid, yMid}, node->bottomRight);

        for (const auto& p : node->points) {
            for (int i = 0; i < 4; ++i) {
                if (isPointInNode(node->children[i], p)) {
                    insert(node->children[i], p);
                    break;
                }
            }
        }

        node->points.clear();
    }

    void query(QuadTreeNode* node, Point topLeft, Point bottomRight, std::vector<Point>& result) {
        if (node == nullptr) {
            return;
        }

        if (node->points.size() > 0) {
            for (const auto& p : node->points) {
                if (p.x >= topLeft.x && p.x <= bottomRight.x && p.y >= topLeft.y && p.y <= bottomRight.y) {
                    result.push_back(p);
                }
            }
        }

        for (int i = 0; i < 4; ++i) {
            if (node->children[i] != nullptr &&
                !(node->children[i]->topLeft.x > bottomRight.x || node->children[i]->bottomRight.x < topLeft.x ||
                  node->children[i]->topLeft.y > bottomRight.y || node->children[i]->bottomRight.y < topLeft.y)) {
                query(node->children[i], topLeft, bottomRight, result);
            }
        }
    }

public:
    QuadTree(Point topLeft, Point bottomRight, int maxPointsPerNode) : maxPointsPerNode(maxPointsPerNode) {
        root = new QuadTreeNode(topLeft, bottomRight);
    }

    void insert(Point point) {
        insert(root, point);
    }

    std::vector<Point> query(Point topLeft, Point bottomRight) {
        std::vector<Point> result;
        query(root, topLeft, bottomRight, result);
        return result;
    }
};

int main() {
    QuadTree quadtree({0, 0}, {100, 100}, 4);  // Example quadtree with 4 max points per node

    // Insert some points
    quadtree.insert({10, 10});
    quadtree.insert({20, 20});
    quadtree.insert({30, 30});
    quadtree.insert({40, 40});
    quadtree.insert({50, 50});
    quadtree.insert({60, 60});
    quadtree.insert({70, 70});
    quadtree.insert({80, 80});
    quadtree.insert({90, 90});
    quadtree.insert({100, 100});

    // Query points in a range
    std::vector<Point> pointsInRange = quadtree.query({20, 20}, {60, 60});

    // Print the points in the range
    std::cout << "Points in the range (20, 20) to (60, 60):\n";
    for (const auto& p : pointsInRange) {
        std::cout <<p.x<<" "<< p.y<<" ";
    }
    return 0;
}

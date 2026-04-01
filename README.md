# recursive-hull-tangent-merge
# Convex Hull Determination (Divide and Conquer - Tangent Method)

## 1. Requirement

Given a set of **N** distinct points in a 2D plane, the objective is to compute the **Convex Hull (CH)** using a **Divide and Conquer** strategy. 

The algorithm recursively divides the point set into vertical strips, computes the hull for each subset, and merges them by identifying the **upper and lower common tangents** (supporting lines) between two disjoint convex polygons.



---

## 2. Algorithm Description

### Phase I: Divide and Conquer
1.  **Preprocessing:** Points are sorted by their **X-coordinate** (and Y-coordinate in case of a tie).
2.  **Divide:** The set $S$ is split into a left half ($S_1$) and a right half ($S_2$).
3.  **Conquer:** The algorithm recursively computes $CH(S_1)$ and $CH(S_2)$.
4.  **Base Case:** For small subsets (e.g., $n \le 3$), the hull is computed directly (e.g., using a simple Graham Scan).

### Phase II: The Merge Step (Common Tangents)
The merge step joins two disjoint convex polygons by finding two common tangents that wrap around both:

#### 1. Initialization
The process starts by identifying the points closest to the vertical dividing line:
* **Point A:** The rightmost point in $CH(S_1)$.
* **Point B:** The leftmost point in $CH(S_2)$.

#### 2. Finding the Upper Tangent
The algorithm iteratively "climbs" the polygons to find the highest supporting line:
* Move **A** counter-clockwise on $CH(S_1)$ as long as the line $AB$ does not have all points of $CH(S_1)$ below it.
* Move **B** clockwise on $CH(S_2)$ as long as the line $AB$ does not have all points of $CH(S_2)$ below it.
* Repeat until no more moves are possible for either **A** or **B**.

#### 3. Finding the Lower Tangent
The process is mirrored to find the lowest supporting line:
* Move **A** clockwise on $CH(S_1)$.
* Move **B** counter-clockwise on $CH(S_2)$.



### Phase III: Reconstruction
Once the upper and lower tangents are identified, the new Convex Hull is formed by:
* The vertices of $CH(S_1)$ between the two tangent contact points (walking along the outer side).
* The vertices of $CH(S_2)$ between the two tangent contact points.
* All points that fall "inside" the region bounded by the two tangents are discarded.

---

## 3. Summary of Traversal Directions

| Operation | CH(S1) Movement | CH(S2) Movement |
| :--- | :--- | :--- |
| **Upper Tangent Search** | Counter-clockwise (CCW) | Clockwise (CW) |
| **Lower Tangent Search** | Clockwise (CW) | Counter-clockwise (CCW) |

---

## 4. Complexity
* **Time Complexity:** $O(N \log N)$. The sorting takes $O(N \log N)$, and the recurrence $T(n) = 2T(n/2) + O(n)$ results in $O(N \log N)$ total time.
* **Space Complexity:** $O(N)$ for storing the points and the recursive structure.

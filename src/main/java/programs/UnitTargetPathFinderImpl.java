package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {
            {-1, 0}, {0, -1}, {1, 0}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Ваше решение
        boolean[][] obstacles = new boolean[WIDTH][HEIGHT];

        if (!targetUnit.isAlive()) {
            return Collections.emptyList();
        }
        for (Unit unit : existingUnitList) {
            if (unit == attackUnit || unit == targetUnit) {
                continue;
            }
            if (unit.isAlive()) {
                obstacles[unit.getxCoordinate()][unit.getyCoordinate()] = true;
            }
        }


        Node start = new Node(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Node goal = new Node(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());


        if (!isValid(start, obstacles) || !isValid(goal, obstacles)) {
            return Collections.emptyList();
        }


        if (start.x == goal.x && start.y == goal.y) {
            List<Edge> result = new ArrayList<>();
            result.add(new Edge(start.x, start.y));
            return result;
        }


        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Map<String, Node> cameFrom = new HashMap<>();
        Map<String, Integer> gScore = new HashMap<>();
        Map<String, Integer> fScore = new HashMap<>();


        String startKey = start.x + "," + start.y;
        String goalKey = goal.x + "," + goal.y;


        start.g = 0;
        start.f = heuristic(start, goal);

        openSet.add(start);
        gScore.put(startKey, 0);
        fScore.put(startKey, start.f);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            String currentKey = current.x + "," + current.y;


            if (current.x == goal.x && current.y == goal.y) {
                return reconstructPath(cameFrom, current);
            }


            for (int[] dir : DIRECTIONS) {
                Node neighbor = new Node(current.x + dir[0], current.y + dir[1]);
                String neighborKey = neighbor.x + "," + neighbor.y;


                if (!isValid(neighbor, obstacles)) {
                    continue;
                }


                int moveCost = (Math.abs(dir[0]) == 1 && Math.abs(dir[1]) == 1) ? 14 : 10;
                int tentativeGScore = gScore.getOrDefault(currentKey, Integer.MAX_VALUE) + moveCost;


                if (tentativeGScore < gScore.getOrDefault(neighborKey, Integer.MAX_VALUE)) {
                    cameFrom.put(neighborKey, current);
                    gScore.put(neighborKey, tentativeGScore);

                    int h = heuristic(neighbor, goal);
                    int f = tentativeGScore + h;
                    fScore.put(neighborKey, f);


                    Node neighborNode = new Node(neighbor.x, neighbor.y);
                    neighborNode.g = tentativeGScore;
                    neighborNode.f = f;


                    openSet.removeIf(node -> node.x == neighbor.x && node.y == neighbor.y);
                    openSet.add(neighborNode);
                }
            }
        }
        return Collections.emptyList();
    }

    private boolean isValid(Node node, boolean[][] obstacles) {

        return node.x >= 0 && node.x < WIDTH &&
                node.y >= 0 && node.y < HEIGHT &&
                !obstacles[node.x][node.y];
    }

    private List<Edge> reconstructPath(Map<String, Node> cameFrom, Node current) {
        List<Edge> path = new ArrayList<>();
        String currentKey = current.x + "," + current.y;


        path.add(new Edge(current.x, current.y));


        while (cameFrom.containsKey(currentKey)) {
            current = cameFrom.get(currentKey);
            currentKey = current.x + "," + current.y;
            path.add(0, new Edge(current.x, current.y));
        }

        return path;
    }

    private static class Node {
        final int x, y;
        int g;
        int f;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.g = 0;
            this.f = 0;
        }

        int getF() {
            return f;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private int heuristic(Node a, Node b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        return 10 * (dx + dy);
    }
}

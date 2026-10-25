import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Maze {
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());

        int[] broken = new int[w.cells() - 1];
        int count = 0;

        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];

            if (dsu.union(a, b)) {
                broken[count] = i;
                count++;
            }
        }

        return Arrays.copyOf(broken, count);
    }

    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];

        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        Arrays.sort(
                order,
                Comparator.comparingInt(i -> w.cost()[i])
        );

        DisjointSet dsu = new DisjointSet(w.cells());

        int[] broken = new int[w.cells() - 1];
        int count = 0;

        for (int i : order) {
            int a = w.a()[i];
            int b = w.b()[i];

            if (dsu.union(a, b)) {
                broken[count] = i;
                count++;
            }

            if (count == w.cells() - 1) {
                break;
            }
        }

        return Arrays.copyOf(broken, count);
    }

    public static long primCost(Input.Walls w) {
        int n = w.cells();

        int[] degree = new int[n];

        for (int i = 0; i < w.count(); i++) {
            degree[w.a()[i]]++;
            degree[w.b()[i]]++;
        }

        int[][] edges = new int[n][];

        for (int i = 0; i < n; i++) {
            edges[i] = new int[degree[i]];
        }

        int[] filled = new int[n];

        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];

            edges[a][filled[a]] = i;
            filled[a]++;

            edges[b][filled[b]] = i;
            filled[b]++;
        }

        boolean[] visited = new boolean[n];

        PriorityQueue<Integer> queue = new PriorityQueue<>(
                Comparator.comparingInt(i -> w.cost()[i])
        );

        visited[0] = true;

        for (int edge : edges[0]) {
            queue.add(edge);
        }

        long total = 0;
        int count = 1;

        while (!queue.isEmpty() && count < n) {
            int edge = queue.poll();

            int a = w.a()[edge];
            int b = w.b()[edge];

            if (visited[a] && visited[b]) {
                continue;
            }

            int next;

            if (visited[a]) {
                next = b;
            } else {
                next = a;
            }

            visited[next] = true;
            count++;

            total += w.cost()[edge];

            for (int i : edges[next]) {
                int from = w.a()[i];
                int to = w.b()[i];

                if (!visited[from] || !visited[to]) {
                    queue.add(i);
                }
            }
        }

        return total;
    }
}
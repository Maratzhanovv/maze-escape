import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Zones {
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] forward = Graphs.slidesFrom(s);
        int[][] reverse = Graphs.slidesInto(s);
        boolean[] visited = new boolean[n];
        int[] nextEdge = new int[n];
        int[] stack = new int[n];
        int[] order = new int[n];
        int finished = 0;
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }
            int top = 0;
            stack[0] = start;
            visited[start] = true;
            while (top >= 0) {
                int cell = stack[top];
                if (nextEdge[cell] < forward[cell].length) {
                    int edge = forward[cell][nextEdge[cell]];
                    nextEdge[cell]++;
                    int next = s.to()[edge];
                    if (!visited[next]) {
                        visited[next] = true;
                        top++;
                        stack[top] = next;
                    }
                } else {
                    order[finished] = cell;
                    finished++;
                    top--;
                }
            }
        }
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zone = 0;
        for (int i = finished - 1; i >= 0; i--) {
            int start = order[i];
            if (zoneOf[start] != -1) {
                continue;
            }
            int top = 0;
            stack[0] = start;
            zoneOf[start] = zone;
            while (top >= 0) {
                int cell = stack[top];
                top--;
                for (int edge : reverse[cell]) {
                    int next = s.from()[edge];
                    if (zoneOf[next] == -1) {
                        zoneOf[next] = zone;
                        top++;
                        stack[top] = next;
                    }
                }
            }
            zone++;
        }
        return zoneOf;
    }

    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        ArrayList<HashMap<Integer, Integer>> graph = new ArrayList<>();
        for (int i = 0; i < zoneCount; i++) {
            graph.add(new HashMap<>());
        }
        for (int i = 0; i < s.count(); i++) {
            int from = zoneOf[s.from()[i]];
            int to = zoneOf[s.to()[i]];
            int cost = s.time()[i];
            if (from == to) {
                continue;
            }
            HashMap<Integer, Integer> edges = graph.get(from);
            if (!edges.containsKey(to) || cost < edges.get(to)) {
                edges.put(to, cost);
            }
        }
        int[] stages = new int[zoneCount];
        Arrays.fill(stages, 1);
        long[] distance = new long[zoneCount];
        Arrays.fill(distance, Long.MAX_VALUE);
        distance[zoneOf[0]] = 0;
        int longest = 1;
        for (int from = 0; from < zoneCount; from++) {
            longest = Math.max(longest, stages[from]);
            for (Map.Entry<Integer, Integer> edge : graph.get(from).entrySet()) {
                int to = edge.getKey();
                int cost = edge.getValue();
                stages[to] = Math.max(stages[to], stages[from] + 1);
                if (distance[from] != Long.MAX_VALUE) {
                    distance[to] = Math.min(distance[to], distance[from] + cost);
                }
            }
        }
        return new long[]{longest, distance[zoneOf[s.n() - 1]]};
    }
}

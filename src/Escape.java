import java.util.Arrays;

public class Escape {
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int[][] graph = Graphs.open(w, broken);
        int deadEnds = 0;
        for (int[] neighbours : graph) {
            if (neighbours.length == 1) {
                deadEnds++;
            }
        }
        boolean[] visited = new boolean[n];
        int[] direction = new int[n];
        int[] stack = new int[n];
        int top = 0;
        stack[0] = 0;
        visited[0] = true;
        int count = 1;
        while (top >= 0) {
            int cell = stack[top];
            if (cell == n - 1) {
                return new Result(Arrays.copyOf(stack, top + 1), count, deadEnds);
            }
            if (direction[cell] == 4) {
                top--;
                continue;
            }
            int d = direction[cell];
            direction[cell]++;
            int next;
            if (d == 0) {
                next = cell - w.cols();
            } else if (d == 1) {
                if (cell % w.cols() == w.cols() - 1) {
                    continue;
                }
                next = cell + 1;
            } else if (d == 2) {
                next = cell + w.cols();
            } else {
                if (cell % w.cols() == 0) {
                    continue;
                }
                next = cell - 1;
            }
            if (next < 0 || next >= n || visited[next]) {
                continue;
            }
            boolean open = false;
            for (int neighbour : graph[cell]) {
                if (neighbour == next) {
                    open = true;
                    break;
                }
            }
            if (open) {
                visited[next] = true;
                count++;
                top++;
                stack[top] = next;
            }
        }
        return new Result(new int[0], count, deadEnds);
    }
}

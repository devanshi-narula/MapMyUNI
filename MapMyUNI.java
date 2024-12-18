import java.util.*;

class Location {
    String name;

    public Location(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Pathway {
    Location start;
    Location end;
    int distance;

    public Pathway(Location start, Location end, int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return start + " to " + end + " : " + distance + " meters";
    }
}

class Graph {
    Map<String, Location> locations = new HashMap<>();
    Map<Location, List<Pathway>> adjList = new HashMap<>();

    public void addLocation(String name) {
        Location location = new Location(name);
        locations.put(name, location);
        adjList.put(location, new ArrayList<>());
    }

    public void addPathway(String start, String end, int distance) {
        Location startLoc = locations.get(start);
        Location endLoc = locations.get(end);
        if (startLoc != null && endLoc != null) {
            Pathway pathway = new Pathway(startLoc, endLoc, distance);
            adjList.get(startLoc).add(pathway);
            adjList.get(endLoc).add(new Pathway(endLoc, startLoc, distance)); // assuming bidirectional paths
        } else {
            System.out.println("Invalid pathway: " + start + " to " + end);
        }
    }

    public List<Location> findShortestPath(String startName, String endName) {
        Location start = locations.get(startName);
        Location end = locations.get(endName);

        if (start == null || end == null) {
            System.out.println("Invalid locations.");
            return Collections.emptyList();
        }

        Map<Location, Integer> distances = new HashMap<>();
        Map<Location, Location> previous = new HashMap<>();
        PriorityQueue<Location> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Location loc : locations.values()) {
            distances.put(loc, Integer.MAX_VALUE);
            previous.put(loc, null);
        }

        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (current.equals(end)) break;

            for (Pathway pathway : adjList.get(current)) {
                Location neighbor = pathway.end;
                int newDist = distances.get(current) + pathway.distance;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<Location> path = new ArrayList<>();
        for (Location at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public int getPathDistance(List<Location> path) {
        int distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Location from = path.get(i);
            Location to = path.get(i + 1);
            for (Pathway pathway : adjList.get(from)) {
                if (pathway.end.equals(to)) {
                    distance += pathway.distance;
                    break;
                }
            }
        }
        return distance;
    }
}

public class MapMyUNI {
    public static void main(String[] args) {
        Graph campusGraph = new Graph();

        // Initialize locations with descriptive names
        campusGraph.addLocation("Main Entrance");
        campusGraph.addLocation("MAC");
        campusGraph.addLocation("Enrollment Office");
        campusGraph.addLocation("Hubble");
        campusGraph.addLocation("Quadrangle");
        campusGraph.addLocation("Library");
        campusGraph.addLocation("IT Block");
        campusGraph.addLocation("Design Block");
        campusGraph.addLocation("Frisco");
        campusGraph.addLocation("Gandhi Chowk");
        campusGraph.addLocation("Computer Science Block");
        campusGraph.addLocation("Health Sciences Block");
        campusGraph.addLocation("Science Laboratories Block");
        campusGraph.addLocation("Infirmary");
        campusGraph.addLocation("Boys Hostel");
        campusGraph.addLocation("Football Arena & Girls Hostel");
        campusGraph.addLocation("Food Court");
        campusGraph.addLocation("Management Block");
        campusGraph.addLocation("Girls Hostel");

        // Initialize pathways with updated names
        campusGraph.addPathway("Main Entrance", "MAC", 150);
        campusGraph.addPathway("Main Entrance", "Library", 292);
        campusGraph.addPathway("MAC", "Library", 130);
        campusGraph.addPathway("MAC", "Design Block", 110);
        campusGraph.addPathway("Library", "Design Block", 40);
        campusGraph.addPathway("Design Block", "Frisco", 134);
        campusGraph.addPathway("Library", "Frisco", 130);
        campusGraph.addPathway("Frisco", "Computer Science Block", 145);
        campusGraph.addPathway("Gandhi Chowk", "Health Sciences Block", 141);
        campusGraph.addPathway("Design Block", "Football Arena & Girls Hostel", 163);
        campusGraph.addPathway("Main Entrance", "Gandhi Chowk", 310);
        campusGraph.addPathway("Main Entrance", "Science Laboratories Block", 445);
        campusGraph.addPathway("MAC", "Football Arena & Girls Hostel", 324);
        campusGraph.addPathway("Main Entrance", "Food Court", 156);
        campusGraph.addPathway("Gandhi Chowk", "Girls Hostel", 215);
        campusGraph.addPathway("Gandhi Chowk", "Infirmary", 115);
        campusGraph.addPathway("Gandhi Chowk", "Boys Hostel", 135);
        campusGraph.addPathway("Food Court", "Gandhi Chowk", 108);
        campusGraph.addPathway("Main Entrance", "Infirmary", 187);
        campusGraph.addPathway("Food Court", "Computer Science Block", 101);
        campusGraph.addPathway("Gandhi Chowk", "Management Block", 188);
        campusGraph.addPathway("Main Entrance", "Enrollment Office", 91);
        campusGraph.addPathway("Food Court", "Frisco", 40);
        campusGraph.addPathway("MAC", "Gandhi Chowk", 143);
        campusGraph.addPathway("Main Entrance", "Quadrangle", 105);
        campusGraph.addPathway("MAC", "Frisco", 162);
        campusGraph.addPathway("MAC", "IT Block", 130);
        campusGraph.addPathway("Frisco", "Computer Science Block", 108);
        campusGraph.addPathway("Gandhi Chowk", "Computer Science Block", 20);

        // User interface for finding the shortest path
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter start location: ");
        String start = scanner.nextLine();
        System.out.print("Enter destination location: ");
        String end = scanner.nextLine();

        List<Location> path = campusGraph.findShortestPath(start, end);
        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Shortest path from " + start + " to " + end + ":");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i < path.size() - 1) {
                    System.out.print(" → ");
                }
            }
            System.out.println("\nTotal Distance: " + campusGraph.getPathDistance(path) + " meters");
        }
    }
}

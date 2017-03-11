import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation for usage of kd tree with arguments in shell.
 * @author Doga Can Yanikoglu
 * @since 3/11/2017
 * @version 1.0
 * Required JAVA Version: 1.8
 */

public class KDTreeQuery {
    private static KDTree tree;

    private enum Directive {
        insert, remove, search, findminx, findminy, findmaxx, findmaxy, displaytree, displaypoints, range, quit;

        static Directive safeValueOf(final String s) {
            try {
                return Directive.valueOf(s);
            } catch (final IllegalArgumentException e) {
                System.err.println("\nInvalid directive");
                return quit;
            }
        }

        void process(String[] args) {
            switch (this) {
                case insert: {
                    double x, y;
                    if (args.length != 3) {
                        System.err.println("\nInvalid parameter for insert directive");
                        return;
                    }
                    try {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        System.err.println("\nOne of given parameter for insert directive is NaN");
                        return;
                    }

                    tree.insert(new Point2D.Double(x, y));
                    break;
                }
                case remove: {
                    double x, y;
                    if (args.length != 3) {
                        System.err.println("\nInvalid parameter for remove directive");
                        return;
                    }
                    try {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        System.err.println("\nOne of given parameter for remove directive is NaN");
                        return;
                    }

                    tree.remove(new Point2D.Double(x, y));
                    break;
                }
                case search: {
                    double x, y;
                    if (args.length != 3) {
                        System.err.println("\nInvalid parameter for search directive");
                        return;
                    }
                    try {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        System.err.println("\nOne of given parameter for search directive is NaN");
                        return;
                    }

                    if(tree.search(new Point2D.Double(x, y)) == null) {
                        System.out.printf("\nNot Found (%s, %s)\n", x, y);
                    } else {
                        System.out.printf("\nFound (%s, %s)\n", x, y);
                    }
                    break;
                }
                case findminx: {
                    Point2D p = tree.findMin(0);
                    System.out.printf("\nMinimum X-Coord Point: (%s, %s)\n", p.getX(), p.getY());
                    break;
                }
                case findminy: {
                    Point2D p = tree.findMin(1);
                    System.out.printf("\nMinimum Y-Coord Point: (%s, %s)\n", p.getX(), p.getY());
                    break;
                }
                case findmaxx: {
                    Point2D p = tree.findMax(0);
                    System.out.printf("\nMaximum X-Coord Point: (%s, %s)\n", p.getX(), p.getY());
                    break;
                }
                case findmaxy: {
                    Point2D p = tree.findMax(1);
                    System.out.printf("\nMaximum Y-Coord Point: (%s, %s)\n", p.getX(), p.getY());
                    break;
                }
                case displaytree:
                    System.out.println("\nDisplaying tree:");
                    tree.displayTree();
                    break;
                case displaypoints:
                    System.out.println("\nDisplaying all points of tree:");
                    tree.displayPoints();
                    break;
                case range: {
                    double llx, lly, urx, ury;
                    if (args.length != 5) {
                        System.err.println("\nInvalid parameter for range directive");
                        return;
                    }
                    try {
                        llx = Double.parseDouble(args[1]);
                        lly = Double.parseDouble(args[2]);
                        urx = Double.parseDouble(args[3]);
                        ury = Double.parseDouble(args[4]);
                    } catch (NumberFormatException e) {
                        System.err.println("\nOne of given parameter for search directive is NaN");
                        return;
                    }
                    System.out.println("\nDisplaying points in given range:");
                    tree.printRange(new Point2D.Double(llx, lly), new Point2D.Double(urx, ury));
                    break;
                }
                case quit:
                    System.out.println("\nEnd of directives...");
                    System.exit(0);
                default:
                    System.err.println("\nUnknown directive, terminating...");
                    System.exit(1);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        String pointsFileName = args[0];
        String directiveFileName = args[1];
        List<String> pointsList = new ArrayList<>();
        List<String> directivesList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(pointsFileName))) {

            //br returns as stream and convert it into a List
            pointsList = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = Files.newBufferedReader(Paths.get(directiveFileName))) {

            //br returns as stream and convert it into a List
            directivesList = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Point2D> allPoints = new ArrayList<>();

        for (String line : pointsList) {
            String[] points = line.split(" ");
            try {
                allPoints.add(new Point2D.Double(Double.parseDouble(points[0]), Double.parseDouble(points[1])));
            } catch(NumberFormatException e){
                System.err.println("\nInvalid file content for points");
            }
        }

        tree = KDTree.buildKDTree(allPoints);

        for (String line : directivesList) {
            line = line.replaceAll("[ ]", "\t"); // In case of usage of tab instead of space
            String[] directive = line.split("\t");
            Directive d = Directive.safeValueOf(directive[0].replace("-","").toLowerCase());
            d.process(directive);
        }
    }
}
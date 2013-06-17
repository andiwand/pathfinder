package pathfinder;

import graph.Path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;

import util.GraphicsUtil;
import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.vector.Vector2d;

public class PathDrawer extends JComponent {

    private static final long serialVersionUID = -5163620295115375429L;

    private static final double PLOT_RADIUS = 3;
    private static final Vector2d PLOT_TEXT_OFFSET = new Vector2d(0, -5);

    private static final int DEBUG_NO = 0x0;
    private static final int DEBUG_PATH_VERTICES = 0x1;
    private static final int DEBUG_PATH_EDGES = 0x2;

    private Vector2d pointA;
    private Vector2d pointB;

    private AbstractWall newWall;

    private FloorMap map = new FloorMap(5);

    private Pathfinder pathfinder = new Pathfinder(map);
    private List<GeometryLine2D> path;

    private int debugMode = DEBUG_NO;

    public PathDrawer() {
	MouseHandler mouseHandler = new MouseHandler();
	addMouseListener(mouseHandler);
	addMouseMotionListener(mouseHandler);

	DebugKeyHandler debugKeyHandler = new DebugKeyHandler();
	addKeyListener(debugKeyHandler);
    }

    private void recalc() {
	if ((pointA == null) || (pointB == null))
	    return;

	Path path = pathfinder.calculatePath(pointA, pointB);

	if (path == null)
	    this.path = new ArrayList<GeometryLine2D>();
	else
	    this.path = path.getLines();
    }

    private void refresh() {
	recalc();
	repaint();
    }

    protected void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
	g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
		RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

	GraphicsUtil graphicsUtil = new GraphicsUtil(g2);

	if ((debugMode & DEBUG_PATH_EDGES) != 0) {
	    g2.setColor(Color.GRAY);
	    for (GeometryLine2D edgeLine : pathfinder.getGraph().getEdgeLines()) {
		graphicsUtil.drawLine(edgeLine);
	    }
	}

	if ((debugMode & DEBUG_PATH_VERTICES) != 0) {
	    g2.setColor(Color.DARK_GRAY);
	    for (Vector2d pathVertex : map.getPathVertices()) {
		graphicsUtil.fillCircle(pathVertex, 1.5);
	    }
	}

	if (newWall != null) {
	    g2.setColor(Color.GRAY);
	    graphicsUtil.drawLine(newWall.getLine());
	}

	g2.setColor(Color.BLACK);
	for (AbstractWall wall : map.getWalls()) {
	    graphicsUtil.drawLine(wall.getLine());
	}

	if (path != null) {
	    g2.setColor(Color.ORANGE);
	    graphicsUtil.drawLines(path);
	}

	if (pointA != null) {
	    g2.setColor(Color.GREEN);
	    graphicsUtil.fillCircle(pointA, PLOT_RADIUS);
	    graphicsUtil.drawXCenterString(pointA.add(PLOT_TEXT_OFFSET), "A");
	}
	if (pointB != null) {
	    g2.setColor(Color.RED);
	    graphicsUtil.fillCircle(pointB, PLOT_RADIUS);
	    graphicsUtil.drawXCenterString(pointB.add(PLOT_TEXT_OFFSET), "B");
	}
    }

    private class MouseHandler extends MouseAdapter {
	private Vector2d start;
	private boolean dragged;

	public void mousePressed(MouseEvent e) {
	    start = new Vector2d(e.getPoint());
	}

	public void mouseReleased(MouseEvent e) {
	    if (!dragged)
		return;

	    map.addWall(newWall);
	    newWall = null;
	    dragged = false;

	    refresh();
	}

	public void mouseClicked(MouseEvent e) {
	    Vector2d mouse = new Vector2d(e.getPoint());

	    if (e.getButton() == 1)
		pointA = mouse;
	    else if (e.getButton() == 3)
		pointB = mouse;
	    else
		return;

	    refresh();
	}

	public void mouseDragged(MouseEvent e) {
	    Vector2d mouse = new Vector2d(e.getPoint());

	    newWall = new UndirectedWall(start, mouse);
	    dragged = true;

	    repaint();
	}
    }

    private class DebugKeyHandler extends KeyAdapter {
	private int code = 0;
	private int[] codeMap = { DEBUG_NO, DEBUG_PATH_VERTICES,
		DEBUG_PATH_VERTICES | DEBUG_PATH_EDGES };

	public void keyTyped(KeyEvent e) {
	    if (e.getKeyChar() != ' ')
		return;

	    if (++code >= codeMap.length)
		code = 0;
	    debugMode = codeMap[code];

	    repaint();
	}
    }

    public static void main(String[] args) {
	JFrame frame = new JFrame("Pathfinder");

	PathDrawer pathDrawer = new PathDrawer();
	readDatabase(pathDrawer);
	pathDrawer.pathfinder.updateGraph();
	frame.add(pathDrawer);

	frame.setSize(500, 500);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);

	pathDrawer.requestFocusInWindow();
    }

    public static void readDatabase(PathDrawer pathDrawer) {
	Map<Integer, Vector2d> pointMap = new HashMap<Integer, Vector2d>();
	Map<Integer, List<Vector2d>> edgeMap = new HashMap<Integer, List<Vector2d>>();
	Set<Integer> wallSet = new HashSet<Integer>();

	InputStream inputStream = null;

	try {
	    inputStream = PathDrawer.class
		    .getResourceAsStream("shopping_center_inserts.sql");
	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    inputStream));

	    for (String line = reader.readLine(); line != null; line = reader
		    .readLine()) {
		Pattern pointPattern = Pattern
			.compile("INSERT INTO points \\(id, x, y\\) VALUES\\((.*?), (.*?), (.*?)\\);");
		Matcher pointMatcher = pointPattern.matcher(line);

		if (pointMatcher.matches()) {
		    int id = Integer.parseInt(pointMatcher.group(1));
		    int x = Integer.parseInt(pointMatcher.group(2));
		    int y = Integer.parseInt(pointMatcher.group(3));

		    Vector2d point = new Vector2d(x, y).div(100);

		    pointMap.put(id, point);

		    continue;
		}

		Pattern edgePattern = Pattern
			.compile("INSERT INTO edges \\(id\\) VALUES\\((.*?)\\);");
		Matcher edgeMatcher = edgePattern.matcher(line);

		if (edgeMatcher.matches()) {
		    int id = Integer.parseInt(edgeMatcher.group(1));

		    edgeMap.put(id, new ArrayList<Vector2d>());

		    continue;
		}

		Pattern points2edgesPattern = Pattern
			.compile("INSERT INTO points2edges \\(edge_id, point_id, succession\\) VALUES\\((.*?), (.*?), (.*?)\\);");
		Matcher points2edgesMatcher = points2edgesPattern.matcher(line);

		if (points2edgesMatcher.matches()) {
		    int edgeId = Integer.parseInt(points2edgesMatcher.group(1));
		    int pointId = Integer
			    .parseInt(points2edgesMatcher.group(2));

		    edgeMap.get(edgeId).add(pointMap.get(pointId));

		    continue;
		}

		Pattern wallPattern = Pattern
			.compile("INSERT INTO walls \\(id\\) VALUES\\((.*?)\\);");
		Matcher wallMatcher = wallPattern.matcher(line);

		if (wallMatcher.matches()) {
		    int edgeId = Integer.parseInt(wallMatcher.group(1));

		    wallSet.add(edgeId);

		    continue;
		}
	    }

	    for (Map.Entry<Integer, List<Vector2d>> edgeEntry : edgeMap
		    .entrySet()) {
		int edgeId = edgeEntry.getKey();
		if (!wallSet.contains(edgeId))
		    continue;

		List<Vector2d> vertices = edgeEntry.getValue();
		if (vertices.size() != 2)
		    continue;

		pathDrawer.map.addWall(new UndirectedWall(vertices.get(0),
			vertices.get(1)));
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		inputStream.close();
	    } catch (Throwable t) {
	    }
	}
    }

}
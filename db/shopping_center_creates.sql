-- SQLite 

-- script consists of 2 steps:

-- 1. drop tables if they exist
-- 1.1. points
-- 1.2. edges and subtypes
-- 1.3. zones
-- 1.4. navigables and subtypes
-- 1.5. routes and waypoints
-- 1.6. fingerprints
-- 1.7. devices and calibration
-- 1.8. mislocations

-- 2. create tables
-- 2.0. metadata
-- 2.1. points
-- 2.2. edges and subtypes
-- 2.3. zones
-- 2.4. navigables and subtypes
-- 2.5. routes and waypoints
-- 2.6. fingerprints
-- 2.7. devices and calibration
-- 2.8. mislocations

-- 1. drop tables if they exist

-- 1.1. points
-- entities
DROP TABLE IF EXISTS points;

-- 1.2. edges and subtypes
-- entities
DROP TABLE IF EXISTS edges;
DROP TABLE IF EXISTS walls;
DROP TABLE IF EXISTS boundaries;
DROP TABLE IF EXISTS portals;
-- associative tables
DROP TABLE IF EXISTS points2edges;
DROP TABLE IF EXISTS portal2portals;

-- 1.3. zones
-- entities
DROP TABLE IF EXISTS zones;
-- associative tables
DROP TABLE IF EXISTS edges2zones;

-- 1.4. navigables and subtypes
-- entities
DROP TABLE IF EXISTS navigables;
DROP TABLE IF EXISTS floors;
DROP TABLE IF EXISTS building;
-- associative tables
DROP TABLE IF EXISTS points2navigables;
DROP TABLE IF EXISTS edges2navigables;
DROP TABLE IF EXISTS zones2navigables;

-- 1.5. routes and waypoints
-- entities
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS waypoint;
-- associative tables
DROP TABLE IF EXISTS waypoint2waypoints;

-- 1.6. fingerprints
-- NOT YET IMPLEMENTED

-- 1.7. devices and calibration
-- NOT YET IMPLEMENTED

-- 1.8. mislocations
-- NOT YET IMPLEMENTED

-- 2. create tables

-- 2.0. metadata
-- NOT YET IMPLEMENTED

-- 2.1. points
-- entities
CREATE TABLE points
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	x INTEGER NOT NULL,
	y INTEGER NOT NULL
)

-- 2.2. edges and subtypes
-- entities
CREATE TABLE edges
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT
)

CREATE TABLE walls
(
	id INTEGER NOT NULL PRIMARY KEY,
	FOREIGN KEY (id) REFERENCES edges(id)
)

CREATE TABLE boundaries
(
	id INTEGER NOT NULL PRIMARY KEY,
	duration INTEGER NOT NULL,
	FOREIGN KEY (id) REFERENCES edges(id)
)

CREATE TABLE portals
(
	id INTEGER NOT NULL PRIMARY KEY,
	FOREIGN KEY (id) REFERENCES edges(id)
)

-- associative tables
CREATE TABLE points2edges
(
	edge_id INTEGER NOT NULL,
	point_id INTEGER NOT NULL,
	succession INTEGER NOT NULL,
	FOREIGN KEY (edge_id) REFERENCES edges(id),
	FOREIGN KEY (point_id) REFERENCES points(id),
	PRIMARY KEY (edge_id, point_id)
)

CREATE TABLE portal2portals
(
	source_portal_id INTEGER NOT NULL,
	target_portal_id INTEGER NOT NULL,
	duration INTEGER NOT NULL,
	succession INTEGER NOT NULL,
	name TEXT,
	FOREIGN KEY(source_portal_id) REFERENCES portals(id),
	FOREIGN KEY(target_portal_id) REFERENCES portals(id),
	PRIMARY KEY(source_portal_id, target_portal_id)
)

-- 2.3. zones
-- entities
CREATE TABLE zones
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	title TEXT NOT NULL,
	description TEXT NOT NULL
)

-- associative tables
CREATE TABLE edges2zones
(
	zone_id INTEGER NOT NULL,
	edge_id INTEGER NOT NULL,
	FOREIGN KEY (zone_id) REFERENCES zones(id),
	FOREIGN KEY (edge_id) REFERENCES edges(id),
	PRIMARY KEY(zone_id, edge_id)
)

-- 2.4. navigables and subtypes
-- entities
CREATE TABLE navigables
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	name TEXT NOT NULL,
	description TEXT
)

CREATE TABLE floors
(
	id INTEGER NOT NULL PRIMARY KEY,
	FOREIGN KEY (id) REFERENCES navigables(id)
)

CREATE TABLE buildings
(
	id INTEGER NOT NULL PRIMARY KEY,
	FOREIGN KEY (id) REFERENCES navigables(id)
)

-- associative tables
CREATE TABLE points2navigables
(
	point_id INTEGER NOT NULL,
	navigable_id INTEGER NOT NULL,
	FOREIGN KEY (point_id) REFERENCES points(id),
	FOREIGN KEY (navigable_id) REFERENCES navigables(id),
	PRIMARY KEY (point_id, navigable_id)
)

CREATE TABLE edges2navigables
(
	edge_id INTEGER NOT NULL,
	navigable_id INTEGER NOT NULL,
	FOREIGN KEY (edge_id) REFERENCES edges(id),
	FOREIGN KEY (navigable_id) REFERENCES navigables(id),
	PRIMARY KEY (edge_id, navigable_id)
)

CREATE TABLE zones2navigables
(
	zone_id INTEGER,
	navigable_id INTEGER,
	FOREIGN KEY (zone_id) REFERENCES zones(id),
	FOREIGN KEY (navigable_id) REFERENCES navigables(id)
	PRIMARY KEY (zone_id, navigable_id)
)

-- 2.5. routes and waypoints
-- entities
CREATE TABLE routes
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	name TEXT NOT NULL
)

CREATE TABLE waypoint
(
	route_id INTEGER NOT NULL,
	floor_id INTEGER NOT NULL,
	point_id INTEGER NOT NULL,
	FOREIGN KEY (route_id) REFERENCES routes(id),
	FOREIGN KEY (floor_id) REFERENCES floors(id),
	FOREIGN KEY (point_id) REFERENCES points(id),
	PRIMARY KEY (route_id, floor_id, point_id)
)

-- associative tables
CREATE TABLE waypoint2waypoints
(
	from_waypoint_route_id INTEGER NOT NULL,
	from_waypoint_floor_id INTEGER NOT NULL,
	from_waypoint_point_id INTEGER NOT NULL,
	to_waypoint_route_id INTEGER NOT NULL,
	to_waypoint_floor_id INTEGER NOT NULL,
	to_waypoint_point_id INTEGER NOT NULL,
	FOREIGN KEY (from_waypoint_route_id) REFERENCES waypoint(route_id),
	FOREIGN KEY (from_waypoint_floor_id) REFERENCES waypoint(floor_id),
	FOREIGN KEY (from_waypoint_point_id) REFERENCES waypoint(point_id),
	FOREIGN KEY (to_waypoint_route_id) REFERENCES waypoint(route_id),
	FOREIGN KEY (to_waypoint_floor_id) REFERENCES waypoint(floor_id),
	FOREIGN KEY (to_waypoint_point_id) REFERENCES waypoint(point_id),
	PRIMARY KEY (from_waypoint_route_id, from_waypoint_floor_id, from_waypoint_point_id,
	             to_waypoint_route_id, to_waypoint_floor_id, to_waypoint_point_id)
)

-- 2.6. fingerprints
-- NOT YET IMPLEMENTED

-- 2.7. devices and calibration
-- NOT YET IMPLEMENTED

-- 2.8. mislocations
-- NOT YET IMPLEMENTED

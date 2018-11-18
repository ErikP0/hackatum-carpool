from cp_backend.models import Routes, DestinationType
import csv

def load_routes(db, click):
    click.echo("loading routes")

    csv.register_dialect("route_dialect", delimiter=";")

    with open(r"table_load/routes.csv") as csvfile:
        reader = csv.DictReader(csvfile, dialect="route_dialect")

        for row in reader:
            route = Routes(
                route_id = row["route_id"],
                path_name = row["path_name"],
                city = row["city"]
            )

            if row["origin_destination"] == "destination":
                route.origin_destination = DestinationType.DESTINATION
            elif row["origin_destination"] == "origin":
                route.origin_destination = DestinationType.ORIGIN

            db.session.add(route)

    db.session.commit()
    click.echo("route data loaded")
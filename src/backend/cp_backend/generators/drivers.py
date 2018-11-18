from cp_backend.models import User
import csv

def load_drivers(db, click):
    click.echo("loading drivers")

    csv.register_dialect("driver_dialect", delimiter=";")

    with open(r"table_load/drivers.csv") as csvfile:
        reader = csv.DictReader(csvfile, dialect="driver_dialect")

        for row in reader:
            user = User(
                id = row["driver_id"],
                username = "{0}.{1}".format(row["first_name"], row["second_name"]),
                first_name = row["first_name"],
                second_name = row["second_name"],
                email = "{0}.{1}@sixt.de".format(row["first_name"], row["second_name"]),
                password = row["first_name"],
                active = True
            )

    click.echo("driver data loaded")
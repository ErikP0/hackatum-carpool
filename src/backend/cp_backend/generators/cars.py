from cp_backend.models import Cars
import csv
import datetime

def load_cars(db, click):
    click.echo("creating car data")

    csv.register_dialect("car_dialect", delimiter=";")

    with open(r"table_load/cars.csv") as csvfile:
        reader = csv.DictReader(csvfile, dialect="car_dialect")

        for row in reader:
            date_components = row["purchase_date"].split("/")
            purchase_date = datetime.date(
                year = int(date_components[2]),
                month = int(date_components[1]),
                day = int(date_components[0])
            )

            purchase_datetime = datetime.datetime.combine(
                date=purchase_date,
                time=datetime.datetime.min.time()
            )
            car = Cars(
                car_id = row["car_id"],
                name = row["name"],
                type = row["type"],
                full_price = row["full_price"],
                purchase_date = purchase_datetime,
                rental_price_day = row["rental_price_day"],
                avg_cost_day = row["avg_cost_day"]
            )

            db.session.add(car)

    db.session.commit()
    click.echo("car data loaded")

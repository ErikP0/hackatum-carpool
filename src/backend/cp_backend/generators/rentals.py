from cp_backend.models import CarRental
import datetime

def load_rentals(db, click):
    click.echo("creating rental data")

    rental = CarRental(
        id="1",
        car_id=1,
        driver_id=1,
        max_passengers=4,
        imageurl="https://example.org/car.png",
        route_id = 1,
        pickup_time=datetime.datetime(2018, 11, 18, 18, 00),
        return_time=datetime.datetime(2018, 11, 18, 21, 00)
    )
    db.session.add(rental)
    db.session.commit()

    click.echo("rental data loaded")
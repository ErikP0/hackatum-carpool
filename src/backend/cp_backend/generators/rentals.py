from cp_backend.models import CarRental
import datetime

def load_rentals(db, click):
    click.echo("creating rental data")

    rental = CarRental(
        id="deadbeef",
        car_name="BMW Model i8",
        max_passengers=2,
        imageurl="https://example.org/car.png",
        pickup_station_id=1,
        pickup_station_name="Munich Airport",
        pickup_time=datetime.datetime(2018, 11, 18, 18, 00),
        return_station_id=2,
        return_station_name="Gattinger Street",
        return_time=datetime.datetime(2018, 11, 18, 21, 00),
        price=50.00
    )
    db.session.add(rental)
    db.session.commit()

    click.echo("rental data loaded")
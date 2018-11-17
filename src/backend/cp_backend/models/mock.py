from cp_backend.extensions import db

class CarRental(db.Model):
    id = db.Column(db.String, primary_key=True)
    car_name = db.Column(db.String, nullable=False)
    max_passengers = db.Column(db.Integer, nullable=False)
    imageurl = db.Column(db.String, nullable=False)

    pickup_station_id = db.Column(db.BigInteger, nullable=False)
    pickup_station_name = db.Column(db.String, nullable=False)
    pickup_time = db.Column(db.DateTime, nullable=False)

    return_station_id = db.Column(db.BigInteger, nullable=False)
    return_station_name = db.Column(db.String, nullable=False)
    return_time = db.Column(db.DateTime, nullable=False)

    price = db.Column(db.Float(precision=2), nullable=False)

    def __init__(self, **kwargs):
        super(CarRental, self).__init__(**kwargs)
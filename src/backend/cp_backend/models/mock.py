from cp_backend.extensions import db

import enum

class DestinationType(enum.Enum):
    ORIGIN = "origin"
    DESTINATION = "destination"

class Routes(db.Model):
    __tablename__ = "routes"
    route_id = db.Column(db.Integer, primary_key=True)
    path_name = db.Column(db.String(80), primary_key=True)
    origin_destination = db.Column(db.Enum(DestinationType), primary_key=True)
    city = db.Column(db.String(80), nullable=False)

class Cars(db.Model):
    __tablename__ = "cars"

    car_id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(80), nullable=False)
    type = db.Column(db.String(80), nullable=False)
    full_price = db.Column(db.Integer, nullable=False)
    purchase_date = db.Column(db.DateTime, nullable=False)
    rental_price_day = db.Column(db.Integer, nullable=False) 
    avg_cost_day = db.Column(db.Integer, nullable=False)

class CarRental(db.Model):
    __tablename__ = "car_rentals"
    
    id = db.Column(db.String, primary_key=True)
    driver_id = db.Column(db.Integer, db.ForeignKey("user.id"))
    car_id = db.Column(db.Integer, db.ForeignKey("cars.car_id"))
    max_passengers = db.Column(db.Integer, nullable=False)
    imageurl = db.Column(db.String)
    route_id = db.Column(db.Integer, db.ForeignKey("routes.route_id"))
    pickup_time = db.Column(db.DateTime, nullable=False)
    return_time = db.Column(db.DateTime, nullable=False)

    def __init__(self, **kwargs):
        super(CarRental, self).__init__(**kwargs)
        self.max_passengers = 4
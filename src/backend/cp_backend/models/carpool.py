from cp_backend.extensions import db
from datetime import datetime
from dateutil import parser

import enum
import requests
import json

class CurrencyType(enum.Enum):
    EURO = "€"
    DOLLAR = "$"

class CarPool(db.Model):
    """
    Model of shared trip via car pooling
    """
    __tablename__ = "carpools"
    id = db.Column(db.Integer, primary_key=True)
    created_on = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)
    created_by = db.Column(db.Integer, db.ForeignKey("user.id"))

    start_date = db.Column(db.DateTime, nullable=False)
    end_date = db.Column(db.DateTime, nullable=False)

    car_model = db.Column(db.String, nullable=False)
    max_seats = db.Column(db.Integer, nullable=False)
    rental_id = db.Column(db.BigInteger, nullable=False)

    buy_in = db.Column(db.Float(precision=2), nullable=False)
    buy_in_currency = db.Column(db.Enum(CurrencyType), nullable=False)

    def __init__(self, **kwargs):
        super(CarPool, self).__init__(**kwargs)

    def validate(self):
        pdb.set_trace()
        result = requests.get("{0}/rentals/{1}".format("http://localhost:5000/api/v1", self.rental_id))

        if result.status_code != 200:
            raise ValueError("Supplied rental ID not found")

        rental = json.loads(result.content)

        if self.start_date < parser.parse(rental["rental"]["pickup_time"]):
            raise ValueError("Trip start time can not be before pick up time")
        
        if self.end_date > parser.parse(rental["rental"]["return_time"]):
            raise ValueError("Trip end time can not be after return time")
        
        if self.car_model != rental["rental"]["car_name"]:
            raise ValueError("Car models do not match")
        
        passenger_constraint = int(rental["rental"]["max_passengers"]) - 1
        if self.max_seats > passenger_constraint:
            raise ValueError("Maximum seat count of {0} exceeded".format(passenger_constraint))

class CarPoolParticipant(db.Model):
    """
    Model of all car pool participants (e.g. driver + passengers)
    """
    __tablename__ = "carpool_participants"
    __table_args__ = {}

    id = db.Column(db.Integer, primary_key=True)
    participant_id = db.Column(db.Integer, primary_key=True)
    role = db.Column(db.Integer, nullable=False)

#class CarPoolRequest(db.Model):
    #"""
    #Model of open requests to join a car pool
    #"""
    #pass
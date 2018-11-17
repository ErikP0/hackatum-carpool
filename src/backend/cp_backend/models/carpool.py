from cp_backend.extensions import db
from datetime import datetime

from flask import Flask

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
        self.created_on = datetime.now()

    def validate(self):
        #TODO: constraint checking via rental API
        app = Flask(__name__)
        result = requests.get("{0}/rentals/{1}".format(app.config["RENTAL_API_BASE"], rental_id))

        if result.status_code != 200:
            raise ValueError("Supplied rental ID not found")

        rental = json.loads(result.content)

        if self.start_date < rental["pickup_time"]:
            raise ValueError("Trip start time can not be before pick up time")
        
        if self.end_date > rental["return_time"]:
            raise ValueError("Trip end time can not be after return time")
        
        if self.car_model != rental["car_name"]:
            raise ValueError("Car models do not match")
        
        passenger_constraint = int(rental["max_passengers"]) - 1
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
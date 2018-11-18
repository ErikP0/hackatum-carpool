from flask import request
from flask_restful import Resource

from cp_backend.models import CarRental, Cars, Routes, DestinationType
from cp_backend.extensions import ma, db
from cp_backend.commons.pagination import paginate

import datetime
import time

class RentalSchema(ma.Schema):
    class Meta:
        fields = (
            'id',
            'driver_id',
            'name',
            'maxPassengers',
            'imageUrl',
            'pickupStationName',
            'pickupTime',
            'returnStationName',
            'returnTime',
            'price'
        )

class RentalViewModel:
    id = None
    driver_id = None
    name = None
    maxPassengers = None
    imageUrl = None
    pickupStationName = None
    pickupTime = None
    returnStationName = None
    returnTime = None
    price = None

class RentalResource(Resource):
    def get(self, rental_id):
        schema = RentalSchema()

        rental = CarRental.query.get_or_404(rental_id)
        car = Cars.query.get_or_404(rental.car_id)
        route_origin = Routes.query.filter_by(
            route_id = rental.route_id,
            origin_destination = DestinationType.ORIGIN
        ).first()
        route_destination = Routes.query.filter_by(
            route_id = rental.route_id,
            origin_destination = DestinationType.DESTINATION
        ).first()

        rental_view = RentalViewModel()

        rental_view.id = rental.id
        rental_view.driver_id = rental.driver_id
        rental_view.name = car.name
        rental_view.imageUrl = rental.imageurl
        rental_view.maxPassengers = rental.max_passengers

        rental_view.pickupStationName = route_origin.city
        rental_view.returnStationName = route_destination.city

        rental_view.pickupTime = time.mktime(rental.pickup_time.timetuple())
        rental_view.returnTime = time.mktime(rental.return_time.timetuple())
        rental_view.price = 0

        return { "rental": schema.dump(rental_view).data }

class RentalList(Resource):
    def get(self):
        schema = RentalSchema(many=True)

        rental_list = []

        for rental in CarRental.query.all():
            car = Cars.query.filter_by(car_id=rental.car_id).first()
            route_origin = Routes.query.filter_by(route_id=rental.route_id, origin_destination=DestinationType.ORIGIN).first()
            route_destination = Routes.query.filter_by(route_id=rental.route_id, origin_destination=DestinationType.DESTINATION).first()

            rental_view = RentalViewModel()

            rental_view.id = rental.id
            rental_view.driver_id = rental.driver_id
            rental_view.name = car.name
            rental_view.imageUrl = rental.imageurl
            rental_view.maxPassengers = rental.max_passengers

            rental_view.pickupStationName = route_origin.city
            rental_view.returnStationName = route_destination.city

            rental_view.pickupTime = time.mktime(rental.pickup_time.timetuple())
            rental_view.returnTime = time.mktime(rental.return_time.timetuple())
            rental_view.price = 0

            rental_list.append(rental_view)

        return { "rentals": schema.dump(rental_list).data }

class RentalByUser(Resource):
    def get(self, user_id):
        schema = RentalSchema()
        query = CarRental.query.filter_by(driver_id=user_id)
        return paginate(query, schema)
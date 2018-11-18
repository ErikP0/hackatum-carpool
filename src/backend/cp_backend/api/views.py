from flask import Blueprint
from flask_restful import Api

from cp_backend.api.resources import UserResource, UserList
from cp_backend.api.resources import RentalResource, RentalList, RentalByUser
from cp_backend.api.resources import CarPoolResource, CarPoolList
from cp_backend.api.resources import DriverRatingResource, DriverRatingList
from cp_backend.api.resources import PassengerRatingResource, PassengerRatingList

blueprint = Blueprint('api', __name__, url_prefix='/api/v1')
api = Api(blueprint)


api.add_resource(UserResource, '/users/<int:user_id>')
api.add_resource(UserList, '/users')

api.add_resource(RentalResource, '/rentals/<string:rental_id>')
api.add_resource(RentalList, '/rentals')
api.add_resource(RentalByUser, '/rentals/by_user/<int:user_id>')

#api.add_resource(CarPoolResource, '/carpools/<int:car_pool_id>')
#api.add_resource(CarPoolList, '/carpools')

#api.add_resource(DriverRatingResource, '/driverratings/<int:user_id>/<int:rating_id>')
#api.add_resource(DriverRatingList, '/driverratings/<int:user_id>')

#api.add_resource(PassengerRatingResource, '/passengerratings/<int:user_id>/<int:rating_id>')
#api.add_resource(PassengerRatingList, '/passengerratings/<int:user_id>')
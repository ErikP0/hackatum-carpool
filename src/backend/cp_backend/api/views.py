from flask import Blueprint
from flask_restful import Api

from cp_backend.api.resources import UserResource, UserList
from cp_backend.api.resources import RentalResource, RentalList, FutureRentalByUser

blueprint = Blueprint('api', __name__, url_prefix='/api/v1')
api = Api(blueprint)


api.add_resource(UserResource, '/users/<int:user_id>')
api.add_resource(UserList, '/users')

api.add_resource(RentalResource, '/rentals/<string:rental_id>')
api.add_resource(RentalList, '/rentals')
api.add_resource(FutureRentalByUser, '/rentals/by_user/<int:user_id>')
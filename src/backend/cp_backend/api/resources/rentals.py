from flask import request
from flask_restful import Resource

from cp_backend.models import CarRental
from cp_backend.extensions import ma, db
from cp_backend.commons.pagination import paginate

class RentalSchema(ma.ModelSchema):
    class Meta:
        model = CarRental
        sqla_session = db.session

class RentalResource(Resource):
    def get(self, rental_id):
        schema = RentalSchema()
        rental = CarRental.query.get_or_404(rental_id)
        return { "rental": schema.dump(rental).data }

class RentalList(Resource):
    def get(self):
        schema = RentalSchema(many=True)
        query = CarRental.query
        return paginate(query, schema)

class FutureRentalByUser(Resource):
    def get(self, user_id):
        schema = RentalSchema()
        query = CarRental.query.filter_by(user_id=user_id)
        return paginate(query, schema)
from flask import request
from flask_restful import Resource

from cp_backend.models import CarPool
from cp_backend.extensions import ma, db
from cp_backend.commons.pagination import paginate

class CarPoolSchema(ma.ModelSchema):
    created_on = ma.Date(dump_only=True)

    class Meta:
        model = CarPool
        sqla_session = db.session

class CarPoolResource(Resource):
    def get(self, car_pool_id):
        schema = CarPoolSchema()
        pool = CarPool.query.get_or_404(car_pool_id)
        return { "pool": schema.dump(pool).data }
    

class CarPoolList(Resource):
    def get(self):
        schema = CarPoolSchema(many=True)
        query = CarPool.query
        return paginate(query, schema)

    def post(self):
        schema = CarPoolSchema()
        carpool, errors = schema.load(request.json)
        if errors:
            return errors, 422

        try:
            carpool.validate()
        except ValueError as v:
            return str(v), 422

        db.session.add(carpool)
        db.session.commit()

        return { "msg": "car pool created", "pool": schema.dump(carpool).data }, 201
from flask import request
from flask_restful import Resource

from cp_backend.models import Rating, RatingType
from cp_backend.extensions import ma, db
from cp_backend.commons.pagination import paginate

class RatingSchema(ma.ModelSchema):
    rating_type = ma.String(load_only=True,dump_only=True)

    class Meta:
        model = Rating
        sqla_session = db.session
    
class DriverRatingResource(Resource):
    def get(self, user_id, rating_id):
        schema = RatingSchema()
        rating = Rating.query.filter_by(
            user_id=user_id,
            rating_type=RatingType.DRIVER,
            rating_id=rating_id
        ).first()

        return { "rating": schema.dump(rating).data }

class DriverRatingList(Resource):
    def get(self, user_id):
        schema = RatingSchema(many=True)
        query = Rating.query.filter_by(
            user_id=user_id,
            rating_type=RatingType.DRIVER
        )
        return paginate(query, schema)

    def post(self, user_id):
        schema = RatingSchema()
        rating, errors = schema.load(request.json)
        if errors:
            return errors, 422

        rating.rating_type = RatingType.DRIVER

        db.session.add(rating)
        db.session.commit()

        return { "msg": "driver rating submitted", "rating": schema.dump(rating).data }, 201

class PassengerRatingResource(Resource):
    def get(self, user_id, rating_id):
        schema = RatingSchema()
        rating = Rating.query.filter_by(
            user_id=user_id,
            rating_type=RatingType.PASSENGER,
            rating_id=rating_id
        ).first()

        return { "rating": schema.dump(rating).data }

class PassengerRatingList(Resource):
    def get(self, user_id):
        schema = RatingSchema(many=True)
        query = Rating.query.filter_by(
            user_id=user_id,
            rating_type=RatingType.PASSENGER
        )
        return paginate(query, schema)

    def post(self, user_id):
        schema = RatingSchema()
        rating, errors = schema.load(request.json)
        if errors:
            return errors, 422

        rating.rating_type = RatingType.PASSENGER

        db.session.add(rating)
        db.session.commit()

        return { "msg": "driver rating submitted", "rating": schema.dump(rating).data }, 201